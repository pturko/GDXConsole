package com.gdx.engine.model.map;

import com.badlogic.gdx.maps.MapObjects;
import lombok.Data;

@Data
public class TiledMapLayerData {
    private String textureAtlasName;
    private String textureName;
    private boolean staticBody;
    private String categoryBits;
    private String maskBits;
    private float friction;
    private boolean sensor;
    private boolean isVisible;
    private MapEntityAnimation mapEntityAnimation;
    private MapObjects entities;
}