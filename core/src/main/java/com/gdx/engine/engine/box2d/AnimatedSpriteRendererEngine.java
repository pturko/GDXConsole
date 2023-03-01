package com.gdx.engine.engine.box2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdx.engine.box2d.component.Mappers;
import com.gdx.engine.box2d.component.graphics.AnimationComponent;
import com.gdx.engine.box2d.component.graphics.SpriteComponent;
import com.gdx.engine.service.*;

public class AnimatedSpriteRendererEngine extends IteratingSystem {
    private final AssetServiceImpl assetService;
    private final ScreenServiceImpl screenService;
    private final PooledEngineServiceImpl pooledEngineService;
    private static ConfigServiceImpl configService;

    private PooledEngine engine;
    private Batch batch;
    private Camera camera;
    private boolean isRendering;

    public AnimatedSpriteRendererEngine() {
        super(Family.all(AnimationComponent.class).get());

        screenService = ServiceFactoryImpl.getScreenService();
        assetService = ServiceFactoryImpl.getAssetService();
        pooledEngineService = ServiceFactoryImpl.getPooledEngineService();
        configService = ServiceFactoryImpl.getConfigService();

        setUp();
    }

    private void setUp() {
        engine = pooledEngineService.getEngine();
        batch = assetService.getBatch();
        camera = screenService.getCamera();

        isRendering = configService.getBox2DConfig().isAnimatedSpriteRendering();
    }

    @Override
    public void update(float delta) {
        if (isRendering) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            super.update(delta);
            batch.end();
        }
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        AnimationComponent<TextureRegion> animation = Mappers.ANIMATION.get(entity);
        SpriteComponent sprite = Mappers.SPRITE.get(entity);

        sprite.setRegion(animation.getKeyFrame(animation.getTimer(), true));
        animation.update(delta);
        sprite.draw(batch);
    }

}