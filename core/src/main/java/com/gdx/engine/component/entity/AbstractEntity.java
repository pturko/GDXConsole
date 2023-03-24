package com.gdx.engine.component.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.model.config.Box2DConfig;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.gdx.engine.util.box2d.BodyBuilder;

public abstract class AbstractEntity extends Entity {
    protected final World world;
    protected static BodyBuilder bodyBuilder;
    protected static float ppm;

    public AbstractEntity() {
        world = ServiceFactoryImpl.getBox2DWorldService().getWorld();
        bodyBuilder = new BodyBuilder(world);

        Box2DConfig box2DConfig = ServiceFactoryImpl.getConfigService().getBox2DConfig();
        ppm = box2DConfig.getPpm();
    }
}
