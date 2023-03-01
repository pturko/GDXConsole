package com.gdx.engine.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.gdx.engine.interfaces.service.AudioService;
import com.gdx.engine.state.AudioState;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class AudioServiceImpl implements AudioService {

    private static AudioServiceImpl audioServiceInstance;
    private static AssetServiceImpl assetService;
    private static ConfigServiceImpl configService;

    private static Music currentMusic;
    private static Sound currentSound;
    private static HashMap<String, Music> queuedMusic;
    private static HashMap<String, Sound> queuedSound;

    AudioServiceImpl() {
        assetService = ServiceFactoryImpl.getAssetService();
        configService = ServiceFactoryImpl.getConfigService();
        queuedMusic = new HashMap<>();
        queuedSound = new HashMap<>();
    }

    public static AudioServiceImpl getInstance() {
        if(audioServiceInstance == null) {
            audioServiceInstance = new AudioServiceImpl();
        }
        return audioServiceInstance;
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public Sound getCurrentSound() {
        return currentSound;
    }

    public void setCurrentMusic(Music currentMusic) {
        this.currentMusic = currentMusic;
    }

    public void setCurrentSound(Sound currentSound) {
        this.currentSound = currentSound;
    }

    public void music(AudioState command, String name) {
        switch(command){
            case MUSIC_PLAY:
                playMusic(name, false);
                break;
            case MUSIC_PLAY_LOOP:
                playMusic(name, true);
                break;
            case MUSIC_STOP:
                Music music = queuedMusic.get(name);
                if (music != null) {
                    music.stop();
                }
                break;
            case MUSIC_STOP_ALL:
                for (Music m: queuedMusic.values()) {
                    m.stop();
                }
                break;
            default:
                break;
        }
    }

    public void sfx(AudioState command, String name) {
        switch(command){
            case SOUND_PLAY:
                playSound(name);
                break;
            case SOUND_STOP:
                Sound sound = queuedSound.get(name);
                if (sound != null) {
                    sound.stop();
                }
                break;
            case SOUND_STOP_ALL:
                for (Sound s: queuedSound.values()) {
                    s.stop();
                }
                break;
            default:
                break;
        }
    }

    public void stopAll(AudioState command) {
        if (command == AudioState.STOP_ALL) {
            for (Music m: queuedMusic.values()) {
                m.stop();
            }
            for (Sound s: queuedSound.values()) {
                s.stop();
            }
        }
    }

    private void playMusic(String name, boolean isLooping) {
        if (configService.getAudioConfig().isMusic()) {
            Music music = assetService.getMusic(name);
            music.setLooping(isLooping);
            music.setVolume(10L);
            setCurrentMusic(music);
            music.play();
            queuedMusic.put(name, music);
        }
    }

    private void playSound(String name) {
        if (configService.getAudioConfig().isSound()) {
            Sound sound = assetService.getSound(name);
            setCurrentSound(sound);
            setCurrentSound(sound);
            sound.play();
            queuedSound.put(name, sound);
        }
    }

    public void dispose() {
        for (Music m: queuedMusic.values()) {
            m.dispose();
        }
        for (Sound s: queuedSound.values()) {
            s.dispose();
        }
    }

}
