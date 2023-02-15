package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class DebugConfig implements Serializable {
    private boolean showFPS;
    private boolean showHeap;
}
