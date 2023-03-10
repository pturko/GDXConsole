package com.gdx.engine.event;

public class ConsoleEnabledEvent extends Event {

    public ConsoleEnabledEvent() {
        super(EventType.CONSOLE_ENABLED);
    }

    public boolean get() {
        return true;
    }

}