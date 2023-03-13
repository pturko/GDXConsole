package com.gdx;

import com.badlogic.gdx.Game;
import com.gdx.engine.service.ServiceFactoryImpl;

public class GdxGame extends Game {

	public void create() {
		// Initialize UI service
		ServiceFactoryImpl.getUIService().load();

		// Initialize application configs
		ServiceFactoryImpl.getConfigService().updateConfigs();

		// Initialize windows
		ServiceFactoryImpl.getScreenService().init(this);

		// Running commands
		ServiceFactoryImpl.getConsoleService().runProfileCommands();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
