package com.mygdx.game;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CopyOfPlayerEntity implements Entity{
	private String id;
	private TextureRegion sprite;
	private ArrayList<BoundingShape> boundingShapes;
	private HashMap<String, ArrayList<BoundingShape>> allBoundingShapes;
	private int numberGroundContacts = 0;
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 acceleration;
	private ArrayList<Collision> previousCollisions;
	private ArrayList<Collision> currentCollisions;
	private GameScreen world;
	private HashMap<String, BoundingShape> primaryHitbox;
	private HashMap<String, BoundingShape> footHitbox;
	private HashMap<String, Animation> animations = new HashMap<String, Animation>();
	private String animationString;
	private Animation currentAnimation;
	private float animTimer = 0;
	private int direction = 1;
	public CopyOfPlayerEntity(String id, GameScreen world){
		this.id = id;
		this.world = world;
		primaryHitbox = new HashMap<String, BoundingShape>();
		footHitbox = new HashMap<String, BoundingShape>();
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("Wheatley.pack"));
		Array<AtlasRegion> walk = atlas.findRegions("Walking-Frame");
		Array<AtlasRegion> airUp = atlas.findRegions("Up-Jump");
		Array<AtlasRegion> airDown = atlas.findRegions("Down-Jump");
		Array<AtlasRegion> idle = atlas.findRegions("Frame");
		Animation walkAnim = new Animation(0.1f, walk);
		Animation airUpAnim = new Animation(0.1f, airUp);
		Animation airDownAnim = new Animation(0.1f, airDown);
		Animation idleAnim = new Animation(0.1f, idle);
		animations.put("walk", walkAnim);
		animations.put("airUp", airUpAnim);
		animations.put("airDown", airDownAnim);
		animations.put("idle", idleAnim);
		//Texture tex = new Texture("batman.png");
		//sprite = new TextureRegion(tex);
		//sprite.flip(false, true);
		
		setAnimation("idle");
		sprite = currentAnimation.getKeyFrame(animTimer, true);
		System.out.println(sprite.getRegionHeight());
		setAnimation("walk");
		sprite = currentAnimation.getKeyFrame(animTimer, true);
		System.out.println(sprite.getRegionHeight());
		boundingShapes = new ArrayList<BoundingShape>();
		BoundingShape pbs = new BoundingShape(this, new Rectangle2D.Float(3, 2, getWidth() - 6, getHeight() - 4));
		primaryHitbox.put(key, pbs);
     	primaryHitbox.setUserData("player_body");
     	BoundingShape fbs = new BoundingShape(this, new Rectangle2D.Float(3, getHeight() - 4, getWidth() - 6, 5) )
     	fbs.setSensor(true);
     	fbs.setUserData("player_foot");
     	footHitbox.put(key, fbs);
     	BoundingShape head = new BoundingShape(this, new Rectangle2D.Float(3, 1, getWidth() - 6, 1) );
     	head.setSensor(true);
     	head.setUserData("player_head");
     	boundingShapes.add(primaryHitbox);
     	boundingShapes.add(footHitbox);
     	boundingShapes.add(head);
		previousCollisions = new ArrayList<Collision>();
		currentCollisions = new ArrayList<Collision>();
		/*
		position = new Vector2(0, 0);
		Vector2 randomPos = new Vector2(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
        while (testAllCollisions(randomPos, new Vector2(0, 0))){
        	randomPos = new Vector2(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
        }
		position = randomPos;
		*/
		position = new Vector2(Gdx.graphics.getWidth() / 2 - getWidth()/2,64);
		velocity = new Vector2(0, 0);
		acceleration = new Vector2(0, 0);
     	
	}
	
	public void update(float delta){
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
			printNode();
		}
		acceleration.set(0, 0);
		updatePhysics(delta);
		updateAnimation(delta);
	}
	
	public void updateAnimation(float delta){
		
		if (onGround() && Math.abs(velocity.x) > 0){
			setAnimation("walk");
		}
		else if (onGround()){
			setAnimation("idle");
		}
		else{
			if (velocity.y >= 0){
				setAnimation("airDown");
			}
			else{
				setAnimation("airUp");
			}
		}
		
		animTimer += delta;
		if (animationString.equals("walk") && velocity.x == 0){
			animTimer = 0;
		}
		
		sprite = currentAnimation.getKeyFrame(animTimer, true);
		if (!sprite.isFlipY()){
			sprite.flip(false, true);
		}
		if (!sprite.isFlipX() && direction == -1){
			sprite.flip(true, false);
		}
		if (sprite.isFlipX() && direction == 1){
			sprite.flip(true, false);
		}
	}
	
	public void setAnimation(String anim){
		if (!anim.equals(animationString)){
			animTimer = 0;
		}
		animationString = anim;
		currentAnimation = animations.get(anim);
	}
	
	public String getAnimation(){
		return animationString;
	}
	
	public void printNode(){
		world.updateQuad();
		world.getQuad().printAll(0);
	}
	
	public void updatePhysics(float delta){
		acceleration.add(new Vector2(0, 2));
		boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
		boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT);
		boolean up = Gdx.input.isKeyPressed(Input.Keys.UP);
		boolean down = Gdx.input.isKeyPressed(Input.Keys.DOWN);
		/*
		velocity.set(new Vector2(
				(right) ? 8 : 0 + ((left) ? -8 : 0), 
				(up) ? -8 : 0 + ((down) ? 8 : 0 )
		));
		*/
		if (onGround()){
		velocity.set( new Vector2(
				(right) ? 3 : 0 + ((left) ? -3 : 0), 
				velocity.y)
		);
		}
		else{
			velocity.set( new Vector2(
					(right) ? 5 : 0 + ((left) ? -5 : 0), 
					velocity.y)
			);
		}
		
		if (right){
			direction = 1;
		}
		else if (left)
			direction = -1;
		
		velocity.add(acceleration);
		if (up && onGround()){
			velocity.y = -20;
		}
		if (Math.abs(velocity.x) > 20){
			velocity.x = Math.signum(velocity.x) * 20;
		}
		if (velocity.y > 10){
			velocity.y = 10;
		}
		//getCollideDynamicObjects( new Vector2(position.x + velocity.x, position.y) ) == null
		if ( Math.abs(velocity.x) > 0 && !testAllCollisions(position.cpy(), new Vector2(velocity.x, 0), primaryHitbox ) ){
			position.add( new Vector2(velocity.x, 0) );
		}
		else{
			float maxMove = Math.abs(velocity.x);
			while (maxMove > 0){
				float inc = 1;
				if (maxMove < 1){
					inc = maxMove;
				}
				//getCollideDynamicObjects( new Vector2(position.x + Math.signum(velocity.x) * inc, position.y) ) == null
				if (!testAllCollisions(position.cpy(), new Vector2(Math.signum(velocity.x) * inc, 0), primaryHitbox ) ){
					position.add( new Vector2(Math.signum(velocity.x) * inc, 0) );
					maxMove--;
				}
				else{
					maxMove = 0;
				}
			}
		}
		//getCollideDynamicObjects( new Vector2(position.x, position.y + velocity.y) ) == null
		if (Math.abs(velocity.y) > 0 && !testAllCollisions(position.cpy(), new Vector2(0, velocity.y), primaryHitbox )){
			position.add( new Vector2(0,velocity.y) );
		}
		else{
			float maxMove = Math.abs(velocity.y);
			while (maxMove > 0){
				float inc = 1;
				if (maxMove < 1){
					inc = maxMove;
				}
				//getCollideDynamicObjects( new Vector2(position.x, position.y  + Math.signum(velocity.y) * inc) ) == null
				if (!testAllCollisions(position.cpy(), new Vector2(0, Math.signum(velocity.y) * inc), primaryHitbox ) ){
					position.add( new Vector2(0, Math.signum(velocity.y) * inc) );
					maxMove--;
				}
				else{
					maxMove = 0;
				}
			}
		}
		//position.add(velocity);
	}
	
	public boolean testAllCollisions(Vector2 currentPosition, Vector2 testVelocity, BoundingShape hitbox){
		Vector2 movePosition = currentPosition.add(testVelocity);
		return (getCollideStaticTiles(currentPosition, testVelocity, hitbox).size() != 0 || getCollideDynamicObjects(movePosition, hitbox) != null);
	}
	
	public ArrayList<Integer> getCollideStaticTiles(Vector2 testPosition, Vector2 dir, BoundingShape hitbox){
		ArrayList<Integer> collideList = new ArrayList<Integer>();
		hitbox.setPosition(testPosition);
		Rectangle2D.Float hitRect = (Rectangle2D.Float) hitbox.getPositionalShape();
		int startTile;
		int endTile;
		int ix = -1, iy = -1;
		if (dir.x != 0){
			startTile = (int) Math.floor(hitRect.getY() / 32);
			endTile = (int) Math.floor((hitRect.getY() + hitRect.getHeight()) / 32);
			if (dir.x < 0){
				ix = (int) Math.floor(hitRect.getX() / 32);
			}
			else{
				ix = (int) Math.floor((hitRect.getX() + hitRect.getWidth()) / 32);
			}
		}
		else{
			startTile = (int) Math.floor(hitRect.getX() / 32);
			endTile = (int) Math.floor((hitRect.getX() + hitRect.getWidth()) / 32);
			if (dir.y < 0){
				iy = (int) Math.floor(hitRect.getY() / 32);
			}
			else{
				iy = (int) Math.floor((hitRect.getY() + hitRect.getHeight()) / 32);
			}
		}
		
		for (int i = startTile; i <= endTile; i++){
			if (dir.x != 0){
				iy = i;
			}
			else{
				ix = i;
			}
			
			int tile = world.getStaticTile(ix, iy);
			if (tile != 0){
				collideList.add(tile);
			}
		}
		return collideList;
	}
	
	public Collision getCollideDynamicObjects(Vector2 testPosition, BoundingShape hitbox){
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){ //debug
			int test = 01;
		}
		Vector2 originalPosition = position.cpy();
		position = testPosition.cpy();
		world.updateQuad();
		Quadtree quad = world.getQuad();
		ArrayList<BoundingShape> returnObjects = new ArrayList<BoundingShape>();
        returnObjects.clear();
        quad.retrieve(returnObjects, hitbox);
        for (int x = 0; x < returnObjects.size(); x++) {
            // Run collision detection algorithm between objects
        	BoundingShape other = returnObjects.get(x);
        	//System.out.println(other.getUserData());
        	if (!other.isSensor() && hitbox.intersects(other)){
        		if (hitbox.getOwner() != other.getOwner()){
        			position = originalPosition;
        			return (new Collision(hitbox.getOwner(), other.getOwner(), hitbox, other));
        		}
        	}
          }
        
    	position = originalPosition;
		return null;
	}
	
	public void collide(Collision c){
		currentCollisions.add(c);
		//System.out.println("Collision between: " + thisBounding.getUserData() + " + " + otherBounding.getUserData());
		if (!collisionContained(previousCollisions, c)){
			newCollision(c);
		}
		else{
			continueCollision(c);
		}
		
	}
	
	public void newCollision(Collision c){
		Entity otherEntity = c.getOtherObject();
		BoundingShape thisBounding = c.getRecipientBoundingShape();
		BoundingShape otherBounding = c.getOtherBoundingShape();
		if (thisBounding.getUserData().equals("player_foot") && otherBounding.getUserData().equals("Ground")){
			//System.out.println("New collision between foot and ground");
			numberGroundContacts++;
			//System.out.println("Collision between: " + thisBounding.getUserData() + " + " + otherBounding.getUserData());
		}
		if (thisBounding.getUserData().equals("player_head") && otherBounding.getUserData().equals("Ground")){
			//System.out.println("New collision between foot and ground");
			if (velocity.y < 0)
				velocity.y = 0;
			//System.out.println("Collision between: " + thisBounding.getUserData() + " + " + otherBounding.getUserData());
		}
	}
	
	public void continueCollision(Collision c){
		Entity otherEntity = c.getOtherObject();
		BoundingShape thisBounding = c.getRecipientBoundingShape();
		BoundingShape otherBounding = c.getOtherBoundingShape();
	}
	
	public void endCollision(Collision c){
		Entity otherEntity = c.getOtherObject();
		BoundingShape thisBounding = c.getRecipientBoundingShape();
		BoundingShape otherBounding = c.getOtherBoundingShape();
		if (thisBounding.getUserData().equals("player_foot") && otherBounding.getUserData().equals("Ground")){
			//System.out.println("End of collision between foot and ground");
			numberGroundContacts--;
			//System.out.println("Collision between: " + thisBounding.getUserData() + " + " + otherBounding.getUserData());
		}
	}
	
	private boolean collisionContained(ArrayList<Collision> list, Collision collision){
		for (Collision c : list){
			if (c.equals(collision)){
				return true;
			}
		}
		return false;
	}
	
	public void postCollisionUpdate(){
		for (Collision c : previousCollisions){
			if (!collisionContained(currentCollisions, c)){
				endCollision(c);
			}
		}
		previousCollisions.clear();
		previousCollisions = (ArrayList<Collision>) currentCollisions.clone();
		currentCollisions.clear();
	}
	
	public BoundingShape getPrimaryHitbox(){
		return primaryHitbox;
	}
	
	public void setPosition(Vector2 position){
		this.position = position;
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public Vector2 getVelocity(){
		return velocity;
	}
	
	public Vector2 getAcceleration(){
		return acceleration;
	}
	
	public float getX(){
		return position.x;
	}
	
	public float getY(){
		return position.y;
	}
	
	public float getWidth(){
		return sprite.getRegionWidth();
	}
	
	public float getHeight(){
		return sprite.getRegionHeight();
	}
	
	public boolean onGround(){
		return testAllCollisions(position, new Vector2(0, 0), footHitbox);
		//return (numberGroundContacts > 0);
	}
	
	public void addGroundContacts(int amount){
		numberGroundContacts += amount;
		if (numberGroundContacts < 0)
			numberGroundContacts = 0;
	}
	
	public ArrayList<BoundingShape> getBoundingShapes(){
		for (BoundingShape bs : boundingShapes){
			bs.setPosition(position);
		}
		return boundingShapes;
	}
	
	public TextureRegion getSprite(){
		return sprite;
	}
	
	public String getId(){
		return id;
	}
}
