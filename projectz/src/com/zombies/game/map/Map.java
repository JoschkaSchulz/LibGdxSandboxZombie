package com.zombies.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zombies.game.GameHandler;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.InputHelper;

public class Map extends Group {
	
	public static final int TYPE_CITY 		= 1;
	public static final int TYPE_FOREST 	= 2;
	
	private Image cityBackground;
	private float scaleFactor;
	
	private float dragX;
	private float dragY;
	
	public Map() {
		dragX = 0;
		dragY = 0;
	}
	
	public void scale(int scaleFactor) {
		cityBackground.scale(scaleFactor);
		this.scaleFactor = scaleFactor;
	}
	
	public void generateMap(int type) {
		getParent().addActor(new MapUI(this));
		
		switch(type) {
			default:	//city
				Texture cityTex = new Texture(Gdx.files.internal("data/gfx/maps/testing_city.png"));
				cityBackground = new Image(cityTex);
				cityBackground.setPosition(0, GUIHelper.getNewCoordinates(0, 2048));
				cityBackground.setName("city");
				this.addActor(cityBackground);
				break;
			case TYPE_FOREST:
				break;
		}
	}
	

	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(InputHelper.DRAG && dragX == 0 && dragY == 0) {
			dragX = InputHelper.DRAG_X;
			dragY = InputHelper.DRAG_Y;
		}else if(InputHelper.DRAG) {
			int vectorX = (int)(InputHelper.DRAG_X - dragX);
			int vectorY = (int)(InputHelper.DRAG_Y - dragY);
			dragX = dragY = 0;
			translate(vectorX, vectorY);
			
			System.out.println(getX() + "/" + getY());
			//Correct x and y for map borders
			if(getX() > 0) setX(0);
			else if(getX() < -2048 + Gdx.graphics.getWidth()) setX(-2048 + Gdx.graphics.getWidth());
			if(getY() < 0) setY(0);
			else if(getY() > 2048 - Gdx.graphics.getHeight()) setY(2048 - Gdx.graphics.getHeight());
		}else dragX = dragY = 0;
	}
}
