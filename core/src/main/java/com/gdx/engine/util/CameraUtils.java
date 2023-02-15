package com.gdx.engine.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.gdx.engine.engine.tlledmap.TiledMapData;

public class CameraUtils {
    
    private CameraUtils() {
    }

    public static void boundCamera(Camera camera, TiledMapData tiledMapData) {
        Vector3 position = camera.position;

        float startX = camera.viewportWidth / 2;
        float startY = camera.viewportHeight / 2;
        float endX =  (tiledMapData.getMapWidth() * tiledMapData.getMapTileSize()) / tiledMapData.getPpm() - camera.viewportWidth / 2;
        float endY = (tiledMapData.getMapHeight() * tiledMapData.getMapTileSize()) / tiledMapData.getPpm() - camera.viewportHeight / 2;
        
        if (position.x < startX) {
            position.x = startX;
        }
        if (position.y < startY) {
            position.y = startY;
        }
        
        if (position.x > endX) {
            position.x = endX;
        }
        if (position.y > endY) {
            position.y = endY;
        }
        
        camera.position.set(position);
        camera.update();
    }

}
