package com.gdx.engine.service;

public class ServiceFactoryImpl {

    public static AssetServiceImpl getAssetService() {
        return AssetServiceImpl.getInstance();
    }

    public static AudioServiceImpl getAudioService() {
        return AudioServiceImpl.getInstance();
    }

    public static Box2DWorldServiceImpl getBox2DWorldService() {
        return Box2DWorldServiceImpl.getInstance();
    }

    public static ConfigServiceImpl getConfigService() {
        return ConfigServiceImpl.getInstance();
    }

    public static ConsoleServiceImpl getConsoleService() {
        return ConsoleServiceImpl.getInstance();
    }

    public static EventServiceImpl getEventService() {
        return EventServiceImpl.getInstance();
    }

    public static PooledEngineServiceImpl getPooledEngineService() {
        return PooledEngineServiceImpl.getInstance();
    }

    public static ScreenServiceImpl getScreenService() {
        return ScreenServiceImpl.getInstance();
    }

    public static TiledMapServiceImpl getTiledMapService() {
        return TiledMapServiceImpl.getInstance();
    }

    public static UIServiceImpl getUIService() {
        return UIServiceImpl.getInstance();
    }

    public static PreferenceServiceImpl getPreferenceService() {
        return PreferenceServiceImpl.getInstance();
    }

}
