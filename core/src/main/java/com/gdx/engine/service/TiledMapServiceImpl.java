package com.gdx.engine.service;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gdx.engine.model.map.TiledMapLayerData;
import com.gdx.engine.model.map.TiledMapData;
import com.gdx.engine.event.MapChangedEvent;
import com.gdx.engine.interfaces.service.TiledMapService;
import com.gdx.engine.util.FileLoaderUtil;
import com.gdx.game.util.TiledEntityBuilderUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
public class TiledMapServiceImpl implements TiledMapService {
    private static TiledMapServiceImpl tiledMapServiceInstance;
    private static TmxMapLoader tmxMapLoader;

    private static final String ASSET = "asset/";
    private static final String MAPS = "map/";
    private static final String MAP_FILE_EXT = ".tmx";

    private final PooledEngine engine;
    private final World world;
    private TiledMapData tiledMapData;

    public TiledMapServiceImpl() {
        world = ServiceFactoryImpl.getBox2DWorldService().getWorld();
        engine = ServiceFactoryImpl.getPooledEngineService().getEngine();

        tiledMapData = new TiledMapData();
        if (ServiceFactoryImpl.getConfigService().getAssetConfig().isExternalFiles()) {
            tmxMapLoader = new TmxMapLoader(new ExternalFileHandleResolver());
        } else {
            tmxMapLoader = new TmxMapLoader(new InternalFileHandleResolver());
        }
    }

    public static synchronized TiledMapServiceImpl getInstance() {
        if (tiledMapServiceInstance == null)
            tiledMapServiceInstance = new TiledMapServiceImpl();
        return tiledMapServiceInstance;
    }

    public void load(String mapName) {
        int tileWidth = 0;
        int tileHeight;

        clear();

        tiledMapData = new TiledMapData();
        tiledMapData.setName(mapName);
        tiledMapData.setLoaded(false);
        tiledMapData.setHasLayers(false);
        tiledMapData.setTiledMap(loadMap(mapName));

        // Map validations and extracting properties
        if (tiledMapData.getTiledMap() != null) {
            if (tiledMapData.getTiledMap().getLayers().size() > 0) {
                tiledMapData.setHasLayers(true);
            } else {
                log.warn("Map has no detected layers!");
            }

            // Extract map properties
            tiledMapData.setMapWidth(tiledMapData.getTiledMap().getProperties().get("width", Integer.class));
            tiledMapData.setMapHeight(tiledMapData.getTiledMap().getProperties().get("height", Integer.class));
            tileWidth = tiledMapData.getTiledMap().getProperties().get("tilewidth", Integer.class);
            tileHeight = tiledMapData.getTiledMap().getProperties().get("tileheight", Integer.class);
            assert tileWidth == tileHeight;

            // Show TiledMap details
            showTiledMapInfo();

            // Collect layer information
            if (tiledMapData.isHasLayers()) {
                setMapLayerData(tiledMapData.getTiledMap().getLayers());
            }
        }

        // Create bodies in the world according to each map layer
        TiledEntityBuilderUtils.parseLayers(tiledMapData.getMapLayersData());

        // Set up tiled map data
        tiledMapData.setMapTileSize(tileWidth);
        tiledMapData.setLoaded(true);
        tiledMapData.setPpm(ServiceFactoryImpl.getConfigService().getBox2DConfig().getPpm());

        // Send map event
        ServiceFactoryImpl.getEventService().sendEvent(new MapChangedEvent(tiledMapData.getTiledMap()));

        log.info("TiledMap '{}' loaded", mapName);
    }

