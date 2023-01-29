package com.gdx;

import com.badlogic.gdx.Game;
import com.gdx.engine.service.ConsoleServiceImpl;
import com.gdx.engine.service.WindowServiceImpl;

public class GdxGame extends Game {

	private static WindowServiceImpl windowService;
	private static ConsoleServiceImpl consoleService;

	public void create() {
		windowService = WindowServiceImpl.getInstance();
		windowService.init(this);

		consoleService = ConsoleServiceImpl.getInstance();
		consoleService.cmd("screen menu");
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
