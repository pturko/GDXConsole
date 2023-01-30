package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class WindowConfig implements Serializable {
    private int width;
    private int height;
    private boolean showFPS;
}
