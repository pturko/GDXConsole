package com.gdx.engine.screen.window;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.kotcrab.vis.ui.widget.*;

public class AudioWindow extends VisWindow {

	private static final float W_POSITION_X = 720;
	private static final float W_POSITION_Y = 480;

	public AudioWindow() {
		super("Audio");

		columnDefaults(0).left();

		audioWidget();
		addCloseButtonCustom();

		setPosition(W_POSITION_X, W_POSITION_Y);
		pack();
	}

	private void audioWidget() {
		VisTable audioTable = new VisTable(true);
		audioTable.defaults().left();

		VisCheckBox musicCheckBox = new VisCheckBox("Music");
		musicCheckBox.setChecked(ServiceFactoryImpl.getConfigService().getAudioConfig().isMusic());
		musicCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				ServiceFactoryImpl.getConsoleService().cmd("cfg audio music " + musicCheckBox.isChecked());
			}
		});

		VisCheckBox soundCheckBox = new VisCheckBox("Sound");
		soundCheckBox.setChecked(ServiceFactoryImpl.getConfigService().getAudioConfig().isSound());
		soundCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				ServiceFactoryImpl.getConsoleService().cmd("cfg audio sound " + soundCheckBox.isChecked());
			}
		});

		audioTable.add(musicCheckBox).row();
		audioTable.add(soundCheckBox).row();

		add(audioTable).expandX().fillX().padTop(3).row();
		add().expand().fill().padBottom(2);
	}

	public void addCloseButtonCustom() {
		Label titleLabel = getTitleLabel();
		Table titleTable = getTitleTable();

		VisImageButton closeButton = new VisImageButton("close-window");
		titleTable.add(closeButton).padRight(-getPadRight() + 0.7f);
		closeButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				fadeOut();
				remove();
				getColor().a = 1f;
			}
		});
		closeButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.cancel();
				return true;
			}
		});

		if (titleLabel.getLabelAlign() == Align.center && titleTable.getChildren().size == 2)
			titleTable.getCell(titleLabel).padLeft(closeButton.getWidth() * 2);
	}

}
