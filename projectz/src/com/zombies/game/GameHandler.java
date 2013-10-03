package com.zombies.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.zombies.game.charakter.Charakter;

public class GameHandler extends Group {
	public GameHandler() {
		Charakter c = new Charakter(Gdx.files.internal("data/char/william_pokerwinski.xml"));
	}
}
