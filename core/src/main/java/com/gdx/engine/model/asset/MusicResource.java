package com.gdx.engine.model.asset;

import com.badlogic.gdx.audio.Music;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MusicResource extends BaseResource {
    private Music music;
}
