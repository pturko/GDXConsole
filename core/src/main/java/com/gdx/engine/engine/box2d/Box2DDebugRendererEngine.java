package com.gdx.engine.engine.box2d;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.service.ServiceFactoryImpl;

public class Box2DDebugRendererEngine extends EntitySystem {
    private final Box2DDebugRenderer renderer;
    private final Camera camera;
    private final World world;

    public Box2DDebugRendererEngine() {
        renderer = new Box2DDebugRenderer();
        camera = ServiceFactoryImpl.getScreenService().getCamera();
        world = ServiceFactoryImpl.getBox2DWorldService().getWorld();

        update(ServiceFactoryImpl.getConfigService().getApplicationConfig());
        configureListeners();
    }

    private void update(ApplicationConfig config) {
        this.setProcessing(config.getBox2DConfig().isBox2DDebugRendering());
    }

    private void configureListeners() {
        // Event reload application config
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_CHANGED, (ConfigChangedEvent e) ->
                update(e.getApplicationConfig()));
    }

    @Override
    public void update(float delta) {
        renderer.render(world, camera.combined);
    }

}