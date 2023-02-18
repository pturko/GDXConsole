package com.gdx.engine.box2d.component.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;

public class SpriteComponent extends Sprite implements Component, Disposable {

    public SpriteComponent() {
    }

    public SpriteComponent(float x, float y) {
        setPosition(x, y);
    }

    public SpriteComponent(Texture texture, float x, float y) {
        super(texture);
        setPosition(x, y);
    }

    @Override
    public void dispose() {
        getTexture().dispose();
    }

}