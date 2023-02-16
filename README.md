# GDXConsole
#### LibGDX console with _LibGDX_, _BOX2D_, _BOX2DLight_ And _Tiled Map_
![GitHub](https://github.com/pturko/GDXConsole)

## Description
<p>GDXConsole</p>
<p>Console to simplify development of applications based on LibGDX</p>
<p>Perform many operations dynamically in runtime</p>

<p>For the opportunity to update config dynamically - external file loading should be used</p>
<p>FileHandle fileHandle = Gdx.files.external(CONFIG_FILE); in ConfigServiceImpl.updateConfigs()</p>

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
- CONFIG
  - CONSOLE
    - show - show/hide console (F2 key)
  - WINDOW
    - showFPS - show/hide FPS (F1 key)
  - AUDIO
    - music - enable/disable music
    - sound  - enable/disable sound
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
