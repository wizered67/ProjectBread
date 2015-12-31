package com.mygdx.game;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

public class BoundingShape {
	private Object userData;
	private Shape2D shape;
	private Shape2D positionalShape = null;
	private Vector2 currentPosition = new Vector2();
	private Entity owner;
	private boolean sensor = false;
	public BoundingShape(Entity creator, Shape2D s){
		owner = creator;
		shape = s;
		positionalShape = s;
	}
	
	public Object getUserData(){
		return userData;
	}
	
	public void setUserData(Object data){
		userData = data;
	}
	
	public Rectangle getShape(){
		return (Rectangle) shape;
	}
	
	public Rectangle getPositionalShape(){
		return (Rectangle) positionalShape;
	}
	
	public void setPosition(Vector2 position){
		if (currentPosition.equals(position))
			return;
		if(Constants.isRectangle(shape)){
			Rectangle oldShape = (Rectangle) shape;
			float newX = (float) (position.x + oldShape.getX());
			float newY = (float) (position.y + oldShape.getY());
			positionalShape = new Rectangle(newX, newY, (float)oldShape.getWidth(),(float) oldShape.getHeight());
		}
	}
	
	public Entity getOwner(){
		return owner;
	}
	
	public boolean intersects(BoundingShape other){
		if (Constants.isRectangle(other.getPositionalShape()) && Constants.isRectangle(positionalShape))
			return ((Rectangle) other.getPositionalShape()).overlaps((Rectangle)positionalShape);
		/*
		if (Constants.isEllipse(other.getPositionalShape()) && Constants.isRectangle(positionalShape)){
			Rectangle2D tr = (Rectangle2D) positionalShape;
			return ((Ellipse2D) other.getPositionalShape()).intersects(tr.getX(), tr.getY(), tr.getWidth(), tr.getHeight());
		}
		if (Constants.isEllipse(positionalShape) && Constants.isRectangle(other.getPositionalShape())){
			Rectangle2D tr = (Rectangle2D) other.getPositionalShape();
			return ((Ellipse2D) positionalShape).intersects(tr.getX(), tr.getY(), tr.getWidth(), tr.getHeight());
		}
		//(x2-x1)^2 + (y1-y2)^2 <= (r1+r2)^2
		if (Constants.isEllipse(positionalShape) && Constants.isEllipse(other.getPositionalShape())){
			
		}
		*/
		return false;
		/*
		Area areaA = new Area(positionalShape);
		areaA.intersect(new Area(other.getPositionalShape()));
		return !areaA.isEmpty();
		*/
		//return false;
		
	}

	public boolean isSensor() {
		return sensor;
	}

	public void setSensor(boolean sensor) {
		this.sensor = sensor;
	}
	
}
