# GDXConsole
#### LibGDX console with _LibGDX_, _BOX2D_, _BOX2DLight_ And _Tiled Map_
![GitHub](https://github.com/pturko/GDXConsole)

## Description
<p>GDXConsole</p>
<p>Console to simplify development of applications based on LibGDX</p>
<p>Perform many operations dynamically in runtime</p>

<p>For the opportunity to update config dynamically - external file loading should be used</p>
<p>ResourceLoaderServiceImpl.EXTERNAL_APPLICATION_CONFIG = true</p>

![screenshot](./previews/screenshot.png)

## Features
- Dynamic resource loading
- Built-in console
- TiledMap support
- Box2D and Box2DLight support
- Modular system - enable/disable each module
- Profile management
- Running commands from file (scripting)

#### Console commands
- VER (VERSION) - version from application.json config
- INFO - send log message
- SCREEN <name> <effect> - set screen
  Effects: fade, fadein, circle
- RESOURCES
  - load - load resources from resources\resources.json
- CFG - config management
  - CONSOLE
    - show - show/hide console (F2 key)
  - WINDOW
    - showFPS - show/hide FPS (F1 key)
  - MAP
    - rendering - rendering tiled map
  - BOX2D
    - rendering - rendering box2d world
    - sprite - box2d static (non-animated) sprite rendering
  - AUDIO
    - music - enable/disable music
    - sound  - enable/disable sound
  - UPDATE - reload application config (EXTERNAL_APPLICATION_CONFIG should be true)
- MUSIC
  - play <name>
  - playLoop <name>
  - stop <name>
  - stopAll
- SOUND
  - play <name>
  - stop <name>
  - stopAll <name>
- MAP
  - load <mapName> - loading tiled map
- CMD <profileName> - run commands from config/startup-<profileName>.json
- PROFILE - show active profile
- EXIT - exit program
