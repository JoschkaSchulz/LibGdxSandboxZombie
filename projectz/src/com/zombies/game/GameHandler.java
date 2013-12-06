package com.zombies.game;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.sun.org.apache.xml.internal.utils.CharKey;
import com.zombies.animation.Animation;
import com.zombies.game.charakter.Charakter;
import com.zombies.game.charakterpicker.CharakterPicker;
import com.zombies.game.event.EventHandler;
import com.zombies.game.inventory.InventoryHandler;
import com.zombies.game.map.Map;
import com.zombies.game.map.MapTile;
import com.zombies.game.skilltree.Skill;
import com.zombies.helper.InputHelper;
import com.zombies.helper.SkinHelper;
import com.zombies.projectz.ProjectZ;

public class GameHandler extends Group {

	/**************************************************************************
	 * 				Variables
	 **************************************************************************/
	
	//static final
	public static final int STATE_CHARAKTERPICKER 	= 1;
	public static final int STATE_INTRO 			= 2;
	public static final int STATE_MAP 				= 3;
	public static final int STATE_EVENTINIT 		= 4;
	public static final int STATE_EVENT 			= 5;
	public static final int STATE_EVENTDONE			= 6;
	public static final int STATE_OPENINVENTORY		= 7;
	public static final int STATE_INVENTORY			= 8;
	public static final int STATE_CLOSEINVENTORY	= 9;
	
	//Referenz to the root
	private ProjectZ projZRef;
	
	//groups
	private CharakterPicker charPicker;
	private Animation intro;
	private Map currentMap;
	private ArrayList<Map> mapList;
	private EventHandler eventHandler;
	private InventoryHandler inventoryActor;
	
	private Charakter charakter;
	
	private int state;
	private ShapeRenderer debugRenderer;
	private Image greyLayer;
	/**************************************************************************
	 * 				Constructor
	 **************************************************************************/
	
	/**
	 * The constructor of the GameHandler
	 */
	public GameHandler(ShapeRenderer debugRenderer, ProjectZ projZRef) {
		this.debugRenderer = debugRenderer;
		charPicker = new CharakterPicker(debugRenderer);
		eventHandler = new EventHandler(debugRenderer);
		charakter = null;
		greyLayer = new Image(SkinHelper.GREY_LAYER);
		greyLayer.size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		state = 0;
		this.projZRef = projZRef;
	}
	/**************************************************************************
	 * 				getter & setter
	 **************************************************************************/
	
	public EventHandler getEventHandler() {
		return eventHandler;
	}
	
	public int getState() {
		return state;
	}
	
	/**************************************************************************
	 * 				Methods
	 **************************************************************************/
	
	/**
	 * Creates a Array of 16 Maps for the game. 
	 * 
	 * @return an array of 16 maps
	 */
	private ArrayList<Map> generateMapList() {
		ArrayList<Map> mapArray = new ArrayList<Map>();
		
		Map map;
		for(int i = 0, n = 16; i < n; i++) {
			//Create a new Map
			map = new Map(16,16, debugRenderer);
			
			//Set a Character Refernz
			map.setCharRef(charakter);
			
			//Choose a tileset for the map
			map.generateMap(Map.TYPE_CITY);
			
			//Adding the map to the map Array
			mapArray.add(map);
		}
		
		return mapArray;
	}
	
	/**
	 * This method is used to start the game on the first map after the intro
	 */
	private void startGame() {
		state = STATE_MAP;									//Set the next State
		
		//Clear all actors in the GameHandler
		this.clear();	
		
		//Generates a List of 16 Maps
		mapList = generateMapList();								
		
		//Get the first Map
		currentMap = mapList.get(0);						
		
		//Set the selected Map as Actor
		addActor(currentMap);							
		
		//Add Charakter reference
		currentMap.setCharRef(charakter);
		
		//Generate a UI for the Map
		currentMap.generateUI();							
		
		//Set the player start position
		MapTile startTile = currentMap.getStartTile();
		currentMap.setCharakterOnPosition(startTile.getPosX(), startTile.getPosY());
		
		//Put Events on the map
		int street = 10 + (0 * 10);		//TODO: replace the 0 with the current number of level
		int lvl1 = 10;
		int lvl2 = 5;
		int lvl3 = (int)(Math.random()*2);
		currentMap.setEvents(street, lvl1, lvl2, lvl3);
		
		//Move the camera to the Character Position
		currentMap.moveCameraToCharacter();
	}
	
