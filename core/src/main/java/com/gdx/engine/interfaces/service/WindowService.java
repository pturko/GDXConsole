package com.gdx.engine.interfaces.service;

import com.badlogic.gdx.Screen;
import com.gdx.GdxGame;

public interface WindowService {
    void init(GdxGame gdxGame);
    void show(String screen);
    Screen getActiveScreen();
}
