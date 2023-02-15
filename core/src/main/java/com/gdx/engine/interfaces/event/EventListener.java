package com.gdx.engine.interfaces.event;

import com.gdx.engine.event.Event;

public interface EventListener<T extends Event> {
    void handle(T e);
}