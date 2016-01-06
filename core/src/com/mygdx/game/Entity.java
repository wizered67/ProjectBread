package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Contact;

public interface Entity {
	public float getX();
	public float getY();
	public float getWidth();
	public float getHeight();
	public float getBoundingWidth();
	public float getBoundingHeight();
	public void beginContact(ContactData c);
	public void endContact(ContactData c);
	public void update(float delta);
	public TextureRegion getSprite();
}
