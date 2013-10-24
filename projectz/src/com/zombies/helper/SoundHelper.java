package com.zombies.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundHelper {
	static Sound Shotgun ; 
	
public static void initialize_all_sounds()
{
	Shotgun = Gdx.audio.newSound(Gdx.files.internal("data/sfx/Shotgun_Blast.mp3"));
}

public static void play_Shotgun()
{
	Shotgun.play(1.0f);
}
	
	
}
