package com.gdx.engine.component.entity;

import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gdx.engine.model.map.TiledMapLayerData;

public class CircleEntity extends AbstractEntity {
    public CircleEntity(EllipseMapObject entity, TiledMapLayerData layerData) {
        super();

        Ellipse ellipse = entity.getEllipse();
        Vector2 centerPos = new Vector2(ellipse.x + ellipse.width/2, ellipse.y + ellipse.height/2);

        // Create body
        bodyBuilder.type(BodyDef.BodyType.StaticBody)
                .position(0, 0, ppm)
                .buildBody();

        // Create fixture
        bodyBuilder.newCircleFixture(centerPos, (int)ellipse.height/2, ppm)
                .categoryBits(Short.parseShort(layerData.getCategoryBits()))
                .friction(layerData.getFriction())
                .setSensor(!layerData.isSensor())
                .buildFixture();
    }

}
