package com.zombies.game;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.zombies.game.charakter.Charakter;
import com.zombies.game.skilltree.Skill;

public class GameHandler extends Group {
	public GameHandler() {
		System.out.println(Skill.loadSkills(Gdx.files.internal("data/xml/skills.xml")).toString());
	}
}
