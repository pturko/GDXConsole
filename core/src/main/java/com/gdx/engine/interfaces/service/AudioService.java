package com.gdx.engine.interfaces.service;

import com.gdx.engine.state.AudioState;

public interface AudioService {
    void music(AudioState command, String name);
    void sfx(AudioState command, String name);
}
