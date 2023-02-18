package com.gdx.engine.box2d.component.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.util.box2d.BodyBuilder;

public class B2BodyComponent implements Component {

    private final BodyBuilder bodyBuilder;
    private Body body;
    private Fixture bodyFixture;

    public B2BodyComponent(World world) {
        bodyBuilder = new BodyBuilder(world);
    }

    public BodyBuilder getBodyBuilder() {
        return bodyBuilder;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public World getWorld() {
        return body.getWorld();
    }

    public void setBodyFixture(Fixture bodyFixture) {
        this.bodyFixture = bodyFixture;
    }

}