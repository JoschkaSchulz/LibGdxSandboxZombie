package com.zombies.game.event;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.TextureHelper;

public class Fight extends Group {
	
	public class Hitzone {
		private float start;
		private float end;
		private Image image;
		
		public Hitzone(Image image, float start, float end) {
			this.start = start;
			this.end = end;
			this.image = image;
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
	}
	
	private static int HITBAR_WIDTH = 1024;
	private static int BAR_POSITION_X = 100;
	private static int BAR_POSITION_Y = 700;
	
	private Image hitbar;
	private ArrayList<Hitzone> hitZone;
	private Image pointer;
	private boolean moveToRight;
	private float speed;
	
	public Fight() {
		hitbar = new Image(TextureHelper.FIGHT_HITBAR);
		pointer = new Image(TextureHelper.FIGHT_POINTER);
		
		pointer.setPosition(BAR_POSITION_X, GUIHelper.getNewCoordinates(BAR_POSITION_Y, 32));
		hitbar.setPosition(BAR_POSITION_X, GUIHelper.getNewCoordinates(BAR_POSITION_Y, 32));
		hitZone = new ArrayList<Hitzone>();
		
		moveToRight = true;
		speed = 250;
	}

	/**
	 * Adds a new HitZone to the fight.
	 * 
	 * @param start the percent start of the Zone
	 * @param end the percent end of the Zone
	 */
	public void addHitZone(float start, float end) {
		start = HITBAR_WIDTH/100*start;
		end = HITBAR_WIDTH/100*end;
		float scale = end - start;
		
		Image hz = new Image(TextureHelper.FIGHT_HIT_ZONE);
		hz.setPosition(hitbar.getX()+start, hitbar.getY());
		hz.scale(scale, 0f);
		hitZone.add(new Hitzone(hz, start, end));
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
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		//Set the borders of the crosshair 
		if(pointer.getX() > (hitbar.getX() + HITBAR_WIDTH) - pointer.getWidth()) moveToRight = false;
		else if(pointer.getX() < BAR_POSITION_X) moveToRight = true;
		
		if(moveToRight) pointer.setPosition(pointer.getX() + (delta * speed), hitbar.getY());
		else pointer.setPosition(pointer.getX() - (delta * speed), hitbar.getY());
	}
}
