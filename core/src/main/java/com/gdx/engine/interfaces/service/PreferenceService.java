package com.gdx.engine.interfaces.service;

public interface PreferenceService {
    String getString(String name);
    Integer getInteger(String name);
    Integer getInteger(String name, Integer defaultValue);
    Float getFloat(String name);
    Float getFloat(String name, Float defaultValue);
    boolean getBoolean(String name);
    void saveString(String name, String value);
    void saveInteger(String name, Integer value);
    void saveFloat(String name, Float value);
    void saveBoolean(String name, boolean value);
}
