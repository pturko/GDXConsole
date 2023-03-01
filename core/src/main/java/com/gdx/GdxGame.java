package com.gdx;

import com.badlogic.gdx.Game;
import com.gdx.engine.service.ServiceFactoryImpl;

public class GdxGame extends Game {

	public void create() {
		// Initialize application configs
		ServiceFactoryImpl.getConfigService().updateConfigs();

		// Initialize windows
		 ServiceFactoryImpl.getScreenService().init(this);

		// Running commands
		ServiceFactoryImpl.getConsoleService().runCommands();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
