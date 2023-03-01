package com.gdx.game.screen;

import com.gdx.GdxGame;
import com.gdx.engine.engine.box2d.*;
import com.gdx.engine.engine.camera.CameraEngine;
import com.gdx.engine.engine.tlledmap.TiledMapEngine;

public class GameScreen extends BaseScreen {

    public GameScreen(GdxGame gdxGame) {
        super(gdxGame);

        loadResources();
    }

    private void loadResources() {
        engine.addSystem(new B2DPhysicsEngine());                       // Physics engine
        engine.addSystem(new TiledMapEngine());                         // Renders TiledMap textures
        engine.addSystem(new CameraEngine());                           // Bound camera
        engine.addSystem(new StaticSpriteRendererEngine());             // Renders non-animated sprites
        engine.addSystem(new AnimatedSpriteRendererEngine());           // Renders animated sprites
        engine.addSystem(new B2LightsEngine());                         // Renders Dynamic box2d lights
        engine.addSystem(new B2DebugRendererEngine());                  // Renders physics debug profiles
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        clearScreen();
        engine.update(delta);
        update();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
