package com.miguel.gdx.simplegame.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.miguel.gdx.simplegame.SimpleGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//Esto en principio es para ahorrar batería
		config.useAccelerometer = false;
		config.useCompass = false;
		initialize(new SimpleGame(), config);
	}
}
