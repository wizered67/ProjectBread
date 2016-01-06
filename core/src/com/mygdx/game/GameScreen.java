package com.mygdx.game;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
	 //bleh test fml
	//640 x 480
    MyGame game; // Note it's "MyGame" not "Game"
    SpriteBatch batch;
	OrthographicCamera camera;
	OrthographicCamera hudCamera;
	OrthographicCamera debugCamera;
	PlayerEntity player;
	ShapeRenderer shapes;
	BitmapFont font;
	ArrayList<Entity> allEntities;
	Array<AtlasRegion> grassTiles;
	AtlasRegion airSoloGrass;
	int[][] staticGrid;
	Box2DDebugRenderer debugRenderer;
	private float accumulator = 0f;
	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
    // constructor to keep a reference to the main Game class
    public GameScreen(MyGame game){
    	this.game = game;
    	font = new BitmapFont(true);
		font.setColor(Color.WHITE);
        batch = new SpriteBatch();
       
        
        Box2D.init();
        WorldManager.init();
        debugRenderer = new Box2DDebugRenderer();
        
        map =  new TmxMapLoader().load("grasslands1.tmx");
        int width = map.getProperties().get("width", Integer.class);
        int height = map.getProperties().get("height", Integer.class);
        float playerX = 0;
        float playerY = 0;
        int numLayers = map.getLayers().getCount();
        
        staticGrid = new int[width][height];
        for (int x = 0; x < staticGrid.length; x++){
        	for (int y = 0; y < staticGrid[0].length; y++){
        		staticGrid[x][y] = 0;
        	}
        }
        
        for (int i = 0; i < numLayers; i++){
        	MapLayer layer = map.getLayers().get(i);
        	if (layer.getObjects().getCount() == 0){ //tile layer
        		TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
        		for (int x = 0; x < width; x++){
        			for (int y = 0; y < height; y++){
        				Cell cell = tileLayer.getCell(x, y);
        				if (cell != null){
	        				TiledMapTile tile = cell.getTile();
	        				String solid = tile.getProperties().get("Solid", String.class);
	        				if (solid.equalsIgnoreCase("true")){
	        					staticGrid[x][y] = 1;
	        				}
        				}
        			}
        		}
        	}
        	else{ //not tile layer
        		MapObjects objects = layer.getObjects();
        		MapObject o = objects.get("PlayerStart");
        		if (o != null){
        			playerX = o.getProperties().get("x", Float.class);
        			playerY = o.getProperties().get("y", Float.class);
        			System.out.println(playerX + ", " + playerY);
        		}
        	}
        }
         
        float unitScale = 1f;
        mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale, batch);
        
        /*
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("Grass.pack"));
		grassTiles = atlas.findRegions("Grass");
		airSoloGrass = atlas.findRegion("SingleBlock_Air");
		grassTiles.add(airSoloGrass);
		*/
		/*
		for (AtlasRegion g : grassTiles){
			g.flip(false, true);
		}
		*/
        /*
        staticGrid[0][14] = 4;
        staticGrid[0][9] = 4;
        staticGrid[6][4] = 4;
        staticGrid[7][11] = 4;
        staticGrid[5][10] = 4;
        staticGrid[5][11] = 4;
        staticGrid[5][9] = 4;
        staticGrid[4][10] = 4;
        staticGrid[3][11] = 4;
        staticGrid[8][9] = 4;
        for (int x = 0; x < 19; x++){
        	staticGrid[x][7] = MathUtils.random(1, 3);
        }
        for (int y = 0; y < 15; y++){
        	staticGrid[19][y] = MathUtils.random(1, 3);
        }
        */
		camera = new OrthographicCamera(Gdx.graphics.getWidth() ,Gdx.graphics.
		          getHeight());
		camera.setToOrtho(false, Gdx.graphics.getWidth() ,Gdx.graphics.
		          getHeight());
		camera.zoom = 0.5f;
		
		hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
		          getHeight());
		hudCamera.setToOrtho(true);
		hudCamera.zoom = 1;
		
		debugCamera = new OrthographicCamera(Constants.toMeters(Gdx.graphics.getWidth()),Constants.toMeters(Gdx.graphics.
		          getHeight()));
		debugCamera.setToOrtho(false, Constants.toMeters(Gdx.graphics.getWidth()),Constants.toMeters(Gdx.graphics.
		          getHeight()));
		debugCamera.zoom = camera.zoom;
		
		shapes = new ShapeRenderer();
		
		allEntities = new ArrayList<Entity>();
		//allEntities.add(new StaticEntity("Ground", new Rectangle(0, 0, 16, 16), new Vector2(400, 190)));
		//allEntities.add(new StaticEntity("Ground", new Rectangle2D.Float(0, 0, 100, 200), new Vector2(400, 120)));
		//allEntities.add(new StaticEntity("Ground", new Rectangle2D.Float(0, 0, 50, 200), new Vector2(500, 300)));
		player = new PlayerEntity("Player 1", this);
		player.setTransform(new Vector2(Constants.toMeters(playerX), Constants.toMeters(playerY)), 0);
		allEntities.add(player);
		for (int i = 0; i < 40; i++){
			//allEntities.add(new PlayerEntity("Player 2", this));
			//System.out.println(i);
			//allEntities.add(new TestEntity());
			int testSize = 32;
			//allEntities.add(new StaticEntity("Ground", new Rectangle2D.Float(0, 0, MathUtils.random(testSize, testSize), MathUtils.random(testSize, testSize)), new Vector2(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()))));
		}
		/*
		BodyDef groundBodyDef = new BodyDef();  
		PolygonShape groundBox = new PolygonShape(); 
		Body groundBody;
		FixtureDef groundDef = new FixtureDef();
		Fixture groundFixture;
		for (int x = 0; x < staticGrid.length; x++){
        	for (int y = 0; y < staticGrid[0].length; y++){
        		if (staticGrid[x][y] == 1 || staticGrid[x][y] == 2 || staticGrid[x][y] == 3 || staticGrid[x][y] == 4){
        			groundBodyDef.position.set(new Vector2(Constants.toMeters(x * Constants.TILE_SIZE + Constants.TILE_SIZE / 2),Constants.toMeters(y * Constants.TILE_SIZE + Constants.TILE_SIZE / 2)));  
	        		groundBody = WorldManager.world.createBody(groundBodyDef);  
	        		groundBody.setUserData("ground test");
	        		groundBox.setAsBox(Constants.toMeters(Constants.TILE_SIZE / 2), Constants.toMeters(Constants.TILE_SIZE / 2));
	        		groundDef.shape = groundBox;
	        		groundDef.density = 0.0f;
	        		groundDef.filter.maskBits = Constants.MASK_SCENERY;
	        		groundDef.filter.categoryBits = Constants.CATEGORY_SCENERY;
	        		groundFixture = groundBody.createFixture(groundDef); 
	        		groundFixture.setUserData(Fixtures.GROUND);
        		}
        	}
        }
		*/
		
		setupGround();
		
		PolygonShape groundBox = new PolygonShape(); 
		BodyDef testCrateBodyDef = new BodyDef();
		testCrateBodyDef.position.set(Constants.toMeters(400), Constants.toMeters(500));
		testCrateBodyDef.fixedRotation = true;
		testCrateBodyDef.type = BodyType.DynamicBody;
		Body testCrate = WorldManager.world.createBody(testCrateBodyDef);
		FixtureDef testCrateDef = new FixtureDef();
		testCrateDef.friction = 1f;
		groundBox.setAsBox(Constants.toMeters(10), Constants.toMeters(10));
		testCrateDef.density = 0.4f;
		testCrateDef.filter.maskBits = Constants.MASK_SCENERY;
		testCrateDef.filter.categoryBits = Constants.CATEGORY_SCENERY;
		testCrateDef.shape = groundBox;
		Fixture testCrateFixture = testCrate.createFixture(testCrateDef);
		testCrateFixture.setUserData(Fixtures.CRATE);
		//testCrate.setLinearDamping(2f);
		
		groundBox.dispose();
		
		/*
		World test = WorldManager.world;
		Array<Body> bodies = new Array<Body>(); 
		test.getBodies(bodies);
		//for (Body b : bodies)
			//System.out.println(b.getUserData());
			 */
			
     }
     
     public void updateCameras(){
    	camera.position.set(Constants.toPixels(player.getX()) + player.getWidth() / 2,Constants.toPixels(player.getY()) , 0);
   		camera.update();
   		debugCamera.position.set(player.getX() + Constants.toMeters(player.getWidth() / 2), player.getY(), 0);
   		debugCamera.update();
     }
     
     @Override
     public void render(float delta) {
        // update and draw stuff
    	Gdx.gl.glClearColor(1, 1, 1, 1);
  		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	//camera.position.set(Constants.toPixels(player.getX()) + player.getWidth() / 2, Constants.toPixels(player.getY()), 0);
  		updateCameras();
		
  		shapes.setProjectionMatrix(hudCamera.combined);
        shapes.setColor(Color.BLACK);
        shapes.begin(ShapeType.Filled);
        shapes.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        shapes.end();
        shapes.setProjectionMatrix(camera.combined); 
        batch.setProjectionMatrix(camera.combined);
       
        
        mapRenderer.setView(camera);
		mapRenderer.render();
        
		
        
        shapes.begin(ShapeType.Filled);
        shapes.setColor(Color.GRAY);
        shapes.circle(Constants.toPixels(player.getX()), Constants.toPixels(player.getY()), 4);
        shapes.end();
        
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
        
        /*
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
        */
        batch.begin();
        
        for (Entity entity : allEntities){
        	if (entity != null){
        		if (entity.getSprite() != null){
        			batch.draw(entity.getSprite(), Constants.toPixels(entity.getX()) - entity.getWidth() / 2, Constants.toPixels(entity.getY() - entity.getBoundingHeight() / 2));
        		}
        	}
        }
                         
        batch.end();
       
        for (Entity entity : allEntities){
        	if (entity != null){
        		entity.update(delta); //update all entities before handling collisions
        	}
        }
        
        if (Constants.DEBUG){
	        batch.setProjectionMatrix(hudCamera.combined);
	        batch.begin();
	        
	        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 50, 50);
	        font.draw(batch, "Mouse X: " + Gdx.input.getX(), 50, 70);
	        font.draw(batch, "Mouse Y: " + Gdx.input.getY(), 50, 90);
	        font.draw(batch, "On Ground: " + player.onGround(), 50, 110);
	        font.draw(batch, "PX: " + player.getX(), 50, 130);
	        font.draw(batch, "Player Velocity: " + player.getVelocity(), 50, 150);
	        font.draw(batch, "Camera Y: " + camera.position.y, 50, 170);
	        double testY = player.getY() + player.getY();
	        font.draw(batch, "Player Y: " + testY, 50, 190);
	        font.draw(batch, "Projected Mouse Y: " + camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y, 50, 210);
	        batch.end();
	        batch.setProjectionMatrix(camera.combined);
        }
        if (Constants.DEBUG){
        	debugRenderer.render(WorldManager.world, debugCamera.combined);
        }
        doPhysicsStep(delta);
        /*
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
        */
        
        
        
        
        
     }

     
     private void doPhysicsStep(float deltaTime) {
    	    // fixed time step
    	    // max frame time to avoid spiral of death (on slow devices)
    	    float frameTime = Math.min(deltaTime, 0.25f);
    	    accumulator += frameTime;
    	    while (accumulator >= Constants.TIME_STEP) {
    	        WorldManager.world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
    	        accumulator -= Constants.TIME_STEP;
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
     
     public void setupGround(){
    	BodyDef allGroundBodyDef = new BodyDef();
 		allGroundBodyDef.position.set(new Vector2(0f, 0f));
 		ChainShape chainShape  = new ChainShape();
 		Body allGroundBody = WorldManager.world.createBody(allGroundBodyDef);
 		FixtureDef groundChain = new FixtureDef();
 		boolean continuous = false;
 		Vector2[] vertices = new Vector2[2];
 		vertices[0] = null;
 		vertices[1] = null;
 		for (int x = 0; x < staticGrid.length + 1; x++){
 			for (int y = 0; y < staticGrid[0].length; y++){
 				if ((x < staticGrid.length && staticGrid[x][y] != 0) || (x != 0 && staticGrid[x-1][y] != 0)){
 					if (!continuous){
 						chainShape = new ChainShape();
 						groundChain.shape = chainShape;
 						groundChain.density = 0.0f;
 						groundChain.filter.maskBits = Constants.MASK_SCENERY;
 						groundChain.filter.categoryBits = Constants.CATEGORY_SCENERY;
 						vertices[0] = new Vector2(Constants.toMeters(x * Constants.TILE_SIZE), Constants.toMeters(y * Constants.TILE_SIZE));
 						continuous = true;
 					}
 					else{
 						vertices[1] = new Vector2(Constants.toMeters(x * Constants.TILE_SIZE), Constants.toMeters(y * Constants.TILE_SIZE + Constants.TILE_SIZE));
 					}
 				}
 				else{
 					if (continuous){
 						if (vertices[1] == null){
 							vertices[1] = vertices[0].cpy().add(new Vector2(0, Constants.toMeters(Constants.TILE_SIZE)));
 						}
 						chainShape.createChain(vertices);
 						allGroundBody.createFixture(groundChain);
 						vertices[0] = null;
 						vertices[1] = null;
 						continuous = false;
 					}
 				}
 			}
 			if (continuous){
 				if (vertices[1] == null){
 					vertices[1] = vertices[0].cpy().add(new Vector2(0, Constants.toMeters(Constants.TILE_SIZE)));
 				}
 				chainShape.createChain(vertices);
 				allGroundBody.createFixture(groundChain);
 				vertices[0] = null;
 				vertices[1] = null;
 				continuous = false;
 			}
 		}
 		
 		
 		for (int y = 0; y < staticGrid[0].length + 1; y++){
 			for (int x = 0; x < staticGrid.length; x++){
 				if ((y < staticGrid[0].length && staticGrid[x][y] != 0) || (y != 0 && staticGrid[x][y - 1] != 0)){
 					if (!continuous){
 						chainShape = new ChainShape();
 						groundChain.shape = chainShape;
 						groundChain.density = 0.0f;
 						groundChain.filter.maskBits = Constants.MASK_SCENERY;
 						groundChain.filter.categoryBits = Constants.CATEGORY_SCENERY;
 						vertices[0] = new Vector2(Constants.toMeters(x * Constants.TILE_SIZE), Constants.toMeters(y * Constants.TILE_SIZE));
 						continuous = true;
 					}
 					else{
 						vertices[1] = new Vector2(Constants.toMeters(x * Constants.TILE_SIZE + Constants.TILE_SIZE), Constants.toMeters(y * Constants.TILE_SIZE));
 					}
 				}
 				else{
 					if (continuous){
 						if (vertices[1] == null){
 							vertices[1] = vertices[0].cpy().add(new Vector2(Constants.toMeters(Constants.TILE_SIZE), 0));
 						}
 						chainShape.createChain(vertices);
 						allGroundBody.createFixture(groundChain);
 						vertices[0] = null;
 						vertices[1] = null;
 						continuous = false;
 					}
 				}
 			}
 			if (continuous){
 				if (vertices[1] == null){
 					vertices[1] = vertices[0].cpy().add(new Vector2(Constants.toMeters(Constants.TILE_SIZE), 0));
 				}
 				chainShape.createChain(vertices);
 				allGroundBody.createFixture(groundChain);
 				vertices[0] = null;
 				vertices[1] = null;
 				continuous = false;
 			}
 		}
 		chainShape.dispose();
     }
     
     public int getStaticTile(int tx, int ty){
    	 if (tx < 0 || tx >= staticGrid.length || ty < 0 || ty >= staticGrid[0].length)
    		 return 0;
    	 return staticGrid[tx][ty];
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