package com.gdx.engine.box2d.component;

import com.badlogic.ashley.core.ComponentMapper;
import com.gdx.engine.box2d.component.graphics.AnimationComponent;
import com.gdx.engine.box2d.component.graphics.SpriteComponent;
import com.gdx.engine.box2d.component.graphics.TextureComponent;
import com.gdx.engine.box2d.component.physics.B2BodyComponent;

public class Mappers {

    public static final ComponentMapper<B2BodyComponent> B2BODY;
    public static final ComponentMapper<SpriteComponent> SPRITE;
    public static final ComponentMapper<AnimationComponent> ANIMATION;
    public static final ComponentMapper<TextureComponent> TEXTURE_COMPONENT;

    static {
        B2BODY = ComponentMapper.getFor(B2BodyComponent.class);
        SPRITE = ComponentMapper.getFor(SpriteComponent.class);
        ANIMATION = ComponentMapper.getFor(AnimationComponent.class);
        TEXTURE_COMPONENT = ComponentMapper.getFor(TextureComponent.class);
    }

}
