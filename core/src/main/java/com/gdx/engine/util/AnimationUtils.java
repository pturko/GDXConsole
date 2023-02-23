package com.gdx.engine.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.gdx.engine.box2d.component.graphics.AnimationComponent;

public class AnimationUtils {

    private static Array<TextureRegion> frames = new Array<>();

    private AnimationUtils() {
    }

    public static AnimationComponent<TextureRegion> createAnimation(Texture region, float frameDuration,
                                                                    int firstFrameCount, int lastFrameCount,
                                                                    int offsetX, int offsetY,
                                                                    int width, int height) {
        frames.clear();
        for (int i = firstFrameCount; i <= lastFrameCount; i++) {
            frames.add(new TextureRegion(region, i * width + offsetX, offsetY, width, height));
        }
        return new AnimationComponent<>(frameDuration, frames);
    }

}