package com.mygdx.game;



import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Constants {
	public final static int TILE_SIZE = 32;
	public final static boolean DEBUG = true;
	public final static int PPM = 32;
	public final static float TIME_STEP = 1 / 60f;
	public final static int POSITION_ITERATIONS = 2; //2
	public final static int VELOCITY_ITERATIONS = 6; //6
	public final static float MAX_VELOCITY = 5f;
	public final static float VIRTUAL_WIDTH = 400;
	public final static float VIRTUAL_HEIGHT = 240;
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
	
	public static Vector2 toPixels(Vector2 metersVector){
		return metersVector.cpy().scl(PPM);
	}
	
	public static Vector2 toMeters(Vector2 pixelsVector){
		return pixelsVector.cpy().scl(1f / PPM);
	}
	
	public static void fixBleeding(TextureRegion region) {
		float x = region.getRegionX();
		float y = region.getRegionY();
		float width = region.getRegionWidth();
		float height = region.getRegionHeight();
		float invTexWidth = 1f / region.getTexture().getWidth();
		float invTexHeight = 1f / region.getTexture().getHeight();
		region.setRegion((x + .5f) * invTexWidth, (y+.5f) * invTexHeight, (x + width - .5f) * invTexWidth, (y + height - .5f) * invTexHeight);       
	}
	
}
