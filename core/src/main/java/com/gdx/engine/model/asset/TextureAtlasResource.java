package com.gdx.engine.model.asset;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TextureAtlasResource extends BaseResource {
    private TextureAtlas textureAtlas;
}
