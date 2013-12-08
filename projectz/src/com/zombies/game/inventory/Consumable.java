package com.zombies.game.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Consumable extends Item {
	/*********************************************
	 * 			fields
	 *********************************************/
	
	private int food;
	private int drink;
	private int health;

	/*********************************************
	 * 			constructor
	 *********************************************/
	
	public Consumable(int id, String name, String description, int food,
			int drink, int health, TextureRegion texture) {
		super(id, Item.GROUP_CONSUMABLE , name, description, texture);
		this.setId(id); 
		this.setName(name);
		this.setDescription(description);
		this.setTexture(texture);
		
		this.food = food;
		this.drink = drink;
		this.health = drink;
	}
	
	/*********************************************
	 * 			getter and setter
	 *********************************************/
	
	public int getFood() {
		return food;
	}

	public int getDrink() {
		return drink;
	}

	public int getHealth() {
		return health;
	}

	/*********************************************
	 * 			methods
	 *********************************************/
	
	@Override
	public Item clone() {
		return new Consumable(this.getId(),this.getName(),this.getDiscription(),this.food, this.drink, this.health, this.getTexture());
	}
	
	
}
