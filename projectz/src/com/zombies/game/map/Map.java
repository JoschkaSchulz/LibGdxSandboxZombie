package com.zombies.game.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.zombies.game.GameHandler;
import com.zombies.game.charakter.Charakter;
import com.zombies.game.event.EventHandler;
import com.zombies.game.event.EventType;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.InputHelper;
import com.zombies.helper.SkinHelper;
import com.zombies.helper.TextureHelper;

public class Map extends Group {
	
	/*********************************************************
	 * 			Variables
	 *********************************************************/
	
	public static final int TYPE_CITY 		= 0;
	public static final int TYPE_FOREST 	= 1;
	
	private static final int NORTH 	= 0;
	private static final int SOUTH 	= 1;
	private static final int WEST 	= 2;
	private static final int EAST 	= 3;
	
	private float dragX;
	private float dragY;
	private int width;
	private int height;
	
	private MapTile[][] world;
	private FogTile[][] fog;
	private Charakter charRef;
	private MapTile charPointer;
	
	private Table ui;
	private Label uiStomach;
	private Label uiThirst;
	private Label uiLP;
	private TextButton inventory;
	private TextButton options;
	
	private ShapeRenderer debugRenderer;
	private TextureRegion[][] tileSet;
	/*********************************************************
	 * 			Constructor
	 *********************************************************/
	
	public Map(int width, int height, ShapeRenderer debugRenderer) {
		dragX = 0;
		dragY = 0;
		
		this.width = width;
		this.height = height;
		world = new MapTile[width][height];
		fog = new FogTile[width][height];
		this.debugRenderer = debugRenderer;
	}
	
	/*********************************************************
	 * 			Getter and Setter
	 *********************************************************/
	
	public int getMapWidth() {
		return this.width * MapTile.TILE_WIDTH;
	}
	
	public int getMapHeight() {
		return this.height * MapTile.TILE_HEIGHT;
	}
	
	public MapTile getTile(int x, int y) {
		return world[x][y];
	}
	
	public MapTile getStartTile() {
		for(int mapY = 0; mapY < world[0].length; mapY++) {
			for(int mapX = 0; mapX < world.length; mapX++) {
				if(world[mapX][mapY].isStart()) return world[mapX][mapY];
			}
		}
		return null;	//Include NoSuchTileException here
	}
	
	public void setCharRef(Charakter charRef) {
		this.charRef = charRef;
	}
	
	/*********************************************************
	 * 			Methods
	 *********************************************************/
	
	public void setCharakterOnPosition(int x, int y) {
		//Set the charakter on the specific point
		charRef.setMapCoordinates(x,y);
		
		//Calculating the fog
		calcFog();
		
		//Show the player on the map
		charPointer = new MapTile(tileSet[1][7], x, y, MapTile.TYPE_EMPTY, this.debugRenderer);
		addActor(charPointer);
	}
	
	/**
	 * Get Level 1 Building TextureRegion
	 */
	private TextureRegion getLvl1TextureRegion() {
		return TextureHelper.MAP_CITY_TILE_SET[(int)((Math.random()*2)+2)][(int)(Math.random()*5)];
	}
	
	/**
	 * Sets the Events on the specific maptile
	 * 
	 * @param eventHandler the event Handler
	 * @param eventType The type of the event
	 * @param eventLevel the level of the maptile
	 * @param numberOfTiles how many iterations should be done
	 */
	private void putEventsToTiles(EventHandler eventHandler, String eventType, int tileLevel, int numberOfTiles, int tileType) {
		ArrayList<EventType> events = eventHandler.getEventsFromGroup(eventType, tileLevel);
		ArrayList<MapTile> tiles = getTilesByGroup(tileType);
		
		int index;
		MapTile tempTile;
		while(numberOfTiles > 0 && tiles.size() > 0) {
			index = (int)(Math.random()*tiles.size());
			tempTile = tiles.get(index);
			if(tempTile.getEventID() == 0 && !tempTile.isStart()) {
				tempTile.setEvent(
						events.get((int)(Math.random()*events.size())).getID(), 
						MapTile.EVENTTYPE_UNDEFINED);
				numberOfTiles--;
				tiles.remove(index);
			}else{
				tiles.remove(index);
			}
		}
	}
	
	/**
	 * Set the Events on the map
	 * 
	 * @param street the number of street events
	 * @param lvl1 the number of level 1 events
	 * @param lvl2 the number of level 2 events
	 * @param lvl3 the number of level 3 events
	 * 
	 * @TODO: Map Types anpassen (city, forest...)
	 */
	public void setEvents(int street, int lvl1, int lvl2, int lvl3) {
		EventHandler eventHandler = ((GameHandler)getParent()).getEventHandler();
		
		//Street Events
		putEventsToTiles(eventHandler, EventHandler.EVENTTYPE_STREET, 0, street, MapTile.TYPE_STREET);
		
		//Level1 Events
		putEventsToTiles(eventHandler, EventHandler.EVENTTYPE_CITY, 1, lvl1, MapTile.TYPE_LVL1);
		
		//Level2 Events
		putEventsToTiles(eventHandler, EventHandler.EVENTTYPE_CITY, 2, lvl2, MapTile.TYPE_LVL2);
		
		//Level3 Events
		putEventsToTiles(eventHandler, EventHandler.EVENTTYPE_CITY, 3, lvl3, MapTile.TYPE_LVL3);
	}
	
