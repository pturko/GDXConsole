package com.gdx.engine.engine.console;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.ConsoleEnabledEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.screen.window.AudioWindow;
import com.gdx.engine.screen.window.ConsoleWindow;
import com.gdx.engine.service.*;
import com.gdx.engine.util.FileLoaderUtil;
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

    private ConsoleWindow consoleWindow;
    private AudioWindow audioWindow;

    public WindowEngine() {
        this.batch = ServiceFactoryImpl.getAssetService().getBatch();
        this.camera = ServiceFactoryImpl.getScreenService().getCamera();

        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        addKeyListener();
        createMenuBar();
        update(ServiceFactoryImpl.getConfigService().getApplicationConfig());
        configureListeners();
    }

    private void addKeyListener() {
        table.setFillParent(true);
        stage.addActor(table);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown (InputEvent event, int keycode) {
                if (keycode == Input.Keys.F2) {
                    ServiceFactoryImpl.getConsoleService().cmd("cfg console show");
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

        Gdx.input.setInputProcessor(stage);
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
                if (consoleWindow == null || consoleWindow.getTouchable().name().equals("disabled")) {
                    consoleWindow = new ConsoleWindow();
                    consoleWindow.setVisible(true);
                    stage.addActor(consoleWindow);
                }
            }
        }).setShortcut("F2"));
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

        windowMenu.addItem(new MenuItem("Audio", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if (audioWindow == null || audioWindow.getTouchable().name().equals("disabled")) {
                    audioWindow = new AudioWindow();
                    stage.addActor(audioWindow);
                }
            }
        }));
        windowMenu.addItem(new MenuItem("Box2D", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
            }
        }));

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
        for (FileHandle entry: FileLoaderUtil.getCmdFileList()) {
            String fileName = entry.name().substring(0, entry.name().length()-5);
            menu.addItem(new MenuItem(fileName, new ChangeListener() {
                @Override
                public void changed (ChangeEvent event, Actor actor) {
                    ServiceFactoryImpl.getConsoleService().runFileCommands(fileName);
                }
            }));
        }
        item.setSubMenu(menu);
        return item;
    }

    private MenuItem createMapLoadMenu() {
        MenuItem item = new MenuItem("Load");

        PopupMenu menu = new PopupMenu();
        for (FileHandle entry: FileLoaderUtil.getMapFileList()) {
            if (entry.extension().equals("tmx")) {
                String fileName = entry.name().substring(0, entry.name().length() - 4);
                menu.addItem(new MenuItem(fileName, new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ServiceFactoryImpl.getTiledMapService().load(fileName);
                    }
                }));
            }
        }

        item.setSubMenu(menu);
        return item;
    }

    private void update(ApplicationConfig config) {
        if (consoleWindow == null || consoleWindow.getTouchable().name().equals("disabled")) {
            if (config.getConsoleConfig().isShowConsole()) {
                consoleWindow = new ConsoleWindow();
                stage.addActor(consoleWindow);
            }
        }
    }

    private void configureListeners() {
        // Event reload application config
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_CHANGED, (ConfigChangedEvent e) -> {
            update(e.getApplicationConfig());
        });

        // Whenever the map is changed, remove previous light objects and update brightness
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONSOLE_ENABLED, (ConsoleEnabledEvent e) ->
                stage.setKeyboardFocus(ConsoleWindow.getCmdTextField()));
    }

    @Override
    public void update(float delta) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            stage.act(Math.min(delta, 1 / 30f));
            stage.draw();

            batch.end();
    }

}