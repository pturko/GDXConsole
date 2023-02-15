package com.gdx.engine.engine.tlledmap;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.gdx.engine.event.EventType;
import com.gdx.engine.event.MapChangedEvent;
import com.gdx.engine.service.CameraServiceImpl;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.EventServiceImpl;
import com.gdx.engine.service.TiledMapServiceImpl;

public class TiledMapEngine extends EntitySystem {

    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer renderer;
    private final ConfigServiceImpl configService;
    private final TiledMapServiceImpl tiledMapService;
    private final EventServiceImpl eventServiceImpl;

    public TiledMapEngine() {
        configService = ConfigServiceImpl.getInstance();
        tiledMapService = TiledMapServiceImpl.getInstance();
        eventServiceImpl = EventServiceImpl.getInstance();

        this.camera = CameraServiceImpl.getInstance().getCamera();
        this.renderer = new OrthogonalTiledMapRenderer(null, 1 /
                configService.getWindowConfig().getCameraConfig().getPpm());

        // Whenever the map has been changed, set the OrthogonalTiledMapRenderer to render our new map.
        eventServiceImpl.addEventListener(EventType.MAP_CHANGED, (MapChangedEvent e) -> {
            renderer.setMap(e.getTiledMap());
        });
    }

    @Override
    public void update(float delta) {
        // Draw only what camera can see, and render the game map
        if (tiledMapService.isMapLoaded()) {
            renderer.setView(camera);
            renderer.render();
        }
    }

}