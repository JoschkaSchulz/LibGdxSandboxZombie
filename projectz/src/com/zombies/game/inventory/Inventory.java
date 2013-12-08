package com.zombies.game.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Inventory {

	private List<Item> inventory;
	private int maxsize;

	public Inventory() {
		maxsize = 8;
		inventory = new ArrayList<Item>();
	}

	public Inventory(int size) {
		maxsize = size;
		inventory = new ArrayList<Item>();
	}
	
	public boolean addItem(Item item) {
		if (inventory.size() >= maxsize)
			return false;
		inventory.add(item);
		return true;
	}

	public boolean hasItem(Item item) {
		if (inventory.indexOf(item) >= 0)
			return true;
		return false;
	}

	public boolean hasItem(int ID) {
		for (Item item : inventory) {
			if (item.getId() == ID)
				return true;
		}
		return false;
	}

	public void removeItem(Item item) {
		inventory.remove(item);
	}

	public void removeItem(int ID) {
		for (Item item : inventory) {
			if (item.getId() == ID) {
				inventory.remove(item);
				break;
			}
		}
	}

	public List<Item> getInventory() {
		return new ArrayList<Item>(inventory);
	}

	
	

}
