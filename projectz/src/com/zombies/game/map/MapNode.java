package com.zombies.game.map;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MapNode extends Image {
	private int id;
	private LinkedList<MapNode> links;
	
	public MapNode(int id, Texture tex, LinkedList<MapNode> links, int x, int y) {
		super(tex);
		
		this.links = links;
		setPosition(x, y);
		this.id = id;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<MapNode> getLinks() {
		return (LinkedList<MapNode>) links.clone();
	}
	
	public int getId() {
		return id;
	}

	public boolean equals(Object other) {
		if ( other == null ) return false;
		if ( other == this ) return true;

		MapNode skill = (MapNode) other;
		return this.getId() == skill.getId();
	}
}