	/**
	 * Opens the inventory
	 */
	public void openInventory() {
		state = STATE_OPENINVENTORY;
	}
	
	/**
	 * Closes the inventory
	 */
	public void closeInventory() {
		state = STATE_CLOSEINVENTORY;
	}
	
	/**
	 * Loads an Event by its id
	 * @param eventID
	 */
	public void loadEventById(int eventID) {
		eventHandler.loadEvent(eventID);
		state = STATE_EVENTINIT;
	}
	
	/**
	 * Loads the event on the characters x and y position and removes it from
	 * the map.
	 */
	public void loadEvent() {
		MapTile tile = currentMap.getTile(charakter.getMapX(), charakter.getMapY());
		eventHandler.loadEvent(tile.getEventID());
		tile.removeEvent();
		state = STATE_EVENTINIT;
	}
	
	/**
	 * Loads an event from the x and y coordinates of the map and removes it
	 * from the map.
	 * 
	 * @param x the map x coordinate in the maptile matrix
	 * @param y the map y coordinate in the maptile matrix
	 */
	public void loadEvent(int x, int y) {
		MapTile tile = currentMap.getTile(x, y);
		eventHandler.loadEvent(tile.getEventID());
		tile.removeEvent();
		state = STATE_EVENTINIT;
	}
	
	public void finishEvent() {
		state = STATE_EVENTDONE;
	}
	
	/**
	 * This method sets a charakter and change thestate to the intro state
	 * 
	 * @param charakter selected character forthe game
	 */
	public void setCharakterAndStart(Charakter charakter) {
		this.charakter = charakter;
		eventHandler.setCharRef(this.charakter);
		inventoryActor = new InventoryHandler(this.charakter);
		state = STATE_INTRO;
		this.clear();
		intro = new Animation();
		this.addActor(intro);
	}
	
	/**
	 * is used for all checks that can let the game ending bad
	 * 
	 * @return	true if the player lost, otherwise false
	 */
	private boolean isGameOver() {
		return charakter.getCurrentLP() <= 0;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		
		//Handle the events
		if(charakter == null && state != STATE_CHARAKTERPICKER) {
			state = STATE_CHARAKTERPICKER;
			this.clear();
			this.addActor(this.charPicker);
		}else if(state == STATE_INTRO && intro != null) {
			if(intro.getFinished()) {
				this.startGame();					
			}
		}else if(state == STATE_MAP) {
			
			//Checks if the game is over!
			if(isGameOver()) {
				this.projZRef.mainMenu();
			}
			
			/*******************************
			 * start testing stuff :)
			 *******************************/
			if(InputHelper.ACTION) {
				
//				map.moveCameraToCharacter(); //Test movement of the camera to char
				//Start Event on the Player pos
//				if(map.getTile(charakter.getMapX(), charakter.getMapY()).getEventID() != 0) {
//					loadEvent(map.getTile(charakter.getMapX(), charakter.getMapY()).getEventID());
//				}
				//map.clear();
				//map.generateMap(Map.TYPE_CITY);
				currentMap.debugInfo();
			}
			/*******************************
			 * End of testing stuff :(
			 *******************************/
			
		}else if(state == STATE_EVENTINIT) {
			currentMap.hideUI();
			this.addActor(greyLayer);
			this.addActor(eventHandler);
			state = STATE_EVENT;
		}else if(state == STATE_EVENTDONE) {
			this.clear();
			this.addActor(currentMap);
			currentMap.setCharRef(charakter);
			currentMap.showUI();
			state = STATE_MAP;
		}else if(state == STATE_OPENINVENTORY) {
			currentMap.hideUI();
			this.addActor(greyLayer);
			this.addActor(inventoryActor);
			state = STATE_INVENTORY;
		}else if(state == STATE_CLOSEINVENTORY) {
			this.clear();
			this.addActor(currentMap);
			currentMap.setCharRef(charakter);
			currentMap.showUI();
			state = STATE_MAP;
		}
	}
}
