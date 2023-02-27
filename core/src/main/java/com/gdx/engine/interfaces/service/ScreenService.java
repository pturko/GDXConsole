package com.gdx.engine.interfaces.service;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gdx.GdxGame;

public interface ScreenService {
    void init(GdxGame gdxGame);
    void show(String nextScreen, String transitionEffectName);
    OrthographicCamera getCamera();
    Screen getActiveScreen();
}
