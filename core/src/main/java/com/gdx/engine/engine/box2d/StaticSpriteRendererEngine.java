package com.gdx.engine.engine.box2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.gdx.engine.box2d.component.Mappers;
import com.gdx.engine.box2d.component.graphics.SpriteComponent;
import com.gdx.engine.box2d.component.graphics.TextureComponent;
import com.gdx.engine.box2d.component.physics.B2BodyComponent;
import com.gdx.engine.service.*;

public class StaticSpriteRendererEngine extends IteratingSystem {
    private static AssetServiceImpl assetService;
    private static ScreenServiceImpl screenService;
    private static Box2DWorldServiceImpl box2DService;
    private static ConfigServiceImpl configService;

    private Batch batch;
    private Camera camera;
    private boolean isRendering;

    public StaticSpriteRendererEngine() {
        super(Family.all(SpriteComponent.class).get());

        assetService = ServiceFactoryImpl.getAssetService();
        screenService = ServiceFactoryImpl.getScreenService();
        box2DService = ServiceFactoryImpl.getBox2DWorldService();
        configService = ServiceFactoryImpl.getConfigService();

        setUp();
    }

    private void setUp() {
        batch = assetService.getBatch();
        camera = screenService.getCamera();

        isRendering = configService.getBox2DConfig().isStaticSpriteRendering();
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

        if (b2body != null && b2body.getBody() != null) {
            float textureX = b2body.getBody().getPosition().x - textureComponent.getWidth() / 2;
            float textureY = b2body.getBody().getPosition().y - textureComponent.getHeight() / 2;
            textureComponent.setPosition(textureX, textureY);
            textureComponent.draw(batch);
        }
    }

}