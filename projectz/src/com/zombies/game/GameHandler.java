package com.zombies.game;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.sun.org.apache.xml.internal.utils.CharKey;
import com.zombies.animation.Animation;
import com.zombies.game.charakter.Charakter;
import com.zombies.game.charakterpicker.CharakterPicker;
import com.zombies.game.map.Map;
import com.zombies.game.skilltree.Skill;

public class GameHandler extends Group {
	//static final
	public static final int STATE_CHARAKTERPICKER = 1;
	public static final int STATE_INTRO = 2;
	public static final int STATE_MAP = 3;
	
	//groups
	private CharakterPicker charPicker;
	private Animation intro;
	private Map map;
	
	private Charakter charakter;
	
	private int state;
	
	public GameHandler() {
		charPicker = new CharakterPicker();
		
		charakter = null;
		state = 0;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(charakter == null && state != STATE_CHARAKTERPICKER) {
			state = STATE_CHARAKTERPICKER;
			this.clear();
			this.addActor(this.charPicker);
		}else if(state == STATE_INTRO && intro != null) {
			if(intro.getFinished()) {
				state = STATE_MAP;
				this.clear();
				map = new Map(75,40);
				this.addActor(map);
				int reserve[] = {10,2,0,0,0}; // 10x lvl1 2x lvl2
				map.generateMap(Map.TYPE_CITY, reserve);
			}
		}
	}
	
	public void setCharakterAndStart(Charakter charakter) {
		this.charakter = charakter;
		state = STATE_INTRO;
		this.clear();
		intro = new Animation();
		this.addActor(intro);
	}
	
}
