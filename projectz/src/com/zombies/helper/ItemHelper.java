package com.zombies.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.zombies.game.inventory.*;
import com.zombies.game.skilltree.Skill;

public class ItemHelper {
	
	public static HashMap<Integer , Consumable> CONSUMABLES;
	
	/**
	 * Reads all the xml files from the items xml file and orders every
	 * group in a seperate HashMap
	 */
	public static void initializeAllItems(){
		//Initialize the static fields
		CONSUMABLES = new HashMap<Integer, Consumable>();
		
		//Fill the static with all consumables
		Consumable consumable; // = new Consumable(ID, name, discription, food, drink, health, texture)
		int id, food, drink, health;
		String name, description;
	
		try {
			XmlReader xml = new XmlReader();
			XmlReader.Element xml_element = xml.parse(Gdx.files.internal("data/xml/items_ger.xml"));
			Iterator iterator_skills = xml_element.getChildByName("consumables").getChildrenByName("consumable").iterator();
			while(iterator_skills.hasNext()){
			     XmlReader.Element consumable_element = (XmlReader.Element)iterator_skills.next();
			     
			     id = consumable_element.getIntAttribute("id");
			     name = consumable_element.getChildByName("name").getText();
			     description = consumable_element.getChildByName("description").getText();
			     food = consumable_element.getChildByName("effects").getChildByName("hunger").getIntAttribute("val");
			     drink = consumable_element.getChildByName("effects").getChildByName("thirst").getIntAttribute("val");
			     health = consumable_element.getChildByName("effects").getChildByName("health").getIntAttribute("val");
			     
			     consumable = new Consumable(id, name, description, food, drink, health, null);
			     
			     CONSUMABLES.put(id, consumable);
			 }
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Fehler beim Laden der XML Datei!");
		}
	}
}
