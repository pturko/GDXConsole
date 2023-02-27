package com.gdx.engine.interfaces.service;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.io.IOException;

public interface AssetService {
    void loadResources() throws IOException;
    Texture getTexture(String name);
    Texture getTexture(String textureAtlasOrSkinName, String name);
    Drawable getDrawable(String textureAtlasOrSkinName, String name);
    Skin getSkin(String skinName);
    SpriteBatch getBatch();
}
