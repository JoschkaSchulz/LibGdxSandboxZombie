package com.zombies.game.charakter;

import java.util.Iterator;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.zombies.game.skilltree.Skill;
import com.zombies.game.skilltree.Skilltree;

public class Charakter {
	private FileHandle xmlFile;
	
	private String name;
	private String age;
	private String height;
	private String image;
	private String story;
	private int meal;
	private Skilltree skilltree;
	
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

	public Skilltree getSkilltree() {
		return skilltree;
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
			
			XmlReader.Element skills = xml_element.getChildByName("skills");
			this.skilltree = new Skilltree(skills);
			
		}catch(Exception e) {
			e.printStackTrace();
//			System.err.println("Fehler beim Laden des Charakters!");
		}
	}
	
	public String toString() {
		String avaibleSkills = "";
		String skilltreeSkills = "";
		
		for(Skill s : this.skilltree.getAvaibleSkills()) {
			avaibleSkills = avaibleSkills + "\n("+s.getId()+")"+s.getName();
		}
		
		for(Skill s : this.skilltree.getSkilltreeSkills()) {
			skilltreeSkills = skilltreeSkills + "\n("+s.getId()+")"+s.getName();
		}
		
		return "~~~Charakter~~~\nName:"+getName()+"\nAge: "+getAge()+"\nHeight: "+getHeight()+"\nAvaible Skills:"+avaibleSkills+"\nComplete Skilltree:"+skilltreeSkills;
	}
}
