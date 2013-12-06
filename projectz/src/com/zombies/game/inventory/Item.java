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
	
	private int id;
	private String name;
	private String description;
	private TextureRegion texture;
	
	/*********************************************
	 * 			getter and setter
	 *********************************************/
	
	public Item(int id, String name, String description, TextureRegion texture) {
		this.id = id;
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
	
	public void setDiscription(String discription) {
		this.description = discription;
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
	
	/**
	 * Gives back a new identical Object of the Item class.
	 */
	public abstract Item clone();

}
