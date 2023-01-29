package com.gdx.engine.model;
import com.gdx.engine.model.asset.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AssetResources implements Serializable {
    private List<TextureResource> textures;
    private List<TextureAtlasResource> textureAtlas;
    private List<PixmapResource> pixmap;
    private List<FontResource> font;
    private List<SkinResource> skin;
    private List<MusicResource> music;
    private List<SoundResource> sound;
}

