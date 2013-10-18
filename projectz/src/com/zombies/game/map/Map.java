package com.zombies.game.map;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.zombies.helper.InputHelper;

public class Map extends Group {
	
	public static final int TYPE_CITY 		= 0;
	public static final int TYPE_FOREST 	= 1;
	
	private static final int NORTH 	= 0;
	private static final int SOUTH 	= 1;
	private static final int WEST 	= 2;
	private static final int EAST 	= 3;
	
	private float dragX;
	private float dragY;
	
	private MapTile[][] world;
	
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
		boolean activity = true;
		int reserve[] = new int[5];
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
								}
							}
							if(choosenDirection == SOUTH && (streetStart.getPosY() + (i+1)) < world[0].length) {
								if(world[streetStart.getPosX()][streetStart.getPosY() + (i+1)].getType() == MapTile.TYPE_EMPTY) {
									world[streetStart.getPosX()][streetStart.getPosY() + (i+1)] = new MapTile(streetStart.getPosX(), streetStart.getPosY() + (i+1));
								}
							}
							if(choosenDirection == EAST && (streetStart.getPosX() + (i+1)) < world.length) {
								if(world[streetStart.getPosX() + (i+1)][streetStart.getPosY()].getType() == MapTile.TYPE_EMPTY) {
									world[streetStart.getPosX() + (i+1)][streetStart.getPosY()] = new MapTile(streetStart.getPosX() + (i+1), streetStart.getPosY());
								}
							}
							if(choosenDirection == WEST && (streetStart.getPosX() - (i+1)) >= 0) {
								if(world[streetStart.getPosX() - (i+1)][streetStart.getPosY()].getType() == MapTile.TYPE_EMPTY) {
									world[streetStart.getPosX() - (i+1)][streetStart.getPosY()] = new MapTile(streetStart.getPosX() - (i+1), streetStart.getPosY());
								}
							}
						}
					}
				}
				
				//*******************************************************************************
				//Exit ~ check if done can be set true
				//*******************************************************************************
				if(!activity) System.out.println("Durchlauf: " + (System.currentTimeMillis() - debugTime) + "ms acitvity => "+activity);
			}
			
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
		
		//Add the world to the group
		for(int h = 0; h < world[0].length; h++){
			for(int w = 0; w < world.length; w++) {
				if(world[w][h] != null && world[w][h].getType() != MapTile.TYPE_EMPTY) addActor(world[w][h]);
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
