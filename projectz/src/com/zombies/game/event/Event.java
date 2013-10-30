package com.zombies.game.event;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.zombies.game.GameHandler;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.SkinHelper;

public abstract class Event extends Group {
	ShapeRenderer debugRenderer;
	
	public Event() {
		
	}
	
	public void configEvent(ShapeRenderer debugRenderer) {
		this.debugRenderer = debugRenderer;
	}
	
	protected void finishEvent() {
		try{
			((GameHandler)getParent().getParent()).finishEvent();			
		}catch(Exception e) {
			System.err.println("Fehler beim finden des GameHandler im Event!");
		}
		try{
			((EventHandler)getParent()).clearCurrentEvents();	
		}catch(Exception e) {
			System.err.println("Fehler beim finden des EventHandler im Event!");
		}
	}
	
	/**
	 * Shows a Dialog with max. 4 Answers
	 * 
	 * @param text a string with the text of the dialog
	 * @param answers a string array with the answers of the dialog
	 */
	public void showDialog(String text, String answers[]){
		Table dialog = new Table();
		dialog.setWidth(1024);
		dialog.setHeight(512);
		dialog.top();
		dialog.setPosition(128, GUIHelper.getNewCoordinates(128, 512));
		dialog.debug();
		
		Label l = new Label(text, SkinHelper.SKIN);
		l.setWrap(true);
		dialog.add(l).top().left().width(1024);
		for(int i = 0, n = (answers.length > 4 ? 4 : answers.length); i < n; i++) {
			dialog.row();
			TextButton tb = new TextButton(answers[i], SkinHelper.SKIN);
			switch(i){
				default:
					tb.addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							dialogAnswer1();
						}
					});
					break;
				case 1:
					tb.addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							dialogAnswer2();
						}
					});
					break;
				case 2:
					tb.addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							dialogAnswer3();
						}
					});
					break;
				case 3:
					tb.addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							dialogAnswer4();
						}
					});
					break;
			}
			dialog.add(tb).top().left();
		}
		addActor(dialog);
	}
	
	abstract protected void dialogAnswer1();
	abstract protected void dialogAnswer2();
	abstract protected void dialogAnswer3();
	abstract protected void dialogAnswer4();
	
}