	private ArrayList<MapTile> getTilesByGroup(int group) {
		ArrayList<MapTile> tiles = new ArrayList<MapTile>();
		
		for(int mapY = 0; mapY < world[0].length; mapY++) {
			for(int mapX = 0; mapX < world.length; mapX++) {
				if(world[mapX][mapY].getType() == group) tiles.add(world[mapX][mapY]);
			}
		}
		
		return tiles;
	}
	
	/**
	 * This Method looks after the Player position and clears only the Fog
	 * on this position
	 */
	public void calcFog() {
		int x = charRef.getMapX();
		int y = charRef.getMapY();

		//Makes all the Fog visible
		setAllFogVisible();
		
		//Show the Position of the player
		if(fog[x][y].getType() != MapTile.TYPE_EMPTY) {
			fog[x][y].setVisible(false);
		}
		
		//Try all directions for making the fog away :D
		boolean findEnd = false;
		int west = x, east = x, north = y, south = y;
		while(!findEnd) {
			if(west > 0 && this.world[west-1][y] != null) {
				west--;
				if(this.world[west][y].getType() == MapTile.TYPE_STREET) fog[west][y].setVisible(false);
				else findEnd = true;
			}else findEnd = true;
		}
		findEnd = false;
		while(!findEnd) {
			if(east < this.width-1 && this.world[east+1][y] != null) {
				east++;
				if(this.world[east][y].getType() == MapTile.TYPE_STREET) fog[east][y].setVisible(false);
				else findEnd = true;
			}else findEnd = true;
		}
		findEnd = false;
		while(!findEnd) {
			if(south > 0 && this.world[x][south-1] != null) {
				south--;
				if(this.world[x][south].getType() == MapTile.TYPE_STREET) fog[x][south].setVisible(false);
				else findEnd = true;
			}else findEnd = true;
		}
		findEnd = false;
		while(!findEnd) {
			if(north < this.height-1 && this.world[x][north+1] != null) {
				north++;
				if(this.world[x][north].getType() == MapTile.TYPE_STREET) fog[x][north].setVisible(false);
				else findEnd = true;
			}else findEnd = true;
		}
		
		for(int mapY = 0; mapY < world[0].length; mapY++) {
			for(int mapX = 0; mapX < world.length; mapX++) {
				if(world[mapX][mapY].getType() == MapTile.TYPE_STREET && !fog[mapX][mapY].isVisible()) {
					if(mapX > 0 && isBuilding(mapX-1, mapY)) fog[mapX-1][mapY].setVisible(false);
					if(mapX < width-1 && isBuilding(mapX+1, mapY)) fog[mapX+1][mapY].setVisible(false);
					if(mapY > 0 && isBuilding(mapX, mapY-1)) fog[mapX][mapY-1].setVisible(false);
					if(mapY < height-1 && isBuilding(mapX, mapY+1)) fog[mapX][mapY+1].setVisible(false);
				}
			}
		}
	}
	
	/**
	 * This method checks if the point mapX and mapY is a building from
	 * level 1 up to level 5
	 * @param mapX the matrix x coordinates of the point
	 * @param mapY the matrix y coordinates of the point
	 * @return a true if the given point is a building
	 */
	private boolean isBuilding(int mapX, int mapY) {
		return world[mapX][mapY].getType() == MapTile.TYPE_LVL1 ||
				world[mapX][mapY].getType() == MapTile.TYPE_LVL2 ||
				world[mapX][mapY].getType() == MapTile.TYPE_LVL3 ||
				world[mapX][mapY].getType() == MapTile.TYPE_LVL4 ||
				world[mapX][mapY].getType() == MapTile.TYPE_LVL5;
	}

	/**
	 * Iterates over each field of the world and set the fog enabled
	 */
	private void setAllFogVisible() {
		for(int y = 0; y < fog[0].length; y++) {
			for(int x = 0; x < fog.length; x++) {
				fog[x][y].setVisible(true);
			}
		}
	}
	
	/**
	 * This method is used to init the fog with the given TextureRegion
	 * @param texture The texture of the fog
	 */
	public void addFog(TextureRegion texture) {
		for(int y = 0; y < fog[0].length; y++) {
			for(int x = 0; x < fog.length; x++) {
				fog[x][y] = new FogTile(texture, x, y, this.debugRenderer);
				addActor(fog[x][y]);
			}
		}
	}
	
	private String getStomachString() {
		return "Hunger:" + charRef.getCurrentStomach() + "/" + charRef.getMaxStomach();
	}
	
	private String getThirstString() {
		return "Durst:" + charRef.getCurrentThirst() + "/" + charRef.getMaxThirst();
	}
	
	private String getLPString() {
		return "LP:" + charRef.getCurrentLP() + "/" + charRef.getMaxLP();
	}
	
	public void refreshUI() {
		uiStomach.setText(getStomachString());
		uiThirst.setText(getThirstString());
		uiLP.setText(getLPString());
	}
	
