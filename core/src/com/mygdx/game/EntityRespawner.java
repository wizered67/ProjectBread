package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

public class EntityRespawner implements Entity {
	float respawnTime;
	Entity childEntity;
	float timerStart;
	Vector2 position;
	
	public EntityRespawner(Entity child){
		childEntity = child;
		position = child.getBody().getPosition();
		timerStart = -1;
	}
	
	public void setRespawnTime(float time){
		respawnTime = time;
	}
	
	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return position.x;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return position.y;
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getBoundingWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getBoundingHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void beginContact(ContactData c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endContact(ContactData c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolveCollision(ContactData c, Manifold m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolveCollision(ContactData c, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		if (timerStart == -1 && (childEntity == null || childEntity.getDestroyed())){
			childEntity = null;
		}
	}

	@Override
	public TextureRegion getSprite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2 getDrawOffset() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Entity e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPlatformVelocity(Vector2 v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlatform(MovingPlatform b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDestroyed(boolean d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getDestroyed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean platformValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
