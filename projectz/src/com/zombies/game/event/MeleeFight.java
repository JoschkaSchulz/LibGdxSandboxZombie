package com.zombies.game.event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zombies.game.charakter.Charakter;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.InputHelper;
import com.zombies.helper.SkinHelper;
import com.zombies.helper.SoundHelper;
import com.zombies.helper.TextureHelper;


/**
 * 
 */
public class MeleeFight extends Group {
	/******************************************
	 * 		variables
	 ******************************************/
	
	private static int ARROW_LEFT 	= 0;
	private static int ARROW_RIGHT 	= 1;
	private static int ARROW_UP 	= 2;
	private static int ARROW_DOWN 	= 3;
	
	private static int COUNTER_UP  		= 0;
	private static int COUNTER_RIGHT  	= 1;
	private static int COUNTER_DOWN  	= 2;
	private static int COUNTER_LEFT  	= 3;
	
	private int meleeX;				//x position of the melee window
	private int meleeY;				//y position of the melee window
	private int meleeW;				//width of the melee weapon
	private int meleeH;				//height of the melee weapon
	private int zombiesLP;			//The total amount of ZombieLP
	private float keyTimer;			//Timer for pressing keys after the other
	
	private Charakter charRef;	//a charakter refernece
	private int zombieCount;	//count of the Zombies in melee fight
	private TimerWidget[] counter;
	private ZombieDrawer zDrawer;
	private float speed;		//The speed the timer runs down
	private int randomKey;
	
	//Graphics
	private TextureRegion[] arrowImage;	//A array with the images for the arrows
	private Image background;			//The background of the window
	/******************************************
	 * 		constructors
	 ******************************************/
	
	public MeleeFight(Charakter charRef) {
		//Create the array for the images
		arrowImage = new TextureRegion[]{TextureHelper.FIGHT_MELEE_LEFT_ARROW,
				TextureHelper.FIGHT_MELEE_RIGHT_ARROW,
				TextureHelper.FIGHT_MELEE_UP_ARROW,
				TextureHelper.FIGHT_MELEE_DOWN_ARROW,};
		//holds a charakter reference
		this.charRef = charRef;
		//start the init
		this.init();
	}
	
	private void init() {
		//Position of the melee window
		this.meleeX = GUIHelper.getPercentPositionX(10);
		this.meleeY = GUIHelper.getPercentPositionY(20);
		this.meleeH = 512;
		this.meleeW = 1024;
		
		//Doesn't start
		this.randomKey = -1;
		
		//Default ZombieLP
		this.zombiesLP = 0;
		
		//Set the defauld zombie count on one
		this.zombieCount = 0;

		//Generates the UI for the meele fight
		this.createMeeleUI();
		
		//Set the default speed
		this.speed = 50;
		this.keyTimer = 5;	//Value greater then 1-4 as default time var
		
		//Creating 4 counters for 4 directions
		counter = new TimerWidget[4];
		counter[COUNTER_UP] = new TimerWidget(meleeX + (meleeW/2)				, meleeY + (meleeH - (meleeH/100*20)));	//up
		counter[COUNTER_RIGHT] = new TimerWidget(meleeX + (meleeW - (meleeW/100*20)), meleeY + (meleeH/2));
		counter[COUNTER_DOWN] = new TimerWidget(meleeX + (meleeW/2)				, meleeY + (meleeH/100*10));
		counter[COUNTER_LEFT] = new TimerWidget(meleeX + (meleeW/100*10)			, meleeY + (meleeH/2));
//		counter[0].startTimer(this.speed);
//		counter[1].startTimer(this.speed);
//		counter[2].startTimer(this.speed);
//		counter[3].startTimer(this.speed);
		this.addActor(counter[COUNTER_UP]);
		this.addActor(counter[COUNTER_RIGHT]);
		this.addActor(counter[COUNTER_DOWN]);
		this.addActor(counter[COUNTER_LEFT]);
	}
	
	/******************************************
	 * 		getter and setter
	 ******************************************/
	
	/******************************************
	 * 		methods
	 ******************************************/

	public void addZombie(int lp) {
		zombieCount++;
		zombiesLP += lp;
		speed = 45 + (zombieCount*5);
		counter[COUNTER_UP].setSpeed(speed);
		counter[COUNTER_RIGHT].setSpeed(speed);
		counter[COUNTER_DOWN].setSpeed(speed);
		counter[COUNTER_LEFT].setSpeed(speed);
	}
	
	private void createMeeleUI() {
		this.background = new Image(TextureHelper.FIGHT_MELEE_BACKGROUND);
		this.background.setPosition(meleeX, meleeY);
		this.background.setWidth(meleeW);
		this.background.setHeight(meleeH);
		System.out.println(meleeX + "/" + meleeY + " == " + meleeW + "/" + meleeH);
		addActor(background);
		
		//Create the Zombie drawer
		this.zDrawer = new ZombieDrawer();
		addActor(zDrawer);
	}
	
	public void endMeleeFight() {
		Fight fightRef = (Fight)getParent();
		fightRef.endMelee();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		keyTimer += delta;
		
		if(zombiesLP <= 0) {
			endMeleeFight();
		}else{
			if(randomKey == -1){
				randomKey = (int) Math.round(Math.random()*3);
				counter[randomKey].startTimer(speed);
			}
			
			if(keyTimer >= 0.5) {
				if(InputHelper.LEFT) {
					if(!counter[COUNTER_LEFT].stop()){
						charRef.reduceLP(5);
					}
					keyTimer = 0;
				}else if(InputHelper.RIGHT) {
					if(!counter[COUNTER_RIGHT].stop()){
						charRef.reduceLP(5);
					}	
					keyTimer = 0;
				}else if(InputHelper.UP) {
					if(!counter[COUNTER_UP].stop()){
						charRef.reduceLP(5);
					}
					keyTimer = 0;
				}else if(InputHelper.DOWN) {
					if(!counter[COUNTER_DOWN].stop()){
						charRef.reduceLP(5);
					}
					keyTimer = 0;
				}
			}
		}
	}
	
