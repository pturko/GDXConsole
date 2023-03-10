package com.gdx.engine.screen.window;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gdx.engine.console.ConsoleMsgLog;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ConsoleWindow extends VisWindow {
	private static final List<ConsoleMsgLog> initCommands = new ArrayList<>();

	private static VisTextField consoleTextEdit;
	private static VisTable consoleTable;
	private static VisScrollPane consoleScrollPane;
	private static String lastCommand;

	public ConsoleWindow() {
		super(StringUtils.EMPTY);

		lastCommand = StringUtils.EMPTY;

		TableUtils.setSpacingDefaults(this);
		columnDefaults(0).left();

		addConsoleWidget();
		addCloseButton();

		setResizable(false);
		setSize(500, 450);
		setPosition(5, 5);
	}

	private void addConsoleWidget() {
		consoleTable = new VisTable();

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
		initCommands.forEach(msg->
				consoleTable.add(new VisLabel(msg.getMessage(), msg.getColor()))
						.left().expand().row());
	}

	public static void addMessage(ConsoleMsgLog msg) {
		if (consoleTextEdit == null) {
			// Add to queue if console don't init yet
			initCommands.add(msg);
		} else {
			lastCommand = consoleTextEdit.getText();
			consoleTable.add(new VisLabel(msg.getMessage(), msg.getColor()))
					.left().expand().row();
		}
	}

	public static VisTextField getCmdTextField() {
		return consoleTextEdit;
	}

	public static String getLastCmd() {
		return lastCommand;
	}

}
