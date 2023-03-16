package com.gdx.engine.service;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.event.ConfigBox2DChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.interfaces.service.Box2DWorldService;
import com.gdx.engine.model.config.*;

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
        b2DSetUp();
        configureListeners();
    }

    private void b2DSetUp() {
        // Initialize the world, and register the world contact listener
        Box2DWorldGravityConfig gravityConfig = ServiceFactoryImpl.getConfigService().
                getApplicationConfig().getBox2DConfig().getBox2DWorldGravityConfig();
        world = new World(new Vector2(gravityConfig.getX(), gravityConfig.getY()),
                ServiceFactoryImpl.getConfigService().getBox2DConfig().isDoSleep());
    }

    private void configureListeners() {
        // Event reload application config
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_BOX2D_CHANGED, (ConfigBox2DChangedEvent e) ->
                b2DSetUp());
    }

    @Override
    public World getWorld() {
        return world;
    }

}