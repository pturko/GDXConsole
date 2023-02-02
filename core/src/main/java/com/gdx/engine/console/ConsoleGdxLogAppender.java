package com.gdx.engine.console;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConsoleGdxLogAppender extends AppenderBase<ILoggingEvent> {

    private final ConcurrentMap<String, ILoggingEvent> eventMap = new ConcurrentHashMap<>();

    private static List<ConsoleMsgLog> cmdCommands = new ArrayList<>();

    @Override
    protected void append(ILoggingEvent event) {
        eventMap.put(String.valueOf(System.currentTimeMillis()), event);
        cmdCommands.add(new ConsoleMsgLog(event.getFormattedMessage(),
                event.getLevel().toString()));
    }

    public static List<ConsoleMsgLog> getCommandList() {
        return cmdCommands;
    }

}
