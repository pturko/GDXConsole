package com.gdx.engine.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.SerializationException;
import com.gdx.engine.interfaces.service.ResourceLoaderService;
import com.gdx.engine.model.AssetResources;
import com.gdx.engine.model.asset.*;
import com.gdx.engine.util.FileLoaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ResourceLoaderServiceImpl implements ResourceLoaderService {

    private static SpriteBatch batch;
    private static BitmapFont bitmapFont;
    private static AssetResources resources;

    private static ResourceLoaderServiceImpl resourceLoaderInstance;

    private static Map<String, TextureResource> textures;
    private static Map<String, TextureAtlasResource> textureAtlas;
    private static Map<String, PixmapResource> pixmaps;
    private static Map<String, FontResource> fonts;
    private static Map<String, SkinResource> skins;
    private static Map<String, MusicResource> music;
    private static Map<String, SoundResource> sound;

    private static final String ASSET = "asset/";
    private static final String CONFIG_FOLDER = "config/";
    private static final String CONFIG_CONSOLE_CMD = "consoleCmd/startup-";
    private static final String CONFIG_RESOURCES = "resources/";
    private static final String FONT = "font/";
    private static final String IMAGE_PIXMAP = "image/pixmap/";
    private static final String IMAGE_SKIN = "image/skin/";
    private static final String IMAGE_TEXTURE = "image/texture/";
    private static final String IMAGE_TEXTURE_ATLAS = "image/textureAtlas/";
    private static final String MUSIC = "audio/music/";
    private static final String SOUND = "audio/sfx/";

    private static final String DEFAULT_SKIN = "uiskin";
    private static final String DEFAULT_AUDIO = "click.mp3";
    private static final String RESOURCE_FILE = "resources";
    private static final String RESOURCE_FILE_EXT = ".json";

    private ResourceLoaderServiceImpl() {
        serviceReset();
    }

    public void serviceReset() {
        batch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        textures = new HashMap<>();
        textureAtlas = new HashMap<>();
        pixmaps = new HashMap<>();
        skins = new HashMap<>();
        fonts = new HashMap<>();
        music = new HashMap<>();
        sound = new HashMap<>();
    }

    public static synchronized ResourceLoaderServiceImpl getInstance( ) {
        if (resourceLoaderInstance == null)
            resourceLoaderInstance = new ResourceLoaderServiceImpl();
        return resourceLoaderInstance;
    }

    public void loadResources() {
        dispose();
        serviceReset();
        String fileResource = ASSET + CONFIG_FOLDER + CONFIG_RESOURCES + RESOURCE_FILE + RESOURCE_FILE_EXT;
        try {
            resources = FileLoaderUtil.getResources(fileResource);
        } catch (IOException e) {
            log.error("Can't load resource file: {}", fileResource);
        }

        List<TextureResource> mTextures = resources.getTextures();
        for (TextureResource m : mTextures) {
            Texture texture;
            String fileName = ASSET + IMAGE_TEXTURE + m.getFileName();
            try {
                texture = new Texture(FileLoaderUtil.getFileHandle(fileName));
            } catch (Exception e) {
                log.error("File '{}' not found!", fileName);
                texture = null;
            }
            m.setTexture(texture);
            textures.put(m.getName(), m);
        }

        List<TextureAtlasResource> mTextureAtlases = resources.getTextureAtlas();
        for (TextureAtlasResource m : mTextureAtlases) {
            TextureAtlas txAtlas;
            String fileName = ASSET + IMAGE_TEXTURE_ATLAS + m.getFileName();
            try {
                txAtlas = new TextureAtlas(FileLoaderUtil.getFileHandle(fileName));
            } catch (Exception e) {
                log.error("File '{}' not found!", fileName);
                txAtlas = null;
            }
            m.setTextureAtlas(txAtlas);
            textureAtlas.put(m.getName(), m);
        }

        List<SkinResource> mSkins = resources.getSkin();
        for (SkinResource m : mSkins) {
            Skin skin;
            String fileName = ASSET + IMAGE_SKIN + m.getFileName();
            try {
                skin = new Skin(FileLoaderUtil.getFileHandle(fileName));
            } catch (SerializationException e) {
                log.error("Serialization Exception! {}", e);
                skin = null;
            } catch (Exception e) {
                log.error("File '{}' not found!", fileName);
                skin = null;
            }
            m.setSkin(skin);
            skins.put(m.getName(), m);
        }

        List<PixmapResource> mPixmaps = resources.getPixmap();
        for (PixmapResource m : mPixmaps) {
            Pixmap pixmap;
            String fileName = ASSET + IMAGE_PIXMAP + m.getFileName();
            try {
                pixmap = new Pixmap(FileLoaderUtil.getFileHandle(fileName));
            } catch (Exception e) {
                log.error("File '{}' not found!", fileName);
                pixmap = null;
            }
            m.setPixmap(pixmap);
            pixmaps.put(m.getName(), m);
        }

        List<FontResource> mFont = resources.getFont();
        for (FontResource m : mFont) {
            BitmapFont font;
            String fileName = ASSET + FONT + m.getFileName();
            try {
                font = new BitmapFont(FileLoaderUtil.getFileHandle(fileName));
            } catch (Exception e) {
                log.error("File '{}' not found!", fileName);
                font = null;
            }
            m.setFont(font);
            fonts.put(m.getName(), m);
        }

        List<MusicResource> mMusic = resources.getMusic();
        for (MusicResource m : mMusic) {
            Music audio;
            String fileName = ASSET + MUSIC + m.getFileName();
            try {
                audio = Gdx.audio.newMusic(FileLoaderUtil.getFileHandle(fileName));
            } catch (Exception e) {
                log.error("File '{}' not found!", fileName);
                audio = null;
            }
            m.setMusic(audio);
            music.put(m.getName(), m);
        }

        List<SoundResource> mSound = resources.getSound();
        for (SoundResource m : mSound) {
            Sound audio;
            String fileName = ASSET + SOUND + m.getFileName();
            try {
                audio = Gdx.audio.newSound(FileLoaderUtil.getFileHandle(fileName));
            } catch (Exception e) {
                log.error("File '{}' not found!", fileName);
                audio = null;
            }
            m.setSound(audio);
            sound.put(m.getName(), m);
        }
    }

    public Texture getTexture(String name) {
        if (textures.get(name) != null) {
            return textures.get(name).getTexture();
        } else {
            log.warn("Texture '{}' not found!", name);
            return getDefaultTexture();
        }
    }

    public Texture getTexture(String textureAtlasOrSkinName, String name) {
        if (textureAtlasOrSkinName.equals(StringUtils.EMPTY) && textures.get(name) != null) {
            return textures.get(name).getTexture();
        } else {

            //Trying to find texture from textureAtlas
            if (textureAtlas.get(textureAtlasOrSkinName) != null) {
                TextureAtlas txAtlas = textureAtlas.get(textureAtlasOrSkinName).getTextureAtlas();
                if (txAtlas != null && txAtlas.findRegion(name) != null) {
                    return extractFromTextureRegion(txAtlas.findRegion(name));
                }
            }

            //Trying to find texture from skin
            if (skins.get(textureAtlasOrSkinName) != null) {
                Skin skin = skins.get(textureAtlasOrSkinName).getSkin();
                TextureAtlas txAtlas = skin.getAtlas();
                if (txAtlas != null && txAtlas.findRegion(name) != null) {
                    return extractFromTextureRegion(txAtlas.findRegion(name));
                }
            }

            if (!textureAtlasOrSkinName.equals(StringUtils.EMPTY)) {
                log.warn("Texture '{}' from '{}' not found!", name, textureAtlasOrSkinName);
            } else {
                log.warn("Texture '{}' not found!", name);
            }

            return getDefaultTexture();
        }
    }

    public Drawable getDrawable(String textureAtlasOrSkinName, String name) {
        if (!textureAtlasOrSkinName.equals(StringUtils.EMPTY)) {
            //Trying to find drawable from textureAtlas
            if (textureAtlas.get(textureAtlasOrSkinName) != null) {
                if (textureAtlas.get(textureAtlasOrSkinName) != null) {
                    Skin skin = new Skin(textureAtlas.get(textureAtlasOrSkinName).getTextureAtlas());
                    if (skin.getDrawable(name) != null) {
                        return skin.getDrawable(name);
                    }
                }
            }

            //Trying to find drawable from skin
            if (skins.get(textureAtlasOrSkinName) != null) {
                Skin skin = skins.get(textureAtlasOrSkinName).getSkin();
                if (skin != null) {
                    return getDrawableFromSkin(skin, textureAtlasOrSkinName, name);
                }
            }
        }

        log.warn("Drawable '{}' not found! TextureAtlas or skin not provided", name);
        return getDefaultDrawable();
    }

    public static TextureAtlas.AtlasRegion getRegion(String textureAtlasOrSkinName, String name) {
        //Trying to find texture from textureAtlas
        if (textureAtlas.get(textureAtlasOrSkinName) != null) {
            TextureAtlas txAtlas = textureAtlas.get(textureAtlasOrSkinName).getTextureAtlas();
            if (txAtlas != null && txAtlas.findRegion(name) != null) {
                return txAtlas.findRegion(name);
            }
        }

        //Trying to find image from skin
        if (skins.get(textureAtlasOrSkinName) != null) {
            Skin skin = skins.get(textureAtlasOrSkinName).getSkin();
            TextureAtlas txAtlas = skin.getAtlas();
            if (txAtlas != null && txAtlas.findRegion(name) != null) {
                return txAtlas.findRegion(name);
            }
        }

        if (!textureAtlasOrSkinName.equals(StringUtils.EMPTY)) {
            log.warn("Region '{}' from '{}' not found!", name, textureAtlasOrSkinName);
        } else {
            log.warn("Region '{}' not found!", name);
        }

        return new TextureAtlas.AtlasRegion(new TextureRegion(getDefaultTexture()));
    }

    public static Image getImage(String textureAtlasOrSkinName, String name) {
        if (textureAtlasOrSkinName.equals(StringUtils.EMPTY) && textures.get(name) != null) {
            return new Image(textures.get(name).getTexture());
        } else {

            //Trying to find image from textureAtlas
            if (textureAtlas.get(textureAtlasOrSkinName) != null) {
                TextureAtlas txAtlas = textureAtlas.get(textureAtlasOrSkinName).getTextureAtlas();
                if (txAtlas != null && txAtlas.findRegion(name) != null) {
                    return new Image(txAtlas.findRegion(name));
                }
            }

            //Trying to find image from skin
            if (skins.get(textureAtlasOrSkinName) != null) {
                Skin skin = skins.get(textureAtlasOrSkinName).getSkin();
                TextureAtlas txAtlas = skin.getAtlas();
                if (txAtlas != null && txAtlas.findRegion(name) != null) {
                    return new Image(txAtlas.findRegion(name));
                }
            }

            if (!textureAtlasOrSkinName.equals(StringUtils.EMPTY)) {
                log.warn("Image '{}' from '{}' not found!", name, textureAtlasOrSkinName);
            } else {
                log.warn("Image '{}' not found!", name);
            }

            return new Image(getDefaultTexture());
        }
    }

    public Skin getSkin(String skinName) {
        if (skins.get(skinName) != null) {
            return skins.get(skinName).getSkin();
        } else {
            log.warn("Skin '{}' not found!", skinName);
            return new Skin();
        }
    }

    public static BitmapFont getDefaultFont() {
        return bitmapFont;
    }

    private static Drawable getDefaultDrawable() {
        return new Image(new Texture(getDefaultPixmap())).getDrawable();
    }

    private static Drawable getDrawableFromSkin(Skin skin, String textureAtlasOrSkinName, String name) {
        try {
            return skin.getDrawable(name);
        } catch(Exception exception) {
            log.warn("Drawable '{}' from '{}' not found!", name, textureAtlasOrSkinName);
        }
        return getDefaultDrawable();
    }

    private static Pixmap getDefaultPixmap() {
        final Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.CORAL);
        pixmap.fill();
        return pixmap;
    }

    private static Texture getDefaultTexture() {
        final Pixmap pixmap = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.ORANGE);
        pixmap.fill();
        final Texture result = new Texture(pixmap);
        pixmap.dispose();
        return result;
    }

    public SpriteBatch getBatch() {
        return batch;
    }


    public static Music getMusic(String name) {
        String musicName = name.toLowerCase();
        if (music.get(musicName) != null) {
            return music.get(musicName).getMusic();
        } else {
            log.warn("Music '{}' not found!", musicName);
            return getDefaultMusic();
        }
    }

    public static Sound getSound(String name) {
        String soundName = name.toLowerCase();
        if (sound.get(soundName) != null) {
            return sound.get(soundName).getSound();
        } else {
            log.warn("Sound '{}' not found!", soundName);
            return getDefaultSfx();
        }
    }

    private static Music getDefaultMusic() {
        return Gdx.audio.newMusic(FileLoaderUtil.getFileHandle(
                ASSET + DEFAULT_AUDIO));
    }

    private static Sound getDefaultSfx() {
        return Gdx.audio.newSound(FileLoaderUtil.getFileHandle(
                ASSET + DEFAULT_AUDIO));
    }

    public static String getConsoleCmdPathFile(String profileName) {
        return ASSET + CONFIG_FOLDER + CONFIG_CONSOLE_CMD + profileName + RESOURCE_FILE_EXT;
    }

    public static BitmapFont getFont(String name) {
        String fontName = name.toLowerCase();
        if (fonts.get(fontName) != null) {
            return fonts.get(fontName).getFont();
        } else {
            log.warn("Font '{}' not found! ", fontName);
            return getDefaultFont();
        }
    }

    private static Texture extractFromTextureRegion(TextureRegion textureRegion) {
        TextureData textureData = textureRegion.getTexture().getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }
        Pixmap pixmap = new Pixmap(
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight(),
                textureData.getFormat()
        );
        pixmap.drawPixmap(
                textureData.consumePixmap(), // The other Pixmap
                0, // The target x-coordinate (top left corner)
                0, // The target y-coordinate (top left corner)
                textureRegion.getRegionX(), // The source x-coordinate (top left corner)
                textureRegion.getRegionY(), // The source y-coordinate (top left corner)
                textureRegion.getRegionWidth(), // The width of the area from the other Pixmap in pixels
                textureRegion.getRegionHeight() // The height of the area from the other Pixmap in pixels
        );
        return new Texture(pixmap);
    }

    public void dispose() {
        batch = null;
        textures.clear();
        textureAtlas.clear();
        pixmaps.clear();
        skins.clear();
        fonts.clear();
        music.clear();
        sound.clear();
    }

}
