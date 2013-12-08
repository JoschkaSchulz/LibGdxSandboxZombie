package com.zombies.game.inventory;



import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.esotericsoftware.tablelayout.Cell;
import com.zombies.game.GameHandler;
import com.zombies.game.charakter.Charakter;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.SkinHelper;

public class InventoryHandler extends Group {
	
	private Charakter charRef;
	private Cell content;
	
	private TextButton inventory;
	private TextButton skills;
	private TextButton close;
	private Table inv;
	
	public InventoryHandler(Charakter charRef) {
		this.charRef = charRef;
		
		drawInventory();
	}
	
	private void drawInventory() {
//		Table inv = new Table();
//		inv.size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		inv.top().left().setPosition(0, GUIHelper.getNewCoordinates(0, Gdx.graphics.getHeight()));
//		
//		TextButton close = new TextButton("Beende Inventar", SkinHelper.SKIN);
		
//		
//		inv.add(close);
		
		//Deklare Tables
		Table leftTable = new Table();
		leftTable.top().left().setPosition(0, GUIHelper.getNewCoordinates(0, Gdx.graphics.getHeight()));
		leftTable.size(512, Gdx.graphics.getHeight());
		leftTable.debug();
		
		Table rightTable = new Table();
		rightTable.top().left().setPosition(512, GUIHelper.getNewCoordinates(0, Gdx.graphics.getHeight()));
		rightTable.size(786, Gdx.graphics.getHeight());
		rightTable.debug();
		
		//Fill the Content
		leftTable.add(new Label("Charakter Info", SkinHelper.SKIN)).height(512).width(512);
		leftTable.row();
		leftTable.add(showMenu()).height(256).width(512);
		
		content = rightTable.add(showInventoy()).height(786).width(786);
		
		//Add the Table to the Inventory
		addActor(leftTable);
		addActor(rightTable);
	}

	private void refreshInventory() {
		content.setWidget(showInventoy());
	}
	
	private Table showInventoy() {
		inv = new Table();
		inv.top().setPosition(0,0);
		inv.setName("inventory");
		inv.debug();
		
//		ArrayList<Item> inventory = new ArrayList<Item>(charRef.getInventory().getInventar());
		
		final ArrayList<Item> inventory = new ArrayList<Item>(charRef.getInventory().getInventory());
		int counter = 0;
		Image img;
		for(int i1 = 0, m = 5; i1 < m; i1++){
			for(int i2 = 0, n = 5; i2 < n; i2++) {
				if(inventory.size() > counter) {
					img = new Image(inventory.get(0).getTexture());
					img.addListener(new ClickListener(){
						@Override
						public void clicked(InputEvent event, float x, float y) {
							super.clicked(event, x, y);
							
							itemClicked(inventory.get(0));
						}
					});
					inv.add(img).width(128).height(128).pad(8);
				}else{
					inv.add(new Image(new Texture(Gdx.files.internal("data/gfx/items/randomimage.png")))).width(128).height(128).pad(8);
				}
				counter++;
			}
			inv.row();
		}
		
		return inv;
	}
	
	private Table showSkills() {
		Table skills = new Table();
		skills.top().setPosition(0,0);
		skills.debug();
		
		for(int i1 = 0, m = 5; i1 < m; i1++){
			for(int i2 = 0, n = 5; i2 < n; i2++) {
				skills.add().width(128).height(128).pad(8);
			}
			skills.row();
		}
		
		return skills;
	}
	
	/**
	 * This method fills the 
	 * @return
	 */
	private Table showMenu() {
		Table menu = new Table();
		menu.top().left().setPosition(0, 0);
		menu.debug();
		
		inventory = new TextButton("Inventar", SkinHelper.SKIN);
		inventory.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				content.setWidget(showInventoy());
				inventory.setChecked(false);
			}
		});
		
		skills = new TextButton("Skills", SkinHelper.SKIN);
		skills.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				content.setWidget(showSkills());
				skills.setChecked(false);
			}
		});

		close = new TextButton("Beende Inventar", SkinHelper.SKIN);
		close.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				((GameHandler)getParent()).closeInventory();
				close.setChecked(false);
			}
		});
		
		menu.add(inventory).width(512).row();
		menu.add(skills).width(512).row();
		menu.add(close).width(512);
		
		return menu;
	}
	
	/**
	 * This method is used for Click events on an item in the inventory
	 * 
	 * @param item the clicked item
 	 */
	private void itemClicked(Item item) {
		switch(item.getGroup()) {
			default:
			case Item.GROUP_UNDEFINED:
				undefinedDialog(item);
				break;
			case Item.GROUP_CONSUMABLE:
				Consumable consumable = (Consumable) item;
				consumableDialog(consumable);
				break;
		}
	}
	
	private void undefinedDialog(final Item item) {
		Table dialog = new Table();
		dialog.setName("item_contex_menu");
		dialog.setWidth(1024);
		dialog.setHeight(512);
		dialog.top();
		dialog.setPosition(128, GUIHelper.getNewCoordinates(128, 512));
		
		TextButton dropButton = new TextButton("Wegwerfen", SkinHelper.SKIN);
		dropButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				charRef.getInventory().removeItem(item);
				removeActor(findActor("item_contex_menu"));
				refreshInventory();
			}
		});
		

		//Build Table
		dialog.add(dropButton).width(1024);
		
		addActor(dialog);
	}
	
	private void consumableDialog(final Consumable item) {
		Table dialog = new Table();
		dialog.setName("item_contex_menu");
		dialog.setWidth(1024);
		dialog.setHeight(512);
		dialog.top();
		dialog.setPosition(128, GUIHelper.getNewCoordinates(128, 512));
		
		TextButton consumButton = new TextButton("Konsumiere", SkinHelper.SKIN);
		consumButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				charRef.setCurrentLP(charRef.getCurrentLP() + item.getHealth());
				charRef.setCurrentStomach(charRef.getCurrentStomach() + item.getFood());
				charRef.setCurrentThirst(charRef.getCurrentThirst() + item.getDrink());
				charRef.getInventory().removeItem(item);
				removeActor(findActor("item_contex_menu"));
				refreshInventory();
			}
		});

		TextButton dropButton = new TextButton("Wegwerfen", SkinHelper.SKIN);
		dropButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				charRef.getInventory().removeItem(item);
				removeActor(findActor("item_contex_menu"));
				refreshInventory();
			}
		});
		

		//Build Table
		dialog.add(consumButton).width(1024);
		dialog.row();
		dialog.add(dropButton).width(1024);
		
		addActor(dialog);
	}
	
	
}
