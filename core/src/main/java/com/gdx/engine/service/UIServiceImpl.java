package com.gdx.engine.service;

import com.gdx.engine.interfaces.service.UIService;
import com.kotcrab.vis.ui.VisUI;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UIServiceImpl implements UIService {
    private static UIServiceImpl uiServiceInstance;

    public static synchronized UIServiceImpl getInstance( ) {
        if (uiServiceInstance == null) {
            uiServiceInstance = new UIServiceImpl();
        }
        return uiServiceInstance;
    }

    @Override
    public void load(String skinName) {
        VisUI.load(skinName);
    }

    public void dispose() {
    }

}