    private void showTiledMapInfo() {
        log.info("TiledMap: [{}, size: {}]", tiledMapData.getName(), tiledMapData.getMapWidth() + "x" + tiledMapData.getMapHeight());

        MapProperties mapProperties = tiledMapData.getTiledMap().getProperties();
        log.debug(" - Orientation: {}", mapProperties.get("orientation"));
        log.debug(" - HexSideLength: {}", mapProperties.get("hexsidelength"));
        log.debug(" - Size: {}", mapProperties.get("width") + "x" + mapProperties.get("height"));
        log.debug(" - TileSize: {}", mapProperties.get("tilewidth") + "x" + mapProperties.get("tileheight"));
        log.debug(" - Orthogonal: {}", mapProperties.get("orthogonal"));
    }

    private void setMapLayerData(MapLayers mapLayers) {
        Map<String, TiledMapLayerData> tiledMapLayerData = new HashMap<>();

        mapLayers.forEach(layer -> {
            String name = layer.getName();
            MapProperties properties = layer.getProperties();

            boolean staticBody = Optional.ofNullable(properties.get("staticBody", Boolean.class)).orElse(true);
            String textureAtlasName = Optional.ofNullable((String)properties.get("textureAtlasName")).orElse(StringUtils.EMPTY);
            String textureName = Optional.ofNullable((String)properties.get("textureName")).orElse(StringUtils.EMPTY);
            String categoryBits = Optional.ofNullable((String)properties.get("categoryBits")).orElse(StringUtils.EMPTY);
            String maskBits = Optional.ofNullable((String)properties.get("maskBits")).orElse(StringUtils.EMPTY);
            float friction = Optional.ofNullable(properties.get("friction", Float.class)).orElse(0f);
            boolean sensor = Optional.ofNullable(properties.get("sensor", Boolean.class)).orElse(false);
            String animationConfig = Optional.ofNullable((String)properties.get("animationConfig")).orElse(StringUtils.EMPTY);

            log.info("Layer: '{}' (isStatic: {})", name, staticBody);
            log.debug(" - collision: [{},{},{},{}]", categoryBits, maskBits, friction, sensor);
            log.debug(" - texture: [{},{},{}]", textureAtlasName, textureName, animationConfig);

            TiledMapLayerData mapLayerData = new TiledMapLayerData();
            mapLayerData.setVisible(layer.isVisible());
            mapLayerData.setStaticBody(staticBody);
            mapLayerData.setTextureAtlasName(textureAtlasName);
            mapLayerData.setTextureName(textureName);
            mapLayerData.setCategoryBits(categoryBits);
            mapLayerData.setMaskBits(maskBits);
            mapLayerData.setFriction(friction);
            mapLayerData.setSensor(sensor);
            mapLayerData.setEntities(layer.getObjects());
            if (!animationConfig.equals(StringUtils.EMPTY)) {
                mapLayerData.setSpriteAnimation(FileLoaderUtil.getSpriteAnimation(animationConfig));
            }

            tiledMapLayerData.put(layer.getName(), mapLayerData);
        });

        tiledMapData.setMapLayersData(tiledMapLayerData);
    }

    private TiledMap loadMap(String mapName) {
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
        if (tiledMapData.getTiledMap() != null) {
            tiledMapData.getTiledMap().dispose();

            // Destroy all entity and bodies
            engine.getEntities().forEach(engine::removeEntity);

            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);
            IntStream.range(0, bodies.size).forEach(i ->
                    world.destroyBody(bodies.get(i)));

            // Destroy entities
            engine.getEntities().forEach(engine::removeEntity);

            TiledEntityBuilderUtils.clearLights();
        }
        tiledMapData = null;
    }



    @Override
    public TiledMap getMap() {
        return tiledMapData.getTiledMap();
    }

    @Override
    public MapLayers getMapLayers() {
        return tiledMapData.getTiledMap().getLayers();
    }

    @Override
    public TiledMapData getMapData() {
        return tiledMapData;
    }

    @Override
    public String getMapName() {
        return tiledMapData.getName();
    }

    public boolean isMapLoaded() {
        return tiledMapData.isLoaded();
    }

    public void dispose() {
        tiledMapData.getTiledMap().dispose();
    }

}
