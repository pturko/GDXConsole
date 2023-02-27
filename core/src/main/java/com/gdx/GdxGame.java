package com.gdx;

import com.badlogic.gdx.Game;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.ConsoleServiceImpl;
import com.gdx.engine.service.ScreenServiceImpl;

public class GdxGame extends Game {

	private static ConfigServiceImpl configService;
	private static ScreenServiceImpl screenService;
	private static ConsoleServiceImpl consoleService;

	public void create() {
		configService = ConfigServiceImpl.getInstance();
		consoleService = ConsoleServiceImpl.getInstance();

		// Initialize application configs
		configService.updateConfigs();

		// Initialize windows
		screenService = ScreenServiceImpl.getInstance();
		screenService.init(this);

		// Running commands
		consoleService.runCommands();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
