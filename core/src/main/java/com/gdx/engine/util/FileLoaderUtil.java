package com.gdx.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.gdx.engine.model.AssetResources;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.model.config.ConsoleCmd;
import com.gdx.engine.model.map.TiledMapLayerData;
import com.gdx.engine.service.ServiceFactoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.gdx.engine.service.AssetServiceImpl.EXTERNAL_APPLICATION_CONFIG;

@Slf4j
public class FileLoaderUtil {
    // TODO - add try catch to JSON convert methods
    public static ApplicationConfig getApplicationConfig(String fileName) throws IOException {
        Json JSON = new Json();
        // TODO - Should be configurable internal/external
        String jsonString = getFileHandle(fileName, EXTERNAL_APPLICATION_CONFIG).readString();
        return JSON.fromJson(ApplicationConfig.class, jsonString);
    }

    public static AssetResources getResources(String fileName) throws IOException {
        Json JSON = new Json();
        String jsonString = getFileHandle(fileName, true).readString();
        return JSON.fromJson(AssetResources.class, jsonString);
    }

    public static TiledMapLayerData getMapEntity(String fileName) {
        Json JSON = new Json();
        String jsonString = getFileHandle(fileName).readString();
        try {
            return JSON.fromJson(TiledMapLayerData.class, jsonString);
        } catch(Exception e) {
            log.error("Can't convert map data '{}' to object", fileName);
        }
        return null;
    }

    public static ConsoleCmd getConsoleCmd(String fileName) {
        Json JSON = new Json();
        String jsonString = getFileHandle(fileName, true).readString();
        return JSON.fromJson(ConsoleCmd.class, jsonString);
    }

    public static FileHandle getFileHandle(String filePath, boolean isExternal) {
        if (isExternal) {
            return Gdx.files.external(filePath);
        } else {
            return Gdx.files.internal(filePath);
        }
    }

    public static FileHandle getFileHandle(String filePath) {
        if (ServiceFactoryImpl.getConfigService().getAssetConfig().isExternalFiles()) {
            return Gdx.files.external(filePath);
        } else {
            return Gdx.files.internal(filePath);
        }
    }

    @Deprecated
    public static TiledMapLayerData loadLayerConfig(String name) {
        TiledMapLayerData mapEntity = null;
        try {
            mapEntity = FileLoaderUtil.getMapEntity(ServiceFactoryImpl.getAssetService().getLayerConfigPath(name));
        } catch (Exception e) {
            log.warn("Layer config file not found: {}", name + ".json");
        }
        return mapEntity;
    }

    public static FileHandle[] getCmdFileList() {
        return getFileHandle(ServiceFactoryImpl.getAssetService().getConsoleCmdPathFolder()).list();
    }

    public static FileHandle[] getMapFileList() {
        return getFileHandle(ServiceFactoryImpl.getAssetService().getConsoleMapPathFolder()).list();
    }
}
