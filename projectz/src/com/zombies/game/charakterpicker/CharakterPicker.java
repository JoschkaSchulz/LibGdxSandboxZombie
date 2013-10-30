package com.zombies.game.charakterpicker;

import java.sql.BatchUpdateException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.Cell;
import com.sun.org.apache.xml.internal.utils.CharKey;
import com.zombies.game.GameHandler;
import com.zombies.game.charakter.Charakter;
import com.zombies.helper.SkinHelper;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.InputHelper;
import com.zombies.helper.XMLHelper;

public class CharakterPicker extends Group {
	private int currentX;
	private int currentY;
	private int targetX;
	private int targetY;
	
	private int maxX;
	private int maxY;

	private boolean inAnimation;
	
	private GameHandler gameHanderRef;
	private ShapeRenderer debugRenderer;
	
	private Image imgBackground1;
	private Image imgBackground2;
	private Image imgBackground3;
	private Image imgBackground4;
	private float time;
	
	private Charakter selection;
	private Charakter[][] chars;
	
	private HashMap<String, String> t;		//Translations for Strings
	
	public CharakterPicker(ShapeRenderer debugRenderer) {
		chars = new Charakter[2][2];
		chars[0][0] = new Charakter(Gdx.files.internal("data/char/william_pokerwinski_ger.xml"));
		chars[1][0] = new Charakter(Gdx.files.internal("data/char/Kasia_vonLuprecht_ger.xml"));
		chars[0][1] = new Charakter(Gdx.files.internal("data/char/Marcel_deBool_ger.xml"));
		chars[1][1] = null;
		time = 0;
		t = XMLHelper.loadXML(Gdx.files.internal("data/xml/charakter_picker_ger.xml"));
		
		this.currentX = 0;
		this.currentY = 0;
		this.targetX = 0;
		this.targetY = 0;
		this.maxX = 1;
		this.maxY = 1;
		
		this.inAnimation = false;
		
		drawTable(0, 0, chars[0][0]);
		drawTable(0, 1, chars[0][1]);
		drawTable(1, 0, chars[1][0]);
		
		selectCharakter(currentX, currentY);
		
		this.debugRenderer = debugRenderer;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(InputHelper.DRAG) {
			((GameHandler) this.getParent()).setCharakterAndStart(selection);
		}
		
		//Move to right side
		if(currentX > targetX) {
			this.translate((500 * delta), 0);
			if(getX() >= (targetX * Gdx.graphics.getWidth())) {
				currentX = targetX;
				this.setX(targetX * Gdx.graphics.getWidth());
				this.inAnimation = false;
				selectCharakter(currentX, currentY);
			}
		}else if(currentX < targetX) {
			this.translate(-(500 * delta), 0);
			if(getX() <= -(targetX * Gdx.graphics.getWidth())) {
				currentX = targetX;
				this.setX(-(targetX * Gdx.graphics.getWidth()));
				this.inAnimation = false;
				selectCharakter(currentX, currentY);
			}
		}else if(currentY > targetY) {
			this.translate(0, -(500 * delta));
			if(getY() <= (targetY * Gdx.graphics.getHeight())) {
				currentY = targetY;
				this.setY((targetY * Gdx.graphics.getHeight()));
				this.inAnimation = false;
				selectCharakter(currentX, currentY);
			}
		}else if(currentY < targetY) {
			this.translate(0, (500 * delta));
			if(getY() >= (targetY * Gdx.graphics.getHeight())) {
				currentY = targetY;
				this.setY(targetY * Gdx.graphics.getHeight());
				this.inAnimation = false;
				selectCharakter(currentX, currentY);
			}
		}
		
		//Keyboard inputs and maybe Controller?
		time += delta;
		if(InputHelper.RIGHT && time > 0.25f && !this.inAnimation && this.currentX < maxX) {
			this.targetX++;
			this.inAnimation = true;
			time = 0;
		}else if (InputHelper.LEFT && time > 0.25f && !this.inAnimation && this.currentX > 0){
			this.targetX--;
			this.inAnimation = true;
			time = 0;
		}else if(InputHelper.UP && time > 0.25f && !this.inAnimation && this.currentY > 0) {
			this.targetY--;
			this.inAnimation = true;
			time = 0;
		}else if (InputHelper.DOWN && time > 0.25f && !this.inAnimation && this.currentY < maxY){
			this.targetY++;
			this.inAnimation = true;
			time = 0;
		}
	}

