package com.gdx.game.screen;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gdx.GdxGame;
import com.gdx.engine.event.ConfigScreenChangedEvent;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.model.config.ScreenConfig;
import com.gdx.game.map.WorldContactListener;
import com.gdx.engine.event.EventType;
import com.gdx.engine.service.*;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BaseScreen implements Screen {
    protected static ConsoleServiceImpl consoleService;

    protected GdxGame gdxGame;
    protected Stage stage = new Stage();
    protected PooledEngine engine;
    protected World world;
    protected float screenHeight;
    protected float screenWidth;
    protected Camera camera;
    protected SpriteBatch spriteBatch;
    protected Matrix4 projectionMatrix;

    protected InputMultiplexer multiplexer;

    private final String FONT_NAME = "sans_serif";

    private boolean isShowFPS;
    private boolean isShowHeap;

    private BitmapFont debugFont;

    //FPS counter
    private int frameCount;
    private long lastRender;
    private static int lastFPS;
    private final static int FPSUpdateInterval = 1;

    public BaseScreen(GdxGame gdxGame) {
        this.gdxGame = gdxGame;

        updateConfig();
        createResources();
        cameraSetup();
        configureListeners();
        createInputProcessor();
    }

    private void createResources() {
        consoleService = ServiceFactoryImpl.getConsoleService();

        spriteBatch = ServiceFactoryImpl.getAssetService().getBatch();
        debugFont = ServiceFactoryImpl.getAssetService().getFont(FONT_NAME);
        
        ServiceFactoryImpl.getScreenService().setBaseScreen(this);
    }

    public void cameraSetup() {
        camera = ServiceFactoryImpl.getScreenService().getCamera();
    }

    private void configureListeners() {
        // Event reload application config
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_SCREEN_CHANGED, (ConfigScreenChangedEvent e) -> {
            updateConfig();
        });
    }

    private void updateConfig() {
        ApplicationConfig config = ServiceFactoryImpl.getConfigService().getApplicationConfig();
        ScreenConfig screenConfig = config.getScreenConfig();
        float ppm = config.getBox2DConfig().getPpm();

        screenWidth = screenConfig.getWidth();
        screenHeight = screenConfig.getHeight();
        projectionMatrix = new Matrix4().setToOrtho2D(0, 0, screenWidth, screenHeight);

        // Initialize pooled engine
        engine = ServiceFactoryImpl.getPooledEngineService().getEngine();

        // Initialize box2D world and contact listener
        world = ServiceFactoryImpl.getBox2DWorldService().getWorld();
        world.setContactListener(new WorldContactListener(engine));

        // Should scale the viewport with PPM
        stage.getViewport().setWorldSize(screenConfig.getWidth()/ppm,
                screenConfig.getHeight()/ppm);

        isShowFPS = screenConfig.getDebugConfig().isShowFPS();
        isShowHeap = screenConfig.getDebugConfig().isShowHeap();
    }

    private void createInputProcessor() {
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void addStageProcessor(Stage stage) {
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void addInputProcessor(InputProcessor inputProcessor) {
        multiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void update() {
        drawingDebug();
    }

    public void drawingDebug() {
        spriteBatch.setProjectionMatrix(projectionMatrix);
        spriteBatch.begin();

        drawingFPS();
        drawingHeap();

        spriteBatch.end();
    }

    void drawingFPS() {
        // Drawing FPS information
        if (isShowFPS) {
            frameCount++;
            long now = System.nanoTime();
            if ((now - lastRender) >= FPSUpdateInterval * 1000000000) {
                lastFPS = frameCount / FPSUpdateInterval;
                frameCount = 0;
                lastRender = System.nanoTime();
            }
            debugFont.draw(spriteBatch, "FPS:" + lastFPS, (screenWidth/2)-30, screenHeight-10);
        }
    }

    void drawingHeap() {
        // Drawing Heap information
        if (isShowHeap) {
            debugFont.draw(spriteBatch, "HEAP:" + Gdx.app.getJavaHeap(), (screenWidth/2)+60, screenHeight-10);
        }
    }

    public void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
