package com.gdx.engine.model.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TiledMapData {
    private String name;
    private int mapWidth;
    private int mapHeight;
    private int mapTileSize;
    private float ppm;
    private boolean hasLayers;
    private boolean isLoaded;

    private TiledMap tiledMap;

    private Map<String, TiledMapLayerData> mapLayersData = new HashMap<>();
}