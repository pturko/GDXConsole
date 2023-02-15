package com.gdx.engine.service;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.gdx.engine.engine.tlledmap.TiledMapData;
import com.gdx.engine.event.MapChangedEvent;
import com.gdx.engine.event.MapDataChangedEvent;
import com.gdx.engine.interfaces.service.TiledMapService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TiledMapServiceImpl implements TiledMapService {

    private static final String ASSET = "asset/";
    private static final String MAPS = "map/";
    private static final String MAP_FILE_EXT = ".tmx";

    private static final TmxMapLoader tmxMapLoader = new TmxMapLoader();

    private static TiledMapServiceImpl tiledMapService;
    private static ConfigServiceImpl configService;
    private static EventServiceImpl eventServiceImpl;

    private TiledMapData tiledMapData;
    private TiledMap tiledMap;
    private MapLayers mapLayers;

    private static boolean hasLayers;
    private static boolean isMapLoaded;

    public TiledMapServiceImpl() {
        configService = ConfigServiceImpl.getInstance();
        eventServiceImpl = EventServiceImpl.getInstance();
    }

    public static synchronized TiledMapServiceImpl getInstance() {
        if (tiledMapService == null)
            tiledMapService = new TiledMapServiceImpl();
        return tiledMapService;
    }

    public boolean load(String mapFilePath) {
        tiledMapData = new TiledMapData();
        hasLayers = false;

        tiledMap = getMap(mapFilePath);
        if (tiledMap == null) {
            return isMapLoaded = false;
        }
        isMapLoaded = true;

        mapLayers = tiledMap.getLayers();
        if (mapLayers.size() > 0) {
            hasLayers = true;
        } else {
            log.warn("Map has no detected layers!");
        }

        // Extract map properties
        tiledMapData.setMapWidth(tiledMap.getProperties().get("width", Integer.class));
        tiledMapData.setMapHeight(tiledMap.getProperties().get("height", Integer.class));
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        assert tileWidth == tileHeight;

        // Set up tiled map data
        tiledMapData.setMapTileSize(tileWidth);
        tiledMapData.setLoaded(isMapLoaded);
        tiledMapData.setHasLayers(hasLayers);
        tiledMapData.setPpm(configService.getWindowConfig().getCameraConfig().getPpm());

        // Send map events
        eventServiceImpl.sendEvent(new MapChangedEvent(tiledMap));
        eventServiceImpl.sendEvent(new MapDataChangedEvent(tiledMapData));

        return true;
    }

    private TiledMap getMap(String mapName) {
        try {
            return tmxMapLoader.load( ASSET + MAPS + mapName.toLowerCase() + MAP_FILE_EXT);
        } catch(Exception e) {
            log.error("Can't load '{}' map: {}", mapName, e.getMessage());
        }

        return null;
    }

    @Override
    public TiledMap getMap() {
        return tiledMap;
    }

    @Override
    public TiledMapData getMapData() {
        return tiledMapData;
    }

    public MapLayers getLayers() {
        return mapLayers;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public boolean isMapLoaded() {
        return isMapLoaded;
    }

    protected void dispose() {}

}
