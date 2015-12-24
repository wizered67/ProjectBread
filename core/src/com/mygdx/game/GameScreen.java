package com.mygdx.game;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
public class GameScreen implements Screen {
	 //bleh test fml
	//640 x 480
    MyGame game; // Note it's "MyGame" not "Game"
    SpriteBatch batch;
	OrthographicCamera camera;
	PlayerEntity player;
	ShapeRenderer shapes;
	Quadtree quad;
	BitmapFont font;
	ArrayList<Entity> allEntities;
	int[][] staticGrid;
    // constructor to keep a reference to the main Game class
     public GameScreen(MyGame game){
    	this.game = game;
    	font = new BitmapFont(true);
		font.setColor(Color.BLACK);
        batch = new SpriteBatch();
        staticGrid = new int[20][15];
        for (int x = 0; x < staticGrid.length; x++){
        	for (int y = 0; y < staticGrid[0].length; y++){
        		staticGrid[x][y] = 0;
        	}
        }
        staticGrid[0][0] = 1;
        staticGrid[0][5] = 1;
        staticGrid[6][10] = 1;
        staticGrid[7][3] = 1;
        staticGrid[8][5] = 1;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
		          getHeight());
		camera.setToOrtho(true);
		shapes = new ShapeRenderer();
		quad = new Quadtree(0, new Rectangle(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		
		allEntities = new ArrayList<Entity>();
		//allEntities.add(new StaticEntity("Ground", new Rectangle2D.Float(0, 0, 16, 16), new Vector2(100, 300)));
		//allEntities.add(new StaticEntity("Ground", new Rectangle2D.Float(0, 0, 100, 200), new Vector2(400, 120)));
		//allEntities.add(new StaticEntity("Ground", new Rectangle2D.Float(0, 0, 50, 200), new Vector2(500, 300)));
		player = new PlayerEntity("Player 1", this);
		allEntities.add(player);
		for (int i = 0; i < 40; i++){
			//allEntities.add(new PlayerEntity("Player 2", this));
			//System.out.println(i);
			//allEntities.add(new TestEntity());
			int testSize = 32;
			allEntities.add(new StaticEntity("Ground", new Rectangle2D.Float(0, 0, MathUtils.random(testSize, testSize), MathUtils.random(testSize, testSize)), new Vector2(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()))));
		}
		
     }
     
     @Override
     public void render(float delta) {
        // update and draw stuff
    	Gdx.gl.glClearColor(1, 1, 1, 1);
  		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapes.setProjectionMatrix(camera.combined); 
        
       
        shapes.begin(ShapeType.Line);
        shapes.setColor(Color.RED);
    	for (int x = 0; x < Gdx.graphics.getWidth(); x += Constants.TILE_SIZE){
    		shapes.line(x, 0, x, Gdx.graphics.getHeight());
    	}
    	for (int y = 0; y < Gdx.graphics.getHeight(); y += Constants.TILE_SIZE){
    		shapes.line(0, y, Gdx.graphics.getWidth(), y);
    	}
        	
        shapes.end();
         
        shapes.begin(ShapeType.Filled);
        shapes.setColor(Color.CYAN);
        for (Entity entity : allEntities){
    		if (entity != null){
    			for (BoundingShape b : entity.getBoundingShapes()){
    				Rectangle2D bounds = b.getShape().getBounds2D();
    				shapes.rect((float)bounds.getX() + entity.getX(), (float)bounds.getY() + entity.getY(), (float)bounds.getWidth(), (float)bounds.getHeight());
    			}
    		}
    	}
    	
        shapes.setColor(Color.GREEN);
    	for (int x = 0; x < staticGrid.length; x++){
        	for (int y = 0; y < staticGrid[0].length; y++){
        		if (staticGrid[x][y] == 1)
        			shapes.rect(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        	}
        }
        shapes.end();
        
        batch.begin();
        
        for (Entity entity : allEntities){
        	if (entity != null){
        		if (entity.getSprite() != null){
        			batch.draw(entity.getSprite(), entity.getX(), entity.getY());
        		}
        	}
        }
        	//batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY(),player.getSprite().getOriginX(),
            //            player.getSprite().getOriginY(),
            //     player.getSprite().getWidth(),player.getSprite().getHeight(),player.getSprite().getScaleX(),player.
            //                     getSprite().getScaleY(),player.getSprite().getRotation());
        	
                         
        batch.end();
        
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 50, 50);
        font.draw(batch, "Mouse X: " + Gdx.input.getX(), 50, 80);
        font.draw(batch, "Mouse Y: " + Gdx.input.getY(), 50, 110);
        batch.end();
        //debugRenderer.render(world, debugMatrix);
        for (Entity entity : allEntities){
        	if (entity != null){
        		entity.update(delta); //update all entities before handling collisions
        	}
        }
        updateQuad();
        
        ArrayList<BoundingShape> returnObjects = new ArrayList<BoundingShape>();
        for (Entity entity : allEntities) {
        	if (entity != null){
	        	ArrayList<BoundingShape> bs = entity.getBoundingShapes();
	        	for (int j = 0; j < bs.size(); j++) {
	                returnObjects.clear();
	                BoundingShape currentBounding = bs.get(j);
	                quad.retrieve(returnObjects, currentBounding);
	                
	                for (int x = 0; x < returnObjects.size(); x++) {
	                    // Run collision detection algorithm between objects
	                	BoundingShape other = returnObjects.get(x);
	                	//System.out.println(other.getUserData());
	                	if (currentBounding.intersects(other)){
	                		if (currentBounding.getOwner() != other.getOwner()){
	                			currentBounding.getOwner().collide(new Collision(currentBounding.getOwner(), other.getOwner(), currentBounding, other));
	                			//System.out.println("Collision: " + currentBounding.getUserData() + " + " + other.getUserData());
	                		}
	                	}
	                  }
	            }
	        	entity.postCollisionUpdate();
	        }
        } 
     }


     public void updateQuad(){
    	 quad.clear();
         for (Entity entity : allEntities) {
        	 if (entity != null){
        		 ArrayList<BoundingShape> bs = entity.getBoundingShapes();
        		 for (int j = 0; j < bs.size(); j++) {
        			 quad.insert(bs.get(j));
        		 }
        	 	}
         }
     }
     /*
     public int checkStaticCollision(Vector2 position){
    	 
    	 int tx = (int) Math.floor(position.x / Constants.TILE_SIZE);
    	 int ty = (int) Math.floor(position.y / Constants.TILE_SIZE);
    	 if (tx < 0 || tx >= staticGrid.length || ty < 0 || ty >= staticGrid[0].length)
    		 return 0;
    	 int tile = staticGrid[tx][ty];
    	 if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
    		 System.out.println("Position X: " + position.x + ", " + "Position Y: " + position.y);
    		 System.out.println("Tile X: " + tx + ", " + "Tile Y: " + ty + ", Tile ID: " + tile);
    	 }
    	 return tile;
     }
     */
     public int getStaticTile(int tx, int ty){
    	 if (tx < 0 || tx >= staticGrid.length || ty < 0 || ty >= staticGrid[0].length)
    		 return 0;
    	 return staticGrid[tx][ty];
     }
     public Quadtree getQuad(){
    	 return quad;
     }
     
    @Override
     public void resize(int width, int height) {
     }


    @Override
     public void show() {
          // called when this screen is set as the screen with game.setScreen();
     }


    @Override
     public void hide() {
          // called when current screen changes from this to a different screen
     }


    @Override
     public void pause() {
     }


    @Override
     public void resume() {
     }


    @Override
     public void dispose() {
             // never called automatically
     }
}