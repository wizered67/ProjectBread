package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class CustomFillViewport extends ScalingViewport {
	float scale = 0;
	public CustomFillViewport(float worldWidth, float worldHeight) {
		super(Scaling.fill, worldWidth, worldHeight);
		// TODO Auto-generated constructor stub
	}

	public CustomFillViewport(float worldWidth, float worldHeight, Camera camera) {
		super(Scaling.fill, worldWidth, worldHeight, camera);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public void update (int screenWidth, int screenHeight, boolean centerCamera) {
	Vector2 scaled = getScaling().apply(getWorldWidth(), getWorldHeight(), screenWidth, screenHeight);
	scale = scaled.x / getWorldWidth();
	System.out.println(scale);
	int viewportWidth = Math.round(scaled.x);
	int viewportHeight = Math.round(scaled.y);

	// Center.
	setScreenBounds((screenWidth - viewportWidth) / 2, (screenHeight - viewportHeight) / 2, viewportWidth, viewportHeight);

	apply(centerCamera);
	}
	
	public float getScale(){
		return scale;
	}
}
