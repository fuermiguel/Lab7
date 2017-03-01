package com.miguel.gdx.simplegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.miguel.gdx.simplegame.Juego;
import com.miguel.gdx.simplegame.SimpleGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title ="Drop";
		config.width = 800;
		config.height = 400;
		new LwjglApplication(new Juego(), config);
	}
}
