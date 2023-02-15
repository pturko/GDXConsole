package com.gdx.game.screen;

import com.badlogic.ashley.core.PooledEngine;
import com.gdx.GdxGame;
import com.gdx.engine.engine.camera.CameraEngine;
import com.gdx.engine.engine.tlledmap.TiledMapEngine;


public class GameScreen extends BaseScreen {

    public GameScreen(GdxGame gdxGame) {
        super(gdxGame);

        loadResources();
        cameraSetup();
    }

    private void loadResources() {
        // Initialize pooled engine
        engine = new PooledEngine();

        engine.addSystem(new TiledMapEngine());                 // Renders TiledMap textures
        engine.addSystem(new CameraEngine());                   // Bound camera
    }

    @Override
    public void show() {
    }

    public void resetConfig() {
    }

    public void resetResources() {
    }

    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        update(delta);
        clearScreen();
        engine.update(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
