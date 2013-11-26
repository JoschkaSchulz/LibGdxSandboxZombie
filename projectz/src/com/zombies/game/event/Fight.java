package com.zombies.game.event;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.InputHelper;
import com.zombies.helper.TextureHelper;

public class Fight extends Group {
	
	public class Hitzone {
		private float start;
		private float end;
		private Image image;
		private int hitpoints;
		
		public Hitzone(Image image, float start, float end, int hitpoints) {
			this.start = start;
			this.end = end;
			this.image = image;
			this.hitpoints = hitpoints;
		}
		
		public float getStart() {
			return this.start;
		}
		
		public float getEnd() {
			return this.end;
		}
		
		public Image getImage() {
			return this.image;
		}
		
		public void getLifeInfo(){
			System.out.println(hitpoints + " Hitpoints remains");
		}
		
		public void hit(int hitpoints) {
			this.hitpoints -= hitpoints;
		}
		
		public boolean isDead() {
			return !(hitpoints > 0);
		}
	}
	
	private static int HITBAR_WIDTH = 1024;
	private static int BAR_POSITION_X = 100;
	private static int BAR_POSITION_Y = 700;
	
	private Image hitbar;
	private ArrayList<Hitzone> hitZone;
	private LinkedList<Image> hits;
	private Image pointer;
	private boolean moveToRight;
	private float speed;
	private boolean inFight;
	
	public Fight() {
		hitbar = new Image(TextureHelper.FIGHT_HITBAR);
		pointer = new Image(TextureHelper.FIGHT_POINTER);
		
		pointer.setPosition(BAR_POSITION_X, GUIHelper.getNewCoordinates(BAR_POSITION_Y, 32));
		hitbar.setPosition(BAR_POSITION_X, GUIHelper.getNewCoordinates(BAR_POSITION_Y, 32));
		hitZone = new ArrayList<Hitzone>();
		hits = new LinkedList<Image>();
		
		moveToRight = true;
		speed = 250;
	}

	public boolean isInFight() {
		return inFight;
	}
	
	/**
	 * Adds a new HitZone to the fight.
	 * 
	 * @param start the percent start of the Zone
	 * @param end the percent end of the Zone
	 * @param hitpoints descriptes the hitpointsof this area
	 */
	public void addHitZone(float start, float end, int hitpoints) {
		start = HITBAR_WIDTH/100*start;
		end = HITBAR_WIDTH/100*end;
		float scale = end - start;
		
		Image hz = new Image(TextureHelper.FIGHT_HIT_ZONE);
		hz.setPosition(hitbar.getX()+start, hitbar.getY());
		hz.scale(scale, 0f);
		hitZone.add(new Hitzone(hz, start, end, hitpoints));
	}
	
	/**
	 * Adds a Single target to the Bar
	 * 
	 * @param percent the position from 0 to 100 on the bar
	 * @param hitpoints descripes the hit  point the zombie has
	 */
	public void addSingleZombie(float percent, int hitpoints) {
		//Set the zombie a bit more in the center and away from the border
		float point;
		if(percent < 5) {
			point = HITBAR_WIDTH/100 * 5;	
		}else if(percent > 95) {
			point = HITBAR_WIDTH/100 * 95;
		}else{
			point = HITBAR_WIDTH/100 * percent;
		}
		
		Image zombie = new Image(TextureHelper.FIGHT_SINGLE_ZOMBIE);
		zombie.setPosition(hitbar.getX()+point, hitbar.getY());
		hitZone.add(new Hitzone(zombie, point, point+zombie.getWidth(), hitpoints));
	}
	
	
	/**
	 * This method initialize the actors of the class. Use this method to
	 * get sure of the right order.
	 */
	public void initActors() {
		//First add the hitbar
		addActor(hitbar);
		
		//Then add all hitzones
		for(Hitzone h : hitZone) {
			addActor(h.getImage());
		}
		
		//And finaly add the pointer/crosshair
		addActor(pointer);
		
		inFight = true;
	}
	
	private void shoot(float percent) {
		Hitzone hz;
		LinkedList<Integer> hitzonesDeletion = new LinkedList<Integer>();
		for(int i = 0, n = hitZone.size(); i < n; i++) {
			hz = hitZone.get(i);
			
			if(percent > hz.getStart() && percent < hz.getEnd()) {
				hz.hit(1);
				Image hitIcon = new Image(TextureHelper.FIGHT_HIT);
				hitIcon.setPosition(pointer.getX(), pointer.getY() + 32);
				addActor(hitIcon);
				hits.add(hitIcon);
				if(hz.isDead()) {
					hitzonesDeletion.add(i);
				}
			}
		}
		
		//Delete all dead Zombies
		for(int i : hitzonesDeletion) {
			removeActor(hitZone.get(i).getImage());
			hitZone.remove(i);
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
		if(hitZone.size() <= 0) inFight = false;
	}
}
