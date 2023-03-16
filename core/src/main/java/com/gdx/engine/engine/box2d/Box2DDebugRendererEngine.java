package com.gdx.engine.engine.box2d;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.event.ConfigBox2DChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.model.config.Box2DConfig;
import com.gdx.engine.service.ServiceFactoryImpl;

public class Box2DDebugRendererEngine extends EntitySystem {
    private final Box2DDebugRenderer renderer;
    private final Camera camera;
    private final World world;

    public Box2DDebugRendererEngine() {
        renderer = new Box2DDebugRenderer();
        camera = ServiceFactoryImpl.getScreenService().getCamera();
        world = ServiceFactoryImpl.getBox2DWorldService().getWorld();

        updateConfig();
        configureListeners();
    }

    private void updateConfig() {
        Box2DConfig box2DConfig = ServiceFactoryImpl.getConfigService().getApplicationConfig().getBox2DConfig();
        this.setProcessing(box2DConfig.isBox2DDebugRendering());
    }

    private void configureListeners() {
        // Event reload application config
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_BOX2D_CHANGED, (ConfigBox2DChangedEvent e) ->
                updateConfig());
    }

    @Override
    public void update(float delta) {
        renderer.render(world, camera.combined);
    }

}