package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApplicationConfig implements Serializable {
    private String version;
    private String profile;
    private WindowConfig windowConfig;
    private ConsoleConfig consoleConfig;
    private AudioConfig audioConfig;
}
