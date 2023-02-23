package com.gdx.engine.service;


import com.badlogic.ashley.core.PooledEngine;
import com.gdx.engine.interfaces.service.PooledEngineService;

public class PooledEngineServiceImpl implements PooledEngineService {

    private static PooledEngineServiceImpl pooledEngineServiceInstance;

    protected PooledEngine engine;

    private PooledEngineServiceImpl() {
        engine = new PooledEngine();
    }

    public static PooledEngineServiceImpl getInstance() {
        if (pooledEngineServiceInstance == null) {
            pooledEngineServiceInstance = new PooledEngineServiceImpl();
        }
        return pooledEngineServiceInstance;
    }

    @Override
    public PooledEngine getEngine() {
        return engine;
    }

}