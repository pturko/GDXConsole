package com.gdx.engine.service;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.IntMap;
import com.gdx.GdxGame;
import com.gdx.engine.interfaces.service.ScreenService;
import com.gdx.engine.model.config.CameraPositionConfig;
import com.gdx.engine.model.config.CameraViewportConfig;
import com.gdx.engine.model.config.ScreenConfig;
import com.gdx.engine.screen.ScreenItems;

import com.gdx.engine.screen.TransitionScreen;
import com.gdx.engine.screen.effects.CoolOutTransitionEffect;
import com.gdx.engine.screen.effects.FadeInTransitionEffect;
import com.gdx.engine.screen.effects.FadeOutTransitionEffect;
import com.gdx.engine.screen.transition.TransitionEffect;
import com.gdx.game.screen.BaseScreen;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class ScreenServiceImpl implements ScreenService {
    private static ConfigServiceImpl configService;

    private static ScreenServiceImpl screenServiceInstance;

    private final IntMap<Screen> screens;
    private static Screen activeScreen;
    private static BaseScreen baseScreen;
    private static OrthographicCamera camera;

    private GdxGame gdxGame;

    private ScreenServiceImpl() {
        camera = new OrthographicCamera();
        cameraSetup();
        screens = new IntMap<>();
    }
 
    public static synchronized ScreenServiceImpl getInstance() {
        if (null == screenServiceInstance) {
            screenServiceInstance = new ScreenServiceImpl();
        }
        return screenServiceInstance;
    }

    public void cameraSetup() {
        configService = ConfigServiceImpl.getInstance();
        ScreenConfig screenConfig = configService.getScreenConfig();
        CameraPositionConfig cameraPositionConfig = screenConfig.getCameraConfig().getCameraPositionConfig();
        CameraViewportConfig cameraViewportConfig = screenConfig.getCameraConfig().getCameraViewportConfig();

        camera.position.set(cameraPositionConfig.getX(), cameraPositionConfig.getY(), cameraPositionConfig.getZ());
        camera.viewportWidth = cameraViewportConfig.getWidth();
        camera.viewportHeight = cameraViewportConfig.getHeight();
    }

    @Override
    public void init(GdxGame gdxGame) {
        this.gdxGame = gdxGame;
    }

    @Override
    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public void show(String nextScreen, String transitionEffectName) {
        ScreenItems screenItem;
        if (null == gdxGame) return;
        try {
            screenItem = ScreenItems.valueOf(nextScreen);
        } catch (Exception e) {
            log.error("Screen '{}' not found", nextScreen);
            return;
        }
        if (!screens.containsKey(screenItem.ordinal())) {
            screens.put(screenItem.ordinal(), screenItem.getScreenInstance(gdxGame));
        }
        Screen nextActiveScreen = screens.get(screenItem.ordinal());
        if (null != transitionEffectName && !transitionEffectName.equals(StringUtils.EMPTY)) {
            nextActiveScreen = getTransitionScreen(transitionEffectName, nextActiveScreen);
        }
        log.info("Set screen: {}", nextScreen);
        gdxGame.setScreen(nextActiveScreen);
        activeScreen = nextActiveScreen;
    }

    private Screen getTransitionScreen(String transitionEffectName, Screen nextScreen) {
        List<TransitionEffect> effects = new ArrayList<>();
        switch (transitionEffectName.toUpperCase()) {
            case "FADE":
                if (null != activeScreen) {
                    effects.add(new FadeOutTransitionEffect(0.5f));
                }
                effects.add(new FadeInTransitionEffect(0.5f));
                break;
            case "FADEIN":
                effects.add(new FadeOutTransitionEffect(0.5f));
                break;
            case "CIRCLE":
                if (null != activeScreen) {
                    effects.add(new CoolOutTransitionEffect(1f));
                }
                break;
            default:
                log.warn("Can't found transition effect: {}", transitionEffectName);
        }

        return new TransitionScreen(gdxGame, (BaseScreen) activeScreen,
                (BaseScreen) nextScreen, effects);
    }

    public void setBaseScreen(BaseScreen baseScreen) {
        this.baseScreen = baseScreen;
    }

    public Screen getActiveScreen() {
        return activeScreen;
    }

    public void dispose(Screen screen) {
        if (screen != null) screen.hide();
    }

}
