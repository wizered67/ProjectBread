package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGame;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Project Bread";
		config.width = 1024; //16 : 9
		config.height = 576; //576
		config.resizable = true;
	    //config.vSyncEnabled = true;
		new LwjglApplication(new MyGame(), config);
	}
}
