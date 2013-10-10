package com.zombies.animation;

import com.badlogic.gdx.scenes.scene2d.Group;

public class Animation extends Group {
	private boolean finished;
	
	public Animation() {
		finished = true;
	}
	
	public boolean getFinished() {
		return finished;
	}
}
