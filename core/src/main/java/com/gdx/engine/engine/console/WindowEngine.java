package com.gdx.engine.engine.console;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gdx.engine.event.AssetChangedEvent;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.ConsoleEnabledEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.screen.window.ConsoleWindow;
import com.gdx.engine.service.*;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import org.apache.commons.lang3.StringUtils;

public class WindowEngine extends EntitySystem {
    private final SpriteBatch batch;
    private final Camera camera;
    private final Stage stage;
    private final Table table;
    private MenuBar menuBar;

    private boolean isShowConsole;

    public WindowEngine() {
        this.batch = ServiceFactoryImpl.getAssetService().getBatch();
        this.camera = ServiceFactoryImpl.getScreenService().getCamera();

        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        update(ServiceFactoryImpl.getConfigService().getApplicationConfig());
        createConsole();
        createMenuBar();
        configureListeners();

        Gdx.input.setInputProcessor(stage);
    }

    private void createConsole() {
        table.setFillParent(true);
        stage.addActor(table);

        stage.addListener(new InputListener() {
            boolean debug = false;
            @Override
            public boolean keyDown (InputEvent event, int keycode) {
                if (keycode == Input.Keys.F9) {
                    debug = !debug;
                    table.setDebug(debug, true);
                    for (Actor actor : stage.getActors()) {
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

        stage.setKeyboardFocus(ConsoleWindow.getCmdTextField());
    }

    private void createMenuBar() {
        menuBar = new MenuBar();
        menuBar.setMenuListener(new MenuBar.MenuBarListener() {
            @Override
            public void menuOpened (Menu menu) {}

            @Override
            public void menuClosed (Menu menu) {}
        });
        table.add(menuBar.getTable()).expandX().fillX().row();
        table.add().expand().fill();

        createMenus();
    }

    private void createMenus () {
        Menu consoleMenu = new Menu("Console");
        Menu assetMenu = new Menu("Asset");
        Menu mapMenu = new Menu("Map");
        Menu windowMenu = new Menu("Window");
        Menu helpMenu = new Menu("Help");

        consoleMenu.addItem(new MenuItem("Console", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                stage.addActor(new ConsoleWindow());
            }
        }));
        consoleMenu.addSeparator();
        consoleMenu.addItem(createConsoleMenu());

        assetMenu.addItem(new MenuItem("Load", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceFactoryImpl.getAssetService().loadResources();
            }
        }));
        assetMenu.addItem(new MenuItem("Reload", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceFactoryImpl.getAssetService().loadResources();
            }
        }));
        MenuItem subAssetClearItem = new MenuItem("Clear");
        subAssetClearItem.setDisabled(true);
        assetMenu.addItem(subAssetClearItem);

        mapMenu.addItem(createMapLoadMenu());
        mapMenu.addItem(new MenuItem("Reload", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                TiledMapServiceImpl tiledMapService = ServiceFactoryImpl.getTiledMapService();
                ServiceFactoryImpl.getTiledMapService().load(tiledMapService.getMapName());
            }
        }));
        mapMenu.addItem(new MenuItem("Clear", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceFactoryImpl.getTiledMapService().clear();
            }
        }));

        mapMenu.addSeparator();
        mapMenu.addItem(new MenuItem("Layers"));
        mapMenu.addItem(new MenuItem("Objects"));

        windowMenu.addItem(new MenuItem("Audio"));
        windowMenu.addItem(new MenuItem("Box2D"));

        helpMenu.addItem(new MenuItem("About", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Dialogs.showOKDialog(stage, "About", "GDXConsole application");
            }
        }));

        menuBar.addMenu(consoleMenu);
        menuBar.addMenu(assetMenu);
        menuBar.addMenu(mapMenu);
        menuBar.addMenu(windowMenu);
        menuBar.addMenu(helpMenu);
    }


    private MenuItem createConsoleMenu() {
        MenuItem item = new MenuItem("Run cmd file");

        PopupMenu menu = new PopupMenu();
        menu.addItem(new MenuItem("audio-enable", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                ServiceFactoryImpl.getConsoleService().runFileCommands("audio-enable");
            }
        }));
        menu.addItem(new MenuItem("audio-disable", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                ServiceFactoryImpl.getConsoleService().runFileCommands("audio-disable");
            }
        }));

        item.setSubMenu(menu);
        return item;
    }

    private MenuItem createMapLoadMenu() {
        MenuItem item = new MenuItem("Load");

        PopupMenu menu = new PopupMenu();
        menu.addItem(new MenuItem("test.tmx", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                ServiceFactoryImpl.getTiledMapService().load("test");
            }
        }));
        menu.addItem(new MenuItem("test2.tmx", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                ServiceFactoryImpl.getTiledMapService().load("test2");
            }
        }));

        item.setSubMenu(menu);
        return item;
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
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONSOLE_ENABLED, (ConsoleEnabledEvent e) ->
                stage.setKeyboardFocus(ConsoleWindow.getCmdTextField()));
    }

    private void update(ApplicationConfig config) {
        isShowConsole = config.getConsoleConfig().isShowConsole();
    }

    @Override
    public void update(float delta) {
        if (isShowConsole) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            batch.setColor(50, 50, 50, 0.5f);

            stage.act(Math.min(delta, 1 / 30f));
            stage.draw();

            batch.end();
        }
    }

}