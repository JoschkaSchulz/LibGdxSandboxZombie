package com.zombies.game.event;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.InputHelper;
import com.zombies.helper.SoundHelper;
import com.zombies.helper.TextureHelper;

public class Fight extends Group {
	
	/******************************************
	 * 		variables
	 ******************************************/
	
	public static int HITBAR_WIDTH = 1024;
	public static int BAR_POSITION_X = 100;
	public static int BAR_POSITION_Y = 700;
	
	private Image hitbar;
	private ArrayList<Zombie> zombies;
	private LinkedList<Image> hits;
	private Image pointer;
	private boolean moveToRight;
	private float speed;
	private boolean inFight;
	
	/******************************************
	 * 		constructor
	 ******************************************/
	
	public Fight() {
		hitbar = new Image(TextureHelper.FIGHT_HITBAR);
		pointer = new Image(TextureHelper.FIGHT_POINTER);
		
		pointer.setPosition(BAR_POSITION_X, GUIHelper.getNewCoordinates(BAR_POSITION_Y, 32));
		hitbar.setPosition(BAR_POSITION_X, GUIHelper.getNewCoordinates(BAR_POSITION_Y, 32));
		hits = new LinkedList<Image>();
		zombies = new ArrayList<Zombie>();
		
		moveToRight = true;
		speed = 250;
	}

	/******************************************
	 * 		getter and setter
	 ******************************************/
	
	/******************************************
	 * 		methods
	 ******************************************/
	
	public boolean isInFight() {
		return inFight;
	}
	
	/**
	 * Adds a Single target to the Bar
	 * 
	 * @param percent the position from 0 to 100 on the bar
	 * @param hitpoints descripes the hit  point the zombie has
	 */
	public void addSingleZombie(float percent, int hitpoints) {
		//Add a zombie to the fight
		zombies.add(new Zombie(hitpoints, 50, 50, percent));
		zombies.get(zombies.size()-1).createHitbarImage(hitbar.getX(), hitbar.getY());
	}
	
	
	/**
	 * This method initialize the actors of the class. Use this method to
	 * get sure of the right order.
	 */
	public void initActors() {
		//First add the hitbar
		addActor(hitbar);
		
		//Then add all zombies to the hitbar
		for(Zombie zed : zombies) {
			addActor(zed.getHitbarImage());
		}
		
		//And finaly add the pointer/crosshair
		addActor(pointer);
		
		inFight = true;
	}
	
	private void shoot(float percent) {
		Zombie zed;
		LinkedList<Zombie> zombiesDeletion = new LinkedList<Zombie>();
		
		//Play a sound as acustical signal for the shoot
		SoundHelper.play_Shotgun();
		
		for(int i = 0, n = zombies.size(); i < n; i++) {
			zed = zombies.get(i);
			
			if(zed.hit(percent)) {
				if(zed.makeDamage(1)) {
					try{
						zombiesDeletion.add(zed);
					}catch(Exception e) {}	
				}
				Image hitIcon = new Image(TextureHelper.FIGHT_HIT);
				hitIcon.setPosition(pointer.getX(), pointer.getY() + 32);
				addActor(hitIcon);
				hits.add(hitIcon);
			}
		}
		
		//Delete all dead Zombies
		for(Zombie z : zombiesDeletion) {
			removeActor(z.getHitbarImage());
			zombies.remove(z);
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		//Set the borders of the crosshair 
		if(pointer.getX() > (hitbar.getX() + HITBAR_WIDTH) - pointer.getWidth()) moveToRight = false;
		else if(pointer.getX() < BAR_POSITION_X) moveToRight = true;
		
		if(moveToRight) pointer.setPosition(pointer.getX() + (delta * speed), hitbar.getY());
		else pointer.setPosition(pointer.getX() - (delta * speed), hitbar.getY());
	
		//Handle the Hit Icons above hits
		LinkedList<Integer> deleteHitIcons = new LinkedList<Integer>();
		Image img;
		for(int i = 0, n = hits.size(); i < n; i++) {
			img = hits.get(i);
			img.translate(0, (delta * 50));
			if(GUIHelper.getNewCoordinates((int) img.getY(),32) < BAR_POSITION_Y - 50) 
				deleteHitIcons.add(i);
		}
		for(int i : deleteHitIcons) {
			removeActor(hits.get(i));
			hits.remove(i);
		}
		
		//Check if the User press action to fire
		if(InputHelper.SPACE) {
			shoot(pointer.getX() - BAR_POSITION_X + (pointer.getWidth()/2));
		}
		
		//Check if the fight is finished
		if(zombies.size() <= 0) inFight = false;
	}
}