	/**
	 * sets the selection on another char
	 *
	 * @param x the x value of the char
	 * @param y the y value of the char
	 */
	public void selectCharakter(int x, int y) {
		selection = chars[x][y];
		if(selection != null) System.out.println(selection.toString());
	}
	
	private void drawTable(int x, int y, Charakter c) {
		x = (int)((Gdx.graphics.getWidth() * x) + getX());
		y = (int)(64 + (Gdx.graphics.getHeight() * y) - getY());
		
		Table leftTable = new Table();
		leftTable.setWidth(786);
		leftTable.setWidth(512);
		leftTable.top();
//		leftTable.debug();
		
		leftTable.add(new Label(t.get("cp_name"), SkinHelper.SKIN));
		leftTable.add(new Label(c.getName(), SkinHelper.SKIN));
		
		leftTable.row();
		leftTable.add(new Label(t.get("cp_age"), SkinHelper.SKIN));
		leftTable.add(new Label(c.getAge(), SkinHelper.SKIN));
		
		leftTable.row();
		leftTable.add(new Label(t.get("cp_height"), SkinHelper.SKIN));
		leftTable.add(new Label(c.getHeight(), SkinHelper.SKIN));
		
		leftTable.row();
		Label storyLabel = new Label(t.get("cp_story") + "\n" + c.getStory(), SkinHelper.SKIN);
		storyLabel.setWrap(true);
		leftTable.add(storyLabel).colspan(2).width(400).height(500).top();
		
		leftTable.setPosition(x, GUIHelper.getNewCoordinates(y, (int)leftTable.getHeight()));
		addActor(leftTable);

		//Right Table
		Table rightTable = new Table();
		rightTable.setWidth(786);
		rightTable.setWidth(512);
		rightTable.top();
//		rightTable.debug();
		
		rightTable.add(new Label(t.get("cp_hobby"), SkinHelper.SKIN));
		rightTable.add(new Label(c.getSkilltree().getAvaibleSkills().get(0).getName(), SkinHelper.SKIN));
		for(int i = 1; i < c.getSkilltree().getAvaibleSkills().size(); i++) {
			rightTable.row();
			rightTable.add();
			rightTable.add(new Label(c.getSkilltree().getAvaibleSkills().get(i).getName(), SkinHelper.SKIN));
		}
		
		rightTable.row();
		rightTable.add(new Label(t.get("cp_become"), SkinHelper.SKIN));
		boolean first = true;
		for(int i = 0; i < c.getSkilltree().getSkilltreeSkills().size(); i++) {
			if(!c.getSkilltree().getAvaibleSkills().contains(c.getSkilltree().getSkilltreeSkills().get(i))){
				if(first) {
					rightTable.add(new Label(c.getSkilltree().getSkilltreeSkills().get(i).getName(), SkinHelper.SKIN));
					first = false;
				}else{
					rightTable.row();
					rightTable.add();
					rightTable.add(new Label(c.getSkilltree().getSkilltreeSkills().get(i).getName(), SkinHelper.SKIN));
				}
			}
		}
		
		rightTable.setPosition(x + leftTable.getWidth() + 128, GUIHelper.getNewCoordinates(y, (int)rightTable.getHeight()));
		addActor(rightTable);

		try{
			Texture charPicTex = new Texture(Gdx.files.internal("data/gfx/charakter/"+c.getImage()));
			Image charPic = new Image(charPicTex);
			charPic.setBounds(x+(Gdx.graphics.getWidth()/2)-64, GUIHelper.getNewCoordinates(y+(Gdx.graphics.getHeight()/2), 256), 256, 256);
			addActor(charPic);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * draws the charakter info Text
	 * 
	 * @param x start x value (0 for first char, 1 for second... not pixel!)
	 * @param y start y value (0 for the first, 1 for the second... not pixel!)
	 * @param c the charakter
	 * @param batch the batch of wich should be drawn
	 */
	private void drawCharakterInfo(int x, int y, Charakter c, SpriteBatch batch) {
		//Calculation for x and y
		x = (int)(128 + (Gdx.graphics.getWidth() * x) + getX());
		y = (int)(128 + (786 * y) - getY());
		
		//Graphical output
		SkinHelper.KITEONE.setColor(1f, 1f, 1f, 1f);
		SkinHelper.KITEONE.draw(batch, t.get("cp_name"), x, GUIHelper.getNewCoordinates(y, 16));
		SkinHelper.KITEONE.draw(batch, c.getName(), x, GUIHelper.getNewCoordinates(y + (32 * 1), 16));
		SkinHelper.KITEONE.draw(batch, t.get("cp_age"), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 2)));
		SkinHelper.KITEONE.draw(batch, c.getAge(), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 3)));
		SkinHelper.KITEONE.draw(batch, t.get("cp_height"), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 4)));
		SkinHelper.KITEONE.draw(batch, c.getHeight(), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 5)));
		SkinHelper.KITEONE.draw(batch, t.get("cp_story"), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 6)));
		for(int i = 0; i < c.getStory().length()/40; i++) {
			SkinHelper.KITEONE.draw(batch, c.getStory().subSequence(i*40, (i+1)*40), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 7+i)));
			if(c.getStory().length() > 40 && i == (c.getStory().length()/40)-1) {
				SkinHelper.KITEONE.draw(batch, c.getStory().subSequence(i+1*40, c.getStory().length()), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 8+i)));
			}
		}
		int counter = 0;
		SkinHelper.KITEONE.draw(batch, t.get("cp_hobby"), x+512, GUIHelper.getNewCoordinates(y, 16));
		for(int i = 0; i < c.getSkilltree().getAvaibleSkills().size(); i++) {
			SkinHelper.KITEONE.draw(batch, c.getSkilltree().getAvaibleSkills().get(i).getName(), x+512, GUIHelper.getNewCoordinates(y, 16 + (32 * 1 + i)));
			counter++;
		}
		String become = "";
		SkinHelper.KITEONE.draw(batch, t.get("cp_become"), x+512, GUIHelper.getNewCoordinates(y, 16 + (32 * 2 + counter)));
		for(int i = 0; i < c.getSkilltree().getSkilltreeSkills().size(); i++) {
			if(!c.getSkilltree().getAvaibleSkills().contains(c.getSkilltree().getSkilltreeSkills().get(i))){
				become =  become + (i != 0 ? ", " : "" ) + c.getSkilltree().getSkilltreeSkills().get(i).getName();
			}
		}
		for(int i = 0; i < become.length()/40; i++) {
			SkinHelper.KITEONE.draw(batch, become.subSequence(i*40, (i+1)*40), x +512, GUIHelper.getNewCoordinates(y, 16 + (32 * 3 + counter)));
			if(become.length() > 40 && i == (become.length()/40)-1) {
				SkinHelper.KITEONE.draw(batch, become.subSequence(i+1*40, become.length()), x + 512, GUIHelper.getNewCoordinates(y, 16 + (32 * 4 + counter)));
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
//		drawCharakterInfo(0, 0, chars[0][0], batch);
//		drawCharakterInfo(1, 0, chars[1][0], batch);
//		drawCharakterInfo(0, 1,chars[0][1], batch);
	}
}
