package com.zombies.game.charakter;

import java.util.Iterator;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;

public class Charakter {
	private FileHandle xmlFile;
	
	public Charakter(FileHandle xmlFile) {
		this.xmlFile = xmlFile;
		System.out.println(this.xmlFile.toString());
		
		this.loadCharakter();
	}
	
	private void loadCharakter() {
		try {
		XmlReader xml = new XmlReader();
		XmlReader.Element xml_element = xml.parse(this.xmlFile);
		
		System.out.println(xml_element.getChildByName("name").getText());
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
