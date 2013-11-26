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
	public static boolean SPACE = false;
	
	public static boolean UP = false;
	public static boolean DOWN = false;
	public static boolean LEFT = false;
	public static boolean RIGHT = false;
	
	public static boolean DRAG = false;
	public static float DRAG_X = -1;
	public static float DRAG_Y = -1;
	
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
		DRAG_X = x;
		DRAG_Y = y;
//		if(mobile) {
//			if(touchpad.getKnobPercentY() > 0.5) InputHelper.UP = true;
//			else InputHelper.UP = false;
//				
//			if(touchpad.getKnobPercentY() < -0.5) InputHelper.DOWN = true;
//			else InputHelper.DOWN = false;
//			
//			if(touchpad.getKnobPercentX() > 0.5) InputHelper.RIGHT = true;
//			else InputHelper.RIGHT = false;
//			
//			if(touchpad.getKnobPercentX() < -0.5) InputHelper.LEFT = true;
//			else InputHelper.LEFT = false;
//		}
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		DRAG = true;
		DRAG_X = x;
		DRAG_Y = y;
		return true;
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		DRAG = false;
		DRAG_X = DRAG_Y = -1;
//		if(mobile) {
//			InputHelper.UP = false;
//			InputHelper.DOWN = false;
//			InputHelper.RIGHT = false;
//			InputHelper.LEFT = false;
//		}
	}

	/*****************************************************************************
	 * 				Keyboard
	 *****************************************************************************/
	
	public boolean keyUp(InputEvent event, int keycode) {
		if(keycode == Input.Keys.W || keycode == Input.Keys.UP) InputHelper.UP = false;
		if(keycode == Input.Keys.S || keycode == Input.Keys.DOWN) InputHelper.DOWN = false;
		if(keycode == Input.Keys.A || keycode == Input.Keys.LEFT) InputHelper.LEFT = false;
		if(keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) InputHelper.RIGHT = false;
		
		if(keycode == Input.Keys.E || keycode == Input.Keys.ENTER) InputHelper.ACTION = false;
		if(keycode == Input.Keys.SPACE) InputHelper.SPACE = false;
		return true;
	}
	
	public boolean keyDown(InputEvent event, int keycode) { 
    	if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {InputHelper.UP = true;}
    	if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {InputHelper.DOWN = true;}
    	if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {InputHelper.LEFT = true;}
    	if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {InputHelper.RIGHT = true;}
    	
    	if(Gdx.input.isKeyPressed(Input.Keys.E) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {InputHelper.ACTION = true;}
    	if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {InputHelper.SPACE = true;}
    			
		return true;
	}
}
