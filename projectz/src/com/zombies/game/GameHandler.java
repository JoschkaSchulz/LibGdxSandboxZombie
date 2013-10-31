package com.zombies.game;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.sun.org.apache.xml.internal.utils.CharKey;
import com.zombies.animation.Animation;
import com.zombies.game.charakter.Charakter;
import com.zombies.game.charakterpicker.CharakterPicker;
import com.zombies.game.event.EventHandler;
import com.zombies.game.inventory.InventoryActor;
import com.zombies.game.map.Map;
import com.zombies.game.skilltree.Skill;
import com.zombies.helper.InputHelper;
import com.zombies.helper.SkinHelper;

public class GameHandler extends Group {

	/**************************************************************************
	 * 				Variables
	 **************************************************************************/
	
	//static final
	public static final int STATE_CHARAKTERPICKER 	= 1;
	public static final int STATE_INTRO 			= 2;
	public static final int STATE_MAP 				= 3;
	public static final int STATE_EVENTINIT 		= 4;
	public static final int STATE_EVENT 			= 5;
	public static final int STATE_EVENTDONE			= 6;
	public static final int STATE_OPENINVENTORY		= 7;
	public static final int STATE_INVENTORY			= 8;
	public static final int STATE_CLOSEINVENTORY	= 9;
	
	//groups
	private CharakterPicker charPicker;
	private Animation intro;
	private Map map;
	private EventHandler eventHandler;
	private InventoryActor inventoryActor;
	
	private Charakter charakter;
	
	private int state;
	private ShapeRenderer debugRenderer;
	private Image greyLayer;
	/**************************************************************************
	 * 				Constructor
	 **************************************************************************/
	
	/**
	 * The constructor of the GameHandler
	 */
	public GameHandler(ShapeRenderer debugRenderer) {
		this.debugRenderer = debugRenderer;
		charPicker = new CharakterPicker(debugRenderer);
		eventHandler = new EventHandler(debugRenderer);
		charakter = null;
		greyLayer = new Image(SkinHelper.GREY_LAYER);
		greyLayer.size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		state = 0;
	}
	/**************************************************************************
	 * 				getter & setter
	 **************************************************************************/
	
	public int getState() {
		return state;
	}
	
	/**************************************************************************
	 * 				Methods
	 **************************************************************************/
	
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
				map = new Map(16,16, debugRenderer);
				this.addActor(map);
				map.generateMap(Map.TYPE_CITY);
				map.setCharRef(charakter);
			}
		}else if(state == STATE_MAP) {
			if(InputHelper.ACTION) {
				loadEvent(1);	//Test Zombie Fight Event!
				//map.clear();
				//map.generateMap(Map.TYPE_CITY);
			}
		}else if(state == STATE_EVENTINIT) {
//			this.clear();
			map.hideUI();
			this.addActor(greyLayer);
			this.addActor(eventHandler);
			state = STATE_EVENT;
		}else if(state == STATE_EVENTDONE) {
			this.clear();
			this.addActor(map);
			map.showUI();
			state = STATE_MAP;
		}else if(state == STATE_OPENINVENTORY) {
//			this.clear();
			map.hideUI();
			this.addActor(greyLayer);
			this.addActor(inventoryActor);
			state = STATE_INVENTORY;
		}else if(state == STATE_CLOSEINVENTORY) {
			this.clear();
			this.addActor(map);
			map.showUI();
			state = STATE_MAP;
		}
	}
	
	public void openInventory() {
		state = STATE_OPENINVENTORY;
	}
	
	public void closeInventory() {
		state = STATE_CLOSEINVENTORY;
	}
	
	public void loadEvent(int eventId) {
		eventHandler.loadEvent(eventId);
		state = STATE_EVENTINIT;
	}
	
	public void finishEvent() {
		state = STATE_EVENTDONE;
	}
	
	/**
	 * This method sets a charakter and change thestate to the intro state
	 * 
	 * @param charakter selected character forthe game
	 */
	public void setCharakterAndStart(Charakter charakter) {
		this.charakter = charakter;
		eventHandler.setCharRef(this.charakter);
		inventoryActor = new InventoryActor(this.charakter);
		state = STATE_INTRO;
		this.clear();
		intro = new Animation();
		this.addActor(intro);
	}
	
}
