package com.gdx.engine.interfaces.service;

import com.gdx.engine.event.Event;

public interface EventService {
    void sendEvent(Event event);
    void clearEventListeners();
}
