package com.gdx.engine.service;

import com.gdx.engine.model.config.CameraPositionConfig;
import com.gdx.engine.model.config.CameraViewportConfig;
import com.gdx.engine.model.config.WindowConfig;
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
        camera = new OrthographicCamera();
        cameraSetup();
    }

    public void cameraSetup() {
        configService = ConfigServiceImpl.getInstance();
        WindowConfig windowConfig = configService.getWindowConfig();
        CameraPositionConfig cameraPositionConfig = windowConfig.getCameraConfig().getCameraPositionConfig();
        CameraViewportConfig cameraViewportConfig = windowConfig.getCameraConfig().getCameraViewportConfig();

        camera.position.set(cameraPositionConfig.getX(), cameraPositionConfig.getY(), cameraPositionConfig.getZ());
        camera.viewportWidth = cameraViewportConfig.getWidth();
        camera.viewportHeight = cameraViewportConfig.getHeight();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void dispose() {}

}
