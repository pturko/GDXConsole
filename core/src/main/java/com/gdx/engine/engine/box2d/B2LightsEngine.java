package com.gdx.engine.engine.box2d;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.event.EventType;
import com.gdx.engine.event.MapChangedEvent;
import com.gdx.engine.model.config.WindowConfig;
import com.gdx.engine.service.Box2DWorldImpl;
import com.gdx.engine.service.CameraServiceImpl;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.EventServiceImpl;
import com.gdx.engine.util.box2d.TiledObjectUtils;
import com.gdx.game.map.MapLayerType;

public class B2LightsEngine extends EntitySystem {
    private static CameraServiceImpl cameraService;
    private static Box2DWorldImpl box2DService;
    private static ConfigServiceImpl configService;
    private static EventServiceImpl eventService;

    private static float ppm;

    private final Camera camera;
    private final World world;
    private final RayHandler rayHandler;

    private final boolean isRendering;
    private final float ambientLight;

    public B2LightsEngine() {
        cameraService = CameraServiceImpl.getInstance();
        box2DService = Box2DWorldImpl.getInstance();
        configService = ConfigServiceImpl.getInstance();
        eventService = EventServiceImpl.getInstance();

        camera = cameraService.getCamera();
        world = box2DService.getWorld();

        rayHandler = new RayHandler(world);

        WindowConfig windowConfig = configService.getWindowConfig();
        ppm = windowConfig.getPpm();

        isRendering = configService.getBox2DConfig().getBox2DLightsConfig().isRendering();
        ambientLight = configService.getBox2DConfig().getBox2DLightsConfig().getAmbientLight();

        // Whenever the map is changed, remove previous light objects and update brightness
        eventService.addEventListener(EventType.MAP_CHANGED, (MapChangedEvent e) -> {
            rayHandler.removeAll();
            rayHandler.setAmbientLight(ambientLight);

            TiledObjectUtils.createLightSources(rayHandler, e.getTiledMap().getLayers(), MapLayerType.LIGHT_SOURCE);
        });
    }

    @Override
    public void update(float delta) {
        // Render box2d lights
        if (isRendering) {
            rayHandler.update();
            rayHandler.setCombinedMatrix(camera.combined);
            rayHandler.render();
        }
    }

}