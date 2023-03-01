package com.gdx.engine.engine.camera;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.gdx.engine.model.map.TiledMapData;
import com.gdx.engine.event.EventType;
import com.gdx.engine.event.MapDataChangedEvent;
import com.gdx.engine.service.*;
import com.gdx.engine.util.CameraUtils;

public class CameraEngine extends EntitySystem {
    private static ScreenServiceImpl screenService;
    private static ConfigServiceImpl configService;
    private static EventServiceImpl eventService;

    private Camera camera;
    private TiledMapData tiledMapData;
    private boolean isRendering;

    public CameraEngine() {
        screenService = ServiceFactoryImpl.getScreenService();
        eventService = ServiceFactoryImpl.getEventService();
        configService = ServiceFactoryImpl.getConfigService();

        setUp();
    }

    private void setUp() {
        camera = screenService.getCamera();
        tiledMapData = ServiceFactoryImpl.getTiledMapService().getMapData();
        isRendering = configService.getBox2DConfig().isRendering();

        eventService.addEventListener(EventType.MAP_DATA_CHANGED, (MapDataChangedEvent e) ->
                this.tiledMapData = e.getTiledMapData());
    }

    @Override
    public void update(float delta) {
        // Make sure to bound the camera within the TiledMap.
        if (isRendering && tiledMapData != null) {
            CameraUtils.boundCamera(camera, tiledMapData);
        }
    }

}