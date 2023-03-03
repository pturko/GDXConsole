package com.gdx.engine.engine.tlledmap;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.event.MapChangedEvent;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.service.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TiledMapEngine extends EntitySystem {
    private final OrthogonalTiledMapRenderer renderer;
    private final OrthographicCamera camera;
    private boolean isRendering;
    private boolean isMapLoaded;

    public TiledMapEngine() {
        renderer = new OrthogonalTiledMapRenderer(null, 1 /
                ServiceFactoryImpl.getConfigService().getBox2DConfig().getPpm());
        camera = ServiceFactoryImpl.getScreenService().getCamera();

        update(ServiceFactoryImpl.getConfigService().getApplicationConfig());
        configureListeners();
    }

    private void update(ApplicationConfig config) {
        isRendering = config.getTiledMapConfig().isRendering();

    }

    private void configureListeners() {
        // Config changed event
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_CHANGED, (ConfigChangedEvent e) ->
                update(e.getApplicationConfig()));

        // Whenever the map has been changed, set the OrthogonalTiledMapRenderer to render our new map
        ServiceFactoryImpl.getEventService().addEventListener(EventType.MAP_CHANGED, (MapChangedEvent e) -> {
                renderer.setMap(e.getTiledMap());
                isMapLoaded = ServiceFactoryImpl.getTiledMapService().isMapLoaded();
            }
        );
    }

    @Override
    public void update(float delta) {
        // Draw only what camera can see, and render the game map
        if (isRendering && isMapLoaded) {
            renderer.setView(camera);
            renderer.render();
        }
    }

}