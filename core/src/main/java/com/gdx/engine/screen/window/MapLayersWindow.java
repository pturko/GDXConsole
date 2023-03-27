package com.gdx.engine.screen.window;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.gdx.engine.model.map.TiledMapLayerData;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.spinner.FloatSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MapLayersWindow extends VisWindow {

	private static final float W_POSITION_X = 548;
	private static final float W_POSITION_Y = 180;

	private VisCheckBox checkBoxIsVisible;
	private VisCheckBox checkBoxIsStatic;
	private VisCheckBox checkBoxIsSensor;

	private Spinner spinnerCategoryBits;
	private Spinner spinnerMaskBits;
	private Spinner spinnerFriction;

	private String firstLevelName = StringUtils.EMPTY;

	public MapLayersWindow() {
		super("Map Layers");

		columnDefaults(0).left();

		mapLayersWidget();
		addCloseButtonCustom();

		setPosition(W_POSITION_X, W_POSITION_Y);
		pack();
	}

	private void mapLayersWidget() {
		Array<MapLayer> listLayers = new Array<>();
		Array<String> listLayerNames = new Array<>();
		MapLayers mapLayers = ServiceFactoryImpl.getTiledMapService().getMapLayers();

		VisTable listSpinnerTable = new VisTable();
		VisList<String> list = new VisList<>();

		mapLayers.forEach(m -> {
			listLayers.add(m);
			listLayerNames.add(m.getName());
			if (firstLevelName.equals(StringUtils.EMPTY)) {
				firstLevelName = m.getName();
			}
		});
		list.setItems(listLayerNames);
		list.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				selectLayer(list.getSelected());
			}
		});

		VisTable mapLayersTable = new VisTable(true);

		checkBoxIsVisible = new VisCheckBox("Visible");
		mapLayersTable.add(checkBoxIsVisible).expandY().top().row();

		checkBoxIsStatic = new VisCheckBox("Static");
		mapLayersTable.add(checkBoxIsStatic).expandY().top().row();

		checkBoxIsSensor = new VisCheckBox("Sensor");
		mapLayersTable.add(checkBoxIsSensor).expandY().top().row();

		final IntSpinnerModel spinnerModelCategoryBit = new IntSpinnerModel(1, 0, 128, 1);
		final IntSpinnerModel spinnerModelMaskBits= new IntSpinnerModel(1, 0, 128, 1);
		final FloatSpinnerModel spinnerModelFriction = new FloatSpinnerModel("1", "0", "128", "1");
		spinnerCategoryBits = new Spinner("categoryBit", spinnerModelCategoryBit);
		spinnerMaskBits = new Spinner("maskBits", spinnerModelMaskBits);
		spinnerFriction = new Spinner("friction", spinnerModelFriction);
		mapLayersTable.add(spinnerCategoryBits).expandY().top().row();
		mapLayersTable.add(spinnerMaskBits).expandY().top().row();
		mapLayersTable.add(spinnerFriction).expandY().top().row();

		listSpinnerTable.add(list);
		listSpinnerTable.add(mapLayersTable);

		add(listSpinnerTable).expandX().fillX().padTop(3).row();

		selectLayer(firstLevelName);
	}

	public void selectLayer(String name) {
		log.info(name);
		TiledMapLayerData tiledMapLayerData = ServiceFactoryImpl.getTiledMapService().getMapData().getMapLayersData().get(name);
		checkBoxIsVisible.setChecked(tiledMapLayerData.isVisible());
		checkBoxIsStatic.setChecked(tiledMapLayerData.isStaticBody());
		checkBoxIsSensor.setChecked(tiledMapLayerData.isSensor());

		spinnerCategoryBits.getTextField().setText(tiledMapLayerData.getCategoryBits());
		spinnerMaskBits.getTextField().setText(tiledMapLayerData.getMaskBits());
		spinnerFriction.getTextField().setText(String.valueOf(tiledMapLayerData.getFriction()));
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
