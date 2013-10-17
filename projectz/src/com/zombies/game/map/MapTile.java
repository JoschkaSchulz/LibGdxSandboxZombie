package com.zombies.game.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zombies.helper.GUIHelper;

public class MapTile extends Image {
	
	/*****************************************************************
	 *					Variables
	 ****************************************************************/
	private static final int TILE_WIDTH = 128;
	private static final int TILE_HEIGHT = 128;
	
	public static final int TYPE_STREET = 0;
	public static final int TYPE_LVL1 = 1;
	public static final int TYPE_LVL2 = 2;
	public static final int TYPE_LVL3 = 3;
	public static final int TYPE_LVL4 = 4;
	public static final int TYPE_LVL5 = 5;
	
	private boolean debug;
	private Color debugColor;
	private ShapeRenderer debugRenderer;
	
	private boolean isStart;
	private boolean isExit;
	private int type;
	
	/*****************************************************************
	 *					Constructors
	 ****************************************************************/

	/**
	 * Debug Constructor, will show the tile as a colored field
	 * @param x
	 * @param y
	 */
	public MapTile(int x, int y, Color c) {
		debug = true;
		debugColor = c;
		setPosition(x, y);
		setBounds(x, y, TILE_WIDTH, TILE_HEIGHT);
		debugRenderer = new ShapeRenderer();
	}
	
	public MapTile(Texture texture, int x, int y) {
		super(texture);
		setPosition(x, y);
		setBounds(x, y, TILE_WIDTH, TILE_HEIGHT);
		debug = false;
	}
	/*****************************************************************
	 *					Getter and Setter
	 ****************************************************************/
	
	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public boolean isExit() {
		return isExit;
	}

	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	/*****************************************************************
	 *					Methods
	 ****************************************************************/
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if(debug) {
			super.draw(batch, parentAlpha);
			
			//Debug functions
			batch.end();
			this.debugRenderer.begin(ShapeType.FilledRectangle);
			this.debugRenderer.setColor(debugColor);
			this.debugRenderer.filledRect(getX(), GUIHelper.getNewCoordinates(((int)getY()), TILE_HEIGHT), TILE_WIDTH, TILE_HEIGHT);
			this.debugRenderer.end();
			batch.begin();
		}else{
			super.draw(batch, parentAlpha);
		}
	}
}
