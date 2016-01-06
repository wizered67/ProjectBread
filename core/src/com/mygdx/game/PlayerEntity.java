package com.mygdx.game;


import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class PlayerEntity implements Entity{
	private String id;
	private TextureRegion sprite;
	private int numberGroundContacts = 0;
	private GameScreen screen;
	private HashMap<Animations, Animation> animations = new HashMap<Animations, Animation>();
	private Animations animationType;
	private Animation currentAnimation;
	private float animTimer = 0;
	private int direction = 1;
	private Body body;
	private float walkSpeed = 0.5f;
	private Fixture mainBody;
	private float boundingWidth, boundingHeight;
	private int jumpTimer = 0;
	private int jumpDelay = 5;
	private float jumpImpulse = 12;
	public float defaultFriction = 0.0f;
	
	public PlayerEntity(String id, GameScreen screen){
		this.id = id;
		this.screen = screen;
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("Wheatley.pack"));
		Array<AtlasRegion> walk = atlas.findRegions("Walking-Frame");
		Array<AtlasRegion> airUp = atlas.findRegions("Up-Jump");
		Array<AtlasRegion> airDown = atlas.findRegions("Down-Jump");
		Array<AtlasRegion> idle = atlas.findRegions("Frame");
		Animation walkAnim = new Animation(0.1f, walk);
		Animation airUpAnim = new Animation(0.1f, airUp);
		Animation airDownAnim = new Animation(0.1f, airDown);
		Animation idleAnim = new Animation(0.1f, idle);
		animations.put(Animations.WALK, walkAnim);
		animations.put(Animations.JUMP, airUpAnim);
		animations.put(Animations.FALL, airDownAnim);
		animations.put(Animations.IDLE, idleAnim);
		setAnimation(Animations.IDLE);
		sprite = currentAnimation.getKeyFrame(animTimer, true);
		//Texture tex = new Texture("batman.png");
		//sprite = new TextureRegion(tex);
		//sprite.flip(false, true);
		/*
		setAnimation("idle");
		sprite = currentAnimation.getKeyFrame(animTimer, true);
		System.out.println(sprite.getRegionHeight());
		setAnimation("walk");
		sprite = currentAnimation.getKeyFrame(animTimer, true);
		System.out.println(sprite.getRegionHeight());
		*/
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(Constants.toMeters(350), Constants.toMeters(500));
		bodyDef.fixedRotation = true;
		body = WorldManager.world.createBody(bodyDef);
		body.setUserData(this);
		PolygonShape rect = new PolygonShape();
		float rectWidthHalf = Constants.toMeters((getWidth() - 9) / 2);
		float rectHeightHalf = Constants.toMeters((getHeight() - 4) / 2);
		rect.setAsBox(rectWidthHalf, rectHeightHalf, new Vector2(0, 0), 0);
		FixtureDef mb = new FixtureDef();
		mb.shape = rect;
		mb.density = 0.5f;
		mb.friction = defaultFriction;
		mb.restitution = 0;
		mb.filter.categoryBits = Constants.CATEGORY_PLAYER;
		mb.filter.maskBits = Constants.MASK_PLAYER;
		boundingWidth = rectWidthHalf * 2;
		boundingHeight = rectHeightHalf * 2;
	    mainBody = body.createFixture(mb);
		mainBody.setUserData(Fixtures.BODY);
		/*
		//- Constants.toMeters(0.5f)
		//rect.setAsBox(Constants.toMeters(0.5f), rectHeightHalf, new Vector2(-rectWidthHalf - Constants.toMeters(0.5f), 0), 0);
		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(-rectWidthHalf - Constants.toMeters(2f), -rectHeightHalf + Constants.toMeters(0.25f));
		vertices[1] = new Vector2(-rectWidthHalf + Constants.toMeters(0f), -rectHeightHalf);
		vertices[2] = new Vector2(-rectWidthHalf + Constants.toMeters(0f), rectHeightHalf);
		vertices[3] = new Vector2(-rectWidthHalf - Constants.toMeters(2f), rectHeightHalf);
		rect.set(vertices);
		FixtureDef ls = new FixtureDef();
		ls.shape = rect;
		ls.density = mb.density;
		ls.restitution = mb.restitution;
		ls.friction = 0.0f;
		ls.filter.categoryBits = Constants.CATEGORY_PLAYER;
		ls.filter.maskBits = Constants.MASK_PLAYER;
		Fixture leftSide = body.createFixture(ls);
		leftSide.setUserData(Fixtures.BODY);
		//- Constants.toMeters(0.5f)
		rect.setAsBox(Constants.toMeters(0.5f), rectHeightHalf, new Vector2(rectWidthHalf + Constants.toMeters(0.5f), 0), 0);
		FixtureDef rs = new FixtureDef();
		rs.shape = rect;
		rs.density = mb.density;
		rs.restitution = mb.restitution;
		rs.friction = 0.0f;
		rs.filter.categoryBits = Constants.CATEGORY_PLAYER;
		rs.filter.maskBits = Constants.MASK_PLAYER;
		Fixture rightSide = body.createFixture(rs);
		rightSide.setUserData(Fixtures.BODY);
		*/
		CircleShape circ = new CircleShape();
		/*
		//rect.setAsBox(Constants.toMeters(0.5f), Constants.toMeters(0.25f), new Vector2(-rectWidthHalf - Constants.toMeters(0.5f), -rectHeightHalf + Constants.toMeters(0.25f)), 0);
		circ.setPosition(new Vector2(-rectWidthHalf - Constants.toMeters(0.55f), -rectHeightHalf + Constants.toMeters(0.65f)));
		circ.setRadius(Constants.toMeters(0.5f));
		FixtureDef botLeft = new FixtureDef();
		botLeft.shape = circ;
		botLeft.density = mb.density;
		botLeft.restitution = mb.restitution;
		botLeft.friction = 1f;
		botLeft.filter.categoryBits = Constants.CATEGORY_PLAYER;
		botLeft.filter.maskBits = Constants.MASK_PLAYER;
		Fixture bottomLeft = body.createFixture(botLeft);
		bottomLeft.setUserData(Fixtures.BODY);
		
		//rect.setAsBox(Constants.toMeters(0.5f), Constants.toMeters(0.25f), new Vector2(rectWidthHalf + Constants.toMeters(0.5f), -rectHeightHalf + Constants.toMeters(0.25f)), 0);
		circ.setPosition(new Vector2(rectWidthHalf + Constants.toMeters(0.45f), -rectHeightHalf + Constants.toMeters(0.65f)));
		circ.setRadius(Constants.toMeters(0.5f));
		FixtureDef botRight = new FixtureDef();
		botRight.shape = circ;
		botRight.density = mb.density;
		botRight.restitution = mb.restitution;
		botRight.friction = 1f;
		botRight.filter.categoryBits = Constants.CATEGORY_PLAYER;
		botRight.filter.maskBits = Constants.MASK_PLAYER;
		Fixture bottomRight = body.createFixture(botRight);
		bottomRight.setUserData(Fixtures.BODY);
		*/
		rect.setAsBox(rectWidthHalf - Constants.toMeters(0.5f), Constants.toMeters(1), new Vector2(0, Constants.toMeters((-getHeight() + 2) / 2)), 0);
		FixtureDef foot = new FixtureDef();
		foot.shape = rect;
		foot.isSensor = true;
		foot.filter.categoryBits = Constants.CATEGORY_PLAYER;
		foot.filter.maskBits = Constants.MASK_PLAYER;
		Fixture footFixture = body.createFixture(foot);
		footFixture.setUserData(Fixtures.FOOT);
		
		circ.dispose();
		rect.dispose();
		/*
		HashMap<String, BoundingShape> head = new HashMap<String, BoundingShape>();;
		for (String key : animations.keySet()){
			
			sprite = currentAnimation.getKeyFrame(animTimer, true);
			System.out.println(sprite.getRegionHeight());
			BoundingShape pbs = new BoundingShape(this, new Rectangle(4, 2, getWidth() - 8, getHeight() - 4));
			pbs.setUserData("player_body");
			primaryHitbox.put(key, pbs);
	     	BoundingShape fbs = new BoundingShape(this, new Rectangle(4, getHeight() - 4, getWidth() - 8, 5) );
	     	fbs.setSensor(true);
	     	fbs.setUserData("player_foot");
	     	footHitbox.put(key, fbs);
	     	
	     	BoundingShape hbs = new BoundingShape(this, new Rectangle(4, 1, getWidth() - 8, 1) );
	     	hbs.setSensor(true);
	     	hbs.setUserData("player_head");
	     	head.put(key, hbs);
		}
		allBoundingShapes.add(primaryHitbox);
     	allBoundingShapes.add(footHitbox);
     	allBoundingShapes.add(head);
     	*/
		/*
		position = new Vector2(0, 0);
		Vector2 randomPos = new Vector2(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
        while (testAllCollisions(randomPos, new Vector2(0, 0))){
        	randomPos = new Vector2(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
        }
		position = randomPos;
		*/
		
	}
	
	public void update(float delta){
		updatePhysics(delta);
		updateAnimation(delta);
		updateTimers();
	}
	
	public void updateTimers(){
		jumpTimer = Math.max(0, jumpTimer - 1);
	}
	
	public void updateAnimation(float delta){
		
		if (onGround() && Math.abs(getVelocity().x) > 1e-5){
			setAnimation(Animations.WALK);
		}
		else if (onGround()){
			setAnimation(Animations.IDLE);
		}
		else{
			if ( getVelocity().y < 0){
				setAnimation(Animations.FALL);
			}
			else{
				setAnimation(Animations.JUMP);
			}
		}
		
		animTimer += delta;
		if (animationType == Animations.WALK && getVelocity().x == 0){
			animTimer = 0;
		}
		
		sprite = currentAnimation.getKeyFrame(animTimer, true);
		
		if (!sprite.isFlipX() && direction == -1){
			sprite.flip(true, false);
		}
		if (sprite.isFlipX() && direction == 1){
			sprite.flip(true, false);
		}
		
	}
	
	public void setAnimation(Animations anim){
		if (anim != animationType){
			animTimer = 0;
			
		}
		animationType = anim;
		currentAnimation = animations.get(anim);
	}
	
	public Animations getAnimation(){
		return animationType;
	}
	
	public void updatePhysics(float delta){
		boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
		boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT);
		boolean up = Gdx.input.isKeyPressed(Input.Keys.UP);
		boolean justUp = Gdx.input.isKeyJustPressed(Input.Keys.UP);
		boolean down = Gdx.input.isKeyPressed(Input.Keys.DOWN);
		
		if (right){
			direction = 1;
			if (getVelocity().x < 0){
				body.setLinearVelocity(0.0f, getVelocity().y);
			}
			body.applyLinearImpulse(walkSpeed * body.getMass(), 0, getX(), getY(), true);
		}
		if (left){
			direction = -1;
			if (getVelocity().x > 0){
				body.setLinearVelocity(0.0f, getVelocity().y);
			}
			body.applyLinearImpulse(-walkSpeed * body.getMass(), 0, getX(), getY(), true);
		}
		/*
		if (!right && !left && !onGround() && getVelocity().y > 0 && Math.abs(getVelocity().x) > 1.5){
			body.setLinearVelocity(getVelocity().x * 0.98f, getVelocity().y);
		}
		*/
		if ((!right && !left) || (right && left)){
			body.setLinearVelocity(0.0f, getVelocity().y);
		}
		if (Math.abs(getVelocity().x) > Constants.MAX_VELOCITY){
			body.setLinearVelocity(Constants.MAX_VELOCITY * Math.signum(getVelocity().x), getVelocity().y);
		}
		
		if (justUp && onGround() && jumpTimer <= 0){
			jumpTimer = jumpDelay;
			body.applyLinearImpulse(0, jumpImpulse * body.getMass(), body.getLocalCenter().x, body.getLocalCenter().x, true);
		}
		else if (!up){
			if (getVelocity().y > 5){
				body.setLinearVelocity(getVelocity().x, 5);
			}
		}
	}
		
	public void beginContact(ContactData c){
		if (c.getThis().getUserData() == Fixtures.FOOT){
			//if (c.getOther().getUserData() == Fixtures.GROUND){
				addGroundContacts(1);
			//}
		}
	}
	
	public void endContact(ContactData c){
		if (c.getThis().getUserData() == Fixtures.FOOT){
			//if (c.getOther().getUserData() == Fixtures.GROUND){
				addGroundContacts(-1);
			//}
		}
	}
	
	public void setTransform(Vector2 position, float angle){
		body.setTransform(position, angle);
	}
	
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
	public Vector2 getVelocity(){
		return body.getLinearVelocity();
	}
	
	
	public float getX(){
		return body.getPosition().x;
	}
	
	public float getY(){
		return body.getPosition().y;
	}
	
	public float getWidth(){
		return sprite.getRegionWidth();
	}
	
	public float getHeight(){
		return sprite.getRegionHeight();
	}
	
	public float getBoundingWidth(){
		return boundingWidth;
	}
	
	public float getBoundingHeight(){
		return boundingHeight;
	}
	
	public boolean onGround(){
		return numberGroundContacts > 0;
	}
	
	public void addGroundContacts(int amount){
		numberGroundContacts += amount;
		if (numberGroundContacts < 0)
			numberGroundContacts = 0;
	}
	
	/*
	public double getLowerY(){
		return (getY() + getPrimaryHitbox().getShape().getY() + getPrimaryHitbox().getShape().getHeight()); 
	}
	*/
	public TextureRegion getSprite(){
		return sprite;
	}
	
	public String getId(){
		return id;
	}
}
