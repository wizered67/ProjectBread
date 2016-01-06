package com.mygdx.game;



import com.badlogic.gdx.math.Rectangle;

public class Constants {
	public final static int TILE_SIZE = 32;
	public final static boolean DEBUG = true;
	public final static int PPM = 32;
	public final static float TIME_STEP = 1 / 45f;
	public final static int POSITION_ITERATIONS = 2;
	public final static int VELOCITY_ITERATIONS = 6;
	public final static float MAX_VELOCITY = 5f;
	
	public final static short CATEGORY_PLAYER = 0x0001;  
	public final static short CATEGORY_ENEMY = 0x0002; 
	public final static short CATEGORY_SCENERY = 0x0004; 
	public final static short MASK_PLAYER = CATEGORY_ENEMY | CATEGORY_SCENERY; 
	public final static short MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_SCENERY; 
	public final static short MASK_SCENERY = CATEGORY_PLAYER | CATEGORY_SCENERY | CATEGORY_ENEMY; 
	
	public static boolean isRectangle(Object c){
		return c.getClass().equals(Rectangle.class);
	}
	
	public static float toPixels(float meters){
		return meters * PPM;
	}
	
	public static float toMeters(float pixels){
		return pixels / PPM;
	}
	
}
