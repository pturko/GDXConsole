package com.gdx.engine.event;

import com.gdx.engine.model.config.ApplicationConfig;

public class ConfigChangedEvent extends Event {

    private final ApplicationConfig applicationConfig;

    public ConfigChangedEvent(ApplicationConfig applicationConfig) {
        super(EventType.CONFIG_CHANGED);
        this.applicationConfig = applicationConfig;
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

}