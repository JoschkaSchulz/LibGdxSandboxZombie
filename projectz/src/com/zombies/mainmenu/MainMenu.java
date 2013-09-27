package com.zombies.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.InputHelper;
import com.zombies.projectz.ProjectZ;

public class MainMenu extends Group {
	/********************************************************
	 * 				Variables
	 ********************************************************/
	public final static int MENU_START 		= 0;	//static final variable for the start button
	public final static int MENU_OPTIONS 	= 1;	//static final variable for the options button
	public final static int MENU_EXIT 		= 2;	//static final variable for the exit button
	
	private ProjectZ projectZRef;			//A reference of the Main Class of the project
	
	private int selection;					//The actual selected  menu
	private Image imgButtonStart;			//The image of the start button
	private Image imgButtonOptions;			//The image of the options button
	private Image imgButtonExit;			//The image of the  exit button
	
	private float time = 0;					//This variable is used for check the time between two inputs
	
	private ShapeRenderer debugRenderer;
	
	/********************************************************
	 * 				Construcktors
	 ********************************************************/
	
	public MainMenu(ProjectZ projectZRef) {
		this.selection = MENU_START;
		this.projectZRef = projectZRef;
		
		Texture texButtonStart = new Texture(Gdx.files.internal("data/gfx/MainMenu/button_start.png"));
		imgButtonStart = new Image(texButtonStart);
		imgButtonStart.setPosition(450, GUIHelper.getNewCoordinates(60, 64));
		imgButtonStart.setName("button_start");
		this.addActor(imgButtonStart);
		
		Texture texButtonOptions = new Texture(Gdx.files.internal("data/gfx/MainMenu/button_options.png"));
		imgButtonOptions = new Image(texButtonOptions);
		imgButtonOptions.setPosition(340, GUIHelper.getNewCoordinates(215, 64));
		imgButtonOptions.setName("button_options");
		this.addActor(imgButtonOptions);
		
		Texture texButtonExit = new Texture(Gdx.files.internal("data/gfx/MainMenu/button_exit.png"));
		imgButtonExit = new Image(texButtonExit);
		imgButtonExit.setPosition(40, GUIHelper.getNewCoordinates(345, 64));
		imgButtonExit.setName("button_exit");
		this.addActor(imgButtonExit);
		
		this.debugRenderer = new ShapeRenderer();
	}

	/********************************************************
	 * 				getter and setter
	 ********************************************************/
	public int getSelection() {
		return this.selection;
	}
	
	/********************************************************
	 * 				Methods
	 ********************************************************/
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		//Mouse and Touch inputs
		Actor overActor = this.hit(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY(), false);
		if(overActor != null) {
			if (overActor.getName().equals("button_start")) {
				selection = MENU_START;
				if(Gdx.input.isTouched()) fireSelection();
			}else if (overActor.getName().equals("button_options")) {
				selection = MENU_OPTIONS;
				if(Gdx.input.isTouched()) fireSelection();
			}else if (overActor.getName().equals("button_exit")) {
				selection = MENU_EXIT;
				if(Gdx.input.isTouched()) fireSelection();
			}
		}
		
		//Keyboard inputs and maybe Controller?
		time += delta;
		if(InputHelper.UP && time > 0.25f) {
			if(selection < MENU_START) {
				selection = MENU_EXIT;
			}else{
				selection--;
			}
			time = 0;
		}else if (InputHelper.DOWN && time > 0.25f){
			if(selection > MENU_EXIT) {
				selection = MENU_START;
			}else{
				selection++;
			}
			time = 0;
		}else if(InputHelper.ACTION) {
			fireSelection();
		}
		
	}

	/**
	 * fireSelection is used to select the action that should be used
	 * after the mouse is pressed, touch is pressed or the action key
	 * on the Keyboard is pressed.
	 */
	private void fireSelection() {
		switch(selection) {
			default: 
				projectZRef.startGame();
			case MENU_OPTIONS:
				break;
			case MENU_EXIT:
				Gdx.app.exit();
				break;
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		//Debug functions
		batch.end();
		this.debugRenderer.begin(ShapeType.Rectangle);
		this.debugRenderer.setColor(Color.RED);
		switch(selection) {
			default:
				this.debugRenderer.rect(450, GUIHelper.getNewCoordinates(60, 64), 256, 64);
				break;
			case MENU_OPTIONS:
				this.debugRenderer.rect(340, GUIHelper.getNewCoordinates(215, 64), 256, 64);
				break;
			case MENU_EXIT:
				this.debugRenderer.rect(40, GUIHelper.getNewCoordinates(345, 64), 256, 64);
				break;
		}
		this.debugRenderer.end();
		batch.begin();
	}
}
