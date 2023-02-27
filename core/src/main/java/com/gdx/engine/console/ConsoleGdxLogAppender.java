package com.gdx.engine.console;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.ConsoleServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class ConsoleGdxLogAppender extends AppenderBase<ILoggingEvent> {

    private static ConfigServiceImpl configService;
    private static ConsoleServiceImpl consoleService;

    public static final int CONSOLE_MAX_MESSAGES = 25;
    private static final int CONSOLE_MAX_MESSAGE_LENGTH = 45;

    private static List<ConsoleMsgLog> cmdCommands = new ArrayList<>();

    @Override
    protected void append(ILoggingEvent event) {
        configService = ConfigServiceImpl.getInstance();

        String consoleLogLevel = configService.getConsoleConfig().getLogLevel();
        if (consoleLogLevel.equals("INFO") || consoleLogLevel.equals("WARN") ||
                consoleLogLevel.equals("ERROR") || consoleLogLevel.equals("DEBUG")) {
            validateAndPushMessage(event.getFormattedMessage(), event.getLevel().toString());
        }

        // Check to enable the console if error or warn log level message
        String logLevel = event.getLevel().levelStr;
        if (logLevel.equals("ERROR") && configService.getConsoleConfig().isShowOnError() ||
                logLevel.equals("WARN") && configService.getConsoleConfig().isShowOnWarn()) {
            configService.getConsoleConfig().setShowConsole(true);
            consoleService = ConsoleServiceImpl.getInstance();
            consoleService.resetActiveScreen();
        }
    }

    public void validateAndPushMessage(String message, String logLevel) {
        if (message.length() > CONSOLE_MAX_MESSAGE_LENGTH) {
            String firstMessage = message.substring(0, CONSOLE_MAX_MESSAGE_LENGTH);
            String secondMessage = message.substring(CONSOLE_MAX_MESSAGE_LENGTH, message.length());
            cmdCommands.add(new ConsoleMsgLog(firstMessage, logLevel));
            validateAndPushMessage(secondMessage, logLevel);
        } else {
            cmdCommands.add(new ConsoleMsgLog(message, logLevel));
        }
    }

    public static List<ConsoleMsgLog> getCommandList() {
        return cmdCommands;
    }

}
