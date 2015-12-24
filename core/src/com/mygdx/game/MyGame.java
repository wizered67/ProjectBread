package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyGame extends Game {
	GameScreen gameScreen;
	@Override
	public void create() {
		gameScreen = new GameScreen(this);
        setScreen(gameScreen);       
	}
	
}
