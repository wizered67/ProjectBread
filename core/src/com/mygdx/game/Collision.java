package com.mygdx.game;

public class Collision {
	private Entity recipientObject;
	private Entity otherObject;
	private BoundingShape recipientBoundingShape;
	private BoundingShape otherBoundingShape;
	private int tileId;
	public Collision(Entity ro, Entity oo, BoundingShape rbs, BoundingShape obs){
		setRecipientObject(ro);
		setOtherObject(oo);
		setRecipientBoundingShape(rbs);
		setOtherBoundingShape(obs);
		tileId = -1;
	}
	public Collision(int tile){
		tileId = tile;
	}
	
	public int getTileId(){
		return tileId;
	}
	
	public Entity getRecipientObject() {
		return recipientObject;
	}
	public void setRecipientObject(Entity recipientObject) {
		this.recipientObject = recipientObject;
	}
	public Entity getOtherObject() {
		return otherObject;
	}
	public void setOtherObject(Entity otherObject) {
		this.otherObject = otherObject;
	}
	public BoundingShape getRecipientBoundingShape() {
		return recipientBoundingShape;
	}
	public void setRecipientBoundingShape(BoundingShape recipientBoundingShape) {
		this.recipientBoundingShape = recipientBoundingShape;
	}
	public BoundingShape getOtherBoundingShape() {
		return otherBoundingShape;
	}
	public void setOtherBoundingShape(BoundingShape otherBoundingShape) {
		this.otherBoundingShape = otherBoundingShape;
	}
	public boolean equals(Collision other){
		return (other.getOtherBoundingShape() == otherBoundingShape && other.getRecipientBoundingShape() == recipientBoundingShape);
	}
}
