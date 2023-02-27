package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class AssetConfig implements Serializable {
    private boolean externalFiles;
    private boolean autoLoad;
}
