package com.gdx.engine.model.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ConsoleCmd implements Serializable {
    private List<String> cmd;
}
