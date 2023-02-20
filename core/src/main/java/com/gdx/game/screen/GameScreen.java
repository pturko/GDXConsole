package com.gdx.game.screen;

import com.badlogic.ashley.core.PooledEngine;
import com.gdx.GdxGame;
import com.gdx.engine.engine.box2d.StaticSpriteRendererEngine;
import com.gdx.engine.box2d.entity.item.BoxEntity;
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
        // Initialize pooled engine
        engine = new PooledEngine();

        engine.addSystem(new B2DPhysicsEngine());                       // Physics engine

        engine.addSystem(new TiledMapEngine());                         // Renders TiledMap textures
        engine.addSystem(new CameraEngine());                           // Bound camera

        engine.addSystem(new StaticSpriteRendererEngine());             // Renders non-animated sprites

        // Add box entity object
        engine.addEntity(new BoxEntity(world, boxTexture, 4.4f, 2.2f));

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
