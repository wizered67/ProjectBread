package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class WorldManager {
	public static World world;
	public static void init(){
		World.setVelocityThreshold(0.1f);
		world = new World(new Vector2(0, -22), true); 
		world.setContactListener(new MyCollisionListener());
	}
}
