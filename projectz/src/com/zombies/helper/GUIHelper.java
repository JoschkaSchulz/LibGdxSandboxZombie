package com.zombies.helper;

import com.badlogic.gdx.Gdx;

public class GUIHelper {	
	public static int getNewCoordinates(int y, int objectHeight) {
		return Gdx.graphics.getHeight() - (y + objectHeight);
	}
}
