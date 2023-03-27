package com.gdx.game.util;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.gdx.engine.component.entity.CircleEntity;
import com.gdx.engine.component.entity.PolylineEntity;
import com.gdx.engine.component.entity.RectangleEntity;
import com.gdx.engine.model.config.Box2DConfig;
import com.gdx.engine.model.map.TiledMapLayerData;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.gdx.engine.util.box2d.LightBuilder;
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

    public static void createLightSources(RayHandler rayHandler, TiledMapLayerData layerData) {
        Box2DConfig box2DConfig = ServiceFactoryImpl.getConfigService().getBox2DConfig();
        int distance = box2DConfig.getBox2DLightsConfig().getLightDistance();
        for (RectangleMapObject o : layerData.getEntities().getByType(RectangleMapObject.class)) {
            Rectangle rect = o.getRectangle();
            float x = rect.getX() + rect.getWidth() / 2;
            float y = rect.getY() + rect.getHeight() / 2;
            pointLights.add(LightBuilder.createPointLight(rayHandler, Color.ORANGE, distance, x, y));
        }
    }

    public static void clearLights() {
        pointLights.clear();
        coneLights.clear();
    }

}