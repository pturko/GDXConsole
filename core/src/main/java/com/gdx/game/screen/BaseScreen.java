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
import com.gdx.engine.model.config.*;
import com.gdx.game.map.WorldContactListener;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


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
    private final String SKIN_NAME = "uiskin";
    private final String CONSOLE_BG = "console";

    private boolean isShowConsole;
    private boolean isShowFPS;
    private boolean isShowHeap;

    // Console
    private String lastCmd = StringUtils.EMPTY;
    private Skin componentSkin;
    private Drawable consoleBg;
    private TextField cmdTextField;
    private BitmapFont consoleFont;
    private BitmapFont debugFont;

    //FPS counter
    private int frameCount;
    private long lastRender;
    private static int lastFPS;
    private final static int FPSUpdateInterval = 1;

    public BaseScreen(GdxGame gdxGame) {
        this.gdxGame = gdxGame;

        update(ServiceFactoryImpl.getConfigService().getApplicationConfig());
        createResources();
        cameraSetup();
        createConsole();
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
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_CHANGED, (ConfigChangedEvent e) -> {
            update(e.getApplicationConfig());
        });
    }

    private void update(ApplicationConfig config) {
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

        isShowConsole = config.getConsoleConfig().isShowConsole();
        isShowFPS = config.getScreenConfig().getDebugConfig().isShowFPS();
        isShowHeap = config.getScreenConfig().getDebugConfig().isShowHeap();
    }

    private void createConsole() {
        AssetServiceImpl assetService = ServiceFactoryImpl.getAssetService();
        componentSkin = assetService.getSkin(SKIN_NAME);

        cmdTextField = new TextField(StringUtils.EMPTY, componentSkin);
        cmdTextField.setPosition(7, 10);
        cmdTextField.setSize(396, 24);
        stage.addActor(cmdTextField);

        consoleFont = assetService.getDefaultFont();
        consoleBg = assetService.getDrawable(CONSOLE_BG);
        stage.setKeyboardFocus(cmdTextField);
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
        // Drawing console and debug information
        drawingConsole();
        drawingDebug();

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            consoleService.cmd("cfg window showFPS");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            consoleService.cmd("cfg console show");
            stage.setKeyboardFocus(cmdTextField);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            consoleService.cmd("cfg map rendering");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
            consoleService.cmd("cfg update");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            consoleService.cmd("map clear");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) {
            consoleService.cmd("map reload");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F7)) {
            consoleService.cmd("map load test");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F8)) {
            consoleService.cmd("map load test2");
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

    void drawingConsole() {
        // Drawing console
        if (isShowConsole) {
            spriteBatch.setProjectionMatrix(projectionMatrix);
            spriteBatch.begin();

            spriteBatch.setColor(50, 50, 50, 0.5f);
            consoleBg.draw(spriteBatch,5,5,400,480);
            consoleService.draw(spriteBatch, consoleFont);

            spriteBatch.setColor(200, 200, 200, 1f);

            stage.draw();
            stage.act(Gdx.graphics.getDeltaTime());

            spriteBatch.end();
        }
    }

    public void drawingDebug() {
        spriteBatch.setProjectionMatrix(projectionMatrix);
        spriteBatch.begin();

        drawingFPS();
        drawingHeap();
        debugFont.draw(spriteBatch, "F1-F8 service keys", 630, 20);

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
            debugFont.draw(spriteBatch, "fps:" + lastFPS, (screenWidth/2)-30, screenHeight-10);
        }
    }

    void drawingHeap() {
        // Drawing Heap information
        if (isShowHeap) {
            debugFont.draw(spriteBatch, "heap:" + Gdx.app.getJavaHeap(), (screenWidth/2)-70, screenHeight-30);
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
