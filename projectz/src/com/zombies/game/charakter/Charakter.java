package com.zombies.game.charakter;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.zombies.game.inventory.Consumable;
import com.zombies.game.inventory.Gun;
import com.zombies.game.inventory.Inventory;
import com.zombies.game.inventory.Item;
import com.zombies.game.skilltree.Skill;
import com.zombies.game.skilltree.Skilltree;
import com.zombies.helper.ItemHelper;

public class Charakter {
	/******************************************
	 * variables
	 ******************************************/

	private FileHandle xmlFile;

	private String name; // Name of the charakter
	private String age; // Age of the charakter
	private String height; // Height of the charakter
	private String image; // The profile photo of the charakter
	private String story; // Background story of the charakter

	private int maxLP; // Maximal Life Points of the charakter
	private int currentLP; // Current Life Points of the charakter
	private int maxStomach; // ???
	private int currentStomach; // ???
	private int maxThirst; // Maximal thirst of the charakter
	private int currentThirst; // Current thirst of the charakter

	private int mapX; // The X coordinate of the map matrix
	private int mapY; // The Y coordinate of the map matrix

	private int meal; // ???
	private Skilltree skilltree; // The skilltree of the charakter

	private Inventory inventory; 	// The charakter inventory
	private Gun equipGunSlot;		//The Slot for the Gund
	private Item equipMeleeSlot;	//The Slot for the Melee Weapon
	private Item equipAmorSlot;		//The Slot for the Armor
	private Item equipBackpackSlot;	//Slot for the backpack
	
	public Charakter(FileHandle xmlFile) {
		this.xmlFile = xmlFile;

		inventory = new Inventory();
		
		this.loadCharakter();
	}

	/******************************************
	 * getter & setter
	 ******************************************/

	public void setMapCoordinates(int x, int y) {
		this.mapX = x;
		this.mapY = y;
	}

	public int getMapX() {
		return this.mapX;
	}

	public int getMapY() {
		return this.mapY;
	}

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

	public int getMaxLP() {
		return this.maxLP;
	}

	public void setMaxLP(int maxLP) {
		this.maxLP = maxLP;
	}

	public int getCurrentLP() {
		return currentLP;
	}

	public void setCurrentLP(int currentLP) {
		if(currentLP >= 0 && currentLP <= getMaxLP()) {
			this.currentLP = currentLP;
		}else if(currentLP < 0){
			this.currentLP = 0;
		}else if(currentLP > getMaxLP()) {
			this.currentLP = getMaxLP();
		}
	}

	public int getMaxStomach() {
		return maxStomach;
	}

	public void setMaxStomach(int maxStomach) {
		this.maxStomach = maxStomach;
	}

	public int getCurrentStomach() {
		return currentStomach;
	}

	public void setCurrentStomach(int currentStomach) {
		if(currentStomach >= 0 && currentStomach <= getMaxStomach()) {
			this.currentStomach = currentStomach;
		}else if(currentStomach < 0){
			this.currentStomach = 0;
		}else if(currentStomach > getMaxStomach()) {
			this.currentStomach = getMaxStomach();
		}
	}

	public int getMaxThirst() {
		return maxThirst;
	}

	public void setMaxThirst(int maxThirst) {
		this.maxThirst = maxThirst;
	}

	public int getCurrentThirst() {
		return currentThirst;
	}

