package com.gdx.engine.engine.tlledmap;

import lombok.Data;

@Data
public class TiledMapData {
    private int mapWidth;
    private int mapHeight;
    private int mapTileSize;
    private float ppm;
    private boolean hasLayers;
    private boolean isLoaded;
}