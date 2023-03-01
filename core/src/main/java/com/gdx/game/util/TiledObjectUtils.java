package com.gdx.game.util;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.util.box2d.BodyBuilder;
import com.gdx.engine.util.box2d.LightBuilder;
import com.gdx.game.entity.animated.Torch;
import com.gdx.game.entity.dynamic.BoxEntity;
import com.gdx.engine.model.config.Box2DConfig;
import com.gdx.engine.model.map.MapEntityData;
import com.gdx.engine.service.AssetServiceImpl;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.PooledEngineServiceImpl;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.gdx.engine.util.FileLoaderUtil;
import com.gdx.game.map.CategoryBits;
import com.gdx.game.map.MapLayerType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TiledObjectUtils {
    private static ConfigServiceImpl configService;
    private static AssetServiceImpl assetService;
    private static PooledEngineServiceImpl pooledEngineService;

    private static boolean GROUND_COLLISION = true;
    private static boolean BOX_COLLISION = true;
    private static float ppm;

    private static PooledEngine engine;

    private final static List<PointLight> pointLights = new ArrayList<>();

    public static void parseLayers(World world, MapLayers mapLayers) {
        configService = ServiceFactoryImpl.getConfigService();
        assetService = ServiceFactoryImpl.getAssetService();
        pooledEngineService = ServiceFactoryImpl.getPooledEngineService();

        BodyBuilder bodyBuilder = new BodyBuilder(world);

        Box2DConfig box2DConfig = configService.getBox2DConfig();
        engine = pooledEngineService.getEngine();
        ppm = box2DConfig.getPpm();

        if (!box2DConfig.isEnableContacts()) {
            GROUND_COLLISION = false;
            BOX_COLLISION = false;
        }

        for (MapLayer layer : mapLayers) {
            MapObjects mapObjects = layer.getObjects();
            String name = layer.getName();
            boolean isVisible = layer.isVisible();

            log.debug("Parsing layer: {}, [isVisible:{}, {}]", name, isVisible, mapObjects.getCount());
            if (isVisible) {
                String layerType = null; // = (String) layer.getProperties().get(MAP_PROPERTY_TYPE);
                //if (layerType == null) {
                    //log.debug("Reading layer properties");
                    MapEntityData mapEntity = loadLayerConfig(name.toLowerCase());
                    if (mapEntity != null) {
                        //log.debug("Layer properties not found. Reading layer configuration from file");
                        layerType = mapEntity.getType();
                    }
                //}

                MapLayerType mapType = null;
                if (layerType != null) {
                    try {
                        mapType = MapLayerType.valueOf(layerType);
                    } catch (Exception e) {
                        log.error("Layer '{}' not found", layerType);
                    }

                    switch (mapType) {
                        case GROUND:
                            createStaticEntities(bodyBuilder, mapObjects, CategoryBits.GROUND, GROUND_COLLISION, 2);
                            break;
                        case BOX:
                            createBoxStaticEntity("box", mapObjects, mapEntity);
                            break;
                        case TORCH:
                            createTorchAnimationEntity("torch", mapObjects, mapEntity);
                            break;
                    }
                }
            } else {
                log.debug("Skipping map layer: {}", name);
            }
        }
    }

    private static void createStaticEntities(BodyBuilder bodyBuilder, MapObjects mapObjects,
                                              short categoryBits, boolean collision, float friction) {
        log.debug("[rectangles: {}, polyLines: {}]",
                mapObjects.getByType(RectangleMapObject.class).size,
                mapObjects.getByType(PolylineMapObject.class).size
        );
        for (RectangleMapObject object : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            Vector2 centerPos = new Vector2(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            bodyBuilder.type(BodyDef.BodyType.StaticBody)
                    .position(centerPos, ppm)
                    .buildBody();

            bodyBuilder.newRectangleFixture(centerPos, rect.getWidth() / 2, rect.getHeight() / 2, ppm)
                    .categoryBits(categoryBits)
                    .friction(friction)
                    .setSensor(!collision)
                    .buildFixture();
        }

        for (PolylineMapObject object : mapObjects.getByType(PolylineMapObject.class)) {
            float[] vertices = object.getPolyline().getTransformedVertices();
            Vector2[] worldVertices = new Vector2[vertices.length / 2];

            for (int i = 0; i < worldVertices.length; i++) {
                worldVertices[i] = new Vector2(vertices[i * 2], vertices[i * 2 + 1]);
            }

            bodyBuilder.type(BodyDef.BodyType.StaticBody)
                    .position(0, 0, ppm)
                    .buildBody();

            bodyBuilder.newPolylineFixture(worldVertices, ppm)
                    .categoryBits(categoryBits)
                    .friction(friction)
                    .setSensor(!collision)
                    .buildFixture();
        }
    }

    private static void createBoxStaticEntity(String textureName, MapObjects mapObjects, MapEntityData mapEntity) {
        for (RectangleMapObject object : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            engine.addEntity(new BoxEntity(textureName, mapEntity,
                        (rect.getX() + rect.getWidth() / 2) / ppm,
                        (rect.getY() + rect.getHeight() / 2) / ppm,
                        rect.getWidth(),
                        rect.getHeight()
                ));
        }
    }

    private static void createTorchAnimationEntity(String textureName, MapObjects mapObjects, MapEntityData mapEntity) {
        for (RectangleMapObject object : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            engine.addEntity(new Torch(textureName, mapEntity,
                    (rect.getX() + rect.getWidth() / 2) / ppm,
                    (rect.getY() + rect.getHeight() / 2) / ppm,
                    rect.getWidth(),
                    rect.getHeight()
            ));
        }
    }

    public static void createLightSources(RayHandler rayHandler, MapObjects mapObjects) {
        int distance = configService.getBox2DConfig().getBox2DLightsConfig().getDistance();
        for (RectangleMapObject o : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = o.getRectangle();
            float x = rect.getX() + rect.getWidth() / 2;
            float y = rect.getY() + rect.getHeight() / 2;
            PointLight pointLight = LightBuilder.createPointLight(rayHandler, Color.ORANGE, distance, x, y);
            pointLights.add(pointLight);
        }
//        LightBuilder.createConeLight(rayHandler, Color.ORANGE, 200, x, y, -20, 30);
    }

    public static MapEntityData loadLayerConfig(String name) {
        MapEntityData mapEntity = null;
        try {
            mapEntity = FileLoaderUtil.getMapEntity(assetService.getLayerConfigPath(name));
        } catch (Exception e) {
            log.error("Layer config file not found: {}", name + ".json");
        }
        return mapEntity;
    }

    public static void clearLights() {
    }

}