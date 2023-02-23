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
import com.gdx.engine.service.CameraServiceImpl;
import com.gdx.engine.service.PooledEngineServiceImpl;
import com.gdx.engine.service.ResourceLoaderServiceImpl;

public class AnimatedSpriteRendererEngine extends IteratingSystem {
    private final CameraServiceImpl cameraService;
    private final ResourceLoaderServiceImpl resourceService;
    private final PooledEngineServiceImpl pooledEngineService;

    private final PooledEngine engine;
    private final Batch batch;
    private final Camera camera;

    public AnimatedSpriteRendererEngine() {
        super(Family.all(AnimationComponent.class).get());

        cameraService = CameraServiceImpl.getInstance();
        resourceService = ResourceLoaderServiceImpl.getInstance();
        pooledEngineService = PooledEngineServiceImpl.getInstance();

        this.engine = pooledEngineService.getEngine();
        this.batch = resourceService.getBatch();
        this.camera = cameraService.getCamera();
    }

    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        super.update(delta);
        batch.end();
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