package com.gdx.game.util;

import box2dLight.ConeLight;
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
import com.gdx.engine.service.ServiceFactoryImpl;
import com.gdx.engine.util.FileLoaderUtil;
import com.gdx.game.map.CategoryBits;
import com.gdx.game.map.MapLayerType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TiledObjectUtils {
    private static PooledEngine engine;
    private static final boolean GROUND_COLLISION = true;
    private static float ppm;

    private final static List<PointLight> pointLights = new ArrayList<>();
    private final static List<ConeLight> coneLights = new ArrayList<>();

    public static void parseLayers(World world, MapLayers mapLayers) {
        BodyBuilder bodyBuilder = new BodyBuilder(world);

        Box2DConfig box2DConfig = ServiceFactoryImpl.getConfigService().getBox2DConfig();
        engine = ServiceFactoryImpl.getPooledEngineService().getEngine();
        ppm = box2DConfig.getPpm();

        for (MapLayer layer : mapLayers) {
            MapObjects mapObjects = layer.getObjects();
            String name = layer.getName();
            boolean isVisible = layer.isVisible();

            log.debug("Parsing layer: {}, [v: {}, c: {}]", name, isVisible, mapObjects.getCount());
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

                    if (mapType != null) {
                        switch (mapType) {
                            case GROUND:
                                createStaticEntities(bodyBuilder, mapObjects, CategoryBits.GROUND, GROUND_COLLISION, 2);
                                break;
                            case BOX:
                                createBoxStaticEntity(mapObjects, mapEntity);
                                break;
                            case TORCH:
                                createTorchAnimationEntity(mapObjects, mapEntity);
                                break;
                        }
                    } else {
                        log.warn("Map layer '{}' not found!", layerType);
                    }
                }
            } else {
                log.debug("Skipping map layer: {}", name);
            }
        }
    }

    private static void createStaticEntities(BodyBuilder bodyBuilder, MapObjects mapObjects,
                                              short categoryBits, boolean collision, float friction) {
        log.debug("Processing: [r: {}, p: {}]",
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

    private static void createBoxStaticEntity(MapObjects mapObjects, MapEntityData mapEntity) {
        for (RectangleMapObject object : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            engine.addEntity(new BoxEntity(mapEntity,
                        (rect.getX() + rect.getWidth() / 2) / ppm,
                        (rect.getY() + rect.getHeight() / 2) / ppm,
                        rect.getWidth(),
                        rect.getHeight()
                ));
        }
    }

    private static void createTorchAnimationEntity(MapObjects mapObjects, MapEntityData mapEntity) {
        for (RectangleMapObject object : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            engine.addEntity(new Torch(mapEntity,
                    (rect.getX() + rect.getWidth() / 2) / ppm,
                    (rect.getY() + rect.getHeight() / 2) / ppm,
                    rect.getWidth(),
                    rect.getHeight()
            ));
        }
    }

    public static void createLightSources(RayHandler rayHandler, MapObjects mapObjects) {
        Box2DConfig box2DConfig = ServiceFactoryImpl.getConfigService().getBox2DConfig();
        int distance = box2DConfig.getBox2DLightsConfig().getLightDistance();
        for (RectangleMapObject o : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = o.getRectangle();
            float x = rect.getX() + rect.getWidth() / 2;
            float y = rect.getY() + rect.getHeight() / 2;
            pointLights.add(LightBuilder.createPointLight(rayHandler, Color.ORANGE, distance, x, y));
        }
    }

    public static MapEntityData loadLayerConfig(String name) {
        MapEntityData mapEntity = null;
        try {
            mapEntity = FileLoaderUtil.getMapEntity(ServiceFactoryImpl.getAssetService().getLayerConfigPath(name));
        } catch (Exception e) {
            log.error("Layer config file not found: {}", name + ".json");
        }
        return mapEntity;
    }

    public static void clearLights() {
        pointLights.clear();
        coneLights.clear();
    }

}