package com.gdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.gdx.GdxGame;
import com.gdx.engine.service.ConsoleServiceImpl;
import com.gdx.engine.service.ResourceLoaderServiceImpl;

public class MenuScreen extends BaseScreen {

    private ConsoleServiceImpl consoleService;
    private ResourceLoaderServiceImpl resourceLoaderService;

    private Texture texture;

    public MenuScreen(GdxGame gdxGame) {
        super(gdxGame);

        initResources();
    }

    private void initResources() {
        resourceLoaderService = ResourceLoaderServiceImpl.getInstance();
        texture = resourceLoaderService.getTexture("ok");
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

        spriteBatch.draw(texture, 380, 300);

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
