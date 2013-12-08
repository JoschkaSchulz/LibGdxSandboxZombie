package com.zombies.game.inventory;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

	private List<Item> Inventar;
	private int maxsize;

	public Inventory() {
		maxsize = 8;
		Inventar = new ArrayList<Item>();
	}

	public Inventory(int size) {
		maxsize = size;
		Inventar = new ArrayList<Item>();
	}
	
	public boolean addItem(Item item) {
		if (Inventar.size() >= maxsize)
			return false;
		Inventar.add(item);
		return true;
	}

	public boolean hasItem(Item item) {
		if (Inventar.indexOf(item) >= 0)
			return true;
		return false;
	}

	public boolean hasItem(int ID) {
		for (Item item : Inventar) {
			if (item.getId() == ID)
				return true;
		}
		return false;
	}

	public void removeItem(Item item) {
		Inventar.remove(item);
	}

	public void removeItem(int ID) {
		for (Item item : Inventar) {
			if (item.getId() == ID) {
				Inventar.remove(item);
				break;
			}
		}
	}

	public List<Item> getInventory() {
		return new ArrayList<Item>(Inventar);
	}

	
	

}