	/**
	 * This method generates the UI for the user on the top of the map
	 */
	public void generateUI() {
		ui = new Table();
		ui.top().left();
		ui.size(Gdx.graphics.getWidth(), 64);
		ui.setPosition(0, GUIHelper.getNewCoordinates(0, 64));
		ui.setBackground(new TextureRegionDrawable(SkinHelper.SKIN_TEXTUREREGION[0][3]));
		ui.debug();
		
		ui.add(new Label(charRef.getName(), SkinHelper.SKIN)).width(256);
		
		//Adding the live points label
		uiLP = new Label(getLPString(), SkinHelper.SKIN);
		ui.add(uiLP).width(192);
		
		//Adding the stomach label
		uiStomach = new Label(getStomachString(), SkinHelper.SKIN);
		ui.add(uiStomach).width(192);
		
		//Adding the thirsts label
		uiThirst = new Label(getThirstString(), SkinHelper.SKIN);
		ui.add(uiThirst).width(192);

		inventory = new TextButton("Inventar", SkinHelper.SKIN);
		inventory.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				((GameHandler)getParent()).openInventory();
				inventory.setChecked(false);
			}
		});
		ui.add(inventory).width(192);
		
		options = new TextButton("Options", SkinHelper.SKIN);
		options.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Options");
				options.setChecked(false);
			}
		});
		ui.add(options).width(192);
		
		showUI();
	}
	
	public void showUI() {
		getParent().addActor(ui);
	}
	
	public void hideUI() {
		getParent().removeActor(ui);
	}
	
	/**
	 * magic in every row! Generates the map :D
	 * Please don't refactore the method... it's easy to
	 * read if the complete steps are in the same method!
	 * Documentation about the algorythm laying in the
	 * google drive folder!
	 * 
	 * Hours wasted: 14
	 * 
	 * @param type the type of the map. Avaible Types are
	 * 			defined as static variables.
	 */
	public void generateMap(int type) {
		long mem = Gdx.app.getNativeHeap();
		
		switch(type) {
			default:	//city
				tileSet = TextureHelper.getCityTileSet();
				break;
			case TYPE_FOREST:
				break;
		}
		
		//Variables
		boolean done = false;
		boolean activity = true;
		long debugTime = System.currentTimeMillis();
		LinkedList<MapTile> freePlaces = new LinkedList<MapTile>();	//Empty MapTiles
		
		int maxStreets = 15;		//Maximal streets from 1 to maxStreets
		int decreaseTiles = 5;	//Tiles decreased for calculation
		
		
		while(!done) {
			//*******************************************************************************
			//Step I ~ place the start and exit
			//*******************************************************************************
			
			//Reset Variables
			activity = true;
			
			//first clear the complete world
			for(int h = 0; h < world[0].length; h++){
				for(int w = 0; w < world.length; w++) {
					world[w][h] = new MapTile(w, h, debugRenderer);
					world[w][h].setType(MapTile.TYPE_EMPTY);
				}
			}
			
			//get position for start
			boolean horizontal = (Math.random() < 0.5 ? true : false);
			if(horizontal) {
				int startPos  = (int)(Math.random()*world[0].length);
				int exitPos = (int)(Math.random()*world[0].length);
				world[0][startPos] = new MapTile(0, startPos, debugRenderer);	//TODO: hier start einfügen
				world[0][startPos].setStart(true);
				world[0][startPos].setType(MapTile.TYPE_STREET);
				world[world.length-1][exitPos] = new MapTile((world.length-1), exitPos, debugRenderer);; //TODO: hier exit einfügen
				world[world.length-1][exitPos].setExit(true);
				world[world.length-1][exitPos].setType(MapTile.TYPE_STREET);
			}else{
				int startPos  = (int)(Math.random()*world[0].length);
				int exitPos = (int)(Math.random()*world[0].length);
				world[startPos][0] = new MapTile(startPos, 0, debugRenderer);;	//TODO: hier start einfügen
				world[startPos][0].setStart(true);
				world[startPos][0].setType(MapTile.TYPE_STREET);
				world[exitPos][world[0].length-1] = new MapTile(exitPos, world[0].length-1, debugRenderer); //TODO: hier exit einfügen
				world[exitPos][world[0].length-1].setExit(true);
				world[exitPos][world[0].length-1].setType(MapTile.TYPE_STREET);
			}

			while(activity) {
				activity = false;
				
				//*******************************************************************************
				//Step II ~ get values from the world
				//*******************************************************************************

				//Clear the old free Places
				freePlaces.clear();
				
				//First find all Streets but not the exit!
				for(int h = 0; h < world[0].length; h++){
					for(int w = 0; w < world.length; w++) {
						if(world[w][h].getType() == MapTile.TYPE_STREET && !world[w][h].isExit()) {
							if(w > 0) {
								if(world[w-1][h].getType() == MapTile.TYPE_EMPTY) 
									freePlaces.add(world[w-1][h]);
							}
							if(w < world.length-1) {
								if(world[w+1][h].getType() == MapTile.TYPE_EMPTY) 
									freePlaces.add(world[w+1][h]);
							}
							if(h > 0) {
								if(world[w][h-1].getType() == MapTile.TYPE_EMPTY) 
									freePlaces.add(world[w][h-1]);
							}
							if(h < world[0].length-1) {
								if(world[w][h+1].getType() == MapTile.TYPE_EMPTY) 
									freePlaces.add(world[w][h+1]);
							}
						}
					}
				}

				//*******************************************************************************
				//Step III ~ place new tiles
				//*******************************************************************************
				
				//Select buildings to set in the free Room
				if(freePlaces.size() > 0) {
					int until = (int)(Math.random()*(freePlaces.size()-decreaseTiles)) + 1;
					for(int i = 0; i < until; i++) {
						int index = (int)(Math.random()*(freePlaces.size()-1));
						MapTile mt = freePlaces.get(index);
						world[mt.getPosX()][mt.getPosY()] = new MapTile(mt.getPosX(),mt.getPosY(), debugRenderer);
						world[mt.getPosX()][mt.getPosY()].setType(MapTile.TYPE_LVL1);
						freePlaces.remove(index);
						activity = true;
					}
				}
				//*******************************************************************************
				//Step IV ~ place new ways on the world
				//*******************************************************************************
	
				//Select an empty Place on a street
				if(freePlaces.size() > 0) {
					activity = true;
					boolean possibleDirection[] = new boolean[4];
					int choosenDirection = -1;
					MapTile streetStart = freePlaces.get((int)(Math.random()*(freePlaces.size()-1)));
					if(streetStart.getPosX() > 0) {
						if(world[streetStart.getPosX()-1][streetStart.getPosY()].getType() == MapTile.TYPE_EMPTY) {
							possibleDirection[WEST] = true;
						}else{
							possibleDirection[WEST] = false;
						}
					}
					if(streetStart.getPosX() < world.length-1) {
						if(world[streetStart.getPosX()+1][streetStart.getPosY()].getType() == MapTile.TYPE_EMPTY){
							possibleDirection[EAST] = true;
						}else{
							possibleDirection[EAST] = false;
						}
					}
					if(streetStart.getPosY() > 0) {
						if(world[streetStart.getPosX()][streetStart.getPosY()-1].getType() == MapTile.TYPE_EMPTY) {
							possibleDirection[SOUTH] = true;
						}else{
							possibleDirection[SOUTH] = false;
						}
					}
					if(streetStart.getPosY() < world[0].length-1) {
						if(world[streetStart.getPosX()][streetStart.getPosY()+1].getType() == MapTile.TYPE_EMPTY) {
							possibleDirection[NORTH] = true;
						}else{
							possibleDirection[NORTH] = false;
						}
					}
				
					//Choose direction
					if(!possibleDirection[SOUTH]&& !possibleDirection[EAST]&& !possibleDirection[NORTH]&& !possibleDirection[WEST]) {
						world[streetStart.getPosX()][streetStart.getPosY()] = new MapTile(streetStart.getPosX(), streetStart.getPosY(), debugRenderer);
						world[streetStart.getPosX()][streetStart.getPosY()].setType(MapTile.TYPE_STREET);
					}else{
						boolean foundDirection = false;
						int rand;
						while(!foundDirection) {
							rand = (int)(Math.random()*4);
							if(possibleDirection[rand]) {
								choosenDirection = rand;
								foundDirection = true;
							}
						}
						
						//calculate how many streets should be max placed
						int streetCount = (int)(Math.random()*maxStreets) + 1;
						
						//Build streets
						world[streetStart.getPosX()][streetStart.getPosY()] = new MapTile(streetStart.getPosX(), streetStart.getPosY(), debugRenderer);
						world[streetStart.getPosX()][streetStart.getPosY()].setType(MapTile.TYPE_STREET);
						for(int i = 0; i < streetCount; i++) {
							if(choosenDirection == NORTH && (streetStart.getPosY() - (i+1)) >= 0) {
								if(world[streetStart.getPosX()][streetStart.getPosY() - (i+1)].getType() == MapTile.TYPE_EMPTY) {
									world[streetStart.getPosX()][streetStart.getPosY() - (i+1)] = new MapTile(streetStart.getPosX(), streetStart.getPosY() - (i+1), debugRenderer);
								}else break;
							}
							if(choosenDirection == SOUTH && (streetStart.getPosY() + (i+1)) < world[0].length) {
								if(world[streetStart.getPosX()][streetStart.getPosY() + (i+1)].getType() == MapTile.TYPE_EMPTY) {
									world[streetStart.getPosX()][streetStart.getPosY() + (i+1)] = new MapTile(streetStart.getPosX(), streetStart.getPosY() + (i+1), debugRenderer);
								}else break;
							}
							if(choosenDirection == EAST && (streetStart.getPosX() + (i+1)) < world.length) {
								if(world[streetStart.getPosX() + (i+1)][streetStart.getPosY()].getType() == MapTile.TYPE_EMPTY) {
									world[streetStart.getPosX() + (i+1)][streetStart.getPosY()] = new MapTile(streetStart.getPosX() + (i+1), streetStart.getPosY(), debugRenderer);
								}else break;
							}
							if(choosenDirection == WEST && (streetStart.getPosX() - (i+1)) >= 0) {
								if(world[streetStart.getPosX() - (i+1)][streetStart.getPosY()].getType() == MapTile.TYPE_EMPTY) {
									world[streetStart.getPosX() - (i+1)][streetStart.getPosY()] = new MapTile(streetStart.getPosX() - (i+1), streetStart.getPosY(), debugRenderer);
								}else break;
							}
						}
					}
				}
				
//				if(!activity) System.out.println("Durchlauf: " + (System.currentTimeMillis() - debugTime) + "ms acitvity => "+activity);
			}
			
			//*******************************************************************************
			//Exit ~ check if done can be set true
			//*******************************************************************************
			
			//Check exit
			MapTile exit = null;
			for(int h = 0; h < world[0].length; h++){
				for(int w = 0; w < world.length; w++) {
					if(world[w][h].isExit()) {
						exit = world[w][h];
					}
				}
			}
			
			if(exit.getPosX() > 0) {
				if(world[exit.getPosX()-1][exit.getPosY()].getType() == MapTile.TYPE_STREET) 
					done = true;
			}
			if(exit.getPosX() < world.length-1) {
				if(world[exit.getPosX()+1][exit.getPosY()].getType() == MapTile.TYPE_STREET)
					done = true;;
			}
			if(exit.getPosY() > 0) {
				if(world[exit.getPosX()][exit.getPosY()-1].getType() == MapTile.TYPE_STREET) 
					done = true;
			}
			if(exit.getPosY() < world[0].length-1) {
				if(world[exit.getPosX()][exit.getPosY()+1].getType() == MapTile.TYPE_STREET) 
					done = true;
			}
		}
		
		//Preparing all ways with the right Texture
		prepareWays();
		//Prepare the buildings or trees
		prepareBuildings();
		//Add the world to the group
		for(int h = 0; h < world[0].length; h++){
			for(int w = 0; w < world.length; w++) {
				if(world[w][h] != null && world[w][h].getType() != MapTile.TYPE_EMPTY) 
					addActor(world[w][h]);
			}
		}
		
		//After drawing the world, draw the fog on the top of it
		addFog(tileSet[0][7]);
		
		System.out.println("~~~~ Map created ~~~~");
		System.out.println("Der Map aufbau hat " + (System.currentTimeMillis() - debugTime) + "ms gedauert");
		System.out.println("Die Map belegt " + ((Gdx.app.getNativeHeap()-mem)/1000000.0) + "MB");
		System.out.println("Das Spiel belegt " + (Gdx.app.getNativeHeap()/1000000.0) + "MB");
		printMapInfo();
		
	}
	
	/**
	 * This method replaces all Street tiles with real street tiles and
	 * looking for double streets to remove.
	 */
	private void prepareWays() {
		int worldWidth = world[0].length;
		int worldHeight = world.length;
		boolean neighbours[] = new boolean[4];
		
		for(int h = 0; h < worldWidth; h++){
			for(int w = 0; w < worldHeight; w++) {
				
				//First set the neighbours to false
				neighbours[NORTH] = neighbours[EAST] = neighbours[SOUTH] = neighbours[WEST] = false;
				
				//Check the neighbours
				if(world[w][h].getType() == MapTile.TYPE_STREET && !world[w][h].isStart() && !world[w][h].isExit()) {
					if(w > 0 				&& world[w-1][h].getType() == MapTile.TYPE_STREET) neighbours[WEST] = true; 
					if(w < (worldWidth-1) 	&& world[w+1][h].getType() == MapTile.TYPE_STREET) neighbours[EAST] = true; 
					if(h > 0 				&& world[w][h-1].getType() == MapTile.TYPE_STREET) neighbours[NORTH] = true; 
					if(h < (worldHeight-1) 	&& world[w][h+1].getType() == MapTile.TYPE_STREET) neighbours[SOUTH] = true; 
				}
				
				//Remove double streets
				/* #=Street o=other or street
				 *   o##
				 *   o?#
				 *   ooo
				 */
				if(neighbours[NORTH] && neighbours[EAST] && !neighbours[WEST] && !neighbours[SOUTH] && world[w+1][h-1].getType() == MapTile.TYPE_STREET) {
					world[w][h] = new MapTile(getLvl1TextureRegion(), w, h, MapTile.TYPE_LVL1, this.debugRenderer);
				/* #=Street o=other or street
				 *   ##o
				 *   #?o
				 *   ooo
				 */
				}else if(neighbours[NORTH] && !neighbours[EAST] && neighbours[WEST] && !neighbours[SOUTH] && world[w-1][h-1].getType() == MapTile.TYPE_STREET) {
					world[w][h] = new MapTile(getLvl1TextureRegion(), w, h, MapTile.TYPE_LVL1, this.debugRenderer);
				/* #=Street o=other or street
				 *   ooo
				 *   #?o
				 *   ##o
				 */
				}else if(!neighbours[NORTH] && !neighbours[EAST] && neighbours[WEST] && neighbours[SOUTH] && world[w-1][h+1].getType() == MapTile.TYPE_STREET) {
					world[w][h] = new MapTile(getLvl1TextureRegion(), w, h, MapTile.TYPE_LVL1, this.debugRenderer);
				/* #=Street o=other or street
				 *   ooo
				 *   o?#
				 *   o##
				 */
				}else if(!neighbours[NORTH] && neighbours[EAST] && !neighbours[WEST] && neighbours[SOUTH] && world[w+1][h+1].getType() == MapTile.TYPE_STREET) {
					world[w][h] = new MapTile(getLvl1TextureRegion(), w, h, MapTile.TYPE_LVL1, this.debugRenderer);
				}
			}
		}
		for(int h = 0; h < worldWidth; h++){
			for(int w = 0; w < worldHeight; w++) {
				
				//First set the neighbours to false
				neighbours[NORTH] = neighbours[EAST] = neighbours[SOUTH] = neighbours[WEST] = false;
				
				//Check the neighbours
				if(world[w][h].getType() == MapTile.TYPE_STREET && !world[w][h].isStart() && !world[w][h].isExit()) {
					if(w > 0 				&& world[w-1][h].getType() == MapTile.TYPE_STREET) neighbours[WEST] = true; 
					if(w < (worldWidth-1) 	&& world[w+1][h].getType() == MapTile.TYPE_STREET) neighbours[EAST] = true; 
					if(h > 0 				&& world[w][h-1].getType() == MapTile.TYPE_STREET) neighbours[NORTH] = true; 
					if(h < (worldHeight-1) 	&& world[w][h+1].getType() == MapTile.TYPE_STREET) neighbours[SOUTH] = true; 
				}
				
				if(neighbours[NORTH] && neighbours[SOUTH] && neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][3], w, h, MapTile.TYPE_STREET, this.debugRenderer);
				}else if(neighbours[NORTH] && neighbours[SOUTH] && !neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][0], w, h, MapTile.TYPE_STREET, this.debugRenderer);
					world[w][h].rotate(90);
					world[w][h].translate(128, -1);
				}else if(!neighbours[NORTH] && neighbours[SOUTH] && neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][2], w, h, MapTile.TYPE_STREET, this.debugRenderer);
				}else if(neighbours[NORTH] && !neighbours[SOUTH] && neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][2], w, h, MapTile.TYPE_STREET, this.debugRenderer);
					world[w][h].rotate(180);
					world[w][h].translate(129, 127);
				}else if(neighbours[NORTH] && neighbours[SOUTH] && !neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][2], w, h, MapTile.TYPE_STREET, this.debugRenderer);
					world[w][h].rotate(90);
					world[w][h].translate(128, -1);
				}else if(neighbours[NORTH] && neighbours[SOUTH] && neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][2], w, h, MapTile.TYPE_STREET, this.debugRenderer);
					world[w][h].rotate(-90);
					world[w][h].translate(1, 128);
				}else if(!neighbours[NORTH] && !neighbours[SOUTH] && neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][0], w, h, MapTile.TYPE_STREET, this.debugRenderer);
				}else if(!neighbours[NORTH] && !neighbours[SOUTH] && neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][4], w, h, MapTile.TYPE_STREET, this.debugRenderer);
				}else if(!neighbours[NORTH] && !neighbours[SOUTH] && !neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][4], w, h, MapTile.TYPE_STREET, this.debugRenderer);
					world[w][h].rotate(180);
					world[w][h].translate(129, 127);
				}else if(!neighbours[NORTH] && neighbours[SOUTH] && !neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][4], w, h, MapTile.TYPE_STREET, this.debugRenderer);
					world[w][h].rotate(90);
					world[w][h].translate(128, -1);
				}else if(neighbours[NORTH] && !neighbours[SOUTH] && !neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][4], w, h, MapTile.TYPE_STREET, this.debugRenderer);
					world[w][h].rotate(-90);
					world[w][h].translate(1, 128);
				}else if(!neighbours[NORTH] && neighbours[SOUTH] && neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][1], w, h, MapTile.TYPE_STREET, this.debugRenderer);
				}else if(neighbours[NORTH] && !neighbours[SOUTH] && !neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][1], w, h, MapTile.TYPE_STREET, this.debugRenderer);
					world[w][h].rotate(180);
					world[w][h].translate(129, 127);
				}else if(!neighbours[NORTH] && neighbours[SOUTH] && !neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][1], w, h, MapTile.TYPE_STREET, this.debugRenderer);
					world[w][h].rotate(90);
					world[w][h].translate(128, -1);
				}else if(neighbours[NORTH] && !neighbours[SOUTH] && neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][1], w, h, MapTile.TYPE_STREET, this.debugRenderer);
					world[w][h].rotate(-90);
					world[w][h].translate(1, 128);
				}
