package com.gdx;

import com.badlogic.gdx.Game;
import com.gdx.engine.service.WindowServiceImpl;

public class GdxGame extends Game {

	private WindowServiceImpl windowService;

	public void create() {
		windowService = WindowServiceImpl.getInstance();
		windowService.init(this);
		windowService.show("MENU");
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
