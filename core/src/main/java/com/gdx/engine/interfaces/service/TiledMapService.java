package com.gdx.engine.interfaces.service;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.gdx.engine.engine.tlledmap.TiledMapData;

public interface TiledMapService {
    boolean load(String mapName);
    TiledMap getMap();
    TiledMapData getMapData();
    MapLayers getLayers();
    TiledMap getTiledMap();
    boolean isMapLoaded();
}
