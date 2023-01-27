package com.gdx.engine.screen;

import com.badlogic.gdx.Screen;
import com.gdx.GdxGame;
import com.gdx.game.screen.MenuScreen;
import com.gdx.game.screen.OptionsScreen;

public enum ScreenItems {
	 
    MENU {
        @Override
        public Screen getScreenInstance(GdxGame gdxGame) {
            return new MenuScreen(gdxGame);
        }
    },

    OPTIONS {
        @Override
        public Screen getScreenInstance(GdxGame gdxGame) {
            return new OptionsScreen(gdxGame);
        }
    };

    public abstract Screen getScreenInstance(GdxGame gdxGame);

}
