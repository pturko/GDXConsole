package com.gdx.engine.engine.box2d;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.event.EventType;
import com.gdx.engine.event.MapChangedEvent;
import com.gdx.engine.service.*;
import com.gdx.game.util.TiledObjectUtils;

public class B2LightsEngine extends EntitySystem {
    private static Box2DWorldServiceImpl box2DService;
    private static ConfigServiceImpl configService;
    private static EventServiceImpl eventService;
    private static ScreenServiceImpl screenService;

    private static final String LIGHT_SOURCE_MAP_LAYER = "LIGHT_SOURCE";

    private Camera camera;
    private World world;
    private RayHandler rayHandler;
    private boolean isRendering;
    private float ambientLight;

    public B2LightsEngine() {
        box2DService = ServiceFactoryImpl.getBox2DWorldService();
        configService = ServiceFactoryImpl.getConfigService();
        eventService = ServiceFactoryImpl.getEventService();
        screenService = ServiceFactoryImpl.getScreenService();

        setUp();
    }

    private void setUp() {
        world = box2DService.getWorld();
        rayHandler = new RayHandler(world);

        camera = screenService.getCamera();
        isRendering = configService.getBox2DConfig().getBox2DLightsConfig().isRendering();
        ambientLight = configService.getBox2DConfig().getBox2DLightsConfig().getAmbientLight();

        // Whenever the map is changed, remove previous light objects and update brightness
        eventService.addEventListener(EventType.MAP_CHANGED, (MapChangedEvent e) -> {
            rayHandler.removeAll();
            rayHandler.setAmbientLight(ambientLight);

            TiledObjectUtils.createLightSources(rayHandler,
                    e.getTiledMap().getLayers().get(LIGHT_SOURCE_MAP_LAYER).getObjects());
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