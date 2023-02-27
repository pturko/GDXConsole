package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConsoleConfig implements Serializable {
    private boolean showConsole;
    private boolean showOnError;
    private boolean showOnWarn;
    private boolean startCommands;
    private String logLevel;
}
