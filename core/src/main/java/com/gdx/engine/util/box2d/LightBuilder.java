package com.gdx.engine.util.box2d;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.game.map.CategoryBits;

public class LightBuilder {
    private static ConfigServiceImpl configService;

    private static final short LIGHT_MASK_BITS = CategoryBits.WALL | CategoryBits.BOX | CategoryBits.TORCH;

    public static PointLight createPointLight(RayHandler rayHandler, Color color, float dist, Body body) {
        configService = ConfigServiceImpl.getInstance();
        int numberOfRays = configService.getBox2DConfig().getBox2DLightsConfig().getNumberOfRays();
        float ppm = configService.getWindowConfig().getPpm();

        PointLight pl = new PointLight(rayHandler, numberOfRays, color, dist / ppm, 0, 0);
        pl.setSoftnessLength(0f);
        pl.attachToBody(body);
        pl.setXray(false);
        return pl;
    }

    public static PointLight createPointLight(RayHandler rayHandler, Color color, float dist, float x, float y) {
        configService = ConfigServiceImpl.getInstance();
        int numberOfRays = configService.getBox2DConfig().getBox2DLightsConfig().getNumberOfRays();
        float ppm = configService.getWindowConfig().getPpm();

        PointLight pl = new PointLight(rayHandler, numberOfRays, color, dist / ppm, x / ppm, y / ppm);
        pl.setContactFilter((short) -1, (short) 0, LIGHT_MASK_BITS);
        pl.setSoftnessLength(0f);
        pl.setXray(false);
        return pl;
    }

}
