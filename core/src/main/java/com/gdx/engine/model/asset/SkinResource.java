package com.gdx.engine.model.asset;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SkinResource extends BaseResource {
    private Skin skin;
}
