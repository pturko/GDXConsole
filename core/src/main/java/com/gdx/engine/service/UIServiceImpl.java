package com.gdx.engine.service;

import com.gdx.engine.interfaces.service.UIService;
import com.kotcrab.vis.ui.VisUI;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UIServiceImpl implements UIService {
    private static UIServiceImpl uiServiceInstance;

    private static final String SKIN_FOLDER = "asset/image/skin/";
    private static final String SKIN_NAME = "uiskin.json"; // TODO - receive from asset

    public static synchronized UIServiceImpl getInstance( ) {
        if (uiServiceInstance == null) {
            uiServiceInstance = new UIServiceImpl();
        }
        return uiServiceInstance;
    }

    @Override
    public void load() {
        VisUI.load(SKIN_FOLDER + SKIN_NAME);
    }

    public void dispose() {
    }

}
