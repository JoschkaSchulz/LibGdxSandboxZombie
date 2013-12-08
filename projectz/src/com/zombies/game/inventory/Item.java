package com.zombies.game.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An abstract class for every group of items.
 */
public abstract class Item {
	/*********************************************
	 * 			fields
	 *********************************************/
	
	//static variables
	public final static int GROUP_UNDEFINED 	= 0;
	public final static int GROUP_CONSUMABLE 	= 1;
	
	//Varibales
	private int id;
	private String name;
	private String description;
	private TextureRegion texture;
	private int group;
	
	/*********************************************
	 * 			getter and setter
	 *********************************************/
	
	public Item(int id, int group, String name, String description, TextureRegion texture) {
		this.id = id;
		this.group = group;
		this.name = name;
		this.description = description;
		this.texture = texture;
	}
	
	/*********************************************
	 * 			getter and setter
	 *********************************************/
	
	public TextureRegion getTexture() {
		return texture;
	}
	
	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int iD) {
		this.id = iD;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDiscription() {
		return description;
	}
	
	public void setDescription(String discription) {
		this.description = discription;
	}
	
	public int getGroup() {
		return this.group;
	}
	
	/*********************************************
	 * 			methods
	 *********************************************/
	
	/**
	 * generates a String to let the class easier print to the console
	 *
	 * @return returns a String with information that is saved in this class
	 */
	public String toString() {
		return "Item[id:" + getId() + ", name:" + getName() + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + group;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (group != other.group)
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * Gives back a new identical Object of the Item class.
	 */
	public abstract Item clone();

}
