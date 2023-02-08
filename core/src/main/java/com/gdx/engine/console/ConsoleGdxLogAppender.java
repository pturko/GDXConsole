package com.gdx.engine.console;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.ConsoleServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConsoleGdxLogAppender extends AppenderBase<ILoggingEvent> {

    private static ConfigServiceImpl configService;
    private static ConsoleServiceImpl consoleService;

    private final ConcurrentMap<String, ILoggingEvent> eventMap = new ConcurrentHashMap<>();

    private static List<ConsoleMsgLog> cmdCommands = new ArrayList<>();

    @Override
    protected void append(ILoggingEvent event) {
        eventMap.put(String.valueOf(System.currentTimeMillis()), event);
        cmdCommands.add(new ConsoleMsgLog(event.getFormattedMessage(),
                event.getLevel().toString()));

        configService = ConfigServiceImpl.getInstance();
        String logLevel = event.getLevel().levelStr;
        if (logLevel.equals("ERROR") && configService.getConsoleConfig().isShowOnError() ||
                logLevel.equals("WARN") && configService.getConsoleConfig().isShowOnWarn()) {
            configService.getConsoleConfig().setShowConsole(true);
            consoleService = ConsoleServiceImpl.getInstance();
            consoleService.resetActiveScreen();
        }
    }

    public static List<ConsoleMsgLog> getCommandList() {
        return cmdCommands;
    }

    public static ConsoleMsgLog getLast() {
        return cmdCommands.get(cmdCommands.size()-1);
    }

}
