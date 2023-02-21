package com.gdx.game.map;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorldContactListener implements ContactListener {

    private final PooledEngine engine;

    public WorldContactListener(PooledEngine engine) {
        this.engine = engine;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef) {
            // When a box hits the ground
            case CategoryBits.BOX | CategoryBits.GROUND:
                log.info("Begin Box contact");
                break;
            default:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef) {
            // When a box hits the ground
            case CategoryBits.BOX | CategoryBits.GROUND:
                log.info("End Box contact");
                break;

            default:
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private static Fixture getTargetFixture(short targetCategoryBits, Fixture fixtureA, Fixture fixtureB) {
        Fixture targetFixture;

        if (fixtureA.getFilterData().categoryBits == targetCategoryBits) {
            targetFixture = fixtureA;
        } else if (fixtureB.getFilterData().categoryBits == targetCategoryBits) {
            targetFixture = fixtureB;
        } else {
            targetFixture = null;
        }

        return targetFixture;
    }

}