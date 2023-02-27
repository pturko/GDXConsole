package com.gdx.game.map;

import lombok.ToString;

@ToString
public enum MapLayerType {
    BACKGROUND ("Background"),
    GROUND ("Ground"),
    WALL ("Wall"),
    LIGHT_SOURCE ("LightSource"),
    TORCH ("Torch"),
    BOX ("Box");

    private final String name;

    MapLayerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}