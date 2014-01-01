package com.zombies.game.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Gun extends Item {

	/*********************************************
	 * 			fields
	 *********************************************/
	
	private float range;
	private float firerate;
	private int damage;
	private int magazineTyp;
	private int ammo;
	
	/*********************************************
	 * 			constructor
	 *********************************************/
	
	public Gun(int id, String name, String description, float range, 
			int damage, int magazineTyp, int ammo, float firerate, 
			TextureRegion texture) {
		super(id, Item.GROUP_GUN, name, description, texture);
		
		this.range = range;
		this.damage = damage;
		this.magazineTyp = magazineTyp;
		this.ammo = ammo;
		this.firerate = firerate;
	}

	/*********************************************
	 * 			getter and setter
	 *********************************************/

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

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

	public int getMagazineTyp() {
		return magazineTyp;
	}

	public void setMagazineTyp(int magazineTyp) {
		this.magazineTyp = magazineTyp;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}
	
	/*********************************************
	 * 			methods
	 *********************************************/
	
	@Override
	public Item clone() {
		return new Gun(getId(), getName(), getDiscription(), getRange(), getDamage(), getMagazineTyp(), getAmmo(), getFirerate(), getTexture());
	}

}
