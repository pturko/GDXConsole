package com.gdx.engine.model.asset;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FontResource extends BaseResource {
    private BitmapFont font;
}
