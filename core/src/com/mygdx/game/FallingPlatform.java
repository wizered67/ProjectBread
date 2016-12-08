package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class FallingPlatform extends MovingPlatform {
	boolean falling = false;
	EntityRespawner respawner;
	public FallingPlatform(Body b, TextureRegion s, float bw, float bh){
		super(b, s, bw, bh, new Vector2(0, 0), new Vector2(0, 0));
		body.setLinearVelocity(0, 0);
		body.setGravityScale(0);
	}
	@Override
	public void update(float delta){
		//super.update(delta);
		updateEntities();
		updatePosition(delta);
	}
	
	public void makeRespawner(float time){
		if (GameManager.game.getScreen() instanceof GameScreen){
			respawner = new EntityRespawner(this);
			respawner.setRespawnTime(time);
			((GameScreen)GameManager.game.getScreen()).addEntity(respawner);
		}
	}
	
	public void setRespawnerProperties(float time){
		if (respawner != null){
			respawner.setRespawnTime(time);
		}
	}
	
	public Entity reconstruct(){
		return new FallingPlatform(null, getSprite(), getBoundingWidth(), getBoundingHeight());
	}
	
	@Override
	public void updateEntities(){
		
		super.updateEntities();
		for (Entity e : onEntities){
			//System.out.println(body.getLinearVelocity());
			if (entityOnPlatform(e) && e.platformValid()){		
				falling = true;
			}
		}
	}
	
	@Override
	public void updatePosition(float delta){
		if (!falling){
			//body.setType(BodyType.DynamicBody);
			//body.setGravityScale(0);
			body.setLinearVelocity(0, 0);
		}
		else{
			//body.setGravityScale(1);
			body.setLinearVelocity(body.getLinearVelocity().cpy().add(WorldManager.world.getGravity().cpy().scl(Constants.TIME_STEP)));
		}
		Screen s = GameManager.game.getScreen();
		if (s instanceof GameScreen){
			if (!((GameScreen) s).inWorld(getRect())){
				((GameScreen) s).removeEntity(this);
				setDestroyed(true);
			}
		}
	}
	
	@Override
	public void beginContact(ContactData c) {
		super.beginContact(c);
		/*
		if (((FixtureData)c.getThis().getUserData()).getType() == Fixtures.PLATFORM_SENSOR){
			Object otherEntity = c.getOther().getBody().getUserData();
			if (otherEntity instanceof PlayerEntity && entityOnPlatform((Entity) otherEntity) ){//&& ((Entity)otherEntity).platformValid()){
				falling = true;
			}
		}
		*/
	}
	
	@Override
	public void preSolveCollision(ContactData c, Manifold m) {
		super.preSolveCollision(c, m);
		Fixture otherFixture = c.getOther();
		if (falling && otherFixture.getBody().getUserData() == null){
			c.getContact().setEnabled(false);
			return;
		}
		/*
		if (otherFixture.getBody().getUserData() instanceof Entity){
			Entity e = (Entity) otherFixture.getBody().getUserData();
			if (!onEntities.contains(e)){
				c.getContact().setEnabled(false);
				return;
			}
		}
		*/
	}
	
	
}
