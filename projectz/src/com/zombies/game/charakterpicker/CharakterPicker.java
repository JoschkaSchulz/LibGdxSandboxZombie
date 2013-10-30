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
		}else if((InputHelper.DRAG || InputHelper.ACTION) && time > 0.25f) {
			((GameHandler) this.getParent()).setCharakterAndStart(selection);
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
		leftTable.debug();
		
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
		rightTable.debug();
		
		rightTable.add(new Label(t.get("cp_hobby"), SkinHelper.SKIN));
		rightTable.add(new Label(c.getSkilltree().getAvaibleSkills().get(0).getName(), SkinHelper.SKIN)).left();
		for(int i = 1; i < c.getSkilltree().getAvaibleSkills().size(); i++) {
			rightTable.row();
			rightTable.add();
			rightTable.add(new Label(c.getSkilltree().getAvaibleSkills().get(i).getName(), SkinHelper.SKIN)).left();
		}
		
		rightTable.row();
		rightTable.add(new Label(t.get("cp_become"), SkinHelper.SKIN));
		boolean first = true;
		for(int i = 0; i < c.getSkilltree().getSkilltreeSkills().size(); i++) {
			if(!c.getSkilltree().getAvaibleSkills().contains(c.getSkilltree().getSkilltreeSkills().get(i))){
				if(first) {
					rightTable.add(new Label(c.getSkilltree().getSkilltreeSkills().get(i).getName(), SkinHelper.SKIN)).left();
					first = false;
				}else{
					rightTable.row();
					rightTable.add();
					rightTable.add(new Label(c.getSkilltree().getSkilltreeSkills().get(i).getName(), SkinHelper.SKIN)).left();
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

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
//		drawCharakterInfo(0, 0, chars[0][0], batch);
//		drawCharakterInfo(1, 0, chars[1][0], batch);
//		drawCharakterInfo(0, 1,chars[0][1], batch);
	}
}
