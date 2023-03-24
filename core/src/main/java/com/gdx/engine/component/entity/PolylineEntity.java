package com.gdx.engine.component.entity;

import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.gdx.engine.model.map.TiledMapLayerData;
import com.gdx.engine.util.OperationUtil;

public class PolylineEntity extends AbstractEntity {

    public PolylineEntity(PolylineMapObject entity, TiledMapLayerData layerData) {
        super();

        float[] vertices = entity.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2], vertices[i * 2 + 1]);
        }

        bodyBuilder.type(OperationUtil.getBodyTypeDef(layerData.isStaticBody()))
                .position(0, 0, ppm)
                .buildBody();

        bodyBuilder.newPolylineFixture(worldVertices, ppm)
                .categoryBits(Short.parseShort(layerData.getCategoryBits()))
                .friction(layerData.getFriction())
                .setSensor(!layerData.isSensor())
                .buildFixture();
    }

}
