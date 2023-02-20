package com.gdx.engine.engine.camera;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.gdx.engine.model.map.TiledMapData;
import com.gdx.engine.event.EventType;
import com.gdx.engine.event.MapDataChangedEvent;
import com.gdx.engine.service.CameraServiceImpl;
import com.gdx.engine.service.EventServiceImpl;
import com.gdx.engine.service.TiledMapServiceImpl;
import com.gdx.engine.util.CameraUtils;

public class CameraEngine extends EntitySystem {
    private final EventServiceImpl eventService;
    private final Camera camera;
    private TiledMapData tiledMapData;

    public CameraEngine() {
        this.camera = CameraServiceImpl.getInstance().getCamera();
        this.tiledMapData = TiledMapServiceImpl.getInstance().getMapData();
        eventService = EventServiceImpl.getInstance();

        eventService.addEventListener(EventType.MAP_DATA_CHANGED, (MapDataChangedEvent e) -> {
            this.tiledMapData = e.getTiledMapData();
        });
    }


    @Override
    public void update(float delta) {
        // Make sure to bound the camera within the TiledMap.
        if (tiledMapData != null) {
            CameraUtils.boundCamera(camera, tiledMapData);
        }
    }

}