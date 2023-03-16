package com.gdx.engine.engine.box2d;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.event.ConfigBox2DChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.event.MapChangedEvent;
import com.gdx.engine.model.config.Box2DConfig;
import com.gdx.engine.model.config.Box2DStepConfig;
import com.gdx.engine.service.ServiceFactoryImpl;

public class Box2DPhysicsEngine extends EntitySystem {
    private Box2DStepConfig box2DStepConfig;
    private final World world;
    private boolean isRendering;

    public Box2DPhysicsEngine() {
        world = ServiceFactoryImpl.getBox2DWorldService().getWorld();

        updateConfig();
        configureListeners();
    }

    private void updateConfig() {
        Box2DConfig box2DConfig = ServiceFactoryImpl.getConfigService().getApplicationConfig().getBox2DConfig();
        box2DStepConfig = box2DConfig.getBox2DStepConfig();
        isRendering = box2DConfig.isRendering();
    }

    private void configureListeners() {
        // Event reload application config
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_BOX2D_CHANGED, (ConfigBox2DChangedEvent e) ->
                updateConfig());

        // Whenever the map has been changed, set the OrthogonalTiledMapRenderer to render our new map
        ServiceFactoryImpl.getEventService().addEventListener(EventType.MAP_CHANGED, (MapChangedEvent e) -> updateConfig()
        );
    }

    @Override
    public void update(float delta) {
        if (isRendering) {
            world.step(1 / box2DStepConfig.getTimeStep(),
                    box2DStepConfig.getVelocityIterations(),
                    box2DStepConfig.getPositionIterations());
        }
    }

}