package com.zombies.game.inventory;

import com.badlogic.gdx.graphics.*;

public class Consumable extends Item {
	private int food, drink, health;

	public Consumable(int ID, String name, String discription, int food,
			int drink, int health, Texture texture) {
		
		this.setID(ID);
		this.setName(name);
		this.setDiscription(discription);
		this.setTexture(texture);
		
		this.food = food;
		this.drink = drink;
		this.health = drink;
	}

	public int getFood() {
		return food;
	}

	public int getDrink() {
		return drink;
	}

	public int getHealth() {
		return health;
	}

	@Override
	public Item clone() {
		return new Consumable(this.getID(),this.getName(),this.getDiscription(),this.food, this.drink, this.health, this.getTexture());
	}
	
	
}
