package com.mygdx.game;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Constants {
	public final static int TILE_SIZE = 32;
	public final static boolean DEBUG = false;
	public static boolean isRectangle(Object c){
		return Rectangle2D.class.isAssignableFrom(c.getClass());
	}
	public static boolean isEllipse(Object c){
		return Ellipse2D.class.isAssignableFrom(c.getClass());
	}
}
