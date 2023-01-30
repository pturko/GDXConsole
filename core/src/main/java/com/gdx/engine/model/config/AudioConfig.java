package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class AudioConfig implements Serializable {
    private boolean music;
    private boolean sound;
}
