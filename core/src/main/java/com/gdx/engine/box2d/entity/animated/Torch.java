package com.gdx.engine.box2d.entity.animated;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdx.engine.box2d.component.graphics.AnimationComponent;
import com.gdx.engine.box2d.component.graphics.SpriteComponent;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.AssetServiceImpl;
import com.gdx.engine.util.AnimationUtils;

public class Torch extends Entity {
    private static ConfigServiceImpl configService;
    private static AssetServiceImpl assetService;

    private static float itemWidth;
    private static float itemHeight;
    private static float ppm;

    public Torch(String textureName, float x, float y, float width, float height) {
        configService = ConfigServiceImpl.getInstance();
        assetService = AssetServiceImpl.getInstance();

        ppm = configService.getBox2DConfig().getPpm();

        itemWidth = width;
        itemHeight = height;

        Texture texture = assetService.getTexture(textureName);

        AnimationComponent<TextureRegion> animation = AnimationUtils.createAnimation(texture,
                7f / ppm, 0, 5, 0, 0, 32, 92);
        SpriteComponent sprite = new SpriteComponent(x, y);

        add(animation);
        add(sprite);

        sprite.setBounds(sprite.getX(), sprite.getY(), itemWidth / ppm, itemHeight / ppm);
    }

}