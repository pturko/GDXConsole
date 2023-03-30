package com.gdx.engine.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.gdx.engine.interfaces.service.PreferenceService;
import org.apache.commons.lang3.StringUtils;

public class PreferenceServiceImpl implements PreferenceService {
    private static PreferenceServiceImpl preferenceServiceInstance;
    private static final String PREFS_NAME = "preference";
    private static Preferences preferences;

    private PreferenceServiceImpl() {
        preferences = Gdx.app.getPreferences(PREFS_NAME);
    }

    public static PreferenceServiceImpl getInstance() {
        if (preferenceServiceInstance == null) {
            preferenceServiceInstance = new PreferenceServiceImpl();
        }
        return preferenceServiceInstance;
    }

    @Override
    public String getString(String name) {
        return preferences.getString(name, StringUtils.EMPTY);
    }

    @Override
    public Integer getInteger(String name) {
        return preferences.getInteger(name, 0);
    }

    @Override
    public Integer getInteger(String name, Integer defaultValue) {
        return preferences.getInteger(name, defaultValue);
    }

    @Override
    public Float getFloat(String name) {
        return preferences.getFloat(name, 0f);
    }

    @Override
    public Float getFloat(String name, Float defaultValue) {
        return preferences.getFloat(name, defaultValue);
    }

    @Override
    public boolean getBoolean(String name) {
        return preferences.getBoolean(name, false);
    }

    @Override
    public void saveString(String name, String value) {
        preferences.putString(name, value);
        preferences.flush();
    }

    @Override
    public void saveInteger(String name, Integer value) {
        preferences.putInteger(name, value);
        preferences.flush();
    }

    @Override
    public void saveFloat(String name, Float value) {
        preferences.putFloat(name, value);
        preferences.flush();
    }

    @Override
    public void saveBoolean(String name, boolean value) {
        preferences.putBoolean(name, value);
        preferences.flush();
    }

}