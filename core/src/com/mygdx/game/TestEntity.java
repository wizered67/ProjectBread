package com.mygdx.game;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


public class TestEntity implements Entity{
	private TextureRegion sprite;
	private ArrayList<BoundingShape> boundingShapes;
	private int numberGroundContacts = 0;
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 acceleration;
	private ArrayList<Collision> previousCollisions;
	private ArrayList<Collision> currentCollisions;
	public TestEntity(){
		boundingShapes = new ArrayList<BoundingShape>();
		previousCollisions = new ArrayList<Collision>();
		currentCollisions = new ArrayList<Collision>();
		Texture tex = new Texture("batman.png");
		sprite = new TextureRegion(tex);
		sprite.flip(false, true);
        
		position = new Vector2(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
		velocity = new Vector2(MathUtils.random(1, 8), MathUtils.random(1, 8));
		acceleration = new Vector2(0, 0);
     	
     	
     	BoundingShape mainBody = new BoundingShape(this, new Rectangle2D.Float(0, 0, getWidth(), getHeight()));
     	mainBody.setUserData("Test Object");
     	//BoundingShape foot = new BoundingShape(this, new Rectangle2D.Float(2, getHeight() - 4, getWidth() - 2, 4));
     	//foot.setSensor(true);
     	//foot.setUserData("player_foot");
     	boundingShapes.add(mainBody);
     	//boundingShapes.add(foot);
     	
	}
	
	public void update(float delta){
		updatePhysics();
	}
	
	public void updatePhysics(){
		/*
		boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
		boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT);
		boolean up = Gdx.input.isKeyPressed(Input.Keys.UP);
		boolean down = Gdx.input.isKeyPressed(Input.Keys.DOWN);
		position.add(new Vector2(
				(right) ? 5 : 0 + ((left) ? -5 : 0), (up) ? -5 : 0 + ((down) ? 5 : 0)
		));
		*/
		velocity.add(acceleration);
		position.add(velocity);
		if (position.x < 0 || position.x > Gdx.graphics.getWidth() || position.y < 0 || position.y > Gdx.graphics.getHeight())
			velocity = new Vector2(-velocity.x, -velocity.y);
	}
	
	public void collide(Collision c){
		currentCollisions.add(c);
		Entity otherEntity = c.getOtherObject();
		BoundingShape thisBounding = c.getRecipientBoundingShape();
		BoundingShape otherBounding = c.getOtherBoundingShape();
		//System.out.println("Collision between: " + thisBounding.getUserData() + " + " + otherBounding.getUserData());
		//if (thisBounding.getUserData().equals("player_foot") && otherBounding.getUserData().equals("Ground")){
			//System.out.println("Collision between: " + thisBounding.getUserData() + " + " + otherBounding.getUserData());
			if (!previousCollisionsContains(c)){
				//System.out.println("New collision between " + thisBounding.getUserData() + " + " + otherBounding.getUserData());
			}
		//}
	}
	
	private boolean previousCollisionsContains(Collision collision){
		for (Collision c : previousCollisions){
			if (c.equals(collision)){
				return true;
			}
		}
		return false;
	}
	
	public void postCollisionUpdate(){
		previousCollisions.clear();
		previousCollisions = (ArrayList<Collision>) currentCollisions.clone();
		currentCollisions.clear();
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
		return (numberGroundContacts > 0);
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

	@Override
	public BoundingShape getPrimaryHitbox() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
