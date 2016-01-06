package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ListenerClass implements ContactListener {
	
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
		/*
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		Object dataA = fixtureA.getBody().getUserData();
		Object dataB = fixtureB.getBody().getUserData();
		Fixture playerFixture = null;
		if (dataA instanceof PlayerEntity){
			playerFixture = fixtureA;
		}
		else if (dataB instanceof PlayerEntity){
			playerFixture = fixtureB;
		}
		if (playerFixture != null){
			PlayerEntity player = (PlayerEntity)(playerFixture.getBody().getUserData());
			if (player.onGround() && Math.abs(player.getVelocity().y) < 1e-6){
				playerFixture.setFriction(player.defaultFriction);
				contact.setFriction(player.defaultFriction);
			}
			else{
				playerFixture.setFriction(0);
				contact.setFriction(0);
			}
		}
		*/
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
};