package com.zombies.game.charakterpicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zombies.game.charakter.Charakter;
import com.zombies.helper.FontHelper;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.InputHelper;

public class CharakterPicker extends Group {
	private int currentX;
	private int currentY;
	private int targetX;
	private int targetY;
	
	private int maxX;
	private int maxY;

	private boolean inAnimation;
	
	private ShapeRenderer debugRenderer;
	
	private Image imgBackground1;
	private Image imgBackground2;
	private Image imgBackground3;
	private Image imgBackground4;
	private float time;
	
	public CharakterPicker() {
		Charakter c = new Charakter(Gdx.files.internal("data/char/william_pokerwinski.xml"));
		time = 0;
		
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
		
		this.debugRenderer = new ShapeRenderer();
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
			}
		}else if(currentX < targetX) {
			this.translate(-(500 * delta), 0);
			if(getX() <= -(targetX * Gdx.graphics.getWidth())) {
				currentX = targetX;
				this.setX(-(targetX * Gdx.graphics.getWidth()));
				this.inAnimation = false;
			}
		}else if(currentY > targetY) {
			this.translate(0, -(500 * delta));
			if(getY() <= (targetY * Gdx.graphics.getHeight())) {
				currentY = targetY;
				this.setY((targetY * Gdx.graphics.getHeight()));
				this.inAnimation = false;
			}
		}else if(currentY < targetY) {
			this.translate(0, (500 * delta));
			if(getY() >= (targetY * Gdx.graphics.getHeight())) {
				currentY = targetY;
				this.setY(targetY * Gdx.graphics.getHeight());
				this.inAnimation = false;
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

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		FontHelper.KiteOne.setColor(0f, 1f, 0f, 1f);
		FontHelper.KiteOne.draw(batch, "Hello World", 10, 20);
	}
}
