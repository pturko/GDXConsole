package com.gdx.engine.engine.box2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdx.engine.component.Mappers;
import com.gdx.engine.component.graphics.AnimationComponent;
import com.gdx.engine.component.graphics.SpriteComponent;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.service.*;

public class Box2DAnimatedSpriteRendererEngine extends IteratingSystem {
    private Batch batch;
    private Camera camera;
    private boolean isRendering;

    public Box2DAnimatedSpriteRendererEngine() {
        super(Family.all(AnimationComponent.class).get());

        update(ServiceFactoryImpl.getConfigService().getApplicationConfig());
        configureListeners();
    }

    private void update(ApplicationConfig config) {
        batch = ServiceFactoryImpl.getAssetService().getBatch();
        camera = ServiceFactoryImpl.getScreenService().getCamera();

        isRendering = config.getBox2DConfig().isAnimatedSpriteRendering();
    }

    private void configureListeners() {
        // Event reload application config
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_CHANGED, (ConfigChangedEvent e) ->
                update(e.getApplicationConfig()));
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