package com.gdx.engine.engine.box2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.engine.box2d.component.Mappers;
import com.gdx.engine.box2d.component.graphics.SpriteComponent;
import com.gdx.engine.box2d.component.graphics.TextureComponent;
import com.gdx.engine.box2d.component.physics.B2BodyComponent;
import com.gdx.engine.service.Box2DWorldImpl;
import com.gdx.engine.service.CameraServiceImpl;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.ResourceLoaderServiceImpl;

public class StaticSpriteRendererEngine extends IteratingSystem {
    private static ResourceLoaderServiceImpl resourceService;
    private static CameraServiceImpl cameraService;
    private static Box2DWorldImpl box2DService;
    private static ConfigServiceImpl configService;

    private final Batch batch;
    private final Camera camera;
    private final World world;

    private boolean isRendering;

    public StaticSpriteRendererEngine() {
        super(Family.all(SpriteComponent.class).get());

        resourceService = ResourceLoaderServiceImpl.getInstance();
        cameraService = CameraServiceImpl.getInstance();
        box2DService = Box2DWorldImpl.getInstance();
        configService = ConfigServiceImpl.getInstance();
        configService = ConfigServiceImpl.getInstance();

        this.batch = resourceService.getBatch();
        this.camera = cameraService.getCamera();
        this.world = box2DService.getWorld();

        isRendering = configService.getBox2DConfig().isStaticSpriteRenderer();
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
    protected void processEntity(Entity entity, float deltaTime) {
        TextureComponent textureComponent = Mappers.TEXTURE_COMPONENT.get(entity);
        B2BodyComponent b2body = Mappers.B2BODY.get(entity);

        if (b2body != null) {
            float textureX = b2body.getBody().getPosition().x - textureComponent.getWidth() / 2;
            float textureY = b2body.getBody().getPosition().y - textureComponent.getHeight() / 2;
            textureComponent.setPosition(textureX, textureY);
            textureComponent.draw(batch);
        }
    }

}