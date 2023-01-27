package com.gdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.gdx.GdxGame;
import com.gdx.engine.service.ConsoleServiceImpl;

public class MenuScreen extends BaseScreen {

    private ConsoleServiceImpl consoleService;

    public MenuScreen(GdxGame gdxGame) {
        super(gdxGame);

        consoleService = ConsoleServiceImpl.getInstance();
        consoleService.cmd("ver 1.0");
        consoleService.cmd("screen options");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();

        spriteBatch.setColor(250,250,250,0.3f);

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
