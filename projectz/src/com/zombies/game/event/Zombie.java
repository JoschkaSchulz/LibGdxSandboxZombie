package com.zombies.game.event;

import sun.security.action.GetLongAction;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zombies.helper.TextureHelper;

public class Zombie {
	/************************************
	 * 			variables
	 ************************************/
	//For the fight on range
	private int hitpoints;		//the hitpoints of the zombie
	private float distance;		//the distance in m
	private float speed;		//the speed of the zombie
	
	//For the aimbar
	private float percent;		//The position of the target on the aimbar
	
	//graphics
	private TextureRegion aimbarIcon;	//The animation for the aimbar
	private TextureRegion zombieWalk;	//The animation for the Zombie Walk
	private Image aimbarImage;		//An actor for the aimbar
	
	/************************************
	 * 			constructor
	 ************************************/
	
	public Zombie(int hitpoints, float distance, float speed, float percent) {
		this.hitpoints = hitpoints;
		this.distance = distance;
		this.speed = speed;
		this.percent = (percent < 5f ? 5 : (percent > 95f ? 95 : percent));
		
		this.aimbarIcon = TextureHelper.FIGHT_SINGLE_ZOMBIE;
		this.zombieWalk = TextureHelper.FIGHT_SINGLE_ZOMBIE;
	}
	
	public Zombie(int hitpoints, float distance, float speed) {
		this(hitpoints, distance, speed, new Float((Math.random()*90)+5));
	}
	
	/************************************
	 * 			getter and setter
	 ************************************/
	
	public int getHitpoints() {
		return hitpoints;
	}

	public void setHitpoints(int hitpoints) {
		if(hitpoints >= 0) this.hitpoints = hitpoints;
		else this.hitpoints = 0;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		if(speed > 0.1f) this.speed = speed;
		else this.speed = 0.1f;
	}

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}

	public TextureRegion getAimbarIcon() {
		return aimbarIcon;
	}

	public TextureRegion getZombieWalk() {
		return zombieWalk;
	}
	
	public Image getAimbarImage() {
		return this.aimbarImage;
	}
	
	/************************************
	 * 			methods
	 ************************************/
	
	/**
	 * Makes the zombie damage and returns if the zombie is dead (dead).
	 * 
	 * @param damage the damage the zombie should gain
	 * @return true if the zombie is dead, otherwise false
	 */
	public boolean makeDamage(int damage) {
		if(getHitpoints() > damage) {
			setHitpoints(getHitpoints()-damage);
			return false;
		}else{
			setHitpoints(0);
			return true;
		}
	}
	
	/**
	 * checks if the coordinate x hits the zombie
	 * 
	 * @param x the position where to shoot at
	 * @return true if the zombie gets hit otherwise false
	 */
	public boolean hit(float x) {
		if(this.aimbarImage == null) return false;
		
		if(x >= getPercent() && x <= getPercent()+getAimbarImage().getWidth()) {
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * create an image actor for the aimbar to display the zombie icon
	 * 
	 * @param barX the x position of the aimbar
	 * @param barY the y position of the aimbar
	 * @return returns the new created aimbarImage
	 */
	public Image createAimbarImage(float barX, float barY) {
		aimbarImage = new Image(getAimbarIcon());
		aimbarImage.setPosition(barX+getPercent(), barY);
		return aimbarImage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((aimbarIcon == null) ? 0 : aimbarIcon.hashCode());
		result = prime * result
				+ ((aimbarImage == null) ? 0 : aimbarImage.hashCode());
		result = prime * result + Float.floatToIntBits(distance);
		result = prime * result + hitpoints;
		result = prime * result + Float.floatToIntBits(percent);
		result = prime * result + Float.floatToIntBits(speed);
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
		Zombie other = (Zombie) obj;
		if (aimbarIcon == null) {
			if (other.aimbarIcon != null)
				return false;
		} else if (!aimbarIcon.equals(other.aimbarIcon))
			return false;
		if (aimbarImage == null) {
			if (other.aimbarImage != null)
				return false;
		} else if (!aimbarImage.equals(other.aimbarImage))
			return false;
		if (Float.floatToIntBits(distance) != Float
				.floatToIntBits(other.distance))
			return false;
		if (hitpoints != other.hitpoints)
			return false;
		if (Float.floatToIntBits(percent) != Float
				.floatToIntBits(other.percent))
			return false;
		if (Float.floatToIntBits(speed) != Float.floatToIntBits(other.speed))
			return false;
		return true;
	}
	
	
}
