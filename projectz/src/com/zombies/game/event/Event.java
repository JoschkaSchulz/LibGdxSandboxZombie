package com.zombies.game.event;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.zombies.game.GameHandler;

public abstract class Event extends Group {
	ShapeRenderer debugRenderer;
	
	public Event() {
		
	}
	
	public void configEvent(ShapeRenderer debugRenderer) {
		this.debugRenderer = debugRenderer;
	}
	
	protected void finishEvent() {
		try{
			((GameHandler)getParent().getParent()).finishEvent();			
		}catch(Exception e) {
			System.err.println("Fehler beim finden des GameHandler im Event!");
		}
		try{
			((EventHandler)getParent()).clearCurrentEvents();	
		}catch(Exception e) {
			System.err.println("Fehler beim finden des EventHandler im Event!");
		}
	}
	
}
