package com.gdx.game.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.GdxGame;


public class BaseScreen implements Screen {

    protected GdxGame gdxGame;
    protected int screenHeight;
    protected int screenWidth;
    protected OrthographicCamera camera;
    protected SpriteBatch spriteBatch;

    public BaseScreen(GdxGame gdxGame) {
        this.gdxGame = gdxGame;

        createResources();
        cameraSetup();
        createInputProcessor();
    }

    private void createResources() {
        spriteBatch = new SpriteBatch();
    }

    private void cameraSetup() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        screenWidth = 800;
        screenHeight = 600;
    }

    private void createInputProcessor() {
        Gdx.input.setInputProcessor(new InputMultiplexer());
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}
