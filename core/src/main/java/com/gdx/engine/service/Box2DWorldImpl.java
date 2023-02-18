package com.gdx.engine.service;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.interfaces.service.Box2DWorld;
import com.gdx.engine.model.config.Box2DWorldGravityConfig;

public class Box2DWorldImpl implements Box2DWorld {
    private static ConfigServiceImpl configService;

    private static Box2DWorldImpl box2DWorldInstance;
    private static World world;

    public static Box2DWorldImpl getInstance() {
        if (box2DWorldInstance == null) {
            box2DWorldInstance = new Box2DWorldImpl();
        }
        return box2DWorldInstance;
    }

    Box2DWorldImpl() {
        configService = ConfigServiceImpl.getInstance();

        // Initialize the world, and register the world contact listener
        Box2DWorldGravityConfig gravityConfig = configService.getBox2DConfig().getBox2DWorldGravityConfig();
        world = new World(new Vector2(gravityConfig.getX(), gravityConfig.getY()),
                configService.getBox2DConfig().isDoSleep());
    }

    @Override
    public World getWorld() {
        return world;
    }

}