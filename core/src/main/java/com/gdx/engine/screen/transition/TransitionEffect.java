package com.gdx.engine.screen.transition;

import com.badlogic.gdx.Screen;

public class TransitionEffect {

    private TimeTransition timeTransition;

    public TransitionEffect(float duration) {
        timeTransition = new TimeTransition();
        timeTransition.start(duration);
    }

    protected float getAlpha() {
        return timeTransition.get();
    }

    public void update(float delta) {
        timeTransition.update(delta);
    }

    public void render(Screen current, Screen next) {
    }

    public boolean isFinished() {
        return timeTransition.isFinished();
    }

}
