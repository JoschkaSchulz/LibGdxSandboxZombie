package com.zombies.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SkinHelper {
	public static BitmapFont KITEONE;
	public static TextureRegion SKIN_TEXTUREREGION[][];
	public static Skin SKIN;
	
	public static void loadFontHelper() {
		KITEONE = new BitmapFont(Gdx.files.internal("data/fonts/KiteOne.fnt"),Gdx.files.internal("data/fonts/KiteOne_0.png"), false);
	
		Texture skinTex = new Texture(Gdx.files.internal("data/gfx/skin/skin.png"));
		SKIN_TEXTUREREGION = TextureRegion.split(skinTex, 128, 64);
		
		SKIN = new Skin();
		
		//Add Fonts to Skin
		SKIN.add("font", KITEONE);
		
		//Add default TextButtonStyle
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = new TextureRegionDrawable(SKIN_TEXTUREREGION[0][0]);
		textButtonStyle.down = new TextureRegionDrawable(SKIN_TEXTUREREGION[0][1]);
		textButtonStyle.over = new TextureRegionDrawable(SKIN_TEXTUREREGION[0][2]);
		textButtonStyle.checked = new TextureRegionDrawable(SKIN_TEXTUREREGION[0][2]);
		textButtonStyle.font = SKIN.get("font", BitmapFont.class);
		SKIN.add("default", textButtonStyle);
		
		//Add default Label Style
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = SKIN.get("font", BitmapFont.class);
		SKIN.add("default", labelStyle);
	}
}
