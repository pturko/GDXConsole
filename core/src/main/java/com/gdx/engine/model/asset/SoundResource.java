package com.gdx.engine.model.asset;

import com.badlogic.gdx.audio.Sound;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SoundResource extends BaseResource {
    private Sound sound;
}
