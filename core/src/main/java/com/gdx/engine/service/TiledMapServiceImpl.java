package com.gdx.engine.service;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gdx.engine.model.map.TiledMapData;
import com.gdx.engine.event.MapChangedEvent;
import com.gdx.engine.interfaces.service.TiledMapService;
import com.gdx.game.util.TiledObjectUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

@Slf4j
public class TiledMapServiceImpl implements TiledMapService {

    private static final String ASSET = "asset/";
    private static final String MAPS = "map/";
    private static final String MAP_FILE_EXT = ".tmx";

    private static final TmxMapLoader tmxMapLoader = new TmxMapLoader();

    private static TiledMapServiceImpl tiledMapServiceInstance;

    private String mapName;
    private final PooledEngine engine;
    private final TiledMapData tiledMapData;
    private TiledMap tiledMap;
    private MapLayers mapLayers;
    private final World world;

    private static boolean hasLayers;
    private static boolean isMapLoaded;

    public TiledMapServiceImpl() {
        world = ServiceFactoryImpl.getBox2DWorldService().getWorld();
        engine = ServiceFactoryImpl.getPooledEngineService().getEngine();

        tiledMapData = new TiledMapData();
    }

    public static synchronized TiledMapServiceImpl getInstance() {
        if (tiledMapServiceInstance == null)
            tiledMapServiceInstance = new TiledMapServiceImpl();
        return tiledMapServiceInstance;
    }

    public void load(String mapName) {
        clear();
        this.mapName = mapName;
        hasLayers = false;

        tiledMap = mapLoad(mapName);
        isMapLoaded = true;

        if (tiledMap != null) {
            mapLayers = tiledMap.getLayers();
            if (mapLayers.size() > 0) {
                hasLayers = true;
            } else {
                log.warn("Map has no detected layers!");
            }
        }

        // Extract map properties
        tiledMapData.setMapWidth(tiledMap.getProperties().get("width", Integer.class));
        tiledMapData.setMapHeight(tiledMap.getProperties().get("height", Integer.class));
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        assert tileWidth == tileHeight;

        // Create bodies in the world according to each map layer
        TiledObjectUtils.parseLayers(world, mapLayers);

        // Set up tiled map data
        tiledMapData.setMapTileSize(tileWidth);
        tiledMapData.setLoaded(isMapLoaded);
        tiledMapData.setHasLayers(hasLayers);
        tiledMapData.setPpm(ServiceFactoryImpl.getConfigService().getBox2DConfig().getPpm());

        // Send map event
        ServiceFactoryImpl.getEventService().sendEvent(new MapChangedEvent(tiledMap));

        log.info("Tiled map '{}' loaded", mapName);
    }

    private TiledMap mapLoad(String mapName) {
        // TODO - should load external/internal as well
        try {
            return tmxMapLoader.load( ASSET + MAPS + mapName.toLowerCase() + MAP_FILE_EXT);
        } catch(Exception e) {
            log.error("Can't load '{}' map: {}", mapName, e.getMessage());
        }

        return null;
    }

    @Override
    public void clear() {
        engine.clearPools();

        // Dispose map data if there is any
        if (tiledMap != null) {
            tiledMap.dispose();

            // Destroy all entity and bodies
            engine.getEntities().forEach(e ->
                    engine.removeEntity(e));

            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);
            IntStream.range(0, bodies.size).forEach(i ->
                    world.destroyBody(bodies.get(i)));

            // Destroy entities
            engine.getEntities().forEach(e ->
                    engine.removeEntity(e));

            TiledObjectUtils.clearLights();
        }
    }

    @Override
    public TiledMap getMap() {
        return tiledMap;
    }

    @Override
    public MapLayers getMapLayers() {
        return mapLayers;
    }

    @Override
    public TiledMapData getMapData() {
        return tiledMapData;
    }

    @Override
    public String getMapName() {
        return mapName;
    }

    public boolean isMapLoaded() {
        return isMapLoaded;
    }

    public void dispose() {
        tiledMap.dispose();
    }

}
