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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
public class GameScreen implements Screen {
	 //bleh test fml
	//640 x 480
    MyGame game; // Note it's "MyGame" not "Game"
    SpriteBatch batch;
	OrthographicCamera camera;
	OrthographicCamera hudCamera;
	PlayerEntity player;
	ShapeRenderer shapes;
	Quadtree quad;
	BitmapFont font;
	ArrayList<Entity> allEntities;
	Array<AtlasRegion> grassTiles;
	AtlasRegion airSoloGrass;
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
        
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("Grass.pack"));
		grassTiles = atlas.findRegions("Grass");
		airSoloGrass = atlas.findRegion("SingleBlock_Air");
		grassTiles.add(airSoloGrass);
		for (AtlasRegion g : grassTiles){
			g.flip(false, true);
		}
        staticGrid[0][0] = 4;
        staticGrid[0][5] = 4;
        staticGrid[6][10] = 4;
        staticGrid[7][3] = 4;
        staticGrid[5][4] = 4;
        staticGrid[3][3] = 4;
        staticGrid[8][5] = 4;
        for (int x = 0; x < 19; x++){
        	staticGrid[x][7] = MathUtils.random(1, 3);
        }
        for (int y = 0; y < 15; y++){
        	staticGrid[19][y] = MathUtils.random(1, 3);
        }
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
		          getHeight());
		camera.setToOrtho(true);
		camera.zoom = 0.5f;
		
		hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
		          getHeight());
		hudCamera.setToOrtho(true);
		hudCamera.zoom = 1;
		
		shapes = new ShapeRenderer();
		quad = new Quadtree(0, new Rectangle(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		
		allEntities = new ArrayList<Entity>();
		//allEntities.add(new StaticEntity("Ground", new Rectangle2D.Float(0, 0, 16, 16), new Vector2(400, 190)));
		//allEntities.add(new StaticEntity("Ground", new Rectangle2D.Float(0, 0, 100, 200), new Vector2(400, 120)));
		//allEntities.add(new StaticEntity("Ground", new Rectangle2D.Float(0, 0, 50, 200), new Vector2(500, 300)));
		player = new PlayerEntity("Player 1", this);
		allEntities.add(player);
		for (int i = 0; i < 40; i++){
			//allEntities.add(new PlayerEntity("Player 2", this));
			//System.out.println(i);
			//allEntities.add(new TestEntity());
			int testSize = 32;
			//allEntities.add(new StaticEntity("Ground", new Rectangle2D.Float(0, 0, MathUtils.random(testSize, testSize), MathUtils.random(testSize, testSize)), new Vector2(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()))));
		}
		
     }
     
     @Override
     public void render(float delta) {
        // update and draw stuff
    	Gdx.gl.glClearColor(1, 1, 1, 1);
  		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	camera.position.set(player.getX() + player.getWidth() / 2, (float) player.getLowerY() - 40, 0);
  		camera.update();
  		//System.out.println(camera.position);
        batch.setProjectionMatrix(camera.combined);
        
        shapes.setProjectionMatrix(hudCamera.combined);
        shapes.setColor(Color.CYAN);
        shapes.begin(ShapeType.Filled);
        shapes.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
       
        shapes.end();
        shapes.setProjectionMatrix(camera.combined); 
        
        if (Constants.DEBUG){
	        shapes.begin(ShapeType.Line);
	        shapes.setColor(Color.RED);
	    	for (int x = 0; x < Gdx.graphics.getWidth(); x += Constants.TILE_SIZE){
	    		shapes.line(x, 0, x, Gdx.graphics.getHeight());
	    	}
	    	for (int y = 0; y < Gdx.graphics.getHeight(); y += Constants.TILE_SIZE){
	    		shapes.line(0, y, Gdx.graphics.getWidth(), y);
	    	}
	        	
	        shapes.end();
        }
        
        if (Constants.DEBUG){ 
	        shapes.begin(ShapeType.Filled);
	        shapes.setColor(Color.MAROON);
	        for (Entity entity : allEntities){
	    		if (entity != null && (entity.getClass() != PlayerEntity.class || true)){
	    			for (BoundingShape b : entity.getBoundingShapes()){
	    				Rectangle2D bounds = b.getShape().getBounds2D();
	    				shapes.rect((float)bounds.getX() + entity.getX(), (float)bounds.getY() + entity.getY(), (float)bounds.getWidth(), (float)bounds.getHeight());
	    			}
	    		}
	    	}
	    	shapes.end();
        }
        
        batch.begin();
        //shapes.setColor(Color.GREEN);
    	for (int x = 0; x < staticGrid.length; x++){
        	for (int y = 0; y < staticGrid[0].length; y++){
        		if (staticGrid[x][y] == 1 || staticGrid[x][y] == 2 || staticGrid[x][y] == 3 || staticGrid[x][y] == 4)
        			batch.draw(grassTiles.get(staticGrid[x][y] - 1), x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
        			//shapes.rect(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        	}
        }
        batch.end();
        
        batch.begin();
        
        for (Entity entity : allEntities){
        	if (entity != null){
        		if (entity.getSprite() != null){
        			if (entity.getClass() == PlayerEntity.class){
        				batch.draw(entity.getSprite(), entity.getX(), (float) (entity.getY() + ((PlayerEntity)entity).getPrimaryHitbox().getShape().getBounds().getY() + ((PlayerEntity)entity).getPrimaryHitbox().getShape().getBounds().getHeight()- ((PlayerEntity) entity).getHeight()));
        			}
        			else
        				batch.draw(entity.getSprite(), entity.getX(), entity.getY());
        		}
        	}
        }
        	//batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY(),player.getSprite().getOriginX(),
            //            player.getSprite().getOriginY(),
            //     player.getSprite().getWidth(),player.getSprite().getHeight(),player.getSprite().getScaleX(),player.
            //                     getSprite().getScaleY(),player.getSprite().getRotation());
        	
                         
        batch.end();
        if (Constants.DEBUG){
	        batch.setProjectionMatrix(hudCamera.combined);
	        batch.begin();
	        
	        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 50, 50);
	        font.draw(batch, "Mouse X: " + Gdx.input.getX(), 50, 80);
	        font.draw(batch, "Mouse Y: " + Gdx.input.getY(), 50, 110);
	        font.draw(batch, "On Ground: " + player.onGround(), 50, 140);
	        font.draw(batch, "PX: " + player.getX(), 50, 170);
	        font.draw(batch, "Camera Y: " + camera.position.y, 50, 200);
	        double testY = player.getY() + player.getPrimaryHitbox().getShape().getBounds().getY() + player.getPrimaryHitbox().getShape().getBounds().getHeight();
	        font.draw(batch, "Player Lower Y: " + testY, 50, 230);
	        font.draw(batch, "Projected Mouse Y: " + camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y, 50, 250);
	        batch.end();
	        batch.setProjectionMatrix(camera.combined);
        }
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