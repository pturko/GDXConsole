package com.gdx.engine.engine.camera;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.gdx.engine.model.map.TiledMapData;
import com.gdx.engine.event.EventType;
import com.gdx.engine.event.MapDataChangedEvent;
import com.gdx.engine.service.*;
import com.gdx.engine.util.CameraUtils;

public class CameraEngine extends EntitySystem {
    private Camera camera;
    private TiledMapData tiledMapData;
    private boolean isRendering;

    public CameraEngine() {
        update();
    }

    private void update() {
        camera = ServiceFactoryImpl.getScreenService().getCamera();
        tiledMapData = ServiceFactoryImpl.getTiledMapService().getMapData();
        isRendering = ServiceFactoryImpl.getConfigService().getBox2DConfig().isRendering();

        ServiceFactoryImpl.getEventService().addEventListener(EventType.MAP_DATA_CHANGED, (MapDataChangedEvent e) ->
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