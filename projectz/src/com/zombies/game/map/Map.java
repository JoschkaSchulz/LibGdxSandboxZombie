package com.zombies.game.map;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.XmlReader;
import com.zombies.game.GameHandler;
import com.zombies.game.skilltree.Skill;
import com.zombies.helper.GUIHelper;
import com.zombies.helper.InputHelper;

public class Map extends Group {
	
	public static final int TYPE_CITY 		= 0;
	public static final int TYPE_FOREST 	= 1;
	
	private LinkedList<String> files[];
	
	private LinkedList<MapNode> nodes;
	private Image cityBackground;
	private float scaleFactor;
	
	private float dragX;
	private float dragY;
	
	@SuppressWarnings("unchecked")
	public Map() {
		//TODO: dynamischer reader sollte eingebaut werden der jede xml in maps findet
		this.files = new LinkedList[2];
		this.files[TYPE_CITY] = new LinkedList<String>();		
		this.files[TYPE_FOREST] = new LinkedList<String>();
		
		this.files[TYPE_CITY].add("testing_city.xml");
		
		dragX = 0;
		dragY = 0;
	}
	
	public void scale(int scaleFactor) {
		cityBackground.scale(scaleFactor);
		this.scaleFactor = scaleFactor;
	}
	
	//TODO: alles  noch nicht fertig
	private void readXML(FileHandle xmlFile) {
		try {
			LinkedList<Skill> skilllist = new LinkedList<Skill>();
			
			XmlReader xml = new XmlReader();
			XmlReader.Element xml_element = xml.parse(xmlFile);
			Iterator iterator_skills = xml_element.getChildrenByName("map").iterator();
			while(iterator_skills.hasNext()){
			     XmlReader.Element skill_element = (XmlReader.Element)iterator_skills.next();
			     
			     int id = skill_element.getIntAttribute("id");
				 String name = skill_element.getChildByName("name").getText();
				 String description = skill_element.getChildByName("description").getText();
			     skilllist.add(new Skill(id, name, description));
			 }
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Fehler beim Laden der Map!");
		}
	}
	
	public void generateMap(int type) {
		getParent().addActor(new MapUI(this));
		
		switch(type) {
			default:	//city
				int rand = (int) (Math.random()*(files[TYPE_CITY].size()-1));
				readXML(Gdx.files.internal("data/maps" + files[TYPE_CITY].get(rand)));
				
				Texture cityTex = new Texture(Gdx.files.internal("data/gfx/maps/testing_city.png"));
				cityBackground = new Image(cityTex);
				cityBackground.setPosition(0, GUIHelper.getNewCoordinates(0, 2048));
				cityBackground.setName("city");
				this.addActor(cityBackground);
				break;
			case TYPE_FOREST:
				break;
		}
	}
	

	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(InputHelper.DRAG && dragX == 0 && dragY == 0) {
			dragX = InputHelper.DRAG_X;
			dragY = InputHelper.DRAG_Y;
		}else if(InputHelper.DRAG) {
			int vectorX = (int)(InputHelper.DRAG_X - dragX);
			int vectorY = (int)(InputHelper.DRAG_Y - dragY);
			dragX = dragY = 0;
			translate(vectorX, vectorY);
			
			System.out.println(getX() + "/" + getY());
			//Correct x and y for map borders
			if(getX() > 0) setX(0);
			else if(getX() < -2048 + Gdx.graphics.getWidth()) setX(-2048 + Gdx.graphics.getWidth());
			if(getY() < 0) setY(0);
			else if(getY() > 2048 - Gdx.graphics.getHeight()) setY(2048 - Gdx.graphics.getHeight());
		}else dragX = dragY = 0;
	}
}
