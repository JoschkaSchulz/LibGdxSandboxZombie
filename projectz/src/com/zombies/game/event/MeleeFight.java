package com.zombies.game.event;

import java.util.LinkedList;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zombies.game.charakter.Charakter;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.TextureHelper;

public class MeleeFight extends Group {
	
	/******************************************
	 * 		inner methods
	 ******************************************/
	
	/**
	 * This Queue is used to manage the falling arrows
	 */
	public class ZombieQueue{
		/********************
		 * 	   variables
		 ********************/
		
		public static final int MAX_ZOMBIES = 8;
		
		public static final int QUEUE_EMPTY		= -1;
		public static final int ARROW_LEFT 		= 0;
		public static final int ARROW_RIGHT 	= 1;
		public static final int ARROW_UP 		= 2;
		public static final int ARROW_DOWN 		= 3;
		
		private LinkedList<Integer>[] slots;
		private LinkedList<Integer> queue;
		/********************
		 * 	  constructor
		 ********************/
		
		@SuppressWarnings("unchecked")
		public ZombieQueue() {
			slots = new LinkedList[MAX_ZOMBIES];
			queue = new LinkedList<Integer>();
			
			for(int i = 0; i < MAX_ZOMBIES; i++) {
				slots[i] = new LinkedList<Integer>();
			}
			
			//Fill the first queue
			for(int i = 0; i < 3; i++) {
				slots[0].add((int)(Math.random()*4));
			}
			
			fillMainQueue(3);
		}
		
		/********************
		 * 	  methods
		 ********************/
		
		/**
		 * Removes a zombie from the slots
		 * 
		 * @return true on success  and false if the slots are all empty
		 */
		public boolean removeZombie() {
			//Searching a free queue (reverse)
			int foundQueue = -1;
			for(int i = (MAX_ZOMBIES-1); i >= 0; i--) {
				if(!slots[i].isEmpty()) {
					foundQueue = i;
					break;
				}
			}
			
			//return false or add the arrows
			if(foundQueue == -1) {
				return false;
			}else{
				slots[foundQueue].clear();
				return true;
			}
		}
		
		/**
		 * Adds a new Zombie to the queue or returns false if all slots are full
		 * 
		 * @return true if success or false if MAX_ZOMBIES was reached
		 */
		public boolean addZombie() {
			//Searching a free queue
			int foundQueue = -1;
			for(int i = 0; i < MAX_ZOMBIES; i++) {
				if(slots[i].isEmpty()) {
					foundQueue = i;
					break;
				}
			}
			
			//return false or add the arrows
			if(foundQueue == -1) {
				return false;
			}else{
				for(int i = 0; i < 3; i++) {
					slots[foundQueue].add((int)(Math.random()*4));
				}
				return true;
			}
		}
		
		/**
		 * fills up the queue with new arrows
		 * 
		 * @param iterations the number of runs of the method (every run 
		 * = MAX_ZOMBIES new arrows)
		 */
		private void fillMainQueue(int iterations) {
			for(int n = 0; n < iterations; n++) {
				for(int i = 0; i < MAX_ZOMBIES; i++) {
					if(slots[i].isEmpty()) { 
						queue.addLast(QUEUE_EMPTY);
					}else{
						queue.addLast(slots[i].getFirst());
						slots[i].removeFirst();
						slots[i].addLast((int)(Math.random()*4));
					}
				}
			}
		}
		
		/**
		 * Gets the first element of the queue and fills it if there were to 
		 * less arrows left
		 * 
		 * @return an integer that represents an arrow or an empty space, see
		 * static variables
		 */
		public int pull() {
			if(queue.size() <= 10) {
				fillMainQueue(3);
			}
			
			int result = queue.getFirst(); 
			queue.removeFirst();
			return result;
		}
		
	}
	
	/******************************************
	 * 		variables
	 ******************************************/
	
	private float fallingSpeed;			//The speed the bubbles falls
	private float zombieCount;			//The amount of zombies in the fight
	private ZombieQueue zombieQueue; 	//Contains the arrows
	
	private Image warning;		//The warning that should be shown if there are to much zombies
	private Image background;	//The Background of the melee fight
	private Charakter charRef;	//a charakter refernece
	
	/******************************************
	 * 		constructors
	 ******************************************/
	
	public MeleeFight(Charakter charRef) {
		this.background = new Image(TextureHelper.FIGHT_MELEE_BACKGROUND);
		this.background.setPosition(GUIHelper.getPercentPositionX(10), GUIHelper.getNewCoordinates(50, 512));
		this.background.setWidth(1024);
		this.background.setHeight(512);
		addActor(background);
		
		this.charRef = charRef;
	}
	
	/******************************************
	 * 		getter and setter
	 ******************************************/
	
	/******************************************
	 * 		methods
	 ******************************************/

	public void addZombie(){
		this.zombieCount++;
		if(this.zombieCount > this.charRef.getMaxMeleeZombies()) this.charRef.setCurrentLP(0);
	}
	
	public void endMeleeFight() {
		Fight fightRef = (Fight)getParent();
		fightRef.endMelee();
	}
}
