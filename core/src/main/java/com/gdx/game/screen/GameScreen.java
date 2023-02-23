package com.gdx.game.screen;

import com.gdx.GdxGame;
import com.gdx.engine.box2d.entity.animated.Torch;
import com.gdx.engine.box2d.entity.sprite.BoxEntity;
import com.gdx.engine.engine.box2d.StaticSpriteRendererEngine;
import com.gdx.engine.engine.box2d.AnimatedSpriteRendererEngine;
import com.gdx.engine.engine.box2d.B2DPhysicsEngine;
import com.gdx.engine.engine.box2d.B2DebugRendererEngine;
import com.gdx.engine.engine.camera.CameraEngine;
import com.gdx.engine.engine.tlledmap.TiledMapEngine;


public class GameScreen extends BaseScreen {

    public GameScreen(GdxGame gdxGame) {
        super(gdxGame);

        loadResources();
        cameraSetup();
    }

    private void loadResources() {
        engine.addSystem(new B2DPhysicsEngine());                       // Physics engine

        engine.addSystem(new TiledMapEngine());                         // Renders TiledMap textures
        engine.addSystem(new CameraEngine());                           // Bound camera

        engine.addSystem(new StaticSpriteRendererEngine());             // Renders non-animated sprites
        engine.addSystem(new AnimatedSpriteRendererEngine());           // Renders animated sprites

        // Add static entity
        engine.addEntity(new BoxEntity("box", 4.4f, 2.2f, 22f, 16f));

        // Add animated entity
        engine.addEntity(new Torch("torch", 3.2f, 0.7f, 32f, 92f));
        engine.addEntity(new Torch("torch", 5.2f, 0.7f, 32f, 92f));

        engine.addSystem(new B2DebugRendererEngine());                  // Renders physics debug profiles.
        if (configService.getBox2DConfig().isBox2DDebugRenderer()) {
            engine.getSystem(B2DebugRendererEngine.class).setProcessing(true);
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        clearScreen();
        engine.update(delta);
        update(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
