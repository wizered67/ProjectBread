package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyCollisionListener implements ContactListener {
	
	@Override
    public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		Object dataA = fixtureA.getBody().getUserData();
		Object dataB = fixtureB.getBody().getUserData();
		//System.out.println(dataA);
		//System.out.println(dataB);
		if (dataA != null && dataA instanceof Entity){
			ContactData cd = new ContactData(contact, fixtureA, fixtureB);
			((Entity) dataA).beginContact(cd);
		}
		if (dataB != null && dataB instanceof Entity){
			ContactData cd = new ContactData(contact, fixtureB, fixtureA);
			((Entity) dataB).beginContact(cd);
		}
    }
	
	@Override
    public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		Object dataA = fixtureA.getBody().getUserData();
		Object dataB = fixtureB.getBody().getUserData();
		if (dataA != null && dataA instanceof Entity){
			ContactData cd = new ContactData(contact, fixtureA, fixtureB);
			((Entity) dataA).endContact(cd);
		}
		if (dataB != null && dataB instanceof Entity){
			ContactData cd = new ContactData(contact, fixtureB, fixtureA);
			((Entity) dataB).endContact(cd);
		}
    }

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		Object dataA = fixtureA.getBody().getUserData();
		Object dataB = fixtureB.getBody().getUserData();
		//System.out.println(dataA);
		//System.out.println(dataB);
		if (dataA != null && dataA instanceof Entity){
			ContactData cd = new ContactData(contact, fixtureA, fixtureB);
			((Entity) dataA).preSolveCollision(cd, oldManifold);
		}
		if (dataB != null && dataB instanceof Entity){
			ContactData cd = new ContactData(contact, fixtureB, fixtureA);
			((Entity) dataB).preSolveCollision(cd, oldManifold);
		}
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		Object dataA = fixtureA.getBody().getUserData();
		Object dataB = fixtureB.getBody().getUserData();
		//System.out.println(dataA);
		//System.out.println(dataB);
		if (dataA != null && dataA instanceof Entity){
			ContactData cd = new ContactData(contact, fixtureA, fixtureB);
			((Entity) dataA).postSolveCollision(cd, impulse);
		}
		if (dataB != null && dataB instanceof Entity){
			ContactData cd = new ContactData(contact, fixtureB, fixtureA);
			((Entity) dataB).postSolveCollision(cd, impulse);
		}
	}
}