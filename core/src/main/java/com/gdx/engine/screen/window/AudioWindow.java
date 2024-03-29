package com.gdx.engine.screen.window;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.gdx.engine.event.ConfigAudioChangedEvent;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.kotcrab.vis.ui.widget.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AudioWindow extends VisWindow {

	private static final float W_POSITION_X = 460;
	private static final float W_POSITION_Y = 480;

	private static VisCheckBox musicCheckBox;
	private static VisCheckBox soundCheckBox;

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

		musicCheckBox = new VisCheckBox("Music");
		musicCheckBox.setChecked(ServiceFactoryImpl.getConfigService().getAudioConfig().isMusic());
		musicCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				ApplicationConfig applicationConfig = ServiceFactoryImpl.getConfigService().getApplicationConfig();
				applicationConfig.getAudioConfig().setMusic(musicCheckBox.isChecked());
				ServiceFactoryImpl.getEventService().sendEvent(new ConfigAudioChangedEvent());
				log.info("music: {}", musicCheckBox.isChecked());
			}
		});

		soundCheckBox = new VisCheckBox("Sound");
		soundCheckBox.setChecked(ServiceFactoryImpl.getConfigService().getAudioConfig().isSound());
		soundCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				ApplicationConfig applicationConfig = ServiceFactoryImpl.getConfigService().getApplicationConfig();
				applicationConfig.getAudioConfig().setSound(soundCheckBox.isChecked());
				ServiceFactoryImpl.getEventService().sendEvent(new ConfigAudioChangedEvent());
				log.info("sound: {}", soundCheckBox.isChecked());
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

		if (titleLabel.getLabelAlign() == Align.center && titleTable.getChildren().size == 2) {
			titleTable.getCell(titleLabel).padLeft(closeButton.getWidth() * 2);
		}
	}

}
