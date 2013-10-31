package com.zombies.game.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.zombies.game.GameHandler;
import com.zombies.game.charakter.Charakter;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.SkinHelper;

public class InventoryActor extends Group {
	
	private Charakter charRef;
	
	public InventoryActor(Charakter charRef) {
		this.charRef = charRef;
		
		drawInventory();
	}
	
	private void drawInventory() {
		Table inv = new Table();
		inv.size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		inv.top().left().setPosition(0, GUIHelper.getNewCoordinates(0, Gdx.graphics.getHeight()));
		
		TextButton close = new TextButton("Beende Inventar", SkinHelper.SKIN);
		close.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				((GameHandler)getParent()).closeInventory();
			}
		});
		
		inv.add(close);
		
		addActor(inv);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
	
	
}
