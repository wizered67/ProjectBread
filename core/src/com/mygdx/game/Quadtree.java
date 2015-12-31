package com.mygdx.game;


import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;


//http://gamedevelopment.tutsplus.com/tutorials/quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space--gamedev-374
public class Quadtree {

	private int MAX_OBJECTS = 10;
	private int MAX_LEVELS = 5;

	private int level;
	private ArrayList<BoundingShape> objects;
	private Rectangle bounds;
	private Quadtree[] nodes;

	/*
	 * Constructor
	 */
	public Quadtree(int pLevel, Rectangle pBounds) {
		level = pLevel;
		objects = new ArrayList<BoundingShape>();
		bounds = pBounds;
		nodes = new Quadtree[4];
	}

	/*
	 * Clears the quadtree
	 */
	public void clear() {
		objects.clear();

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}

	/*
	 * Splits the node into 4 subnodes
	 */
	private void split() {
		int subWidth = (int)(bounds.getWidth() / 2);
		int subHeight = (int)(bounds.getHeight() / 2);
		int x = (int)bounds.getX();
		int y = (int)bounds.getY();

		nodes[0] = new Quadtree(level+1, new Rectangle(x + subWidth, y, subWidth, subHeight));
		nodes[1] = new Quadtree(level+1, new Rectangle(x, y, subWidth, subHeight));
		nodes[2] = new Quadtree(level+1, new Rectangle(x, y + subHeight, subWidth, subHeight));
		nodes[3] = new Quadtree(level+1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
	}

	/*
	 * Determine which node the object belongs to. -1 means
	 * object cannot completely fit within a child node and is part
	 * of the parent node
	 */
	private boolean[] getIndex(BoundingShape boundingShape) {
		boolean[] allIndexes = new boolean[4];
		for (int i = 0; i < 4; i++){
			allIndexes[i] = false;
		}
		Rectangle pRect = (Rectangle) boundingShape.getShape();
		int index = -1;
		double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
		double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

		// Object can completely fit within the top quadrants
		boolean topQuadrant = (pRect.getY() < horizontalMidpoint && pRect.getY() + pRect.getHeight() < horizontalMidpoint);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (pRect.getY() > horizontalMidpoint);
		boolean topAndBottomQuadrant = (pRect.getY() <= horizontalMidpoint && pRect.getY() + pRect.getHeight() >= horizontalMidpoint);

		boolean leftQuadrant = (pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint); 
		boolean rightQuadrant = (pRect.getX() > verticalMidpoint);
		boolean leftAndRightQuadrant = pRect.getX() <= verticalMidpoint && pRect.getX() + pRect.getWidth() >= verticalMidpoint;
		
		if (topAndBottomQuadrant){
			if (leftAndRightQuadrant){
				for (int i = 0; i < 4; i++){
					allIndexes[i] = true;
				}
			}
			else if (leftQuadrant){
				allIndexes[1] = true;
				allIndexes[2] = true;
			}
			else if (rightQuadrant){
				allIndexes[0] = true;
				allIndexes[3] = true;
			}
		}
		else if (topQuadrant){
			if (leftAndRightQuadrant){
				allIndexes[0] = true;
				allIndexes[1] = true;
			}
			else if (leftQuadrant){
				allIndexes[1] = true;
			}
			else if (rightQuadrant){
				allIndexes[0] = true;
			}
		}
		else if (bottomQuadrant){
			if (leftAndRightQuadrant){
				allIndexes[2] = true;
				allIndexes[3] = true;
			}
			else if (leftQuadrant){
				allIndexes[2] = true;
			}
			else if (rightQuadrant){
				allIndexes[3] = true;
			}
		}
		else{
			for (int i = 0; i < 4; i++){
				allIndexes[i] = true;
			}
		}
		return allIndexes;
	}

	/*
	 * Insert the object into the quadtree. If the node
	 * exceeds the capacity, it will split and add all
	 * objects to their corresponding nodes.
	 */
	public void insert(BoundingShape boundingShape) {
		if (nodes[0] != null) {
			boolean[] indexes = getIndex(boundingShape);
			for (int i = 0; i < 4; i++){
				if (indexes[i]){
					if (i != -1) {
						nodes[i].insert(boundingShape);
					}
				}
			}
			return;
		}

		objects.add(boundingShape);

		if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) { 
				split(); 
			}
			boolean removed;
			int i = 0;
			while (i < objects.size()) {
				removed = false;
				boolean[] indexes = getIndex(objects.get(i));
				BoundingShape o = objects.remove(i);
				for (int ii = 0; ii < 4; ii++){
					if (indexes[ii]){
						nodes[ii].insert(o);
					}
				}
			}
		}
	}

	public void printAll(int node){
		if (objects.size() != 0){
			for (BoundingShape bs : objects){
				System.out.println("Level: " + level + ", Node: " + node + ", contains " + bs.getUserData());
			}
		}
		for (int i = 0; i < nodes.length; i++){
			if (nodes[i] != null){
				nodes[i].printAll(i);
			}
		}
	}

	/*
	 * Return all objects that could collide with the given object
	 */
	public ArrayList<BoundingShape> retrieve(ArrayList<BoundingShape> returnObjects, BoundingShape boundingShape) {
		boolean[] indexes = getIndex(boundingShape);
		for (int i = 0; i < 4; i++){
			if (indexes[i]){
				if (i != -1 && nodes[0] != null) {
					nodes[i].retrieve(returnObjects, boundingShape);
				}
			}
		}
		returnObjects.addAll(objects);

		return returnObjects;
	}
}