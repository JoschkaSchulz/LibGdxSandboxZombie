package com.zombies.game.event;

public class EventType {
	private int id;
	private String name;
	private String className;
	private String groups;
	private int level;
	
	public EventType(int id, String name, String className, String groups, int level) {
		this.id = id;
		this.name = name;
		this.className = className;
		this.groups = groups;
		this.level = level;
	}
	
	public String getName() {
		return name;
	}
	
	public int getID() {
		return id;
	}
	
	public String getClassName() {
		return className;
	}
	
	public boolean containsGroup(String group) {
		return groups.contains(group);
	}
	
	public boolean hasLevel(int level) {
		return level == this.level;
	}
	
	public String toString() {
		return "Event("+id+"):"+name;
	}
}
