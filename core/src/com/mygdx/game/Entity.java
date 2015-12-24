package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface Entity {
	public ArrayList<BoundingShape> getBoundingShapes();
	public float getX();
	public float getY();
	public void collide(Collision c);
	public void update(float delta);
	public void postCollisionUpdate();
	public TextureRegion getSprite();
	public BoundingShape getPrimaryHitbox();
}
