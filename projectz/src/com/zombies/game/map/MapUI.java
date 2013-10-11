package com.zombies.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zombies.helper.GUIHelper;

public class MapUI extends Group {
	private Map mapRef;
	
	private Image zoomIn;
	private Image zoomOut;
	
	public MapUI(Map mapRef) {
		this.mapRef = mapRef;
		
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		
		Texture zoomInTex = new Texture(Gdx.files.internal("data/gfx/map_ui/zoom_in.png"));
		zoomIn = new Image(zoomInTex);
		zoomIn.setPosition(w - 200, GUIHelper.getNewCoordinates(h-70, 64));
		zoomIn.setName("zoom_in");
		this.addActor(zoomIn);

		Texture zoomOutTex = new Texture(Gdx.files.internal("data/gfx/map_ui/zoom_out.png"));
		zoomOut = new Image(zoomOutTex);
		zoomOut.setPosition(w - 100, GUIHelper.getNewCoordinates(h-70, 64));
		zoomOut.setName("zoom_out");
		this.addActor(zoomOut);
	}
}
