package com.gdx.engine.screen.window;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.service.EventServiceImpl;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

public class Box2DWindow extends VisWindow {

	private static final float W_POSITION_X = 600;
	private static final float W_POSITION_Y = 356;

	public Box2DWindow() {
		super("Box2D");

		columnDefaults(0).left();

		box2dWidget();
		addCloseButtonCustom();

		setPosition(W_POSITION_X, W_POSITION_Y);
		pack();
	}

	private void box2dWidget() {
		VisTable box2DTable = new VisTable(true);
		box2DTable.defaults().left();

		VisCheckBox box2DRenderingCheckBox = new VisCheckBox("Box2DUpdate");
		box2DRenderingCheckBox.setChecked(ServiceFactoryImpl.getConfigService().getBox2DConfig().isRendering());
		box2DRenderingCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				ServiceFactoryImpl.getConfigService().getBox2DConfig().setRendering(box2DRenderingCheckBox.isChecked());
				ServiceFactoryImpl.getEventService().sendEvent(
						new ConfigChangedEvent(ServiceFactoryImpl.getConfigService().getApplicationConfig()));
			}
		});

		VisCheckBox box2DEnableContactsCheckBox = new VisCheckBox("Box2DEnableContacts");
		box2DEnableContactsCheckBox.setChecked(ServiceFactoryImpl.getConfigService().getBox2DConfig().isEnableContacts());
		box2DEnableContactsCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				ServiceFactoryImpl.getConfigService().getBox2DConfig().setEnableContacts(box2DEnableContactsCheckBox.isChecked());
				ServiceFactoryImpl.getEventService().sendEvent(
						new ConfigChangedEvent(ServiceFactoryImpl.getConfigService().getApplicationConfig()));
			}
		});

		VisCheckBox box2DDebugRenderingCheckBox = new VisCheckBox("Box2DDebugRendering");
		box2DDebugRenderingCheckBox.setChecked(ServiceFactoryImpl.getConfigService().getBox2DConfig().isBox2DDebugRendering());
		box2DDebugRenderingCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				ServiceFactoryImpl.getConfigService().getBox2DConfig().setBox2DDebugRendering(box2DDebugRenderingCheckBox.isChecked());
				ServiceFactoryImpl.getEventService().sendEvent(
						new ConfigChangedEvent(ServiceFactoryImpl.getConfigService().getApplicationConfig()));
			}
		});

		VisCheckBox box2DStaticSpriteRenderingCheckBox = new VisCheckBox("Box2DStaticSpriteRendering");
		box2DStaticSpriteRenderingCheckBox.setChecked(ServiceFactoryImpl.getConfigService().getBox2DConfig().isStaticSpriteRendering());
		box2DStaticSpriteRenderingCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				ServiceFactoryImpl.getConfigService().getBox2DConfig().setStaticSpriteRendering(box2DStaticSpriteRenderingCheckBox.isChecked());
				ServiceFactoryImpl.getEventService().sendEvent(
						new ConfigChangedEvent(ServiceFactoryImpl.getConfigService().getApplicationConfig()));
			}
		});

		VisCheckBox box2DAnimatedSpriteRenderingCheckBox = new VisCheckBox("Box2DAnimatedSpriteRendering");
		box2DAnimatedSpriteRenderingCheckBox.setChecked(ServiceFactoryImpl.getConfigService().getBox2DConfig().isAnimatedSpriteRendering());
		box2DAnimatedSpriteRenderingCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				ServiceFactoryImpl.getConfigService().getBox2DConfig().setAnimatedSpriteRendering(box2DAnimatedSpriteRenderingCheckBox.isChecked());
				ServiceFactoryImpl.getEventService().sendEvent(
						new ConfigChangedEvent(ServiceFactoryImpl.getConfigService().getApplicationConfig()));
			}
		});

		VisCheckBox box2DLightsRenderingCheckBox = new VisCheckBox("Box2DLightsRendering");
		box2DLightsRenderingCheckBox.setChecked(ServiceFactoryImpl.getConfigService().getBox2DConfig().isStaticSpriteRendering());
		box2DLightsRenderingCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				ServiceFactoryImpl.getConfigService().getBox2DConfig().getBox2DLightsConfig().setRendering(box2DLightsRenderingCheckBox.isChecked());
				ServiceFactoryImpl.getEventService().sendEvent(
						new ConfigChangedEvent(ServiceFactoryImpl.getConfigService().getApplicationConfig()));
			}
		});

		box2DTable.add(box2DRenderingCheckBox).row();
		box2DTable.add(box2DEnableContactsCheckBox).row();
		box2DTable.add(box2DDebugRenderingCheckBox).row();
		box2DTable.add(box2DStaticSpriteRenderingCheckBox).row();
		box2DTable.add(box2DAnimatedSpriteRenderingCheckBox).row();
		box2DTable.add(box2DLightsRenderingCheckBox).row();

		add(box2DTable).expandX().fillX().padTop(3).row();
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
