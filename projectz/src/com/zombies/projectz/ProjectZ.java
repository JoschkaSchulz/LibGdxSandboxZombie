package com.zombies.projectz;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.zombies.game.GameHandler;
import com.zombies.helper.InputHelper;
import com.zombies.mainmenu.MainMenu;

public class ProjectZ implements ApplicationListener {
	private Stage stage;
	private MainMenu mainMenu;
	private GameHandler gameHandler;
	private InputHelper input;
	
	public void create() {
		stage = new Stage();
		
		//Start with the main Menu (memo: stage.clear removes all actors)
		mainMenu = new MainMenu(this);
		stage.addActor(mainMenu);
		
		Gdx.input.setInputProcessor(stage);
		
		//InputListener first parameter is the touchpad that isn't active
		input = new InputHelper(null, true);
		stage.addListener(input);
	}

	public void startGame() {
		stage.clear();
		
		this.gameHandler = new GameHandler();
		stage.addActor(gameHandler);
	}
	
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}

	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public void dispose() {
		stage.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
