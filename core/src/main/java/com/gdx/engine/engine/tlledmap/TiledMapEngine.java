package com.gdx.engine.engine.tlledmap;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.event.MapChangedEvent;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.EventServiceImpl;
import com.gdx.engine.service.ScreenServiceImpl;
import com.gdx.engine.service.TiledMapServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TiledMapEngine extends EntitySystem {
    private boolean isRendering;
    private OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer renderer;

    private final ConfigServiceImpl configService;
    private final TiledMapServiceImpl tiledMapService;
    private final ScreenServiceImpl screenService;
    private final EventServiceImpl eventService;

    public TiledMapEngine() {
        configService = ConfigServiceImpl.getInstance();
        tiledMapService = TiledMapServiceImpl.getInstance();
        eventService = EventServiceImpl.getInstance();
        screenService = ScreenServiceImpl.getInstance();

        isRendering = configService.getTiledMapConfig().isRendering();
        camera = screenService.getCamera();
        renderer = new OrthogonalTiledMapRenderer(null, 1 /
                configService.getBox2DConfig().getPpm());

        // Config changed event
        eventService.addEventListener(EventType.CONFIG_CHANGED, (ConfigChangedEvent e) -> {
            camera = screenService.getCamera();
            isRendering = configService.getTiledMapConfig().isRendering();
        });

        // Whenever the map has been changed, set the OrthogonalTiledMapRenderer to render our new map
        eventService.addEventListener(EventType.MAP_CHANGED, (MapChangedEvent e) -> {
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