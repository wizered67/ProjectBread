package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyGame extends Game {
	GameScreen gameScreen;
	@Override
	public void create() {
		GameManager.init(this);
		gameScreen = new GameScreen(this);
        setScreen(gameScreen);       
	}
	
}
