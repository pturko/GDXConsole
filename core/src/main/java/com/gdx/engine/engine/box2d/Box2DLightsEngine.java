package com.gdx.engine.engine.box2d;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.event.*;
import com.gdx.engine.model.config.Box2DLightsConfig;
import com.gdx.engine.service.*;
import com.gdx.game.util.TiledObjectUtils;

public class Box2DLightsEngine extends EntitySystem {
    private static final String LIGHT_SOURCE_MAP_LAYER = "LIGHT_SOURCE";

    private final Camera camera;
    private final World world;
    private final RayHandler rayHandler;
    private boolean isRendering;
    private float ambientLight;

    public Box2DLightsEngine() {
        world = ServiceFactoryImpl.getBox2DWorldService().getWorld();
        camera = ServiceFactoryImpl.getScreenService().getCamera();
        rayHandler = new RayHandler(world);

        updateConfig();
        configureListeners();
    }

    private void updateConfig() {
        Box2DLightsConfig box2DLightsConfig = ServiceFactoryImpl.getConfigService().getBox2DConfig().getBox2DLightsConfig();
        isRendering = box2DLightsConfig.isRendering();
        ambientLight = box2DLightsConfig.getAmbientLight();
    }

    private void configureListeners() {
        // Event reload application config
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_BOX2D_CHANGED, (ConfigBox2DChangedEvent e) ->
                updateConfig());

        // Whenever the map is changed, remove previous light objects and update brightness
        ServiceFactoryImpl.getEventService().addEventListener(EventType.MAP_CHANGED, (MapChangedEvent e) -> {
            rayHandler.removeAll();
            rayHandler.setAmbientLight(ambientLight);
            if (e.getTiledMap().getLayers().get(LIGHT_SOURCE_MAP_LAYER) != null) {
                TiledObjectUtils.createLightSources(rayHandler,
                        e.getTiledMap().getLayers().get(LIGHT_SOURCE_MAP_LAYER).getObjects());
            }
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