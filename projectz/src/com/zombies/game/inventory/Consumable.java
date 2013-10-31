package com.zombies.game.inventory;

public class Consumable extends Item {
	private int food, drink, health;

	public Consumable(int ID, String name, String discription, int food,
			int drink, int health) {
		
		this.setID(ID);
		this.setName(name);
		this.setDiscription(discription);
		
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
	
	
}
