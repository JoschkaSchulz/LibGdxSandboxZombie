package com.zombies.game.skilltree;

import java.util.LinkedList;

import com.badlogic.gdx.utils.XmlReader;


public class Skilltree {
	private LinkedList<Skill> avaibleSkills;
	private LinkedList<Skill> SkilltreeSkills;
	
	public Skilltree() {
		
	}
	
	public void fillSkilltree(LinkedList<Skills>) {
		
	}
	
	public void learnSkill(Skill skill) {
		learnSkill(skill.getId());
	}
	
	public void learnSkill(int skillID) {
		for(Skill s : SkilltreeSkills) {
			if(s.getId() == skillID) avaibleSkills.add(s);
		}
	}
}
