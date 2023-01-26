package com.gdx;

import com.badlogic.gdx.Game;
import com.gdx.game.screen.MenuScreen;

public class GdxGame extends Game {

	public void create() {
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
