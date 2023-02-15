package com.gdx.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.gdx.engine.model.AssetResources;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.model.config.ConsoleCmd;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class FileLoaderUtil {

    public static ApplicationConfig getApplicationConfig(final FileHandle handle) throws IOException {
        Json JSON = new Json();
        String jsonString = handle.readString();
        return JSON.fromJson(ApplicationConfig.class, jsonString);
    }

    public static AssetResources getResources(String fileName) throws IOException {
        Json JSON = new Json();
        String jsonString = getFileHandle(fileName).readString();
        return JSON.fromJson(AssetResources.class, jsonString);
    }

    public static ConsoleCmd getConsoleCmd(String fileName) {
        Json JSON = new Json();
        String jsonString = getFileHandle(fileName).readString();
        return JSON.fromJson(ConsoleCmd.class, jsonString);
    }

    public static FileHandle getFileHandle(String filePath) {
        return Gdx.files.internal(filePath);
    }

}
