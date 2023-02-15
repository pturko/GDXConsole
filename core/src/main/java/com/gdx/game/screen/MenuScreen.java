package com.gdx.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.gdx.GdxGame;
import com.gdx.engine.service.ResourceLoaderServiceImpl;

public class MenuScreen extends BaseScreen {

    private ResourceLoaderServiceImpl resourceLoaderService;

    public MenuScreen(GdxGame gdxGame) {
        super(gdxGame);

        initResources();
    }

    private void initResources() {
        resourceLoaderService = ResourceLoaderServiceImpl.getInstance();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        update(delta);
        clearScreen();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
