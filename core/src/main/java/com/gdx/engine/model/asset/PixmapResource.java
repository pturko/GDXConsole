package com.gdx.engine.model.asset;

import com.badlogic.gdx.graphics.Pixmap;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PixmapResource extends BaseResource {
    private Pixmap pixmap;
}
