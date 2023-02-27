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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gdx.GdxGame;
import com.gdx.engine.model.config.Box2DConfig;
import com.gdx.game.map.WorldContactListener;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.model.config.ConsoleConfig;
import com.gdx.engine.model.config.DebugConfig;
import com.gdx.engine.model.config.ScreenConfig;
import com.gdx.engine.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class BaseScreen implements Screen {
    protected static AssetServiceImpl assetService;
    protected static ConfigServiceImpl configService;
    protected static ScreenServiceImpl screenService;
    protected static ConsoleServiceImpl consoleService;
    protected static EventServiceImpl eventService;
    protected static Box2DWorldImpl box2DService;
    protected static PooledEngineServiceImpl pooledEngineService;

    protected static ConsoleConfig consoleConfig;
    protected static ScreenConfig screenConfig;
    protected static DebugConfig debugConfig;
    protected static Box2DConfig box2DConfig;

    protected GdxGame gdxGame;

    protected Stage stage;
    protected PooledEngine engine;
    protected World world;
    protected float screenHeight;
    protected float screenWidth;
    protected Camera camera;
    protected SpriteBatch spriteBatch;
    protected Matrix4 projectionMatrix;

    protected InputMultiplexer multiplexer;

    // Console
    private String lastCmd;
    private Skin componentSkin;
    private Drawable consoleBg;
    private TextField cmdTextField;
    private BitmapFont consoleFont;
    private BitmapFont debugFont;

    // FPS counter
    private long now;
    private int frameCount;
    private long lastRender;
    private static int lastFPS;
    private final static int FPSUpdateInterval = 1;

    public BaseScreen(GdxGame gdxGame) {
        this.gdxGame = gdxGame;

        configService = ConfigServiceImpl.getInstance();
        assetService = AssetServiceImpl.getInstance();
        consoleService = ConsoleServiceImpl.getInstance();
        screenService = ScreenServiceImpl.getInstance();
        eventService = EventServiceImpl.getInstance();
        box2DService = Box2DWorldImpl.getInstance();
        pooledEngineService = PooledEngineServiceImpl.getInstance();

        createResources();
        cameraSetup();
        createConsole();
        configureListeners();
        createInputProcessor();
    }

    private void createResources() {
        stage = new Stage();
        lastCmd = StringUtils.EMPTY;

        spriteBatch = assetService.getBatch();
        debugFont = assetService.getFont("sans_serif");
        consoleConfig = configService.getConsoleConfig();
        screenConfig = configService.getScreenConfig();
        box2DConfig = configService.getBox2DConfig();
        debugConfig = configService.getScreenConfig().getDebugConfig();
        screenService.setBaseScreen(this);

        screenWidth = screenConfig.getWidth();
        screenHeight = screenConfig.getHeight();
        projectionMatrix = new Matrix4().setToOrtho2D(0, 0, screenWidth, screenHeight);

        // Initialize pooled engine
        engine = pooledEngineService.getEngine();

        // Initialize box2D world and contact listener
        world = box2DService.getWorld();
        world.setContactListener(new WorldContactListener(engine));

        // Should scale the viewport with PPM
        stage.getViewport().setWorldSize(screenConfig.getWidth()/box2DConfig.getPpm(),
                screenConfig.getHeight()/box2DConfig.getPpm());
    }

    public void cameraSetup() {
        camera = screenService.getCamera();
    }

    private void configureListeners() {
        // Reload application config
        eventService.addEventListener(EventType.CONFIG_CHANGED, (ConfigChangedEvent e) -> {
            consoleConfig = e.getApplicationConfig().getConsoleConfig();
            screenService.cameraSetup();

            screenConfig = configService.getScreenConfig();
            screenWidth = screenConfig.getWidth();
            screenHeight = screenConfig.getHeight();

            stage.getViewport().setWorldSize(screenConfig.getWidth()/box2DConfig.getPpm(),
                    screenConfig.getHeight()/box2DConfig.getPpm());
        });
    }

    private void createConsole() {
        componentSkin = assetService.getSkin("uiskin");

        cmdTextField = new TextField(StringUtils.EMPTY, componentSkin);
        cmdTextField.setPosition(7, 10);
        cmdTextField.setSize(396, 24);
        stage.addActor(cmdTextField);

        consoleFont = assetService.getDefaultFont();
        consoleBg = assetService.getDrawable("console");
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

    public void update(float delta) {
        //---------- Drawing console and debug information ----------------------------
        drawingDebug();

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            consoleService.cmd("cfg window showFPS");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            consoleService.cmd("cfg console show");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            consoleService.cmd("cfg map rendering");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
            consoleService.cmd("cfg update");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            lastCmd = cmdTextField.getText();
            consoleService.cmd(lastCmd);
            cmdTextField.setText(StringUtils.EMPTY);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            cmdTextField.setText(lastCmd);
        }
    }

    public void drawingDebug() {
        spriteBatch.setProjectionMatrix(projectionMatrix);
        spriteBatch.begin();

        drawingConsole();
        drawingFPS();
        drawingHeap();

        spriteBatch.end();

        camera.update();
    }

    void drawingConsole() {
        if (consoleConfig.isShowConsole()) {
            spriteBatch.setColor(50, 50, 50, 0.5f);
            consoleBg.draw(spriteBatch,5,5,400,480);
            consoleService.draw(spriteBatch, consoleFont);

            spriteBatch.setColor(200, 200, 200, 1f);

            stage.draw();
            stage.act(Gdx.graphics.getDeltaTime());
        }
    }

    void drawingFPS() {
        //---------- Drawing FPS ----------------------------
        if (debugConfig.isShowFPS()) {
            frameCount++;
            now = System.nanoTime();
            if ((now - lastRender) >= FPSUpdateInterval * 1000000000) {
                lastFPS = frameCount / FPSUpdateInterval;
                frameCount = 0;
                lastRender = System.nanoTime();
            }
            debugFont.draw(spriteBatch, "fps:" + lastFPS, (screenWidth/2)-30, screenHeight-10);
        }
    }

    void drawingHeap() {
        //---------- Drawing Heap ----------------------------
        if (debugConfig.isShowHeap()) {
            debugFont.draw(spriteBatch, "heap:" + Gdx.app.getJavaHeap(), 0, screenHeight-10);
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
