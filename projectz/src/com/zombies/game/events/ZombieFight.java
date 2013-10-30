package com.zombies.game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.zombies.game.event.Event;
import com.zombies.helper.SkinHelper;
import com.zombies.helper.GUIHelper;

public class ZombieFight extends Event {
	
	private float timer;
	
	public ZombieFight() {
		super();

		String dialogText = "Zombies überall! Dies ist ein Zombie" +
				"Test Event es soll momentan nur demonstieren" +
				"wie man mit den Events arbeitet!";
		String[] a = {"Zombies überall! Kämpfe dich durch bis zum bitteren Ende",
						"Springe über den Zaun und lauf um dein Leben"};
		showDialog(dialogText, a);
	}
	
	public void act(float delta) {
		super.act(delta);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
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