	public void setCurrentThirst(int currentThirst) {
		if(currentThirst >= 0 && currentThirst <= getMaxThirst()) {
			this.currentThirst = currentThirst;
		}else if(currentThirst < 0){
			this.currentThirst = 0;
		}else if(currentThirst > getMaxThirst()) {
			this.currentThirst = getMaxThirst();
		}
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public Gun getEquipGunSlot() {
		return equipGunSlot;
	}

	public void setEquipGunSlot(Gun equipGunSlot) {
		this.equipGunSlot = equipGunSlot;
	}

	public Item getEquipMeleeSlot() {
		return equipMeleeSlot;
	}

	public void setEquipMeleeSlot(Item equipMeleeSlot) {
		this.equipMeleeSlot = equipMeleeSlot;
	}

	public Item getEquipAmorSlot() {
		return equipAmorSlot;
	}

	public void setEquipAmorSlot(Item equipAmorSlot) {
		this.equipAmorSlot = equipAmorSlot;
	}

	public Item getEquipBackpackSlot() {
		return equipBackpackSlot;
	}

	public void setEquipBackpackSlot(Item equipBackpackSlot) {
		this.equipBackpackSlot = equipBackpackSlot;
	}
	
	/******************************************
	 * methods
	 ******************************************/

	/**
	 * Reduce the Stomach of the character but not under zero
	 * 
	 * @param reduce the value to reduce
	 */
	public void reduceStomach(int reduce) {
		if(getCurrentStomach() - reduce >= 0) {
			setCurrentStomach(getCurrentStomach() - reduce);
		}else{
			setCurrentStomach(0);
			reduceLP(reduce - getCurrentStomach());
		}
	}

	/**
	 * Reduce the Thirst of the character but not under zero
	 * 
	 * @param reduce the value to reduce
	 */
	public void reduceThirst(int reduce) {
		if(getCurrentThirst() - reduce >= 0) {
			setCurrentThirst(getCurrentThirst() - reduce);
		}else{
			setCurrentThirst(0);
			reduceLP(reduce - getCurrentThirst());
		}
	}
	
	public void reduceLP(int reduce) {
		if(getCurrentLP() - reduce >= 0) {
			setCurrentLP(getCurrentLP() - reduce);
		}else{
			setCurrentLP(0);
		}
	}
	
	/**
	 * This method loads a character from the .xml file
	 */
	private void loadCharakter() {
		try {
			XmlReader xml = new XmlReader();
			XmlReader.Element xml_element = xml.parse(this.xmlFile);

			//load story values
			this.name = xml_element.getChildByName("name").getText();
			this.age = xml_element.getChildByName("age").getText();
			this.height = xml_element.getChildByName("height").getText();
			this.image = xml_element.getChildByName("image").getText();
			this.story = xml_element.getChildByName("story").getText();

			//load the maximal values
			this.meal = xml_element.getChildByName("meal")
					.getIntAttribute("id");
			this.maxLP = xml_element.getChildByName("Attributes")
					.getIntAttribute("maxLP");
			this.maxStomach = xml_element.getChildByName("Attributes")
					.getIntAttribute("maxStomach");
			this.maxThirst = xml_element.getChildByName("Attributes")
					.getIntAttribute("maxThirst");

			//load the effects
			this.currentLP = maxLP;
			this.currentStomach = maxStomach;
			this.currentThirst = maxThirst;

			//Load the skills
			XmlReader.Element skills = xml_element.getChildByName("skills");
			this.skilltree = new Skilltree(skills);

			//Load the Inventory
			loadInventory(xml_element.getChildByName("inventory"));
			
		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println("Fehler beim Laden des Charakters!");
		}
	}

	private void loadInventory(XmlReader.Element inventory) {
		try {
			XmlReader xml = new XmlReader();
			Iterator iterator_items = inventory.getChildrenByName("item").iterator();
			while(iterator_items.hasNext()){
			     XmlReader.Element inventory_element = (XmlReader.Element)iterator_items.next();
			     
			     String group = inventory_element.getAttribute("group");
			     
			     Item item = null;
			     if(group.equals("consumable")) {
			    	 item = ItemHelper.CONSUMABLES.get(inventory_element.getIntAttribute("id"));
			     }
			     
			     this.inventory.addItem(item);
			 }
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Fehler beim Laden der XML Datei!");
		}
	}
	
	public String toString() {
		String avaibleSkills = "";
		String skilltreeSkills = "";
		String inventory = "";
		for (Skill s : this.skilltree.getAvaibleSkills()) {
			avaibleSkills = avaibleSkills + "\n(" + s.getId() + ")"
					+ s.getName();
		}

		for (Skill s : this.skilltree.getSkilltreeSkills()) {
			skilltreeSkills = skilltreeSkills + "\n(" + s.getId() + ")"
					+ s.getName();
		}
		
		for (Item i : this.inventory.getInventory()) {
			inventory = inventory + i.toString() + "\n";
		}

		return "~~~Charakter~~~\nName:" + getName() + "\nAge: " + getAge()
				+ "\nHeight: " + getHeight() + "\nAvaible Skills:"
				+ avaibleSkills + "\nComplete Skilltree:" + skilltreeSkills
				+ "Inventory:\n" + inventory;
	}
}
