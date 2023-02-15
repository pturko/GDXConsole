package com.gdx.engine.service;

import com.badlogic.gdx.utils.Array;
import com.gdx.engine.event.Event;
import com.gdx.engine.interfaces.event.EventListener;
import com.gdx.engine.event.EventType;

import java.util.HashMap;
import java.util.Map;

public class EventServiceImpl {

    private static EventServiceImpl eventServiceInstance;

    private final Map<EventType, Array<EventListener>> listeners;

    private EventServiceImpl() {
        listeners = new HashMap<>();

        for (EventType gameEventType : EventType.values()) {
            listeners.put(gameEventType, new Array<>());
        }
    }

    public static EventServiceImpl getInstance() {
        if (eventServiceInstance == null) {
            eventServiceInstance = new EventServiceImpl();
        }
        return eventServiceInstance;
    }

    public void addEventListener(EventType eventType, EventListener<? extends Event> handler) {
        this.listeners.get(eventType).add(handler);
    }

    public void sendEvent(Event event) {
        EventType eventType = event.getEventType();

        for (EventListener<? super Event> listener : this.listeners.get(eventType)) {
            listener.handle(event);
        }
    }

    public void clearEventListeners() {
        for (Array<EventListener> listeners : listeners.values()) {
            listeners.clear();
        }
    }

}