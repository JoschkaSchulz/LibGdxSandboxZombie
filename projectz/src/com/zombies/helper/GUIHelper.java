package com.zombies.helper;

import com.badlogic.gdx.Gdx;

public class GUIHelper {	
	public static int getNewCoordinates(int y, int objectHeight) {
		return Gdx.graphics.getHeight() - (y + objectHeight);
	}
	
	public static int getPercentPositionY(int percent) {
		return Gdx.graphics.getHeight()/100*percent;
	}

	public static int getPercentPositionX(int percent) {
		return Gdx.graphics.getWidth()/100*percent;
	}
}