//				if(world[w][h].getType() == MapTile.TYPE_STREET) world[w][h].setType(MapTile.TYPE_STREET);
			}
		}
	}

	/**
	 * This method replaces all Buildings andreplace it with real buildings.
	 * If it is happen it will also look if some buildings can upgraded to
	 * lvl2 or lvl3 buildings.
	 */
	private void prepareBuildings() {
		
		float chanceLvl2 = 0.15f;
		float chanceLvl3 = 0.25f;
		
		int worldWidth = world[0].length;
		int worldHeight = world.length;
		boolean neighbours[] = new boolean[4];
		
		for(int h = 0; h < worldWidth; h++){
			for(int w = 0; w < worldHeight; w++) {
				//First set the neighbours to false
				neighbours[NORTH] = neighbours[EAST] = neighbours[SOUTH] = neighbours[WEST] = false;
				
				//Check the neighbours
				if(world[w][h].getType() == MapTile.TYPE_LVL1 && !world[w][h].isStart() && !world[w][h].isExit()) {
					if(w > 0 				&& world[w-1][h].getType() == MapTile.TYPE_LVL1) neighbours[WEST] = true; 
					if(w < (worldWidth-1) 	&& world[w+1][h].getType() == MapTile.TYPE_LVL1) neighbours[EAST] = true; 
					if(h > 0 				&& world[w][h-1].getType() == MapTile.TYPE_LVL1) neighbours[NORTH] = true; 
					if(h < (worldHeight-1) 	&& world[w][h+1].getType() == MapTile.TYPE_LVL1) neighbours[SOUTH] = true; 
				}
				
				if(neighbours[WEST] && Math.random() < chanceLvl2 && w > 0 && w < worldWidth-1) {
					//Set MapTiles
					world[w][h] = new MapTile(tileSet[4][1], w, h, MapTile.TYPE_LVL2, this.debugRenderer);
					world[w-1][h] = new MapTile(tileSet[4][0], w-1, h, MapTile.TYPE_LVL2, this.debugRenderer);
					//Set Neighbors
					world[w][h].setWestNeighbor(world[w-1][h]);
					world[w-1][h].setEastNeighbor(world[w][h]);
				}else if(neighbours[EAST] && Math.random() < chanceLvl2 && w > 0 && w < worldWidth-1) {
					world[w][h] = new MapTile(tileSet[4][0], w, h, MapTile.TYPE_LVL2, this.debugRenderer);
					world[w+1][h] = new MapTile(tileSet[4][1], w+1, h, MapTile.TYPE_LVL2, this.debugRenderer);
					world[w][h].setEastNeighbor(world[w+1][h]);
					world[w+1][h].setWestNeighbor(world[w][h]);
				}else if(neighbours[NORTH] && Math.random() < chanceLvl2 && h > 0 && h < worldHeight-1) {
					world[w][h] = new MapTile(tileSet[5][2], w, h, MapTile.TYPE_LVL2, this.debugRenderer);
					world[w][h-1] = new MapTile(tileSet[4][2], w, h-1, MapTile.TYPE_LVL2, this.debugRenderer);
					world[w][h].setNorthNeighbor(world[w][h-1]);
					world[w][h-1].setSouthNeighbor(world[w][h]);
				}else if(neighbours[SOUTH] && Math.random() < chanceLvl2 && h > 0 && h < worldHeight-1) {
					world[w][h] = new MapTile(tileSet[4][2], w, h, MapTile.TYPE_LVL2, this.debugRenderer);
					world[w][h+1] = new MapTile(tileSet[5][2], w, h+1, MapTile.TYPE_LVL2, this.debugRenderer);
					world[w][h].setNorthNeighbor(world[w][h+1]);
					world[w][h+1].setSouthNeighbor(world[w][h]);
				}
				
				//A Second run for lvl3 buildings
				neighbours[NORTH] = neighbours[EAST] = neighbours[SOUTH] = neighbours[WEST] = false;
				
				//Check the neighbours
				if(world[w][h].getType() == MapTile.TYPE_LVL2 && !world[w][h].isStart() && !world[w][h].isExit()) {
					if(w > 0 				&& world[w-1][h].getType() == MapTile.TYPE_LVL2) neighbours[WEST] = true; 
					if(w < (worldWidth-1) 	&& world[w+1][h].getType() == MapTile.TYPE_LVL2) neighbours[EAST] = true; 
					if(h > 0 				&& world[w][h-1].getType() == MapTile.TYPE_LVL2) neighbours[NORTH] = true; 
					if(h < (worldHeight-1) 	&& world[w][h+1].getType() == MapTile.TYPE_LVL2) neighbours[SOUTH] = true; 
				}
				
				if(neighbours[NORTH] && neighbours[EAST] && world[w+1][h-1].getType() == MapTile.TYPE_LVL2) {
					world[w][h] 	= new MapTile(tileSet[7][0], w, h, MapTile.TYPE_LVL3, this.debugRenderer);
					world[w+1][h] 	= new MapTile(tileSet[7][1], w+1, h, MapTile.TYPE_LVL3, this.debugRenderer);
					world[w+1][h-1] = new MapTile(tileSet[6][1], w+1, h-1, MapTile.TYPE_LVL3, this.debugRenderer);
					world[w][h-1] 	= new MapTile(tileSet[6][0], w, h-1, MapTile.TYPE_LVL3, this.debugRenderer);
					
					/*
					 *  w/h-1 	w+1/h-1
					 * 	w/h		w+1/h
					 * 	
					 */
					world[w][h].setEastNeighbor(world[w+1][h]);
					world[w][h].setNorthNeighbor(world[w][h-1]);
					
					world[w+1][h].setWestNeighbor(world[w][h]);
					world[w+1][h].setNorthNeighbor(world[w+1][h-1]);					

					world[w+1][h-1].setWestNeighbor(world[w][h-1]);					
					world[w+1][h-1].setSouthNeighbor(world[w+1][h]);
					
					world[w][h-1].setSouthNeighbor(world[w][h]);
					world[w][h-1].setEastNeighbor(world[w+1][h-1]);
				}
				
				if(world[w][h].getType() == MapTile.TYPE_LVL1) world[w][h] = new MapTile(getLvl1TextureRegion(), w, h, MapTile.TYPE_LVL1, this.debugRenderer);
			}
		}
		
		//Fix all the splitted level 2 Buildings
		boolean checkSplitBug = true;
		boolean onLvl3 = false;
		for(int h = 0; h < worldWidth; h++){
			for(int w = 0; w < worldHeight; w++) {
				if(world[w][h].getType() == MapTile.TYPE_LVL2) {
					if(w > 0 				&& world[w-1][h].getType() == MapTile.TYPE_LVL3) onLvl3 = true; 
					if(w < (worldWidth-1) 	&& world[w+1][h].getType() == MapTile.TYPE_LVL3) onLvl3 = true; 
					if(h > 0 				&& world[w][h-1].getType() == MapTile.TYPE_LVL3) onLvl3 = true; 
					if(h < (worldHeight-1) 	&& world[w][h+1].getType() == MapTile.TYPE_LVL3) onLvl3 = true; 
					
					if(w > 0 				&& world[w-1][h].getType() == MapTile.TYPE_LVL2) checkSplitBug = false; 
					if(w < (worldWidth-1) 	&& world[w+1][h].getType() == MapTile.TYPE_LVL2) checkSplitBug = false; 
					if(h > 0 				&& world[w][h-1].getType() == MapTile.TYPE_LVL2) checkSplitBug = false; 
					if(h < (worldHeight-1) 	&& world[w][h+1].getType() == MapTile.TYPE_LVL2) checkSplitBug = false; 
				
					if(checkSplitBug && onLvl3) {
						world[w][h] = new MapTile(getLvl1TextureRegion(), w, h, MapTile.TYPE_LVL1, this.debugRenderer);
						
						checkSplitBug = true;
						onLvl3 = false;
					}
				}
			}
		}
	}

	/**
	 * Moves the Character in the middle of the screen without
	 * leaving the borders of the map
	 */
	public void moveCameraToCharacter() {
		//Calculate the player position
		int calcedX = -(charRef.getMapX()*MapTile.TILE_WIDTH) + (Gdx.graphics.getWidth()/2);
		int calcedY = charRef.getMapY()*MapTile.TILE_WIDTH - (Gdx.graphics.getHeight()/2);
		
		//Don't leave the borders!
		if(calcedX > 0) calcedX = 0;
		else if(calcedX < -getMapWidth() + Gdx.graphics.getWidth()) calcedX = -getMapWidth() + Gdx.graphics.getWidth();
		if(calcedY < -64) calcedY = -64;
		else if(calcedY > getMapHeight() - Gdx.graphics.getHeight()) calcedY = getMapHeight() - Gdx.graphics.getHeight();
		
		//Set the new position to camera
		this.setPosition(calcedX, calcedY);
	}
	
	/**
	 * This method prints a test Info on the console
	 */
	private void printMapInfo() {
		int empty = 0;
		int level1 = 0;
		int level2 = 0;
		int level3 = 0;
		int streets = 0;
		int worldWidth = world[0].length;
		int worldHeight = world.length;
		for(int h = 0; h < worldWidth; h++){
			for(int w = 0; w < worldHeight; w++) {
				if(world[w][h].getType() == MapTile.TYPE_EMPTY) empty++;
				if(world[w][h].getType() == MapTile.TYPE_STREET) streets++;
				if(world[w][h].getType() == MapTile.TYPE_LVL1) level1++;
				if(world[w][h].getType() == MapTile.TYPE_LVL2) level2++;
				if(world[w][h].getType() == MapTile.TYPE_LVL3) level3++;
			}
		}
		
		System.out.println("Info:");
		System.out.println("Fläche gesamt:\t\t" + worldWidth + "x" + worldHeight + " = " + (worldHeight*worldWidth));
		System.out.println("Leere Flächen:\t\t" + empty);
		System.out.println("Straßen:\t\t" + streets);
		System.out.println("Level1:\t\t\t" + level1);
		System.out.println("Level2:\t\t\t" + (level2/2));
		System.out.println("Level3:\t\t\t" + (level3/4));
	}
	
	public void debugInfo() {
		int x = charRef.getMapX();
		int y = charRef.getMapY();
		System.out.println("~~~~MapTile " + x + " / " + y + "~~~~\n" +
				world[x][y].toString());
	}
	
	/**
	 * Method to handle movement of a charakter.
	 * 
	 * @param x move the character to the x position of the map
	 * @param y move the character to the y position of the map
	 */
	public void moveCharacter(int x, int y) {
		//Set the charakter map coordinates
		charRef.setMapCoordinates(x, y);
		//Set the graphic of the character to this position
		charPointer.setPosition(x, y);
		//calculate the new fog
		calcFog();
		//reset the timer for pressing the direction
		arrowTimer = 0;
		//try to move the character in the middle of the screen
		moveCameraToCharacter();
		//Reduce Stomach
		charRef.reduceStomach(5);
		//Reduce Thirst
		charRef.reduceThirst(5);
		
		//Draw the UI again
		refreshUI();
	}
	
	/**
	 * Let the Actor act.
	 * @param float  delta is the time that happens after each frame
	 */
	private float arrowTimer = 0;
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(((GameHandler)getParent()).getState() == GameHandler.STATE_MAP) {
			
			//Add a dragging Method for Mouse and Touch
			if(InputHelper.DRAG && dragX == 0 && dragY == 0) {
				dragX = InputHelper.DRAG_X;
				dragY = InputHelper.DRAG_Y;
			}else if(InputHelper.DRAG) {
				int vectorX = (int)(InputHelper.DRAG_X - dragX);
				int vectorY = (int)(InputHelper.DRAG_Y - dragY);
				dragX = dragY = 0;
				translate(vectorX, vectorY);
				
				//Correct x and y for map borders
				if(getX() > 0) setX(0);
				else if(getX() < -getMapWidth() + Gdx.graphics.getWidth()) setX(-getMapWidth() + Gdx.graphics.getWidth());
				if(getY() < -64) setY(-64);
				else if(getY() > getMapHeight() - Gdx.graphics.getHeight()) setY(getMapHeight() - Gdx.graphics.getHeight());
				
			}else dragX = dragY = 0;
			
			//Handle charakter movement
			arrowTimer += delta;
			boolean moved = false;
			int x = charRef.getMapX();
			int y = charRef.getMapY();
			if(InputHelper.DOWN && y < height && arrowTimer > 0.5) {
				if(world[x][y+1].getType() == MapTile.TYPE_STREET) {
					moveCharacter(x, y+1);
					moved = true;
				}else if (world[x][y+1].getEventID() != 0) {
					((GameHandler)getParent()).loadEvent(x, y+1);
				}
			}else if(InputHelper.UP && y > 0 && arrowTimer > 0.5) {
				if(world[x][y-1].getType() == MapTile.TYPE_STREET) {
					moveCharacter(x, y-1);
					moved = true;
				}else if (world[x][y-1].getEventID() != 0) {
					((GameHandler)getParent()).loadEvent(x, y-1);
				}
			}else if(InputHelper.LEFT && x > 0 && arrowTimer > 0.5) {
				if(world[x-1][y].getType() == MapTile.TYPE_STREET) {
					moveCharacter(x-1, y);
					moved = true;
				}else if (world[x-1][y].getEventID() != 0) {
					((GameHandler)getParent()).loadEvent(x-1, y);
				}
			}else if(InputHelper.RIGHT && x < width && arrowTimer > 0.5) {
				if(world[x+1][y].getType() == MapTile.TYPE_STREET) {
					moveCharacter(x+1, y);
					moved = true;
				}else if (world[x+1][y].getEventID() != 0) {
					((GameHandler)getParent()).loadEvent(x+1, y);
				}
			}
			
			if(moved) {
				if(getTile(charRef.getMapX(), charRef.getMapY()).getEventID() != 0) {
					((GameHandler)getParent()).loadEvent();
				}
				moved = false;
			}
		}
	}
}
