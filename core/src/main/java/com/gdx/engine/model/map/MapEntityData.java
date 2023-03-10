package com.gdx.engine.model.map;

import lombok.Data;

@Data
public class MapEntityData {
    private String textureAtlasName;
    private String textureName;
    private String type;
    private String categoryBits;
    private String maskBits;
    private MapEntityAnimation mapEntityAnimation;
}