package com.gdx.engine.interfaces.service;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.gdx.engine.model.map.TiledMapData;

public interface TiledMapService {
    void load(String mapName);
    TiledMap getMap();
    TiledMapData getMapData();
    boolean isMapLoaded();
    void clear();
    String getMapName();
}
