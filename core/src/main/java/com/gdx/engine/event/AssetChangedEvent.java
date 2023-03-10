package com.gdx.engine.event;

public class AssetChangedEvent extends Event {

    public AssetChangedEvent() {
        super(EventType.ASSET_CHANGED);
    }

    public boolean get() {
        return true;
    }

}