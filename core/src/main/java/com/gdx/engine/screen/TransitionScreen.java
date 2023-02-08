package com.gdx.engine.screen;

import com.gdx.GdxGame;
import com.gdx.engine.screen.transition.TransitionEffect;
import com.gdx.game.screen.BaseScreen;

import java.util.List;

public class TransitionScreen extends BaseScreen {
    private final GdxGame gdxGame;
    private final BaseScreen current;
    private final BaseScreen next;

    private int currentTransitionEffect;
    private List<TransitionEffect> transitionEffects;

    public TransitionScreen(GdxGame gdxGame, BaseScreen current, BaseScreen next, List<TransitionEffect> transitionEffects) {
        super(gdxGame);
        this.current = current;
        this.next = next;
        this.transitionEffects = transitionEffects;
        this.currentTransitionEffect = 0;
        this.gdxGame = gdxGame;
    }

    @Override
    public void render(float delta) {
        if  (currentTransitionEffect >= transitionEffects.size()) {
            gdxGame.setScreen(next);
            return;
        }

        transitionEffects.get(currentTransitionEffect).update(delta);
        transitionEffects.get(currentTransitionEffect).render(current, next);

        if (transitionEffects.get(currentTransitionEffect).isFinished()) {
            currentTransitionEffect++;
        }
    }

    @Override
    public void show() {
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
        current.dispose();
        next.dispose();
    }

}
