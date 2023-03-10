package com.gdx.engine.console;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.ServiceFactoryImpl;

public class ConsoleGdxLogAppender extends AppenderBase<ILoggingEvent> {

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
        ConsoleLogLevel configLevel = ConsoleLogLevel.valueOf(configLogLevel);
        ConsoleLogLevel msgLevel = ConsoleLogLevel.valueOf(msgLogLevel);
        if (configLevel.getLevel() <= msgLevel.getLevel()) {
            ServiceFactoryImpl.getConsoleService().addMessage(new ConsoleMsgLog(message, msgLogLevel));
        }
    }

}
