package com.zombies.game.events;

import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;



import com.zombies.game.event.Event;
import com.zombies.game.event.Fight;



public class ZombieFight extends Event {
	
	private float timer;
	private Fight fight;
	
	public ZombieFight() {
		super();
		
		//Create a fight
		fight = new Fight();
		fight.addSingleZombie(50,1);
		fight.initActors();
		
		//Create a startup Dialog
		String dialogText = "Zombies überall! Dies ist ein Zombie" +
				"Test Event es soll momentan nur demonstieren" +
				"wie man mit den Events arbeitet!";
		String[] a = {"Zombies überall! Kämpfe dich durch bis zum bitteren Ende",
						"Springe über den Zaun und lauf um dein Leben"};
		showDialog(dialogText, a);
	}
	
	

	@Override
	protected void dialogAnswer1() {
		addActor(fight);
	}

	@Override
	protected void dialogAnswer2() {
		finishEvent();
	}

	@Override
	protected void dialogAnswer3() {
		//Not used here
	}

	@Override
	protected void dialogAnswer4() {
		//Not used here
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		//Check if the fight is finished
		if(!fight.isInFight()) {
			finishEvent();
		}
	}
	
	
}
