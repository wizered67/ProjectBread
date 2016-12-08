package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GenericEntity implements Entity{
	protected Body body;
	protected TextureRegion sprite;
	protected float boundingWidth;
	protected float boundingHeight;
	protected int depth = 10;
	protected Vector2 platformVelocity = new Vector2(0, 0);
	protected MovingPlatform platform;
	protected boolean destroyed = false;
	public GenericEntity(Body b, TextureRegion s, float bw, float bh){
		body = b;
		sprite = s;
		boundingWidth = bw;
		boundingHeight = bh;
	}
	/*
	public void makeBody(){
		BodyDef movingPlatformBodyDef = new BodyDef();
		movingPlatformBodyDef.position.set(Constants.toMeters(320), Constants.toMeters(320));
		movingPlatformBodyDef.fixedRotation = true;
		movingPlatformBodyDef.type = BodyType.KinematicBody;
		Body testMovingPlatform = WorldManager.world.createBody(movingPlatformBodyDef);
		FixtureDef testMovingPlatformFDef = new FixtureDef();
		testMovingPlatformFDef.friction = 1f;
		groundBox.setAsBox(Constants.toMeters(16), Constants.toMeters(4f), new Vector2(0, Constants.toMeters(0)), 0);
		testMovingPlatformFDef.filter.maskBits = Constants.MASK_SCENERY;
		testMovingPlatformFDef.filter.categoryBits = Constants.CATEGORY_SCENERY;
		testMovingPlatformFDef.shape = groundBox;
		Fixture testMovingPlatformFixture = testMovingPlatform.createFixture(testMovingPlatformFDef);
		FixtureData mfd = new FixtureData(Fixtures.PLATFORM_BODY);
		mfd.setAttribute(Attributes.ONE_WAY, "true");
		testMovingPlatformFixture.setUserData(mfd);
	}
	*/
	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return body.getPosition().x;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return body.getPosition().y;
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return sprite.getRegionWidth();
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return sprite.getRegionHeight();
	}

	@Override
	public float getBoundingWidth() {
		// TODO Auto-generated method stub
		return boundingWidth;
	}

	@Override
	public float getBoundingHeight() {
		// TODO Auto-generated method stub
		return boundingHeight;
	}

	@Override
	public void beginContact(ContactData c) {
		/*
		// TODO Auto-generated method stub
		if (c.getThis().getUserData() == Fixtures.CRATE_FOOT){
			//if (c.getOther().getUserData() == Fixtures.GROUND){
				numGroundContacts++;
			//}
		}
		if (c.getThis().getUserData() == Fixtures.CRATE_HEAD){
			if (c.getOther().getUserData() == Fixtures.BODY){
				numPlayerContacts++;
			}
		}
		*/
	}

	@Override
	public void endContact(ContactData c) {
		/*
		// TODO Auto-generated method stub
		if (c.getThis().getUserData() == Fixtures.CRATE_FOOT){
			//if (c.getOther().getUserData() == Fixtures.GROUND){
				numGroundContacts--;
			//}
		}
		
		if (c.getThis().getUserData() == Fixtures.CRATE_HEAD){
			if (c.getOther().getUserData() == Fixtures.BODY){
				numPlayerContacts--;
			}
		}
		*/
	}

	@Override
	public void update(float delta) {
		/*
		//System.out.println(body.getLinearVelocity());
		if (numPlayerContacts > 0 && onGround() && Math.abs(body.getLinearVelocity().x) < 1e-6 && Math.abs(body.getLinearVelocity().y) < 1e-6){
			body.setType(BodyType.StaticBody);
			System.out.println("Static");
		}
		else{
			body.setType(BodyType.DynamicBody);
			System.out.println("Dynamic");
		}
		*/
	}
	
	@Override
	public TextureRegion getSprite() {
		// TODO Auto-generated method stub
		return sprite;
	}

	@Override
	public void preSolveCollision(ContactData c, Manifold m) {
		Object thisData = c.getThis().getUserData();
		if (thisData instanceof FixtureData){
			if (((FixtureData)thisData).hasAttribute(Attributes.ONE_WAY)){
				Object otherBodyData = c.getOther().getBody().getUserData();
				if (otherBodyData instanceof Entity){
					if (c.getOther().getBody().getPosition().y - ((Entity) otherBodyData).getBoundingHeight() / 2 < (body.getPosition().y + boundingHeight / 2)){
						c.getContact().setEnabled(false);
					}
					else{
						//System.out.println("test");
					}
				}
			}
		}
	}

	@Override
	public void postSolveCollision(ContactData c, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector2 getDrawOffset() {
		// TODO Auto-generated method stub
		return new Vector2(0, -0.5f);
	}

	@Override
	public int getDepth() {
		return depth;
	}
	
	@Override
	public boolean equals(Entity e) {
		// TODO Auto-generated method stub
		return e == this;
	}
	
	@Override
	public void setPlatformVelocity(Vector2 v) {
		platformVelocity = v;
	}
	
	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return body;
	}

	@Override
	public void setPlatform(MovingPlatform e) {
		// TODO Auto-generated method stub
		platform = e;
	}
	
	public Rectangle getRect(){
		float xp = body.getPosition().x;
		float yp = body.getPosition().y;
		return new Rectangle(xp - boundingWidth / 2, yp - boundingHeight / 2, boundingWidth, boundingHeight);
	}
	@Override
	public void setDestroyed(boolean d) {
		// TODO Auto-generated method stub
		destroyed = d;
	}
	@Override
	public boolean getDestroyed() {
		// TODO Auto-generated method stub
		return destroyed;
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
