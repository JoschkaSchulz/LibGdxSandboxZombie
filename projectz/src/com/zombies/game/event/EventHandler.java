package com.zombies.game.event;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;

import com.badlogic.gdx.utils.XmlReader;

import com.zombies.game.charakter.Charakter;
import com.zombies.game.skilltree.Skill;

public class EventHandler extends Group {
	
	//Static Variables
	public static final String EVENTTYPE_STREET = "w";
	public static final String EVENTTYPE_CITY = "c";
	public static final String EVENTTYPE_FOREST = "f";
	
	private ArrayList<EventType> allEvents;
	private ShapeRenderer debugRenderer;
	
	private Event currentEvent;
	private Charakter charRef;
	
	public EventHandler(ShapeRenderer debugRenderer) {
		allEvents = new ArrayList<EventType>();
		this.debugRenderer = debugRenderer;
		
		//Loads all events from the events_[lang].xml
		loadEvents();
	}
	
	public void setCharRef(Charakter charRef) {
		this.charRef = charRef;
	}
	
	/**
	 * Returns all Events from the group String it is given
	 * 
	 * @param group a String with the characters of the groups
	 * @return an ArrayList of the events
	 */
	public ArrayList<EventType> getEventsFromGroup(String group) {
		ArrayList<EventType> events = new ArrayList<EventType>();
		
		for(int i = 0; i < group.length(); i++) {
			for(EventType e : allEvents) {
				if(e.containsGroup(Character.toString(group.charAt(i)))) events.add(e);
			}
		}
		
		return events;
	}
	
	/**
	 * Returns all Events from the group String it is given
	 * 
	 * @param group a String with the characters of the groups
	 * @param level a Integer with the level
	 * @return an ArrayList of the events
	 * 
	 * TODO: Level muss noch bestimmt werden!
	 */
	public ArrayList<EventType> getEventsFromGroup(String group, int level) {
		ArrayList<EventType> events = new ArrayList<EventType>();
		
		for(int i = 0; i < group.length(); i++) {
			for(EventType e : allEvents) {
				if(e.containsGroup(Character.toString(group.charAt(i))) && e.hasLevel(level)) {
					events.add(e);
				}
			}
		}
		return events;
	}
	
	/**
	 * Used by the constructor to load all the Events
	 */
	public void loadEvents() {
		try {
			LinkedList<Skill> skilllist = new LinkedList<Skill>();
			
			XmlReader xml = new XmlReader();
			XmlReader.Element xmlElement = xml.parse(Gdx.files.internal("data/xml/events_ger.xml"));
			Iterator iteratorEvents = xmlElement.getChildrenByName("event").iterator();
			while(iteratorEvents.hasNext()){
			     XmlReader.Element eventElement = (XmlReader.Element)iteratorEvents.next();
			     
			     EventType event = new EventType(eventElement.getIntAttribute("id"),
			    		 						 eventElement.getChildByName("name").getText(),
			    		 						 eventElement.getChildByName("classname").getText(),
			    		 						 eventElement.getChildByName("group").getAttribute("group"),
			    		 						 eventElement.getChildByName("level").getIntAttribute("level"));
			     allEvents.add(event);
			     System.out.println("Adding something!");
			 }
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Fehler beim Laden der XML Datei!");
		}
	}

	public EventType getEventById(int id) {
		EventType event = new EventType(0, "No Event", "No Class", "", 0);
		
		for(EventType e : allEvents) {
			if(e.getID() == id) event = e;
		}
		
		return event;
	}
	
	public void loadEvent(int eventID) {
		try {
			//Find the Event in all Events
			EventType eventType = getEventById(eventID);
			//Generate dynamicly a new Event
			Class eventClass = Class.forName("com.zombies.game.events." + eventType.getClassName());
			//Get the right Constructor
			Constructor eventConstructor = eventClass.getConstructor(ShapeRenderer.class, Charakter.class);
			
			Event event = (Event)eventConstructor.newInstance(this.debugRenderer, this.charRef);
			
			//Start the Event
			currentEvent = event;
			addActor(currentEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clearCurrentEvents() {
		currentEvent = null;
		this.clear();
	}
	
	
	
}
