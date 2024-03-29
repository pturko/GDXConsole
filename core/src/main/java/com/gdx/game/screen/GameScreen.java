package com.gdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.gdx.GdxGame;
import com.gdx.engine.engine.box2d.*;
import com.gdx.engine.engine.camera.CameraEngine;
import com.gdx.engine.engine.tlledmap.TiledMapEngine;
import com.gdx.engine.engine.window.WindowEngine;

public class GameScreen extends BaseScreen {

    public GameScreen(GdxGame gdxGame) {
        super(gdxGame);

        loadEngines();
    }

    private void loadEngines() {
        engine.addSystem(new Box2DPhysicsEngine());                     // Physics engine
        engine.addSystem(new TiledMapEngine());                         // Renders TiledMap textures
        engine.addSystem(new CameraEngine());                           // Bound camera
        engine.addSystem(new Box2DStaticSpriteRendererEngine());        // Renders non-animated sprites
        engine.addSystem(new Box2DAnimatedSpriteRendererEngine());      // Renders animated sprites
        engine.addSystem(new Box2DLightsEngine());                      // Renders Dynamic box2D lights
        engine.addSystem(new Box2DDebugRendererEngine());               // Renders physics debug profiles
        engine.addSystem(new WindowEngine());                           // UI window engine
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

        // Translate camera
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-0.02f,0,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(0.02f,0,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0, -0.02f,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0, 0.02f,0);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
