package com.gdx.engine.util.box2d;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.box2d.entity.animated.Torch;
import com.gdx.engine.box2d.entity.sprite.BoxEntity;
import com.gdx.engine.model.config.Box2DConfig;
import com.gdx.engine.model.config.WindowConfig;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.PooledEngineServiceImpl;
import com.gdx.game.map.CategoryBits;
import com.gdx.game.map.MapLayerType;

public class TiledObjectUtils {
    private static ConfigServiceImpl configService;
    private static PooledEngineServiceImpl pooledEngineService;

    private static boolean GROUND_COLLISION = true;
    private static boolean BOX_COLLISION = true;

    private static PooledEngine engine;

    private static final int FRICTION = 2;
    private static float ppm;

    public static void parseLayers(World world, MapLayers mapLayers) {
        configService = ConfigServiceImpl.getInstance();
        pooledEngineService = PooledEngineServiceImpl.getInstance();

        Box2DConfig box2DConfig = configService.getBox2DConfig();
        WindowConfig windowConfig = configService.getWindowConfig();
        engine = pooledEngineService.getEngine();
        ppm = windowConfig.getPpm();

        if (!box2DConfig.isEnableContacts()) {
            GROUND_COLLISION = false;
            BOX_COLLISION = false;
        }
        createStaticRectangles(world, ppm, mapLayers, MapLayerType.WALLS, CategoryBits.WALL, GROUND_COLLISION, FRICTION);
        createDynamicRectangles(mapLayers, MapLayerType.BOX, ppm);
        createAnimatedRectangles(mapLayers, MapLayerType.TORCH, ppm);
    }

    private static void createDynamicRectangles(MapLayers mapLayers, MapLayerType mapType, float ppm) {
        MapObjects mapObjects = mapLayers.get(mapType.ordinal()).getObjects();
        for (RectangleMapObject object : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            engine.addEntity(new BoxEntity("box",
                        (rect.getX() + rect.getWidth() / 2) / ppm,
                        (rect.getY() + rect.getHeight() / 2) / ppm,
                        rect.getWidth(),
                        rect.getHeight()
                ));
        }
    }

    private static void createAnimatedRectangles(MapLayers mapLayers, MapLayerType mapType, float ppm) {
        MapObjects mapObjects = mapLayers.get(mapType.ordinal()).getObjects();
        for (RectangleMapObject object : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            //Vector2 centerPos = new Vector2(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
            engine.addEntity(new Torch("torch",
                    (rect.getX() + rect.getWidth() / 2) / ppm,
                    (rect.getY() + rect.getHeight() / 2) / ppm,
                    rect.getWidth(),
                    rect.getHeight()
            ));
        }
    }

    private static void createStaticRectangles(World world, float ppm, MapLayers mapLayers, MapLayerType mapType,
                                         short categoryBits, boolean collision, float friction) {
        BodyBuilder bodyBuilder = new BodyBuilder(world);
        MapObjects mapObjects = mapLayers.get(mapType.ordinal()).getObjects();

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
    }

    private static void createStaticPolyLines(World world, float ppm, MapLayers mapLayers, MapLayerType mapType,
                                        short categoryBits, boolean collision, float friction) {
        BodyBuilder bodyBuilder = new BodyBuilder(world);
        MapObjects mapObjects = mapLayers.get(mapType.ordinal()).getObjects();

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

    public static void createLightSources(RayHandler rayHandler, MapLayers mapLayers, MapLayerType mapType) {
        MapObjects mapObjects = mapLayers.get(mapType.ordinal()).getObjects();

        for (RectangleMapObject o : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = o.getRectangle();
            float x = rect.getX() + rect.getWidth() / 2;
            float y = rect.getY() + rect.getHeight() / 2;
            LightBuilder.createPointLight(rayHandler, Color.ORANGE, 100, x, y);
        }
    }

}