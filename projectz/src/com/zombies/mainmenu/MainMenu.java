package com.zombies.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.zombies.helper.GUIHelper;

public class MainMenu extends Group {
	public final static int MENU_START = 0;
	public final static int MENU_OPTIONS = 1;
	public final static int MENU_EXIT = 2;
	
	private int selection;
	
	private ShapeRenderer debugRenderer;
	
	public MainMenu() {
		selection = 0;
		debugRenderer = new ShapeRenderer();
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		//Debug functions
		batch.end();
		debugRenderer.begin(ShapeType.FilledRectangle);
        debugRenderer.setColor(Color.RED);
        debugRenderer.filledRect(450, GUIHelper.getNewCoordinates(60, 80), 300, 80);
        debugRenderer.filledRect(340, GUIHelper.getNewCoordinates(215, 80), 300, 80);
        debugRenderer.filledRect(40, GUIHelper.getNewCoordinates(345, 80), 300, 80);
        debugRenderer.end();
		batch.begin();
	}
}
