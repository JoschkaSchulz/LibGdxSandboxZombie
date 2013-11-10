package com.zombies.game.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class FogTile extends MapTile {

	private boolean visible;
	private TextureRegionDrawable texture;
	
	public FogTile(TextureRegion texture, int x, int y) {
		super(texture, x, y, MapTile.TYPE_FOG);
		
		this.texture = new TextureRegionDrawable(texture);
		this.visible = true;
	}
	
	/**
	 * Changes the state of the fog from visible to invisible
	 */
	public void toggleFog() {
		this.visible = !this.visible;
	}
	
	/**
	 * Getter for visibility
	 */
	public boolean isVisible() {
		return this.visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
		if(visible){
			super.setDrawable(texture);
		} else {
			super.setDrawable(null);
		}
	}

}
