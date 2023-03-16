package com.gdx.engine.event;

public enum EventType {

    // Console related events
    CONSOLE_ENABLED,

    // Config related events
    CONFIG_ASSET_CHANGED,
    CONFIG_AUDIO_CHANGED,
    CONFIG_BOX2D_CHANGED,
    CONFIG_CONSOLE_CHANGED,
    CONFIG_MAP_CHANGED,
    CONFIG_SCREEN_CHANGED,

    // Asset related events
    ASSET_LOADED,

    // Map related events
    MAP_CHANGED,
    MAP_DATA_CHANGED

}