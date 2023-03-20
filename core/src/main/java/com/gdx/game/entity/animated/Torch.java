package com.gdx.game.entity.animated;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdx.engine.component.graphics.AnimationComponent;
import com.gdx.engine.component.graphics.SpriteComponent;
import com.gdx.engine.model.map.TiledMapLayerData;
import com.gdx.engine.model.map.MapEntityAnimation;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.gdx.engine.util.AnimationUtils;

public class Torch extends Entity {
    private static float itemWidth;
    private static float itemHeight;
    private static float ppm;

    public Torch(TiledMapLayerData mapEntity, float x, float y, float width, float height) {
        ppm = ServiceFactoryImpl.getConfigService().getBox2DConfig().getPpm();

        itemWidth = width;
        itemHeight = height;

        Texture texture = ServiceFactoryImpl.getAssetService().getTexture(
                mapEntity.getTextureAtlasName(), mapEntity.getTextureName());

        MapEntityAnimation mapEntityAnimation = mapEntity.getMapEntityAnimation();
        AnimationComponent<TextureRegion> animation = AnimationUtils.createAnimation(texture,
                mapEntityAnimation.getFrameDuration() / ppm,
                mapEntityAnimation.getFirstFrameCount(),
                mapEntityAnimation.getLastFrameCount(),
                mapEntityAnimation.getOffsetX(),
                mapEntityAnimation.getOffsetY(),
                mapEntityAnimation.getWidth(),
                mapEntityAnimation.getHeight());
        SpriteComponent sprite = new SpriteComponent(x, y);

        add(animation);
        add(sprite);

        sprite.setBounds(sprite.getX(), sprite.getY(), itemWidth / ppm, itemHeight / ppm);
    }

}