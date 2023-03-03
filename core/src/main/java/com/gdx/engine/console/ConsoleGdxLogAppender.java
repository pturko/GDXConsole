package com.gdx.engine.console;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.ServiceFactoryImpl;

import java.util.ArrayList;
import java.util.List;

public class ConsoleGdxLogAppender extends AppenderBase<ILoggingEvent> {
    public static final int CONSOLE_MAX_MESSAGES = 25;
    private static final int CONSOLE_MAX_MESSAGE_LENGTH = 45;

    private static final List<ConsoleMsgLog> cmdCommands = new ArrayList<>();

    @Override
    protected void append(ILoggingEvent event) {
        ConfigServiceImpl configService = ServiceFactoryImpl.getConfigService();
        String consoleLogLevel = configService.getConsoleConfig().getLogLevel();
        validateAndPushMessage(event.getFormattedMessage(), event.getLevel().toString(), consoleLogLevel);

        String logLevel = event.getLevel().levelStr;
        if (logLevel.equals("ERROR") && configService.getConsoleConfig().isShowOnError() ||
                logLevel.equals("WARN") && configService.getConsoleConfig().isShowOnWarn()) {
            configService.getConsoleConfig().setShowConsole(true);
        }
    }

    public void validateAndPushMessage(String message, String msgLogLevel, String configLogLevel) {
        if (message.length() > CONSOLE_MAX_MESSAGE_LENGTH) {
            String firstMessage = message.substring(0, CONSOLE_MAX_MESSAGE_LENGTH);
            String secondMessage = message.substring(CONSOLE_MAX_MESSAGE_LENGTH);
            cmdCommands.add(new ConsoleMsgLog(firstMessage, msgLogLevel));
            validateAndPushMessage(secondMessage, msgLogLevel, configLogLevel);
        } else {
            ConsoleLogLevel configLevel = ConsoleLogLevel.valueOf(configLogLevel);
            ConsoleLogLevel msgLevel = ConsoleLogLevel.valueOf(msgLogLevel);
            if (configLevel.getLevel() <= msgLevel.getLevel()) {
                cmdCommands.add(new ConsoleMsgLog(message, msgLogLevel));
            }
        }
    }

    public static List<ConsoleMsgLog> getCommandList() {
        return cmdCommands;
    }

}
