package com.gdx.engine.engine.console;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gdx.engine.event.AssetChangedEvent;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.ConsoleEnabledEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.screen.window.ConsoleWindow;
import com.gdx.engine.service.*;
import org.apache.commons.lang3.StringUtils;

public class ConsoleEngine extends EntitySystem {
    private Stage consoleStage = new Stage();
    private Table consoleTable;

    private final Camera camera;
    private final SpriteBatch batch;

    private boolean isShowConsole;

    public ConsoleEngine() {
        this.batch = ServiceFactoryImpl.getAssetService().getBatch();
        this.camera = ServiceFactoryImpl.getScreenService().getCamera();

        update(ServiceFactoryImpl.getConfigService().getApplicationConfig());
        configureListeners();
        createConsole();

        consoleStage.addActor(new ConsoleWindow());
        consoleStage.addListener(new InputListener() {
            boolean debug = false;
            @Override
            public boolean keyDown (InputEvent event, int keycode) {
                if (keycode == Input.Keys.F9) {
                    debug = !debug;
                    consoleTable.setDebug(debug, true);
                    for (Actor actor : consoleStage.getActors()) {
                        if (actor instanceof Group) {
                            Group group = (Group) actor;
                            group.setDebug(debug, true);
                        }
                    }
                    return true;
                }
                if (keycode == Input.Keys.ENTER) {
                    ServiceFactoryImpl.getConsoleService().cmd(ConsoleWindow.getCmdTextField().getText());
                    ConsoleWindow.getCmdTextField().setText(StringUtils.EMPTY);
                }
                if (keycode == Input.Keys.TAB) {
                    ConsoleWindow.getCmdTextField().setText(ConsoleWindow.getLastCmd());
                }
                return false;
            }
        });
    }

    private void update(ApplicationConfig config) {
        isShowConsole = config.getConsoleConfig().isShowConsole();
    }

    private void createConsole() {
        consoleStage = new Stage(new ScreenViewport());
        consoleTable = new Table();
        consoleTable.setFillParent(true);
        consoleStage.addActor(consoleTable);

        Gdx.input.setInputProcessor(consoleStage);

        consoleStage.setKeyboardFocus(ConsoleWindow.getCmdTextField());
    }

    private void configureListeners() {
        // Event reload application config
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_CHANGED, (ConfigChangedEvent e) -> {
            update(e.getApplicationConfig());
            createConsole();
        });

        // Whenever the map is changed, remove previous light objects and update brightness
        ServiceFactoryImpl.getEventService().addEventListener(EventType.ASSET_CHANGED, (AssetChangedEvent e) ->
                createConsole());

        // Whenever the map is changed, remove previous light objects and update brightness
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONSOLE_ENABLED, (ConsoleEnabledEvent e) -> {
            consoleStage.setKeyboardFocus(ConsoleWindow.getCmdTextField());
        });
    }

    @Override
    public void update(float delta) {
        if (isShowConsole) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            batch.setColor(50, 50, 50, 0.5f);

            consoleStage.act(Math.min(delta, 1 / 30f));
            consoleStage.draw();

            batch.end();
        }
    }

}