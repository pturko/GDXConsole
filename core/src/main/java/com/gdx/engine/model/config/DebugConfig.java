package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class DebugConfig implements Serializable {
    private boolean fps;
    private boolean heap;
    private boolean menuBar;
}
