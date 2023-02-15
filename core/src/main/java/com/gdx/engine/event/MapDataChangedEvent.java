package com.gdx.engine.event;

import com.gdx.engine.engine.tlledmap.TiledMapData;

public class MapDataChangedEvent extends Event {

    private final TiledMapData tiledMapData;

    public MapDataChangedEvent(TiledMapData tiledMapData) {
        super(EventType.MAP_DATA_CHANGED);
        this.tiledMapData = tiledMapData;
    }

    public TiledMapData getTiledMapData() {
        return tiledMapData;
    }

}