package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class MovingPlatform extends GenericEntity {
	protected Vector2 moveDistance;
	protected Vector2 moveVelocity;
	protected Vector2 startPosition;
	protected ArrayList<Entity> onEntities;
	Vector2 min = new Vector2(5000, 5000);
	Vector2 max = new Vector2(0, 0);
	public MovingPlatform(Body b, TextureRegion s, float bw, float bh, Vector2 md, Vector2 mv) {
		super(b, s, bw, bh);
		FixtureDef sensorFDef = new FixtureDef();
		sensorFDef.isSensor = true;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(bw / 2 - Constants.toMeters(0.1f), Constants.toMeters(0.1f), new Vector2(0, bh / 2), 0);
		sensorFDef.filter.maskBits = Constants.MASK_SCENERY;
		sensorFDef.filter.categoryBits = Constants.CATEGORY_SCENERY;
		sensorFDef.shape = shape;
		Fixture sensorFixture = b.createFixture(sensorFDef);
		FixtureData mfd = new FixtureData(Fixtures.PLATFORM_SENSOR);
		sensorFixture.setUserData(mfd);
		shape.dispose();
		startPosition = b.getPosition().cpy();
		moveVelocity = mv;
		moveDistance = md;
		body.setLinearVelocity(moveVelocity);
		onEntities = new ArrayList<Entity>();
	}
	
	public Vector2 getMoveVelocity(){
		return moveVelocity;
	}
	
	public void updatePosition(float delta){
		Vector2 moved = body.getPosition().cpy().sub(startPosition);
		//System.out.println(moved);
		if (Math.abs(moved.x) >= Math.abs(moveDistance.x)){
			float newV = -moveVelocity.x;
			body.setLinearVelocity(newV, body.getLinearVelocity().y);
		}
		
		if (moved.x * Math.signum(moveVelocity.x) <= 0){
			float newV = moveVelocity.x;
			body.setLinearVelocity(newV, body.getLinearVelocity().y);
		}
		
		if (Math.abs(moved.y) >= Math.abs(moveDistance.y)){
			float newV = -moveVelocity.y;
			body.setLinearVelocity(body.getLinearVelocity().x, newV);
		}
		
		if (moved.y * Math.signum(moveVelocity.y) <= 0){
			float newV = moveVelocity.y;
			body.setLinearVelocity(body.getLinearVelocity().x, newV);
		}
	}
	
	public void updateEntities(){
		ArrayList<Entity> destroyList = new ArrayList<Entity>();
		for (Entity e : onEntities){
			if (e.getDestroyed()){
				destroyList.add(e);
				continue;
			}
			if (entityOnPlatform(e)){		
				e.setPlatformVelocity(body.getLinearVelocity().cpy());
				e.setPlatform(this);
			}
			else{
				e.setPlatformVelocity(new Vector2(0, 0));
				e.setPlatform(null);
			}
		}
		onEntities.removeAll(destroyList);
	}
	
	
	public boolean entityOnPlatform(Entity e){
		return (e.getBody().getPosition().y - e.getBoundingHeight() / 2 > (body.getPosition().y + boundingHeight / 2));
	}
	
	@Override
	public void update(float delta){
		super.update(delta);
		updatePosition(delta);
		updateEntities();
		/*
		if (body.getPosition().x < min.x){
			min.x = body.getPosition().x;
			System.out.println("Min: " + min);
		}
		if (body.getPosition().y < min.y){
			min.y = body.getPosition().y;
			System.out.println("Min: " + min);
		}
		if (body.getPosition().x > max.x){
			max.x = body.getPosition().x;
			System.out.println("Max: " + max.cpy().sub(startPosition));
		}
		if (body.getPosition().y > max.y){
			max.y = body.getPosition().y;
			System.out.println("Max: " + max.cpy().sub(startPosition));
		}
		*/
	}
	
	
	
	@Override
	public void beginContact(ContactData c) {
		super.beginContact(c);
		if (((FixtureData)c.getThis().getUserData()).getType() == Fixtures.PLATFORM_SENSOR){
			Object otherBodyData = c.getOther().getBody().getUserData();
			if (otherBodyData instanceof Entity){
				Entity otherEntity = (Entity) otherBodyData;
				if (!onEntities.contains(otherEntity)){
					onEntities.add(otherEntity);
				}
				
			}
			
		}
		
	}
	
	@Override
	public void endContact(ContactData c) {
		super.endContact(c);
		if (((FixtureData)c.getThis().getUserData()).getType() == Fixtures.PLATFORM_SENSOR){
			Object otherBodyData = c.getOther().getBody().getUserData();
			if (otherBodyData instanceof Entity){
				Entity otherEntity = (Entity) otherBodyData;
				if (onEntities.contains(otherEntity)){
					onEntities.remove(otherEntity);
					otherEntity.setPlatformVelocity(new Vector2(0, 0));
					otherEntity.setPlatform(null);
				}
			}
		}
	}

}
