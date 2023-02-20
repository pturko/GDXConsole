package com.gdx.engine.engine.box2d;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.service.Box2DWorldImpl;
import com.gdx.engine.service.CameraServiceImpl;
import com.gdx.engine.service.ConfigServiceImpl;

public class B2DebugRendererEngine extends EntitySystem {
    private static CameraServiceImpl cameraService;
    private static Box2DWorldImpl box2DService;
    private static ConfigServiceImpl configService;

    private Camera camera;
    private World world;
    private Box2DDebugRenderer renderer;

    private boolean isRendering;

    public B2DebugRendererEngine() {
        cameraService = CameraServiceImpl.getInstance();
        box2DService = Box2DWorldImpl.getInstance();
        configService = ConfigServiceImpl.getInstance();

        this.camera = cameraService.getCamera();
        this.world = box2DService.getWorld();
        renderer = new Box2DDebugRenderer();
        isRendering = configService.getBox2DConfig().isBox2DDebugRenderer();
    }

    @Override
    public void update(float delta) {
        if (isRendering) {
            renderer.render(world, camera.combined);
        }
    }

}