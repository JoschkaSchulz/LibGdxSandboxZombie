package com.zombies.game.map;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zombies.helper.InputHelper;

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
	private TextureRegion tileSet[][];
	
	/*********************************************************
	 * 			Constructor
	 *********************************************************/
	
	public Map(int width, int height) {
		dragX = 0;
		dragY = 0;
		
		this.width = width;
		this.height = height;
		world = new MapTile[width][height];
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
	
	
	/*********************************************************
	 * 			Methods
	 *********************************************************/
	
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
		getParent().addActor(new MapUI(this));
		
		switch(type) {
			default:	//city
				Texture tileSetTexture = new Texture(Gdx.files.internal("data/gfx/map_tiles/tileset_city.png"));
				tileSet = TextureRegion.split(tileSetTexture, 128, 128);
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
					world[w][h] = new MapTile(w, h);
					world[w][h].setType(MapTile.TYPE_EMPTY);
				}
			}
			
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
				
				//Select buildings to set in the free Room (TODO: at the moment only 1x1)
				if(freePlaces.size() > 0) {
					int until = (int)(Math.random()*(freePlaces.size()-decreaseTiles)) + 1;
					for(int i = 0; i < until; i++) {
						int index = (int)(Math.random()*(freePlaces.size()-1));
						MapTile mt = freePlaces.get(index);
						world[mt.getPosX()][mt.getPosY()] = new MapTile(mt.getPosX(),mt.getPosY());
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
						world[streetStart.getPosX()][streetStart.getPosY()] = new MapTile(streetStart.getPosX(), streetStart.getPosY());
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
						world[streetStart.getPosX()][streetStart.getPosY()] = new MapTile(streetStart.getPosX(), streetStart.getPosY());
						world[streetStart.getPosX()][streetStart.getPosY()].setType(MapTile.TYPE_STREET);
						for(int i = 0; i < streetCount; i++) {
							if(choosenDirection == NORTH && (streetStart.getPosY() - (i+1)) >= 0) {
								if(world[streetStart.getPosX()][streetStart.getPosY() - (i+1)].getType() == MapTile.TYPE_EMPTY) {
									world[streetStart.getPosX()][streetStart.getPosY() - (i+1)] = new MapTile(streetStart.getPosX(), streetStart.getPosY() - (i+1));
								}else break;
							}
							if(choosenDirection == SOUTH && (streetStart.getPosY() + (i+1)) < world[0].length) {
								if(world[streetStart.getPosX()][streetStart.getPosY() + (i+1)].getType() == MapTile.TYPE_EMPTY) {
									world[streetStart.getPosX()][streetStart.getPosY() + (i+1)] = new MapTile(streetStart.getPosX(), streetStart.getPosY() + (i+1));
								}else break;
							}
							if(choosenDirection == EAST && (streetStart.getPosX() + (i+1)) < world.length) {
								if(world[streetStart.getPosX() + (i+1)][streetStart.getPosY()].getType() == MapTile.TYPE_EMPTY) {
									world[streetStart.getPosX() + (i+1)][streetStart.getPosY()] = new MapTile(streetStart.getPosX() + (i+1), streetStart.getPosY());
								}else break;
							}
							if(choosenDirection == WEST && (streetStart.getPosX() - (i+1)) >= 0) {
								if(world[streetStart.getPosX() - (i+1)][streetStart.getPosY()].getType() == MapTile.TYPE_EMPTY) {
									world[streetStart.getPosX() - (i+1)][streetStart.getPosY()] = new MapTile(streetStart.getPosX() - (i+1), streetStart.getPosY());
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
				if(world[w][h] != null && world[w][h].getType() != MapTile.TYPE_EMPTY) addActor(world[w][h]);
			}
		}
		
		System.out.println("~~~~ Map created ~~~~");
		System.out.println("Der Map aufbau hat " + (System.currentTimeMillis() - debugTime) + "ms gedauert");
		
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
					world[w][h] = new MapTile(tileSet[1][0], w, h, MapTile.TYPE_LVL1);
				/* #=Street o=other or street
				 *   ##o
				 *   #?o
				 *   ooo
				 */
				}else if(neighbours[NORTH] && !neighbours[EAST] && neighbours[WEST] && !neighbours[SOUTH] && world[w-1][h-1].getType() == MapTile.TYPE_STREET) {
					world[w][h] = new MapTile(tileSet[1][0], w, h, MapTile.TYPE_LVL1);
				/* #=Street o=other or street
				 *   ooo
				 *   #?o
				 *   ##o
				 */
				}else if(!neighbours[NORTH] && !neighbours[EAST] && neighbours[WEST] && neighbours[SOUTH] && world[w-1][h+1].getType() == MapTile.TYPE_STREET) {
					world[w][h] = new MapTile(tileSet[1][0], w, h, MapTile.TYPE_LVL1);
				/* #=Street o=other or street
				 *   ooo
				 *   o?#
				 *   o##
				 */
				}else if(!neighbours[NORTH] && neighbours[EAST] && !neighbours[WEST] && neighbours[SOUTH] && world[w+1][h+1].getType() == MapTile.TYPE_STREET) {
					world[w][h] = new MapTile(tileSet[1][0], w, h, MapTile.TYPE_LVL1);
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
					world[w][h] = new MapTile(tileSet[0][3], w, h, MapTile.TYPE_STREET);
				}else if(neighbours[NORTH] && neighbours[SOUTH] && !neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][0], w, h, MapTile.TYPE_STREET);
					world[w][h].rotate(90);
					world[w][h].translate(128, -1);
				}else if(!neighbours[NORTH] && neighbours[SOUTH] && neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][2], w, h, MapTile.TYPE_STREET);
				}else if(neighbours[NORTH] && !neighbours[SOUTH] && neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][2], w, h, MapTile.TYPE_STREET);
					world[w][h].rotate(180);
					world[w][h].translate(129, 127);
				}else if(neighbours[NORTH] && neighbours[SOUTH] && !neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][2], w, h, MapTile.TYPE_STREET);
					world[w][h].rotate(90);
					world[w][h].translate(128, -1);
				}else if(neighbours[NORTH] && neighbours[SOUTH] && neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][2], w, h, MapTile.TYPE_STREET);
					world[w][h].rotate(-90);
					world[w][h].translate(1, 128);
				}else if(!neighbours[NORTH] && !neighbours[SOUTH] && neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][0], w, h, MapTile.TYPE_STREET);
				}else if(!neighbours[NORTH] && !neighbours[SOUTH] && neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][4], w, h, MapTile.TYPE_STREET);
				}else if(!neighbours[NORTH] && !neighbours[SOUTH] && !neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][4], w, h, MapTile.TYPE_STREET);
					world[w][h].rotate(180);
					world[w][h].translate(129, 127);
				}else if(!neighbours[NORTH] && neighbours[SOUTH] && !neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][4], w, h, MapTile.TYPE_STREET);
					world[w][h].rotate(90);
					world[w][h].translate(128, -1);
				}else if(neighbours[NORTH] && !neighbours[SOUTH] && !neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][4], w, h, MapTile.TYPE_STREET);
					world[w][h].rotate(-90);
					world[w][h].translate(1, 128);
				}else if(!neighbours[NORTH] && neighbours[SOUTH] && neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][1], w, h, MapTile.TYPE_STREET);
				}else if(neighbours[NORTH] && !neighbours[SOUTH] && !neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][1], w, h, MapTile.TYPE_STREET);
					world[w][h].rotate(180);
					world[w][h].translate(129, 127);
				}else if(!neighbours[NORTH] && neighbours[SOUTH] && !neighbours[WEST] && neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][1], w, h, MapTile.TYPE_STREET);
					world[w][h].rotate(90);
					world[w][h].translate(128, -1);
				}else if(neighbours[NORTH] && !neighbours[SOUTH] && neighbours[WEST] && !neighbours[EAST]){ 
					world[w][h] = new MapTile(tileSet[0][1], w, h, MapTile.TYPE_STREET);
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
					world[w][h] = new MapTile(tileSet[1][1], w, h, MapTile.TYPE_LVL2);
					world[w-1][h] = new MapTile(tileSet[1][1], w-1, h, MapTile.TYPE_LVL2);
				}else if(neighbours[EAST] && Math.random() < chanceLvl2 && w > 0 && w < worldWidth-1) {
					world[w][h] = new MapTile(tileSet[1][1], w, h, MapTile.TYPE_LVL2);
					world[w+1][h] = new MapTile(tileSet[1][1], w+1, h, MapTile.TYPE_LVL2);
				}else if(neighbours[NORTH] && Math.random() < chanceLvl2 && h > 0 && h < worldHeight-1) {
					world[w][h] = new MapTile(tileSet[1][1], w, h, MapTile.TYPE_LVL2);
					world[w][h-1] = new MapTile(tileSet[1][1], w, h-1, MapTile.TYPE_LVL2);
				}else if(neighbours[SOUTH] && Math.random() < chanceLvl2 && h > 0 && h < worldHeight-1) {
					world[w][h] = new MapTile(tileSet[1][1], w, h, MapTile.TYPE_LVL2);
					world[w][h+1] = new MapTile(tileSet[1][1], w, h+1, MapTile.TYPE_LVL2);
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
					world[w][h] 	= new MapTile(tileSet[1][2], w, h, MapTile.TYPE_LVL3);
					world[w+1][h] 	= new MapTile(tileSet[1][2], w+1, h, MapTile.TYPE_LVL3);
					world[w+1][h-1] = new MapTile(tileSet[1][2], w+1, h-1, MapTile.TYPE_LVL3);
					world[w][h-1] 	= new MapTile(tileSet[1][2], w, h-1, MapTile.TYPE_LVL3);
				}
				
				if(world[w][h].getType() == MapTile.TYPE_LVL1) world[w][h] = new MapTile(tileSet[1][0], w, h, MapTile.TYPE_LVL1);
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
						world[w][h] = new MapTile(tileSet[1][0], w, h, MapTile.TYPE_LVL1);
						
						checkSplitBug = true;
						onLvl3 = false;
					}
				}
			}
		}
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
	
	/**
	 * Let the Actor act.
	 * @param float  delta is the time that happens after each frame
	 */
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
			
//			System.out.println(getX() + "/" + getY());
			//Correct x and y for map borders
			if(getX() > 0) setX(0);
			else if(getX() < -getMapWidth() + Gdx.graphics.getWidth()) setX(-getMapWidth() + Gdx.graphics.getWidth());
			if(getY() < 0) setY(0);
			else if(getY() > getMapHeight() - Gdx.graphics.getHeight()) setY(getMapHeight() - Gdx.graphics.getHeight());
		}else dragX = dragY = 0;
	}
}
