package com.gdx.engine.screen.window;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.gdx.engine.console.ConsoleMsgLog;
import com.gdx.engine.event.ConfigChangedEvent;
import com.gdx.engine.event.EventType;
import com.gdx.engine.model.config.ApplicationConfig;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ConsoleWindow extends VisWindow {
	private static final List<ConsoleMsgLog> consoleCommands = new ArrayList<>();

	private static VisTextField consoleTextEdit;
	private static VisTable consoleTable;
	private static VisScrollPane consoleScrollPane;
	private static String lastCommand;

	private static final float W_POSITION_X = 5;
	private static final float W_POSITION_Y = 5;
	private static final float W_WIDTH = 400;
	private static final float W_HEIGHT = 450;

	public ConsoleWindow() {
		super(StringUtils.EMPTY);

		lastCommand = StringUtils.EMPTY;

		TableUtils.setSpacingDefaults(this);
		columnDefaults(0).left();

		consoleWidget();
		addCloseButtonCustom();
		configureListeners();

		setResizable(false);
		setSize(W_WIDTH, W_HEIGHT);
		setPosition(W_POSITION_X, W_POSITION_Y);
	}

	private void consoleWidget() {
		consoleTable = new VisTable();
		consoleTable.setVisible(ServiceFactoryImpl.getConfigService().getConsoleConfig().isShowConsole());

		consoleScrollPane = new VisScrollPane(consoleTable);
		consoleScrollPane.setFlickScroll(true);
		consoleScrollPane.setFadeScrollBars(true);

		consoleTextEdit = new VisTextField();
		VisTable bottomTable = new VisTable(true);
		VisTextButton cmdButton = new VisTextButton(" > ");
		cmdButton.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				lastCommand = consoleTextEdit.getText();
				ServiceFactoryImpl.getConsoleService().cmd(lastCommand);
				consoleTextEdit.setText(StringUtils.EMPTY);
			}
		});

		bottomTable.defaults();

		bottomTable.add(consoleTextEdit).expandX().fillX();
		bottomTable.add(cmdButton).fillX();

		add(consoleScrollPane).fillX().expandY().row();
		add(bottomTable).expandX().fillX().row();
		pack();

		// Init console with message queue
		consoleCommands.forEach(msg->
				consoleTable.add(new VisLabel(msg.getMessage(), msg.getColor()))
						.left().expand().row());
	}

	public static void addMessage(ConsoleMsgLog msg) {
		if (consoleTextEdit != null) {
			lastCommand = consoleTextEdit.getText();
			consoleTable.add(new VisLabel(msg.getMessage(), msg.getColor()))
					.left().expand().row();
		}

		consoleCommands.add(msg);
	}

	private void configureListeners() {
		// Event reload application config
		ServiceFactoryImpl.getEventService().addEventListener(EventType.CONFIG_CHANGED, (ConfigChangedEvent e) -> {
			update(e.getApplicationConfig());
		});
	}

	private void update(ApplicationConfig config) {
		consoleTable.setVisible(ServiceFactoryImpl.getConfigService().getConsoleConfig().isShowConsole());
	}

	public static VisTextField getCmdTextField() {
		return consoleTextEdit;
	}

	public static String getLastCmd() {
		return lastCommand;
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
