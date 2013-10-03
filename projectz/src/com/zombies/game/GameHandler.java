package com.zombies.game;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.zombies.game.charakter.Charakter;
import com.zombies.game.skilltree.Skill;

public class GameHandler extends Group {
	public GameHandler() {
		Charakter c = new Charakter(Gdx.files.internal("data/char/william_pokerwinski.xml"));
		System.out.println(c.toString());
	}
}
