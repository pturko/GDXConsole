package com.gdx.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.gdx.engine.model.AssetResources;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.model.config.ConsoleCmd;
import com.gdx.engine.model.map.MapEntityData;
import com.gdx.engine.service.ServiceFactoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.gdx.engine.service.AssetServiceImpl.EXTERNAL_APPLICATION_CONFIG;

@Slf4j
public class FileLoaderUtil {

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

    public static MapEntityData getMapEntity(String fileName) {
        Json JSON = new Json();
        String jsonString = getFileHandle(fileName).readString();
        return JSON.fromJson(MapEntityData.class, jsonString);
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

    public static FileHandle[] getCmdFileList() {
        return getFileHandle(ServiceFactoryImpl.getAssetService().getConsoleCmdPathFolder()).list();
    }

    public static FileHandle[] getMapFileList() {
        return getFileHandle(ServiceFactoryImpl.getAssetService().getConsoleMapPathFolder()).list();
    }
}
