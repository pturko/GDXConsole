package com.gdx.engine.screen.window;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.gdx.engine.service.TiledMapServiceImpl;
import com.kotcrab.vis.ui.widget.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapDetailsWindow extends VisWindow {

	private static final float W_POSITION_X = 370;
	private static final float W_POSITION_Y = 300;

	public MapDetailsWindow() {
		super("Map Details");

		columnDefaults(0).left();

		mapDetailsWidget();
		addCloseButtonCustom();

		setPosition(W_POSITION_X, W_POSITION_Y);
		pack();
	}

	private void mapDetailsWidget() {
		TiledMapServiceImpl tiledMapService = ServiceFactoryImpl.getTiledMapService();
		MapProperties properties = tiledMapService.getMapData().getTiledMap().getProperties();

		VisTable detailsTable = new VisTable(true);

		VisLabel labelName = new VisLabel("Name: " + tiledMapService.getMapName());
		VisLabel labelOrientation = new VisLabel("Orientation: " + properties.get("orientation"));
		VisLabel labelHexSideLength = new VisLabel("Hexsidelength: " + properties.get("hexsidelength"));
		VisLabel labelMapSize = new VisLabel("Size: " + properties.get("width") + "x" + properties.get("height"));
		VisLabel labelTileSize = new VisLabel("TileSize: " + properties.get("tilewidth") + "x" + properties.get("tileheight"));

		detailsTable.add(labelName).left().row();
		detailsTable.add(labelOrientation).left().row();
		detailsTable.add(labelHexSideLength).left().row();
		detailsTable.add(labelMapSize).left().row();
		detailsTable.add(labelTileSize).left().row();

		add(detailsTable).expandX().fillX().row();
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
