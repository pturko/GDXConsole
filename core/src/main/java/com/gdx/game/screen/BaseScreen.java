package com.gdx.game.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gdx.GdxGame;
import com.gdx.engine.model.config.ConsoleConfig;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.ConsoleServiceImpl;
import com.gdx.engine.service.ResourceLoaderServiceImpl;
import com.gdx.engine.service.WindowServiceImpl;
import org.apache.commons.lang3.StringUtils;


public class BaseScreen implements Screen {
    protected static ResourceLoaderServiceImpl resourceService;
    protected static ConfigServiceImpl configService;
    protected static WindowServiceImpl windowService;
    protected static ConsoleServiceImpl consoleService;

    protected GdxGame gdxGame;

    protected int screenHeight;
    protected int screenWidth;
    protected OrthographicCamera camera;
    protected SpriteBatch spriteBatch;

    protected ConsoleConfig consoleConfig;

    protected Stage baseStage;
    protected InputMultiplexer multiplexer;

    private String lastCmd;
    private Skin componentSkin;
    private Drawable consoleBg;
    private TextField cmdTextField;
    private BitmapFont consoleFont;
    private BitmapFont debugFont;

    private long now;
    private int frameCount;
    private long lastRender;
    private static int lastFPS;
    private final static int FPSUpdateInterval = 1;

    public BaseScreen(GdxGame gdxGame) {
        this.gdxGame = gdxGame;

        configService = ConfigServiceImpl.getInstance();
        resourceService = ResourceLoaderServiceImpl.getInstance();
        consoleService = ConsoleServiceImpl.getInstance();
        windowService = WindowServiceImpl.getInstance();

        windowService.setBaseScreen(this);

        consoleConfig = configService.getConsoleConfig();

        createResources();
        cameraSetup();
        createConsole();
        createInputProcessor();
    }

    private void createResources() {
        baseStage = new Stage();
        spriteBatch = new SpriteBatch();
        lastCmd = StringUtils.EMPTY;
        debugFont = resourceService.getFont("sans_serif");
    }

    private void cameraSetup() {
        windowService = WindowServiceImpl.getInstance();
        screenWidth = windowService.getScreenWidth();
        screenHeight = windowService.getScreenHeight();

        camera = windowService.getCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
    }

    private void createConsole() {
        componentSkin = resourceService.getSkin("uiskin");

        cmdTextField = new TextField(StringUtils.EMPTY, componentSkin);
        cmdTextField.setPosition(2, 15);
        cmdTextField.setSize(400, 24);
        baseStage.addActor(cmdTextField);

        consoleFont = resourceService.getDefaultFont();
        consoleBg = resourceService.getDrawable("uiskin", "default-window");
    }

    private void createInputProcessor() {
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(baseStage);
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

    public void drawingDebug() {
        //---------- Drawing console ----------------------------
        drawingConsole();

        //---------- Drawing FPS ----------------------------
        drawingFPS();
    }

    void drawingConsole() {
        if (consoleConfig.isShowConsole()) {
            spriteBatch.setColor(30, 30, 30, 0.5f);
            consoleBg.draw(spriteBatch,5,5,400,480);
            consoleService.draw(spriteBatch, consoleFont);

            spriteBatch.setColor(0, 0, 0, 1f);

            baseStage.draw();
            baseStage.act(Gdx.graphics.getDeltaTime());
        }
    }

    public void inputProcessing() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            lastCmd = cmdTextField.getText();
            consoleService.cmd(lastCmd);
            cmdTextField.setText(StringUtils.EMPTY);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            consoleService.cmd("config window showFPS");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            consoleService.cmd("config console show");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            cmdTextField.setText(lastCmd);
        }
    }

    void drawingFPS() {
        //---------- Drawing FPS ----------------------------
        if (configService.getWindowConfig().isShowFPS()) {
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

    public void resetConfig() {
    }

    public void resetResources() {
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
        resetConfig();
        resetResources();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}
