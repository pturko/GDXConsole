package com.gdx.engine.engine.tlledmap;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.event.MapChangedEvent;
import com.gdx.engine.service.CameraServiceImpl;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.EventServiceImpl;
import com.gdx.engine.service.TiledMapServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TiledMapEngine extends EntitySystem {
    private boolean isRendering;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;

    private final ConfigServiceImpl configService;
    private final TiledMapServiceImpl tiledMapService;
    private final CameraServiceImpl cameraService;
    private final EventServiceImpl eventServiceImpl;

    public TiledMapEngine() {
        configService = ConfigServiceImpl.getInstance();
        tiledMapService = TiledMapServiceImpl.getInstance();
        eventServiceImpl = EventServiceImpl.getInstance();
        cameraService = CameraServiceImpl.getInstance();

        isRendering = configService.getTiledMapConfig().isRendering();
        camera = cameraService.getCamera();
        renderer = new OrthogonalTiledMapRenderer(null, 1 /
                configService.getWindowConfig().getCameraConfig().getPpm());

        // Config changed event
        eventServiceImpl.addEventListener(EventType.CONFIG_CHANGED, (ConfigChangedEvent e) -> {
            camera = cameraService.getCamera();
            isRendering = configService.getTiledMapConfig().isRendering();
        });

        // Whenever the map has been changed, set the OrthogonalTiledMapRenderer to render our new map
        eventServiceImpl.addEventListener(EventType.MAP_CHANGED, (MapChangedEvent e) -> {
            renderer.setMap(e.getTiledMap());
        });
    }

    @Override
    public void update(float delta) {
        // Draw only what camera can see, and render the game map
        if (isRendering && tiledMapService.isMapLoaded()) {
            renderer.setView(camera);
            renderer.render();
        }
    }

}