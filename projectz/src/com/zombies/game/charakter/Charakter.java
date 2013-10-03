package com.zombies.game.charakter;

import java.util.Iterator;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;

public class Charakter {
	private FileHandle xmlFile;
	
	private String name;
	private String age;
	private String height;
	private String image;
	private String story;
	private int meal;
//	private Skilltree skilltree;
	
	public Charakter(FileHandle xmlFile) {
		this.xmlFile = xmlFile;
		
		this.loadCharakter();
	}

	/******************************************
	 * 			getter & setter
	 ******************************************/
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}
	
	public int getMeal() {
		return meal;
	}

	public void setMeal(int meal) {
		this.meal = meal;
	}

	/******************************************
	 * 			methods
	 ******************************************/
	
	private void loadCharakter() {
		try {
			XmlReader xml = new XmlReader();
			XmlReader.Element xml_element = xml.parse(this.xmlFile);
		
			this.name = xml_element.getChildByName("name").getText();
			this.age = xml_element.getChildByName("age").getText();
			this.height = xml_element.getChildByName("height").getText();
			this.image = xml_element.getChildByName("image").getText();
			this.story = xml_element.getChildByName("story").getText();
		
			this.meal = xml_element.getChildByName("meal").getIntAttribute("id");
			
		}catch(Exception e) {
//			e.printStackTrace();
			System.err.println("Fehler beim Laden des Charakters!");
		}
	}
}
