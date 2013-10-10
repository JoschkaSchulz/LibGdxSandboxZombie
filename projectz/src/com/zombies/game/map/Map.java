package com.zombies.game.map;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.zombies.game.GameHandler;

public class Map extends Group {
	
	public static final int TYPE_CITY 		= 1;
	public static final int TYPE_FOREST 	= 2;
	
	public Map() {
		
	}
	
	public void generateMap(int type) {
		switch(type) {
			default:	//city
				break;
			case TYPE_FOREST:
				break;
		}
	}
}
