package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

public interface Entity {
	public float getX();
	public float getY();
	public float getWidth();
	public float getHeight();
	public float getBoundingWidth();
	public float getBoundingHeight();
	public int getDepth();
	public void beginContact(ContactData c);
	public void endContact(ContactData c);
	public void preSolveCollision(ContactData c, Manifold m);
	public void postSolveCollision(ContactData c, ContactImpulse impulse);
	public void update(float delta);
	public TextureRegion getSprite();
	public Vector2 getDrawOffset();
	public boolean equals(Entity e);
	public Body getBody();
	public void setPlatformVelocity(Vector2 v);
	public void setPlatform(MovingPlatform b);
}
