package com.gdx.engine.event;

public class ConfigAssetChangedEvent extends Event {

    public ConfigAssetChangedEvent() {
        super(EventType.CONFIG_ASSET_CHANGED);
    }

    public boolean get() {
        return true;
    }

}