package com.zombies.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontHelper {
	public static BitmapFont KiteOne;
	public static void loadFontHelper() {
		KiteOne = new BitmapFont(Gdx.files.internal("data/fonts/KiteOne.fnt"),Gdx.files.internal("data/fonts/KiteOne_0.png"), false);
	}
}
