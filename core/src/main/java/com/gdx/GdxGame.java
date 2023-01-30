package com.gdx;

import com.badlogic.gdx.Game;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.engine.service.ConsoleServiceImpl;
import com.gdx.engine.service.WindowServiceImpl;

public class GdxGame extends Game {

	private static ConfigServiceImpl configService;
	private static WindowServiceImpl windowService;
	private static ConsoleServiceImpl consoleService;

	public void create() {
		configService = ConfigServiceImpl.getInstance();
		consoleService = ConsoleServiceImpl.getInstance();
		windowService = WindowServiceImpl.getInstance();

		// Initialize application configs
		configService.updateConfigs();

		//Initialize windows
		windowService.init(this);

		consoleService.cmd("screen menu");
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
