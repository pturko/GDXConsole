package com.gdx.engine.model.map;

import lombok.Data;

@Data
public class SpriteAnimation {
    private float frameDuration;
    private int firstFrameCount;
    private int lastFrameCount;
    private int offsetX;
    private int offsetY;
    private int width;
    private int height;
}