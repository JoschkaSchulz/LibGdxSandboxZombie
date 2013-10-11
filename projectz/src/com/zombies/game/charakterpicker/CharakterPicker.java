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
import com.sun.org.apache.xml.internal.utils.CharKey;
import com.zombies.game.GameHandler;
import com.zombies.game.charakter.Charakter;
import com.zombies.helper.FontHelper;
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
	
	public CharakterPicker() {
		chars = new Charakter[2][2];
		chars[0][0] = new Charakter(Gdx.files.internal("data/char/william_pokerwinski_ger.xml"));
		chars[1][0] = null;
		chars[0][1] = null;
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
		
		Texture texBackground1 = new Texture(Gdx.files.internal("data/gfx/charakterpicker/bg1.png"));
		imgBackground1 = new Image(texBackground1);
		imgBackground1.setPosition(128, GUIHelper.getNewCoordinates(128, 512));
		imgBackground1.setName("button_start");
		this.addActor(imgBackground1);
		
		Texture texBackground2 = new Texture(Gdx.files.internal("data/gfx/charakterpicker/bg2.png"));
		imgBackground2 = new Image(texBackground2);
		imgBackground2.setPosition(128 + 1280, GUIHelper.getNewCoordinates(128, 512));
		imgBackground2.setName("button_start");
		this.addActor(imgBackground2);
		
		Texture texBackground3 = new Texture(Gdx.files.internal("data/gfx/charakterpicker/bg3.png"));
		imgBackground3 = new Image(texBackground3);
		imgBackground3.setPosition(128, GUIHelper.getNewCoordinates(128 + 786, 512));
		imgBackground3.setName("button_start");
		this.addActor(imgBackground3);
		
		Texture texBackground4 = new Texture(Gdx.files.internal("data/gfx/charakterpicker/bg4.png"));
		imgBackground4 = new Image(texBackground4);
		imgBackground4.setPosition(128 + 1280, GUIHelper.getNewCoordinates(128 + 786, 512));
		imgBackground4.setName("button_start");
		this.addActor(imgBackground4);
		
		selectCharakter(currentX, currentY);
		
		this.debugRenderer = new ShapeRenderer();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(InputHelper.ACTION) {
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

	public void selectCharakter(int x, int y) {
		selection = chars[x][y];
		if(selection != null) System.out.println(selection.toString());
	}
	
	private void drawCharakterInfo(int x, int y, Charakter c, SpriteBatch batch) {
		//Texts for Charslot [0] [0] (William Pokerwinski)
		FontHelper.KITEONE.setColor(1f, 1f, 1f, 1f);
		FontHelper.KITEONE.draw(batch, t.get("cp_name"), x, GUIHelper.getNewCoordinates(y, 16));
		FontHelper.KITEONE.draw(batch, c.getName(), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 1)));
		FontHelper.KITEONE.draw(batch, t.get("cp_age"), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 2)));
		FontHelper.KITEONE.draw(batch, c.getAge(), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 3)));
		FontHelper.KITEONE.draw(batch, t.get("cp_height"), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 4)));
		FontHelper.KITEONE.draw(batch, c.getHeight(), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 5)));
		FontHelper.KITEONE.draw(batch, t.get("cp_story"), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 6)));
		for(int i = 0; i < c.getStory().length()/40; i++) {
			FontHelper.KITEONE.draw(batch, c.getStory().subSequence(i*40, (i+1)*40), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 7+i)));
			if(c.getStory().length() > 40 && i == (c.getStory().length()/40)-1) {
				FontHelper.KITEONE.draw(batch, c.getStory().subSequence(i+1*40, c.getStory().length()), x, GUIHelper.getNewCoordinates(y, 16 + (32 * 8+i)));
			}
		}
		int counter = 0;
		FontHelper.KITEONE.draw(batch, t.get("cp_hobby"), x+512, GUIHelper.getNewCoordinates(y, 16));
		for(int i = 0; i < c.getSkilltree().getAvaibleSkills().size(); i++) {
			FontHelper.KITEONE.draw(batch, c.getSkilltree().getAvaibleSkills().get(i).getName(), x+512, GUIHelper.getNewCoordinates(y, 16 + (32 * 1 + i)));
			counter++;
		}
		String become = "";
		FontHelper.KITEONE.draw(batch, t.get("cp_become"), x+512, GUIHelper.getNewCoordinates(y, 16 + (32 * 2 + counter)));
		for(int i = 0; i < c.getSkilltree().getSkilltreeSkills().size(); i++) {
			if(!c.getSkilltree().getAvaibleSkills().contains(c.getSkilltree().getSkilltreeSkills().get(i))){
				become =  become + (i != 0 ? ", " : "" ) + c.getSkilltree().getSkilltreeSkills().get(i).getName();
			}
		}
		for(int i = 0; i < become.length()/40; i++) {
			FontHelper.KITEONE.draw(batch, become.subSequence(i*40, (i+1)*40), x +512, GUIHelper.getNewCoordinates(y, 16 + (32 * 3 + counter)));
			if(become.length() > 40 && i == (become.length()/40)-1) {
				FontHelper.KITEONE.draw(batch, become.subSequence(i+1*40, become.length()), x + 512, GUIHelper.getNewCoordinates(y, 16 + (32 * 4 + counter)));
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		drawCharakterInfo((int)(128 + getX()), (int)(GUIHelper.getNewCoordinates(128, 512) + -getY()), chars[0][0], batch);
		drawCharakterInfo((int)(1280 + 128 + getX()), (int)(GUIHelper.getNewCoordinates(128, 512) + -getY()), chars[0][0], batch);
	}
}
