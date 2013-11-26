package com.zombies.game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;



import com.zombies.game.event.Event;
import com.zombies.game.event.Fight;



public class ZombieFight extends Event {
	
	private float timer;
	
	public ZombieFight() {
		super();
		
		Fight fight = new Fight();
		fight.addHitZone(0, 10);
		fight.addHitZone(20, 30);
		fight.addHitZone(50, 100);
		fight.initActors();
		addActor(fight);
//		String dialogText = "Zombies überall! Dies ist ein Zombie" +
//				"Test Event es soll momentan nur demonstieren" +
//				"wie man mit den Events arbeitet!";
//		String[] a = {"Zombies überall! Kämpfe dich durch bis zum bitteren Ende",
//						"Springe über den Zaun und lauf um dein Leben"};
//		showDialog(dialogText, a);
	}
	
	

	@Override
	protected void dialogAnswer1() {
		finishEvent();
	}

	@Override
	protected void dialogAnswer2() {
		//Not used here
	}

	@Override
	protected void dialogAnswer3() {
		//Not used here
	}

	@Override
	protected void dialogAnswer4() {
		//Not used here
	}
	
	
}
