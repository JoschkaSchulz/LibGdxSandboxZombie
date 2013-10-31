package com.zombies.game.inventory;

import com.badlogic.gdx.graphics.Texture;

public abstract class Item {
	private int ID;
	private String Name;
	private String Discription;
	private Texture Texture;
	
	
	public abstract Item clone();
	
	
	
	public Texture getTexture() {
		return Texture;
	}
	public void setTexture(Texture texture) {
		Texture = texture;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDiscription() {
		return Discription;
	}
	public void setDiscription(String discription) {
		Discription = discription;
	}
	
	
	
}
