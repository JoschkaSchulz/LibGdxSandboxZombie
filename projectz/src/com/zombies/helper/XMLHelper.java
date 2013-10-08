package com.zombies.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.zombies.game.skilltree.Skill;

public class XMLHelper {
	/**
	 * reads:
	 * <list>
	 * 		<item id="[unique]" value="[string]">
	 * </list>
	 */
	public static HashMap<String, String> loadXML(FileHandle file) {
		HashMap<String, String> xmlList = new HashMap<String, String>();
		try {
			LinkedList<Skill> skilllist = new LinkedList<Skill>();
			
			XmlReader xml = new XmlReader();
			XmlReader.Element xml_element = xml.parse(file);
			Iterator iterator_skills = xml_element.getChildrenByName("item").iterator();
			while(iterator_skills.hasNext()){
			     XmlReader.Element skill_element = (XmlReader.Element)iterator_skills.next();
			     
			     xmlList.put(skill_element.getAttribute("id"), skill_element.getAttribute("value"));
			 }
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Fehler beim Laden der XML Datei!");
		}
		return xmlList;
	}
}
