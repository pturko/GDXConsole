package com.gdx.engine.event;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class MapChangedEvent extends Event {

    private final TiledMap tiledMap;

    public MapChangedEvent(TiledMap tiledMap) {
        super(EventType.MAP_CHANGED);
        this.tiledMap = tiledMap;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

}