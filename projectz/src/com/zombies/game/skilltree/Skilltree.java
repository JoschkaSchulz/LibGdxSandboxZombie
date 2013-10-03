package com.zombies.game.skilltree;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;


public class Skilltree {
	private LinkedList<Skill> avaibleSkills;
	private LinkedList<Skill> skilltreeSkills;
	
	public Skilltree(XmlReader.Element skills) {
		this.fillSkilltree(skills);
	}
	
	/**
	 * Creates a Skilltree out of an XML Element
	 * 
	 * @param skills the XML element from the charakter XML file
	 */
	public void fillSkilltree(XmlReader.Element skills) {
		LinkedList<Skill> allSkills = Skill.loadSkills(Gdx.files.internal("data/xml/skills.xml"));
		
		Iterator iterator_skills = skills.getChildrenByName("skill").iterator();
		while(iterator_skills.hasNext()){
		     XmlReader.Element skill_element = (XmlReader.Element)iterator_skills.next();
		     
		     int id = skill_element.getIntAttribute("id");
		     boolean known = skill_element.getBooleanAttribute("known");
			 
		     for(Skill s : allSkills) {
		    	 if(s.getId() == id) {
		    		 skilltreeSkills.add(s);
		    		 if(known) avaibleSkills.add(s);
		    	 }
		     }
		 }
	}
	
	/**
	 * Let the player learn the Skill if it is in the list of the avaible skills
	 * 
	 * @param skill the Skill that should be learnd
	 */
	public void learnSkill(Skill skill) {
		learnSkill(skill.getId());
	}
	
	/**
	 * Let the player learn the Skill from the SkillID if it is in the list of the avaible skills
	 * 
	 * @param skill the SkillID that should be learnd
	 */
	public void learnSkill(int skillID) {
		for(Skill s : skilltreeSkills) {
			if(s.getId() == skillID) avaibleSkills.add(s);
		}
	}
}
