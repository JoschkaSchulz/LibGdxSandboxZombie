package com.zombies.game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zombies.game.event.Event;
import com.zombies.helper.FontHelper;
import com.zombies.helper.GUIHelper;

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
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		FontHelper.KITEONE.setColor(1f, 1f, 1f, 1f);
		FontHelper.KITEONE.draw(batch, "Zombie Fight DEMO EVENT ( "+Math.round(5-timer)+"s verbleibend)", 10, GUIHelper.getNewCoordinates(50, 16));
	}
}
