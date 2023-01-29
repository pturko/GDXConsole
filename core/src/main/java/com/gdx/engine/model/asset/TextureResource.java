package com.gdx.engine.model.asset;

import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TextureResource extends BaseResource {
    private Texture texture;
}
