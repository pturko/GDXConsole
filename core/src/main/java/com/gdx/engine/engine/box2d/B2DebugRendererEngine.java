package com.gdx.engine.engine.box2d;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.service.Box2DWorldServiceImpl;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.ScreenServiceImpl;
import com.gdx.engine.service.ServiceFactoryImpl;

public class B2DebugRendererEngine extends EntitySystem {
    private static ScreenServiceImpl screenService;
    private static Box2DWorldServiceImpl box2DService;
    private static ConfigServiceImpl configService;

    private Camera camera;
    private World world;
    private Box2DDebugRenderer renderer;

    private boolean isRendering;

    public B2DebugRendererEngine() {
        screenService = ServiceFactoryImpl.getScreenService();
        box2DService = ServiceFactoryImpl.getBox2DWorldService();
        configService = ServiceFactoryImpl.getConfigService();

        setUp();
    }

    private void setUp() {
        camera = screenService.getCamera();
        world = box2DService.getWorld();
        renderer = new Box2DDebugRenderer();
        isRendering = configService.getBox2DConfig().isBox2DDebugRendering();

        if (configService.getBox2DConfig().isBox2DDebugRendering()) {
            this.setProcessing(true);
        }
    }

    @Override
    public void update(float delta) {
        if (isRendering) {
            renderer.render(world, camera.combined);
        }
    }

}