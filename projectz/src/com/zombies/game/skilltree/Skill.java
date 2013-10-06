package com.zombies.game.skilltree;

import java.util.Iterator;
import java.util.LinkedList;

import org.xml.sax.XMLReader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;

public class Skill {
	private int id;
	private String name;
	private String description;
	
	public Skill(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean equals(Object other) {
		if ( other == null ) return false;
		if ( other == this ) return true;

		Skill skill = (Skill) other;
		return this.getId() == skill.getId();
	}
	
	public static LinkedList<Skill> loadSkills(FileHandle xmlFile) {
		try {
			LinkedList<Skill> skilllist = new LinkedList<Skill>();
			
			XmlReader xml = new XmlReader();
			XmlReader.Element xml_element = xml.parse(xmlFile);
			Iterator iterator_skills = xml_element.getChildrenByName("skill").iterator();
			while(iterator_skills.hasNext()){
			     XmlReader.Element skill_element = (XmlReader.Element)iterator_skills.next();
			     
			     int id = skill_element.getIntAttribute("id");
				 String name = skill_element.getChildByName("name").getText();
				 String description = skill_element.getChildByName("description").getText();
			     skilllist.add(new Skill(id, name, description));
			 }
			
			return skilllist;
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Fehler beim Laden der Skills!");
			return new LinkedList<Skill>();
		}
	}
}
