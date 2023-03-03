package com.gdx.engine.console;

public enum ConsoleLogLevel {
    ERROR (3),
    WARN (2),
    INFO (1),
    DEBUG (0);

    private final int level;

    ConsoleLogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

}
