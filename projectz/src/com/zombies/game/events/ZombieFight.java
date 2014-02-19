package com.zombies.game.events;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.zombies.game.charakter.Charakter;
import com.zombies.game.event.Event;
import com.zombies.game.event.Fight;

public class ZombieFight extends Event {
	
	private Fight fight;
	
	public ZombieFight(ShapeRenderer debugRenderer, Charakter charRef) {
		super(debugRenderer, charRef);
		
		//Create a fight
		fight = new Fight(this.charRef);
		fight.addSingleZombie(50,5);
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
		hideDialog();
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