	/**
	 * Everytime a timer ends the placer loses 25 of his max. HP
	 */
	private void timerFinished() {
		charRef.reduceLP(25);
		randomKey = -1;
	}

	private void timerStopped() {
		randomKey = -1;
		zombiesLP -= 1;
	}
	
	@Override
	protected void drawChildren(SpriteBatch batch, float parentAlpha) {
		super.drawChildren(batch, parentAlpha);
	}
	
	/******************************************
	 * 		inner classes
	 ******************************************/
	private class ZombieDrawer extends Actor {
		@Override
		public void draw(SpriteBatch batch, float parentAlpha) {
			//Draws the zombies
			switch(zombieCount) {
				default:	//more then 8 Zombies draw all (>8 is over the default max amount)
				case 8:
					batch.draw(TextureHelper.FIGHT_MELEE_ZOMBIE[7], 
							meleeX + ((meleeW/2)-TextureHelper.FIGHT_MELEE_ZOMBIE[7].getRegionWidth()/2), 
							meleeY + TextureHelper.FIGHT_MELEE_ZOMBIE[7].getRegionHeight());
				case 7:
					batch.draw(TextureHelper.FIGHT_MELEE_ZOMBIE[6], 
							meleeX + ((meleeW/2)-TextureHelper.FIGHT_MELEE_ZOMBIE[6].getRegionWidth()/2), 
							meleeY + TextureHelper.FIGHT_MELEE_ZOMBIE[6].getRegionHeight());
				case 6:
					batch.draw(TextureHelper.FIGHT_MELEE_ZOMBIE[5], 
							meleeX + ((meleeW/2)-TextureHelper.FIGHT_MELEE_ZOMBIE[5].getRegionWidth()/2), 
							meleeY + TextureHelper.FIGHT_MELEE_ZOMBIE[5].getRegionHeight());
				case 5:
					batch.draw(TextureHelper.FIGHT_MELEE_ZOMBIE[4], 
							meleeX + ((meleeW/2)-TextureHelper.FIGHT_MELEE_ZOMBIE[4].getRegionWidth()/2), 
							meleeY + TextureHelper.FIGHT_MELEE_ZOMBIE[4].getRegionHeight());
				case 4:
					batch.draw(TextureHelper.FIGHT_MELEE_ZOMBIE[3], 
							meleeX + ((meleeW/2)-TextureHelper.FIGHT_MELEE_ZOMBIE[3].getRegionWidth()/2), 
							meleeY + TextureHelper.FIGHT_MELEE_ZOMBIE[3].getRegionHeight());
				case 3:
					batch.draw(TextureHelper.FIGHT_MELEE_ZOMBIE[2], 
							meleeX + ((meleeW/2)-TextureHelper.FIGHT_MELEE_ZOMBIE[2].getRegionWidth()/2), 
							meleeY + TextureHelper.FIGHT_MELEE_ZOMBIE[2].getRegionHeight());
				case 2:
					batch.draw(TextureHelper.FIGHT_MELEE_ZOMBIE[1], 
							meleeX + ((meleeW/2)-TextureHelper.FIGHT_MELEE_ZOMBIE[1].getRegionWidth()/2), 
							meleeY + TextureHelper.FIGHT_MELEE_ZOMBIE[1].getRegionHeight());
				case 1:
					batch.draw(TextureHelper.FIGHT_MELEE_ZOMBIE[0],
							meleeX + ((meleeW/2)-TextureHelper.FIGHT_MELEE_ZOMBIE[0].getRegionWidth()/2), 
							meleeY );
				case 0:
					break;
			}
		}
	}
	
	private class TimerWidget extends Actor{
		
		private int x,y, calcX, calcY;
		private float timer;
		private boolean hidden;
		private boolean running;
		private String timerStr;
		private float speed;
		
		public TimerWidget(int x, int y) {
			this.x = x;
			this.y = y;
			this.timer = 0;
			this.hidden = true;
			this.running = false;
		}
		
		public boolean isRunning() {
			return this.running;
		}
		
		public void setSpeed(float speed) {
			this.speed = speed;
		}
		
		private void startTimer(float speed) {
			this.timer = 100;
			this.speed = speed;
			this.running = true;
			this.hidden = false;
		}
		
		private boolean stop() {
			if(running) {
				timer = 0;
				running = false;
				hidden = true;
				timerStopped();
				return true;
			}else{
				System.out.println("WRONG!");
				return false;
			}
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
			
			if(timer-(delta * speed) > 0 && running) {
				timer -= delta * speed;
			}else if(running){
				timer = 0;
				running = false;
				hidden = true;
				timerFinished();
			}
		}
		
		@Override
		public void draw(SpriteBatch batch, float parentAlpha) {
			if(!hidden){
				timerStr = ""+(Math.round(timer * 100.0)/100.0)+"%";
				//The string "0.0" is a medium workaround, the text will blinking by variable values!
				calcX = (int)(x - (SkinHelper.KITEONE.getBounds("0.0").width/2));
				calcY = (int)(y - (SkinHelper.KITEONE.getBounds("0.0").height/2));
				SkinHelper.KITEONE.setColor(0, 0, 0, 1f);
				SkinHelper.KITEONE.draw(batch, timerStr, calcX, calcY);
				SkinHelper.KITEONE.setColor(1f, 1f, 1f, 1f);
			}
			super.draw(batch, parentAlpha);
		}
	}
}
