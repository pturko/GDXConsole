package com.gdx.engine.service;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.IntMap;
import com.gdx.GdxGame;
import com.gdx.engine.interfaces.service.WindowService;
import com.gdx.engine.screen.ScreenItems;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class WindowServiceImpl implements WindowService {

    private Screen activeScreen;

    private static WindowServiceImpl windowServiceInstance;

    private GdxGame gdxGame;
 
    private final IntMap<Screen> screens;
 
    private WindowServiceImpl() {
        screens = new IntMap<>();
    }
 
    public static synchronized WindowServiceImpl getInstance() {
        if (null == windowServiceInstance) {
            windowServiceInstance = new WindowServiceImpl();
        }
        return windowServiceInstance;
    }

    @Override
    public void init(GdxGame gdxGame) {
        this.gdxGame = gdxGame;
    }

    @Override
    public void show(String screen) {
        if (null == gdxGame) return;
        ScreenItems screenItem = ScreenItems.valueOf(screen);
        if (null == screenItem) return;
        if (!screens.containsKey(screenItem.ordinal())) {
            screens.put(screenItem.ordinal(), screenItem.getScreenInstance(gdxGame));
        }
        activeScreen = screens.get(screenItem.ordinal());

        gdxGame.setScreen(activeScreen);
    }

    public Screen getActiveScreen () {
        return activeScreen;
    }

    public void dispose(Screen screen) {
        if (screen != null) screen.hide();
    }

}
