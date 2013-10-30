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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.InputHelper;
import com.zombies.helper.SkinHelper;
import com.zombies.helper.SoundHelper;
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
	private Image imgBackground;			//The image of the background
	private TextButton buttons[];				//Menubuttons
	
	private float time = 0;					//This variable is used for check the time between two inputs
	
	private ShapeRenderer debugRenderer;
	
	/********************************************************
	 * 				Construcktors
	 ********************************************************/
	
	public MainMenu(ProjectZ projectZRef, ShapeRenderer debugRenderer) {
		this.selection = MENU_START;
		this.projectZRef = projectZRef;
		
		System.out.println(Gdx.graphics.getHeight() + "/" + Gdx.graphics.getWidth());
		
		Texture texBackground = new Texture(Gdx.files.internal("data/gfx/MainMenu/background_image.png"));
		imgBackground = new Image(texBackground);
		imgBackground.setPosition(0, GUIHelper.getNewCoordinates(0, (int)(1024 * 1.5)));
		imgBackground.setName("background");
		imgBackground.scale(0.35f,0.5f);
		this.addActor(imgBackground);
		
		createMenu();
		
		this.debugRenderer = debugRenderer;
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
	
	private void createMenu() {
		int menuW = 1024, menuH = 786;
		Table menu = new Table().top().left();
		menu.size(menuW, menuH);
		menu.setPosition(128, GUIHelper.getNewCoordinates(128, menuH));
		menu.debug();
		
		buttons = new TextButton[3];	
		
		buttons[MENU_START] = new TextButton("Start", SkinHelper.SKIN);
		buttons[MENU_START].toggle();
		buttons[MENU_OPTIONS] = new TextButton("Optionen", SkinHelper.SKIN);
		buttons[MENU_EXIT] = new TextButton("Beenden", SkinHelper.SKIN);
		
		buttons[MENU_START].addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(buttons[MENU_START].isPressed()) {
					fireSelection();
				}
			}
		});
		
		buttons[MENU_OPTIONS].addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(buttons[MENU_OPTIONS].isPressed()) {
					fireSelection();
				}
			}
		});
		
		buttons[MENU_EXIT].addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(buttons[MENU_EXIT].isPressed()) {
					fireSelection();
				}
			}
		});
		
		menu.add(buttons[MENU_START]).width(256);
		menu.row().height(128);
		menu.add().row();
		menu.add(buttons[MENU_OPTIONS]).width(256);
		menu.row().height(128);
		menu.add().row();
		menu.add(buttons[MENU_EXIT]).width(256);
		
		addActor(menu);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(buttons[MENU_START].isOver()) {
			buttons[selection].toggle();
			buttons[MENU_START].toggle();
			selection = MENU_START;
		}else if(buttons[MENU_OPTIONS].isOver()) {
			buttons[selection].toggle();
			buttons[MENU_OPTIONS].toggle();
			selection = MENU_OPTIONS;
		}else if(buttons[MENU_EXIT].isOver()) {
			buttons[selection].toggle();
			buttons[MENU_EXIT].toggle();
			selection = MENU_EXIT;
		}
		
		//Keyboard inputs and maybe Controller?
		time += delta;
		if(InputHelper.UP && time > 0.25f) {
			if(selection <= MENU_START) {
				buttons[selection].toggle();
				selection = MENU_EXIT;
				buttons[selection].toggle();
			}else{
				buttons[selection].toggle();
				selection--;
				buttons[selection].toggle();
			}
			time = 0;
		}else if (InputHelper.DOWN && time > 0.25f){
			if(selection >= MENU_EXIT) {
				buttons[selection].toggle();
				selection = MENU_START;
				buttons[selection].toggle();
			}else{
				buttons[selection].toggle();
				selection++;
				buttons[selection].toggle();
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
				SoundHelper.play_Shotgun();
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
	}
}
