# GDXConsole
#### LibGDX console with _LibGDX_, _BOX2D_ And _Tiled Map_
![build workflow](https://github.com/pturko/GDXConsole)

## Description
<p>GDXConsole</p>
<p>Console to simplify development of applications based on LibGDX</p>
<p>Perform many operations dynamically in runtime</p>>

## Features
- Dynamic resource loading
- Built-in console
- Profile management
- Running commands from file
- TiledMap support (in development)
- Box2D support (in development)

#### Console commands
- VER (VERSION) - version from application.json config
- INFO - send log message
- SCREEN <name> <effect> - set screen
  Effects: fade, fadein, circle
-RESOURCES
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
- CMD <profileName> - run commands from config/startup-<profileName>.json
- PROFILE - show active profile
- EXIT - exit program
