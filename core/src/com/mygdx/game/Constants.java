package com.mygdx.game;



import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;

public class Constants {
	public final static int TILE_SIZE = 32;
	public final static boolean DEBUG = false;
	public static boolean isRectangle(Object c){
		return c.getClass().equals(Rectangle.class);
	}
	/*
	public static boolean isEllipse(Object c){
		return ((Class)(Ellipse.class)).isAssignableFrom(c.getClass());
	}
	*/
	
}
