package com.gdx.engine.engine.window;

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
import com.gdx.engine.event.ConfigConsoleChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.model.config.ConsoleConfig;
import com.gdx.engine.screen.window.*;
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
    private Box2DWindow box2DWindow;
    private DebugWindow debugWindow;
    private MapDetailsWindow mapDetailsWindow;
    private MapLayersWindow mapLayersWindow;

    public WindowEngine() {
        this.batch = ServiceFactoryImpl.getAssetService().getBatch();
        this.camera = ServiceFactoryImpl.getScreenService().getCamera();

        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        addKeyListener();
        createMenuBar();
        updateConfig();
        configureListeners();
    }

    private void addKeyListener() {
        table.setFillParent(true);
        stage.addActor(table);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown (InputEvent event, int keycode) {
                boolean debug = false;
                if (keycode == Input.Keys.F1) {
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
                if (keycode == Input.Keys.F2) {
                    ServiceFactoryImpl.getConsoleService().cmd("cfg console show");
                }
                if (keycode == Input.Keys.F3) {
                    if (mapDetailsWindow == null || mapDetailsWindow.getTouchable().name().equals("disabled")) {
                        mapDetailsWindow = new MapDetailsWindow();
                        stage.addActor(mapDetailsWindow);
                    }
                }
                if (keycode == Input.Keys.F4) {
                    if (mapLayersWindow == null || mapLayersWindow.getTouchable().name().equals("disabled")) {
                        mapLayersWindow = new MapLayersWindow();
                        stage.addActor(mapLayersWindow);
                    }
                }
                if (keycode == Input.Keys.F5) {
                    TiledMapServiceImpl tiledMapService = ServiceFactoryImpl.getTiledMapService();
                    ServiceFactoryImpl.getTiledMapService().load(tiledMapService.getMapName());
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
        if (ServiceFactoryImpl.getConfigService().getScreenConfig().getDebugConfig().isMenuBar()) {
            menuBar = new MenuBar();
            menuBar.setMenuListener(new MenuBar.MenuBarListener() {
                @Override
                public void menuOpened(Menu menu) {
                }

                @Override
                public void menuClosed(Menu menu) {
                }
            });
            table.add(menuBar.getTable()).expandX().fillX().row();
            table.add().expand().fill();

            createMenus();
        }
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
                    stage.addActor(consoleWindow);
                    ServiceFactoryImpl.getConfigService().getConsoleConfig().setShowConsole(true);
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
        }).setShortcut("F5"));
        mapMenu.addItem(new MenuItem("Clear", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceFactoryImpl.getTiledMapService().clear();
            }
        }));

        mapMenu.addSeparator();
        mapMenu.addItem(new MenuItem("Details", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if (mapDetailsWindow == null || mapDetailsWindow.getTouchable().name().equals("disabled")) {
                    mapDetailsWindow = new MapDetailsWindow();
                    stage.addActor(mapDetailsWindow);
                }
            }
        }).setShortcut("F3"));
        mapMenu.addItem(new MenuItem("Layers", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if (mapLayersWindow == null || mapLayersWindow.getTouchable().name().equals("disabled")) {
                    mapLayersWindow = new MapLayersWindow();
                    stage.addActor(mapLayersWindow);
                }
            }
        }).setShortcut("F4"));

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
                if (box2DWindow == null || box2DWindow.getTouchable().name().equals("disabled")) {
                    box2DWindow = new Box2DWindow();
                    stage.addActor(box2DWindow);
                }
            }
        }));
        windowMenu.addItem(new MenuItem("Debug", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if (debugWindow == null || debugWindow.getTouchable().name().equals("disabled")) {
                    debugWindow = new DebugWindow();
                    stage.addActor(debugWindow);
                }
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

    private void updateConfig() {
        ConsoleConfig consoleConfig = ServiceFactoryImpl.getConfigService().getConsoleConfig();
        if (consoleConfig.isShowConsole()) {
            if (consoleWindow == null || consoleWindow.getTouchable().name().equals("disabled")) {
                consoleWindow = new ConsoleWindow();
                stage.addActor(consoleWindow);
            }
        } else {
            if (consoleWindow != null) {
                consoleWindow.remove();
                consoleWindow = null;
            }
        }
    }

    private void configureListeners() {
        // Event reload application config
        ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_CONSOLE_CHANGED, (ConfigConsoleChangedEvent e) -> {
            updateConfig();
        });
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