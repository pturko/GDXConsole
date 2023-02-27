package com.gdx.engine.console;

import com.badlogic.gdx.graphics.Color;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ConsoleMsgLog {

    private final String message;
    private String logLevel;
    private Color color;
    private final String date;

    public ConsoleMsgLog(String message, String logLevel){
        this.message = message;
        this.logLevel = logLevel.substring(0,1).toUpperCase();
        this.color = getColor(logLevel);
        this.date = LocalTime.now()
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public String getDate() {
        return this.date;
    }

    public Color getColor() {
        return this.color;
    }

    public Color getColor(String logLevel) {
        switch (logLevel) {
            case "WARN":
                return Color.CORAL;
            case "ERROR":
                return Color.RED;
            case "DEBUG":
                return Color.CYAN;
            default:
                return Color.WHITE;
        }
    }

    public String getMessage() {
        return message;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
