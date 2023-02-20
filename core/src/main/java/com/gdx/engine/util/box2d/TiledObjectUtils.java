package com.gdx.engine.util.box2d;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.game.map.CollisionMaskBits;
import com.gdx.game.map.MapLayerType;

public class TiledObjectUtils {

    public static void parseLayers(World world, MapLayers mapLayers) {
        createStaticRectangles(world, mapLayers, MapLayerType.GROUND, CollisionMaskBits.GROUND, false, 2);
        createStaticRectangles(world, mapLayers, MapLayerType.BOX, CollisionMaskBits.BOX, false, 2);
    }

    private static void createStaticRectangles(World world, MapLayers mapLayers, MapLayerType mapType,
                                         short categoryBits, boolean collidable, float friction) {
        BodyBuilder bodyBuilder = new BodyBuilder(world);
        MapObjects mapObjects = mapLayers.get(mapType.ordinal()).getObjects();

        for (RectangleMapObject object : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            Vector2 centerPos = new Vector2(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            bodyBuilder.type(BodyDef.BodyType.StaticBody)
                    .position(centerPos, 100)
                    .buildBody();

            bodyBuilder.newRectangleFixture(centerPos, rect.getWidth() / 2, rect.getHeight() / 2, 100)
                    .categoryBits(categoryBits)
                    .friction(friction)
                    .setSensor(!collidable)
                    .buildFixture();
        }
    }

    private static void createStaticPolyLines(World world, MapLayers mapLayers, MapLayerType mapType,
                                        short categoryBits, boolean collidable, float friction) {
        BodyBuilder bodyBuilder = new BodyBuilder(world);
        MapObjects mapObjects = mapLayers.get(mapType.ordinal()).getObjects();

        for (PolylineMapObject object : mapObjects.getByType(PolylineMapObject.class)) {
            float[] vertices = object.getPolyline().getTransformedVertices();
            Vector2[] worldVertices = new Vector2[vertices.length / 2];

            for (int i = 0; i < worldVertices.length; i++) {
                worldVertices[i] = new Vector2(vertices[i * 2], vertices[i * 2 + 1]);
            }

            bodyBuilder.type(BodyDef.BodyType.StaticBody)
                    .position(0, 0, 100)
                    .buildBody();

            bodyBuilder.newPolylineFixture(worldVertices, 100)
                    .categoryBits(categoryBits)
                    .friction(friction)
                    .setSensor(!collidable)
                    .buildFixture();
        }
    }

}