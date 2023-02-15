package com.gdx.engine.service;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gdx.engine.interfaces.service.CameraService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CameraServiceImpl implements CameraService {

    private static CameraServiceImpl cameraServiceInstance;
    private static ConfigServiceImpl configService;
    private static OrthographicCamera camera;

    public static synchronized CameraServiceImpl getInstance( ) {
        if (cameraServiceInstance == null)
            cameraServiceInstance = new CameraServiceImpl();
        return cameraServiceInstance;
    }

    public CameraServiceImpl() {
        configService = ConfigServiceImpl.getInstance();
        camera = new OrthographicCamera();

        getCamera().position.set(3, 1.5f, 0);
        getCamera().viewportWidth = 6;
        getCamera().viewportHeight = 3;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void dispose() {}

}
