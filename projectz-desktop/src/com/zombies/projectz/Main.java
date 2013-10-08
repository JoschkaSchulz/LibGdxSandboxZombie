package com.zombies.projectz;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "projectz";
		cfg.useGL20 = false;

		cfg.resizable = false;
		cfg.width = 1280;
		cfg.height = 759; //786 klappt nicht mehr
		
		new LwjglApplication(new ProjectZ(), cfg);
	}
}
