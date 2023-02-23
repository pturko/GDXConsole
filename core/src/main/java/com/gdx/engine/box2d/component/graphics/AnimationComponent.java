package com.gdx.engine.box2d.component.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

public class AnimationComponent<T> extends Animation<T> implements Component {

    private float timer;

    /** Constructor, storing the frame duration and key frames.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames the objects representing the frames. If this Array is type-aware, {@link #getKeyFrames()} can return the
     *           correct type of array. Otherwise, it returns an Object[]. */
    public AnimationComponent(float frameDuration, Array<? extends T> keyFrames) {
        super(frameDuration, keyFrames);
    }

    /** Constructor, storing the frame duration and key frames.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames the objects representing the frames. If this Array is type-aware, {@link #getKeyFrames()} can
     * return the correct type of array. Otherwise, it returns an Object[].*/
    public AnimationComponent(float frameDuration, Array<? extends T> keyFrames, PlayMode playMode) {
        super(frameDuration, keyFrames);
        setPlayMode(playMode);
    }

    /** Constructor, storing the frame duration and key frames.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames the objects representing the frames. */
    public AnimationComponent(float frameDuration, T... keyFrames) {
        super(frameDuration, keyFrames);
        setKeyFrames(keyFrames);
    }


    /**
     * Gets the state timer of this animation.
     * @return state timer.
     */
    public float getTimer() {
        return timer;
    }

    /**
     * Updates the state timer.
     * @param delta delta time since the last frame.
     */
    public void update(float delta) {
        timer += delta;
    }

}