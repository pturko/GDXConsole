package com.gdx.engine.engine.box2d;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.model.config.Box2DStepConfig;
import com.gdx.engine.service.Box2DWorldImpl;
import com.gdx.engine.service.ConfigServiceImpl;

public class Box2DPhysicsEngine extends EntitySystem {
    private static Box2DWorldImpl box2DService;
    private static ConfigServiceImpl configService;
    private static Box2DStepConfig box2DStepConfig;

    private final World world;

    public Box2DPhysicsEngine() {
        box2DService = Box2DWorldImpl.getInstance();
        configService = ConfigServiceImpl.getInstance();
        box2DStepConfig = configService.getBox2DConfig().getBox2DStepConfig();
        this.world = box2DService.getWorld();
    }

    @Override
    public void update(float delta) {
        world.step(1/box2DStepConfig.getTimeStep(),
                box2DStepConfig.getVelocityIterations(),
                box2DStepConfig.getPositionIterations());
    }

}