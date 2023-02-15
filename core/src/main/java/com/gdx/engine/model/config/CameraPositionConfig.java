package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class CameraPositionConfig implements Serializable {
    private float x;
    private float y;
    private float z;
}
