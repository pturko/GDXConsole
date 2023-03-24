package com.gdx.game.util;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.Array;
import com.gdx.engine.component.entity.CircleEntity;
import com.gdx.engine.component.entity.PolylineEntity;
import com.gdx.engine.component.entity.RectangleEntity;
import com.gdx.engine.model.config.Box2DConfig;
import com.gdx.engine.model.map.TiledMapLayerData;
import com.gdx.engine.service.ServiceFactoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class TiledEntityBuilderUtils {
    private static PooledEngine engine;
    private static float ppm;

    private final static List<PointLight> pointLights = new ArrayList<>();
    private final static List<ConeLight> coneLights = new ArrayList<>();

    public static void parseLayers(Map<String, TiledMapLayerData> mapLayersData) {
        Box2DConfig box2DConfig = ServiceFactoryImpl.getConfigService().getBox2DConfig();
        engine = ServiceFactoryImpl.getPooledEngineService().getEngine();
        ppm = box2DConfig.getPpm();

        for (Map.Entry<String, TiledMapLayerData> layer : mapLayersData.entrySet()) {
            String layerName = layer.getKey();
            TiledMapLayerData layerData = layer.getValue();

            if (layerData.isVisible()) {
                createEntities(layerData);
            } else {
                log.warn("Skipping map layer: {} (invisible)", layerName);
            }
        }
    }

    private static void createEntities(TiledMapLayerData layerData) {
        Array<RectangleMapObject> r = layerData.getEntities().getByType(RectangleMapObject.class);
        for (RectangleMapObject entity : r) {
            engine.addEntity(new RectangleEntity(entity, layerData));
        }

        Array<EllipseMapObject> e = layerData.getEntities().getByType(EllipseMapObject.class);
        for (EllipseMapObject entity : e) {
            engine.addEntity(new CircleEntity(entity, layerData));
        }

        Array<PolylineMapObject> p = layerData.getEntities().getByType(PolylineMapObject.class);
        for (PolylineMapObject entity : p) {
            engine.addEntity(new PolylineEntity(entity, layerData));
        }

    }

/*
    private static void createStaticEntities(BodyBuilder bodyBuilder, TiledMapLayerData layerData) {
        // Rectangle entity type
        for (RectangleMapObject object : layerData.getEntities().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            Vector2 centerPos = new Vector2(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            bodyBuilder.type(BodyDef.BodyType.StaticBody)
                    .position(centerPos, ppm)
                    .buildBody();

            bodyBuilder.newRectangleFixture(centerPos, rect.getWidth() / 2, rect.getHeight() / 2, ppm)
                    .categoryBits(Short.parseShort(layerData.getCategoryBits()))
                    .friction(layerData.getFriction())
                    .setSensor(!layerData.isSensor())
                    .buildFixture();
        }

        // Polyline entity type
        for (PolylineMapObject object : layerData.getEntities().getByType(PolylineMapObject.class)) {
            float[] vertices = object.getPolyline().getTransformedVertices();
            Vector2[] worldVertices = new Vector2[vertices.length / 2];

            for (int i = 0; i < worldVertices.length; i++) {
                worldVertices[i] = new Vector2(vertices[i * 2], vertices[i * 2 + 1]);
            }

            bodyBuilder.type(BodyDef.BodyType.StaticBody)
                    .position(0, 0, ppm)
                    .buildBody();

            bodyBuilder.newPolylineFixture(worldVertices, ppm)
                    .categoryBits(Short.parseShort(layerData.getCategoryBits()))
                    .friction(layerData.getFriction())
                    .setSensor(!layerData.isSensor())
                    .buildFixture();
        }

        // Circle entity type from ellipse
        for (EllipseMapObject object : layerData.getEntities().getByType(EllipseMapObject.class)) {
            Ellipse ellipse = object.getEllipse();
            Vector2 centerPos = new Vector2(ellipse.x + ellipse.height / 2, ellipse.y + ellipse.width / 2);

            // Create body
            bodyBuilder.type(BodyDef.BodyType.StaticBody)
                    .position(0, 0, ppm)
                    .buildBody();

            // Create fixture
            bodyBuilder.newCircleFixture(centerPos, (int)ellipse.height, ppm)
                    .categoryBits(Short.parseShort(layerData.getCategoryBits()))
                    .friction(layerData.getFriction())
                    .setSensor(!layerData.isSensor())
                    .buildFixture();
        }
    }

    private static void createDynamicEntity(TiledMapLayerData layerData) {
        for (RectangleMapObject object : layerData.getEntities().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            engine.addEntity(new BoxEntity(layerData,
                        (rect.getX() + rect.getWidth() / 2) / ppm,
                        (rect.getY() + rect.getHeight() / 2) / ppm,
                        rect.getWidth(),
                        rect.getHeight()
                ));
        }
    }

    private static void createAnimationEntity(TiledMapLayerData layerData) {
        for (RectangleMapObject object : layerData.getEntities().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            engine.addEntity(new Torch(layerData,
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
*/

    public static void clearLights() {
        pointLights.clear();
        coneLights.clear();
    }

}