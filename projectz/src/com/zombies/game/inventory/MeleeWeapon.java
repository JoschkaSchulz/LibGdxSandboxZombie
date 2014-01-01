package com.zombies.game.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MeleeWeapon extends Item {

	/*********************************************
	 * 			fields
	 *********************************************/
	
	private float firerate;
	private int damage;
	
	/*********************************************
	 * 			constrcutor
	 *********************************************/
	
	public MeleeWeapon(int id, String name, String description, float firerate,
			int damage, TextureRegion texture) {
		super(id, Item.GROUP_MELEE, name, description, texture);
		
		this.firerate = firerate;
		this.damage = damage;
	}

	/*********************************************
	 * 			gett and setter
	 *********************************************/
	
	public float getFirerate() {
		return firerate;
	}

	public void setFirerate(float firerate) {
		this.firerate = firerate;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	/*********************************************
	 * 			methods
	 *********************************************/
	
	@Override
	public Item clone() {
		return new MeleeWeapon(getId(), getName(), getDiscription(), getFirerate(), getDamage(), getTexture());
	}

}
