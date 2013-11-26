package com.zombies.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureHelper {
	
	/***************************************************************************
	 * 			Textures
	 **************************************************************************/

	//Map
	public static TextureRegion MAP_CITY_TILE_SET[][];
	public static TextureRegion MAP_HIGHLIGHT;
	
	//Event
	public static TextureRegion FIGHT_HITBAR;
	public static TextureRegion FIGHT_POINTER;
	public static TextureRegion FIGHT_HIT_ZONE;
	
	/***************************************************************************
	 * 			Methods
	 **************************************************************************/

	public static void initializeTextures() {
		//Loading Map Textures
		Texture tileCitySetTexture = new Texture(Gdx.files.internal("data/gfx/map_tiles/tileset_city.png"));
		MAP_CITY_TILE_SET = TextureRegion.split(tileCitySetTexture, 128, 128);
		MAP_HIGHLIGHT = MAP_CITY_TILE_SET[2][7];
	
		//Loading Event Textures
		Texture fight = new Texture(Gdx.files.internal("data/gfx/events/fight.png"));
		FIGHT_HITBAR = new TextureRegion(fight, 0, 0, 1024, 32);
		FIGHT_HIT_ZONE = new TextureRegion(fight, 0, 31, 1, 32);
		FIGHT_POINTER = new TextureRegion(fight, 32, 32, 32, 32);
	}
	
	public static TextureRegion[][] getCityTileSet() {
		return MAP_CITY_TILE_SET;
	}
}
