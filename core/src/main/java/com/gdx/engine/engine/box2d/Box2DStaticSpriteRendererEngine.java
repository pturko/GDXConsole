package com.gdx.engine.engine.box2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.gdx.engine.component.Mappers;
import com.gdx.engine.component.graphics.SpriteComponent;
import com.gdx.engine.component.graphics.TextureComponent;
import com.gdx.engine.component.physics.B2BodyComponent;
import com.gdx.engine.event.ConfigBox2DChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.model.config.Box2DConfig;
import com.gdx.engine.service.*;

public class Box2DStaticSpriteRendererEngine extends IteratingSystem {
    private final Batch batch;
    private final Camera camera;
    private boolean isRendering;

    public Box2DStaticSpriteRendererEngine() {
        super(Family.all(SpriteComponent.class).get());

        batch = ServiceFactoryImpl.getAssetService().getBatch();
        camera = ServiceFactoryImpl.getScreenService().getCamera();

        updateConfig();
        configureListeners();
    }

    private void updateConfig() {
        Box2DConfig box2DConfig = ServiceFactoryImpl.getConfigService().getApplicationConfig().getBox2DConfig();
        isRendering = box2DConfig.isStaticSpriteRendering();
    }

    private void configureListeners() {
        // Event reload application config
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_BOX2D_CHANGED, (ConfigBox2DChangedEvent e) -> {
            updateConfig();
        });
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