package com.zombies.game.event;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.XmlReader;
import com.zombies.game.skilltree.Skill;

public class EventHandler extends Group {
	
	private ArrayList<EventType> allEvents;
	private ShapeRenderer debugRenderer;
	
	private Event currentEvent;
	
	public EventHandler(ShapeRenderer debugRenderer) {
		allEvents = new ArrayList<EventType>();
		this.debugRenderer = debugRenderer;
		
		//Loads all events from the events_[lang].xml
		loadEvents();
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
			    		 						 eventElement.getChildByName("group").getText());
			     allEvents.add(event);
			 }
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Fehler beim Laden der XML Datei!");
		}
	}

	public EventType getEventById(int id) {
		EventType event = new EventType(0, "No Event", "No Class", "");
		
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
			Event event = (Event)Class.forName("com.zombies.game.events." + eventType.getClassName()).newInstance();
			//Configure the Event
			event.configEvent(this.debugRenderer);
			
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
	
	@Override
	public void act(float delta) {
		super.act(delta);		
	}
	
	
}
