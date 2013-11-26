/*
 * 	__________                   __               __ __________
	\______   \_______  ____    |__| ____   _____/  |\____    /
	 |     ___/\_  __ \/  _ \   |  |/ __ \_/ ___\   __\/     / 
	 |    |     |  | \(  <_> )  |  \  ___/\  \___|  | /     /_ 
	 |____|     |__|   \____/\__|  |\___  >\___  >__|/_______ \
                        	\______|    \/     \/            \/
 * 
 */

package com.zombies.projectz;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;






import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.ui.Table;



import com.zombies.game.GameHandler;
import com.zombies.helper.SkinHelper;
import com.zombies.helper.InputHelper;
import com.zombies.helper.TextureHelper;
import com.zombies.mainmenu.MainMenu;

public class ProjectZ implements ApplicationListener {
	/**************************************************************************
	 * 				Variables
	 **************************************************************************/
	
	private Stage stage;
	private MainMenu mainMenu;
	private GameHandler gameHandler;
	private InputHelper input;
	private ShapeRenderer debugRenderer;
	
	/**************************************************************************
	 * 				Constructor
	 **************************************************************************/
	
	/**
	 * This class doesn't need a constructor.
	 */
	
	/**************************************************************************
	 * 				Methods
	 **************************************************************************/
	
	/**
	 * Create the the game start
	 */
	public void create() {
		//Load Skin
		SkinHelper.loadSkinHelper();
		
		//Load Textures
		TextureHelper.initializeTextures();
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		debugRenderer = new ShapeRenderer();
		
		//Start with the main Menu (memo: stage.clear removes all actors)
		mainMenu = new MainMenu(this, this.debugRenderer);
		stage.addActor(mainMenu);
		
		
		//InputListener first parameter is the touchpad that isn't active
		input = new InputHelper(null, true);
		stage.addListener(input);
		
		//Preloading sound in Gamecache
		com.zombies.helper.SoundHelper.initialize_all_sounds();
		
		//Debug output something goes wrong with the height and width :(
		System.out.println("~~Spiel gestartet~~");
		System.out.println("Width: "+Gdx.graphics.getWidth()+" Height:" + Gdx.graphics.getHeight());
	}

	/**
	 * Clears the stage and creates a new GameHandler
	 */
	public void startGame() {
		stage.clear();
		
		this.gameHandler = new GameHandler(this.debugRenderer);
		stage.addActor(gameHandler);
	}
	
	/**
	 * resizes the Window and set the Viewport
	 */
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	/**
	 * Renders every single frame
	 * 
	 * TODO: the try catch block is only a workaround on a wrong counter! 
	 */
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		try{
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();
			Table.drawDebug(stage);
		}catch(Exception e) {
			System.err.println("Fehler beim render!");
		}
	}

	/**
	 * clean exit the game
	 */
	public void dispose() {
		stage.dispose();
	}

	/**
	 * If the game is paused this method is started
	 */
	@Override
	public void pause() {
	}

	/**
	 * If the game is resumed this method is used
	 */
	@Override
	public void resume() {
	}
}
