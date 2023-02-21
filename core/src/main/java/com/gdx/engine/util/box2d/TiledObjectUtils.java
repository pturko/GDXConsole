package com.gdx.engine.util.box2d;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.model.config.Box2DConfig;
import com.gdx.engine.model.config.WindowConfig;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.game.map.CategoryBits;
import com.gdx.game.map.MapLayerType;

public class TiledObjectUtils {
    private static ConfigServiceImpl configService;

    private static boolean GROUND_COLLISION = true;
    private static boolean BOX_COLLISION = true;

    private static final int FRICTION = 2;

    public static void parseLayers(World world, MapLayers mapLayers) {
        configService = ConfigServiceImpl.getInstance();
        Box2DConfig box2DConfig = configService.getBox2DConfig();
        WindowConfig windowConfig = configService.getWindowConfig();
        float ppm = windowConfig.getCameraConfig().getPpm();

        if (!box2DConfig.isEnableContacts()) {
            GROUND_COLLISION = false;
            BOX_COLLISION = false;
        }
        createStaticRectangles(world, ppm, mapLayers, MapLayerType.GROUND, CategoryBits.GROUND, GROUND_COLLISION, FRICTION);
        createStaticRectangles(world, ppm, mapLayers, MapLayerType.BOX, CategoryBits.BOX, BOX_COLLISION, FRICTION);
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

}