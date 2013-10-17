package com.zombies.game.map;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.XmlReader;
import com.zombies.game.GameHandler;
import com.zombies.game.skilltree.Skill;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.InputHelper;

public class Map extends Group {
	
	public static final int TYPE_CITY 		= 0;
	public static final int TYPE_FOREST 	= 1;
	
	private float dragX;
	private float dragY;
	
	private MapTile[][] world;
	
	@SuppressWarnings("unchecked")
	public Map(int width, int height) {
		dragX = 0;
		dragY = 0;
		
		world = new MapTile[width][height];
	}
	
	/**
	 * 
	 * @param type
	 * @param lvl[5] The Levels they should used
	 */
	public void generateMap(int type, int lvl[]) {
		getParent().addActor(new MapUI(this));
		
		switch(type) {
			default:	//city
				break;
			case TYPE_FOREST:
				break;
		}
		
		//Variables
		boolean done = false;
		int reserve[] = new int[5];
		long debugTime = System.currentTimeMillis();
		
		while(!done) {
			//*******************************************************************************
			//Step I ~ place the start and exit
			//*******************************************************************************
			
			//first clear the complete world
			for(int h = 0; h < world.length; h++){
				for(int w = 0; w < world[0].length; w++) {
					world[w][h] = new MapTile(w, h);
					world[w][h].setType(MapTile.TYPE_EMPTY);
				}
			}
			
			//fill the reserve
			reserve = lvl;
			
			//get position for start
			boolean horizontal = (Math.random() < 0.5 ? true : false);
			if(horizontal) {
				int startPos  = (int)(Math.random()*world[0].length);
				int exitPos = (int)(Math.random()*world[0].length);
				world[0][startPos] = new MapTile(0, startPos);	//TODO: hier start einfügen
				world[0][startPos].setStart(true);
				world[0][startPos].setType(MapTile.TYPE_STREET);
				world[world.length-1][exitPos] = new MapTile((world.length-1), exitPos);; //TODO: hier exit einfügen
				world[world.length-1][exitPos].setExit(true);
				world[world.length-1][exitPos].setType(MapTile.TYPE_STREET);
			}else{
				int startPos  = (int)(Math.random()*world[0].length);
				int exitPos = (int)(Math.random()*world[0].length);
				world[startPos][0] = new MapTile(startPos, 0);;	//TODO: hier start einfügen
				world[startPos][0].setStart(true);
				world[startPos][0].setType(MapTile.TYPE_STREET);
				world[exitPos][world[0].length-1] = new MapTile(exitPos, world[0].length-1); //TODO: hier exit einfügen
				world[exitPos][world[0].length-1].setExit(true);
				world[exitPos][world[0].length-1].setType(MapTile.TYPE_STREET);
			}

			//*******************************************************************************
			//Step II ~ get values from the world
			//*******************************************************************************

			//First find all Streets but not the exit!
			for(int h = 0; h < world.length; h++){
				for(int w = 0; w < world[0].length; w++) {
					
				}
			}
			
			//*******************************************************************************
			//Step III ~ place new tiles
			//*******************************************************************************

			//*******************************************************************************
			//Step IV ~ place new ways on the world
			//*******************************************************************************

			//*******************************************************************************
			//Exit ~ check if done can be set true
			//*******************************************************************************
			done = true;
		}
		
		//Add the world to the group
		for(int h = 0; h < world.length; h++){
			for(int w = 0; w < world[0].length; w++) {
				if(world[w][h] != null) addActor(world[w][h]);
			}
		}
		
		System.out.println("~~~~ Map created ~~~~");
		System.out.println("Der Map aufbau hat " + (System.currentTimeMillis() - debugTime) + "ms gedauert");
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
