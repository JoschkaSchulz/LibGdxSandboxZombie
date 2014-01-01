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
	public static TextureRegion MAP_HIGHLIGHT_EVENT;
	
	//Event
	public static TextureRegion FIGHT_HITBAR;
	public static TextureRegion FIGHT_POINTER;
	public static TextureRegion FIGHT_HIT_ZONE;
	public static TextureRegion FIGHT_SINGLE_ZOMBIE;
	public static TextureRegion FIGHT_WALKING_ZOMBIE;
	public static TextureRegion FIGHT_HIT;
	
	//Items
	public static TextureRegion ITEM_TILE_SET[][];
	
	/***************************************************************************
	 * 			Methods
	 **************************************************************************/

	public static void initializeTextures() {
		//Loading Map Textures
		Texture tileCitySetTexture = new Texture(Gdx.files.internal("data/gfx/map_tiles/tileset_city.png"));
		MAP_CITY_TILE_SET = TextureRegion.split(tileCitySetTexture, 128, 128);
		MAP_HIGHLIGHT = MAP_CITY_TILE_SET[2][7];
		MAP_HIGHLIGHT_EVENT = MAP_CITY_TILE_SET[1][0];
	
		//Loading Event Textures
		Texture fight = new Texture(Gdx.files.internal("data/gfx/events/fight.png"));
		FIGHT_HITBAR = new TextureRegion(fight, 0, 0, 1024, 64);
		FIGHT_POINTER = new TextureRegion(fight, 0, 64, 64, 64);
		FIGHT_SINGLE_ZOMBIE = new TextureRegion(fight, 64, 64, 41, 53);
		FIGHT_HIT = new TextureRegion(fight, 128, 64, 64, 64);
		FIGHT_WALKING_ZOMBIE = new TextureRegion(fight, 0, 64, 64, 128);
		
		//Loading the item tile set
		Texture items = new Texture(Gdx.files.internal("data/gfx/items/item_tileset.png"));
		ITEM_TILE_SET = TextureRegion.split(items, 128, 128);
	}
	
	public static TextureRegion[][] getCityTileSet() {
		return MAP_CITY_TILE_SET;
	}
}
