package com.zombies.game.events;

import com.zombies.game.event.Event;

public class ZombieFight extends Event {
	
	private float timer;
	
	public ZombieFight() {
		super();
		
		timer = 0;
	}
	
	public void act(float delta) {
		super.act(delta);
		
		timer += delta;
		if(timer > 5.0) {
			finishEvent();
		}
	}
}
