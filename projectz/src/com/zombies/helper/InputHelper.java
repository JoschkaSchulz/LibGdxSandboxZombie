package com.zombies.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

public class InputHelper extends InputListener{
	private Touchpad touchpad;
	
	private boolean mobile;
	
	public static boolean ACTION = false;
	
	public static boolean UP = false;
	public static boolean DOWN = false;
	public static boolean LEFT = false;
	public static boolean RIGHT = false;

	public static boolean TOUCH_UP = false;
	public static boolean TOUCH_DOWN = false;
	public static boolean TOUCH_LEFT = false;
	public static boolean TOUCH_RIGHT = false;
	
	public InputHelper(Touchpad touchpad, boolean mobile) {
		//this.playerRef = playerRef;
		this.touchpad = touchpad;
		this.mobile = mobile;
	}
	
	/*****************************************************************************
	 * 				Touch
	 *****************************************************************************/
	
	@Override
	public void touchDragged(InputEvent event, float x, float y, int pointer) {
		if(mobile) {
			if(touchpad.getKnobPercentY() > 0.5) InputHelper.TOUCH_UP = true;
			else InputHelper.TOUCH_UP = false;
				
			if(touchpad.getKnobPercentY() < -0.5) InputHelper.TOUCH_DOWN = true;
			else InputHelper.TOUCH_DOWN = false;
			
			if(touchpad.getKnobPercentX() > 0.5) InputHelper.TOUCH_RIGHT = true;
			else InputHelper.TOUCH_RIGHT = false;
			
			if(touchpad.getKnobPercentX() < -0.5) InputHelper.TOUCH_LEFT = true;
			else InputHelper.TOUCH_LEFT = false;
		}
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		return true;
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		if(mobile) {
			InputHelper.TOUCH_UP = false;
			InputHelper.TOUCH_DOWN = false;
			InputHelper.TOUCH_RIGHT = false;
			InputHelper.TOUCH_LEFT = false;
		}
	}

	/*****************************************************************************
	 * 				Keyboard
	 *****************************************************************************/
	
	public boolean keyUp(InputEvent event, int keycode) {
		if(keycode == Input.Keys.W || keycode == Input.Keys.UP) InputHelper.UP = false;
		if(keycode == Input.Keys.S || keycode == Input.Keys.DOWN) InputHelper.DOWN = false;
		if(keycode == Input.Keys.A || keycode == Input.Keys.LEFT) InputHelper.LEFT = false;
		if(keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) InputHelper.RIGHT = false;
		
		if(keycode == Input.Keys.E) InputHelper.ACTION = false;
		return true;
	}
	
	public boolean keyDown(InputEvent event, int keycode) {   
    	if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {InputHelper.UP = true;}
    	if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {InputHelper.DOWN = true;}
    	if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {InputHelper.LEFT = true;}
    	if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {InputHelper.RIGHT = true;}
    	
    	if(Gdx.input.isKeyPressed(Input.Keys.E)) {InputHelper.ACTION = true;}
		return true;
	}
}
