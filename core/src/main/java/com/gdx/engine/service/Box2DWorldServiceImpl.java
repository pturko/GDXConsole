package com.gdx.engine.service;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.interfaces.service.Box2DWorldService;
import com.gdx.engine.model.config.Box2DWorldGravityConfig;

public class Box2DWorldServiceImpl implements Box2DWorldService {
    private static Box2DWorldServiceImpl box2DWorldInstance;
    private static World world;

    public static Box2DWorldServiceImpl getInstance() {
        if (box2DWorldInstance == null) {
            box2DWorldInstance = new Box2DWorldServiceImpl();
        }
        return box2DWorldInstance;
    }

    Box2DWorldServiceImpl() {
        // Initialize the world, and register the world contact listener
        Box2DWorldGravityConfig gravityConfig = ServiceFactoryImpl.getConfigService()
                .getBox2DConfig().getBox2DWorldGravityConfig();
        world = new World(new Vector2(gravityConfig.getX(), gravityConfig.getY()),
                ServiceFactoryImpl.getConfigService().getBox2DConfig().isDoSleep());
    }

    @Override
    public World getWorld() {
        return world;
    }

}