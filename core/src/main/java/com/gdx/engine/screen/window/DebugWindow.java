package com.gdx.engine.screen.window;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.gdx.engine.event.ConfigScreenChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DebugWindow extends VisWindow {

	private static final float W_POSITION_X = 370;
	private static final float W_POSITION_Y = 480;

	private static VisCheckBox fpsCheckBox;
	private static VisCheckBox heapCheckBox;

	public DebugWindow() {
		super("Debug");

		columnDefaults(0).left();

		debugWidget();
		addCloseButtonCustom();
		configureListeners();

		setPosition(W_POSITION_X, W_POSITION_Y);
		pack();
	}

	private void debugWidget() {
		VisTable debugTable = new VisTable(true);
		debugTable.defaults().left();

		fpsCheckBox = new VisCheckBox("FPS");
		fpsCheckBox.setChecked(ServiceFactoryImpl.getConfigService().getScreenConfig().getDebugConfig().isShowFPS());
		fpsCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				ApplicationConfig applicationConfig = ServiceFactoryImpl.getConfigService().getApplicationConfig();
				applicationConfig.getScreenConfig().getDebugConfig().setShowFPS(fpsCheckBox.isChecked());
				ServiceFactoryImpl.getEventService().sendEvent(new ConfigScreenChangedEvent());
				log.info("fps: {}", fpsCheckBox.isChecked());
			}
		});

		heapCheckBox = new VisCheckBox("Heap");
		heapCheckBox.setChecked(ServiceFactoryImpl.getConfigService().getScreenConfig().getDebugConfig().isShowHeap());
		heapCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				ApplicationConfig applicationConfig = ServiceFactoryImpl.getConfigService().getApplicationConfig();
				applicationConfig.getScreenConfig().getDebugConfig().setShowHeap(heapCheckBox.isChecked());
				ServiceFactoryImpl.getEventService().sendEvent(new ConfigScreenChangedEvent());
				log.info("heap: {}", heapCheckBox.isChecked());
			}
		});

		debugTable.add(fpsCheckBox).row();
		debugTable.add(heapCheckBox).row();

		add(debugTable).expandX().fillX().padTop(3).row();
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

	private void configureListeners() {
		// Event reload screen config
		ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_SCREEN_CHANGED, (ConfigScreenChangedEvent e) -> {
			updateConfig();
		});
	}

	private void updateConfig() {
//		ScreenConfig screenConfig = ServiceFactoryImpl.getConfigService().getScreenConfig();
//		fpsCheckBox.setChecked(screenConfig.getDebugConfig().isShowFPS());
//		heapCheckBox.setChecked(screenConfig.getDebugConfig().isShowHeap());
	}

}
