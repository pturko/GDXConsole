package com.gdx.engine.engine.box2d;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.event.MapChangedEvent;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.model.config.Box2DConfig;
import com.gdx.engine.model.config.Box2DStepConfig;
import com.gdx.engine.service.ServiceFactoryImpl;

public class Box2DPhysicsEngine extends EntitySystem {
    private Box2DStepConfig box2DStepConfig;
    private World world;
    private boolean isRendering;

    public Box2DPhysicsEngine() {
        world = ServiceFactoryImpl.getBox2DWorldService().getWorld();

        b2DSetUp(ServiceFactoryImpl.getConfigService().getApplicationConfig());
        configureListeners();
    }

    private void b2DSetUp(ApplicationConfig config) {
        Box2DConfig box2DConfig = config.getBox2DConfig();
        box2DStepConfig = box2DConfig.getBox2DStepConfig();
        isRendering = box2DConfig.isRendering();
    }

    private void configureListeners() {
        // Event reload application config
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_CHANGED, (ConfigChangedEvent e) ->
                b2DSetUp(e.getApplicationConfig()));

        // Whenever the map has been changed, set the OrthogonalTiledMapRenderer to render our new map
        ServiceFactoryImpl.getEventService().addEventListener(EventType.MAP_CHANGED, (MapChangedEvent e) -> {
                b2DSetUp(ServiceFactoryImpl.getConfigService().getApplicationConfig());
                }
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