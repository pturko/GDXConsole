package com.gdx.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.gdx.engine.model.AssetResources;
import com.gdx.engine.model.config.ApplicationConfig;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class FileLoaderUtil {

    public static ApplicationConfig getApplicationConfig(final FileHandle handle) throws IOException {
        String jsonString = handle.readString();
        return new Gson().fromJson(jsonString, ApplicationConfig.class);
    }

    public static AssetResources getResources(String fileName) throws IOException {
        String jsonString = getFileHandle(fileName).readString();
        return new Gson().fromJson(jsonString, AssetResources.class);
    }

    public static FileHandle getFileHandle(String filePath) {
        return Gdx.files.internal(filePath);
    }

}
