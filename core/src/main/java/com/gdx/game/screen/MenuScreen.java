package com.gdx.game.screen;

import com.gdx.GdxGame;

public class MenuScreen extends BaseScreen {

    public MenuScreen(GdxGame gdxGame) {
        super(gdxGame);

        initResources();
    }

    private void initResources() {
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        update();
        clearScreen();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
