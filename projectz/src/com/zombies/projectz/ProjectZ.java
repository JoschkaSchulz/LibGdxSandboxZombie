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
import com.zombies.mainmenu.MainMenu;

public class ProjectZ implements ApplicationListener {
	private Stage stage;
	private MainMenu mainMenu;
	
	public void create() {
		stage = new Stage();
		
		//Start with the main Menu (memo: stage.clear removes all actors)
		mainMenu = new MainMenu();
		stage.addActor(mainMenu);
		
		Gdx.input.setInputProcessor(stage);
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
