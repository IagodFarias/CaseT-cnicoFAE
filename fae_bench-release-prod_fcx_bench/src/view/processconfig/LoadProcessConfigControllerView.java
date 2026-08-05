//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file LoadFlowRateControllerView.java
*    @author osmenio
*    @date 13 de mar de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.processconfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import enumerations.SceneTypeEnum;
import enumerations.ViewActionEnum;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import si.dbcomm.model.CalculatedFixedConstModel;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.ProcessConfigModel;
import si.dbcomm.service.ProcessConfigService;
import view.controller.SceneFactory;

/**
 * @author marcos
 *
 */
public class LoadProcessConfigControllerView {

	@FXML
	private AnchorPane root;

	@FXML
	private Button btnAction;

	@FXML
	private Button btnCancel;

	// Select table
	@FXML
	private TableView<ProcessConfigModel> processConfigTable;

	@FXML
	private TableColumn columnSelect;

	@FXML
	private TableColumn<ProcessConfigModel, Long> columnId;

	@FXML
	private TableColumn<ProcessConfigModel, String> columnDescription;

	@FXML
	private TableColumn columnDelete;

	// ----------------------------------------------------------------
	// Details Process

	@FXML
	private CheckBox cbChooseQaSamples;

	@FXML
	private TextField tfMeterRadioConfigTries;

	@FXML
	private TextField tfMeterDateUpdateTries;

	// Process steps
	@FXML
	private CheckBox cbZeroFlowEnable;

	@FXML
	private CheckBox cbCalibration;

	@FXML
	private CheckBox cbVerification;

	// Calib
	@FXML
	private TextField tfInitCalibTemp;

	@FXML
	private TextField tfMaxTrimAgcTries;

	@FXML
	private TextField tfPercentageToVerif;

	// Pump
	@FXML
	private TextField tfLinePressurePumpLoad;

	@FXML
	private TextField tfTimeCheckPressure;
	// Times
	@FXML
	private TextField tfZeroFlowTime;

	@FXML
	private TextField tfFlowStabilizationTime;

	@FXML
	private TextField tfFlowStabilyCheckTime;

	@FXML
	private TextField tfFlowRateTimeout;

	// Matfile
	@FXML
	private CheckBox cbSaveMatData;

	@FXML
	private CheckBox cbSaveMatDataReprovedMeters;

	@FXML
	private CheckBox cbSaveMatDataCalibLut;

	// Sizes
	@FXML
	private TextField tfFlowBuffSize;

	@FXML
	private TextField tfWindowSize;

	// Calib tab
	@FXML
	private TableView<FlowRateModel> tvListFlowRateCalib;

	@FXML
	private TableColumn<FlowRateModel, Double> columnListFlowRateCalib;

	@FXML
	private TableColumn<FlowRateModel, String> columnListDescriptionCalib;

	@FXML
	private TableColumn<FlowRateModel, String> columnListRefMeterCalib;

	@FXML
	private TableColumn<FlowRateModel, String> columnListRamalCalib;

	@FXML
	private TableColumn<FlowRateModel, String> columnListPumpCalib;

	// Verif tab
	@FXML
	private TableView<FlowRateModel> tvListFlowRateVerif;

	@FXML
	private TableColumn<FlowRateModel, Double> columnListFlowRateVerif;

	@FXML
	private TableColumn<FlowRateModel, String> columnListDescriptionVerif;

	@FXML
	private TableColumn<FlowRateModel, String> columnListRefmMeterVerif;

	@FXML
	private TableColumn<FlowRateModel, String> columnListRamalVerif;

	@FXML
	private TableColumn<FlowRateModel, String> columnListPumpVerif;

	// Calculated fixed const
	@FXML
	private TableView<CalculatedFixedConstModel> tvCalculateFixConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnDescriptionConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnFlowRateConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnKConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnReConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnTempConst;

	//
	private ProcessConfigService processConfigService;

	// private ObservableList<ProcessConfigModel> processConfigData;

	private Consumer<ProcessConfigModel> processConfigCallback;
	//
	private HashMap<CheckBox, Boolean> hashCheck = new HashMap<>();

	private ViewActionEnum viewAction = ViewActionEnum.NEW;

	private ProcessConfigModel selectedProcessConfig;

	/**
	 * 
	 */
	public void setCallback(Consumer<ProcessConfigModel> callback) {
		this.processConfigCallback = callback;
	}

	/**
	 *
	 */
	private void refreshTable() {
		// processConfigData = FXCollections.observableArrayList(processConfigService.findAll());
		// processConfigTable.setItems(processConfigData);
		processConfigTable.setItems(FXCollections.observableArrayList(processConfigService.findAll()));
	}

	/**
	*
	*/
	private void updateView(ProcessConfigModel processConfig) {

		// Process
		// cbRadioWmBus.setSelected(processConfig.isRadioWmBus());
		cbChooseQaSamples.setSelected(processConfig.isChooseQaSamples());
		tfMeterRadioConfigTries.setText("" + processConfig.getMeterRadioConfigTries());
		tfMeterDateUpdateTries.setText("" + processConfig.getMeterDateUpdateTries());
		// Process steps
		cbZeroFlowEnable.setSelected(processConfig.isZeroFlowEnabled());
		cbCalibration.setSelected(!processConfig.isOnlyVerification());
		cbVerification.setSelected(true);
		// Calib
		tfInitCalibTemp.setText("" + processConfig.getInitCalibTemp());
		tfMaxTrimAgcTries.setText("" + processConfig.getTrimAgcMaxTries());
		tfPercentageToVerif.setText("" + processConfig.getPercentageToVerif());
		// Pump
		tfLinePressurePumpLoad.setText("" + processConfig.getLinePressurePumpLoad());
		tfTimeCheckPressure.setText("" + processConfig.getTimeCheckPressurePumpLoad());
		// Times
		tfZeroFlowTime.setText("" + processConfig.getZeroFlowTime());
		tfFlowStabilizationTime.setText("" + processConfig.getPreFlowStabilizationTime());
		tfFlowStabilyCheckTime.setText("" + processConfig.getFlowStabilityCheckTime());
		tfFlowRateTimeout.setText("" + processConfig.getTimeOutFlowRate());
		// Matfile
		cbSaveMatData.setSelected(processConfig.isSaveMatData());
		cbSaveMatDataReprovedMeters.setSelected(processConfig.isSaveMatDataReprovedMeters());
		cbSaveMatDataCalibLut.setSelected(processConfig.isSaveCalibLutMatData());
		// Sizes
		tfFlowBuffSize.setText("" + processConfig.getFlowBuffSize());
		tfWindowSize.setText("" + processConfig.getWindowSize());

		// Calib table
		tvListFlowRateCalib.setItems(FXCollections.observableArrayList(processConfig.getCalibFlowRates()));
		// Verif table
		tvListFlowRateVerif.setItems(FXCollections.observableArrayList(processConfig.getVerifFlowRates()));
		// Calculated fixed const
		tvCalculateFixConst.setItems(FXCollections.observableArrayList(processConfig.getCalcFixedConsts()));
	}

	/**
	 * 
	 */
	private Callback<TableColumn<Object, String>, TableCell<Object, String>> cellFactorySelect = new Callback<TableColumn<Object, String>, TableCell<Object, String>>() {
		@Override
		public TableCell<Object, String> call(final TableColumn<Object, String> param) {
			final TableCell<Object, String> cell = new TableCell<Object, String>() {

				// final Button btn = new Button("Excluir");
				final CheckBox cb = new CheckBox();

				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {

						hashCheck.put(cb, false);
						cb.setOnAction((ActionEvent event) -> {

							if (viewAction == ViewActionEnum.NEW) {
								selectedProcessConfig = (ProcessConfigModel) getTableView().getItems().get(getIndex());
								btnAction.setText("Selecionar");
								viewAction = ViewActionEnum.SELECT;
							} else if (viewAction == ViewActionEnum.SELECT) {
								if (cb.isSelected()) {
									for (Map.Entry<CheckBox, Boolean> entry : hashCheck.entrySet()) {
										entry.getKey().setSelected(false);
									}
									cb.setSelected(true);
								} else {
									btnAction.setText("Novo");
									viewAction = ViewActionEnum.NEW;
								}
							}
						});
						setGraphic(cb);
						setText(null);
					}
				}
			};
			return cell;
		}
	};

	/**
	 * 
	 */
	private Callback<TableColumn<Object, String>, TableCell<Object, String>> cellFactoryDelete = new Callback<TableColumn<Object, String>, TableCell<Object, String>>() {
		@Override
		public TableCell<Object, String> call(final TableColumn<Object, String> param) {
			final TableCell<Object, String> cell = new TableCell<Object, String>() {

				final Button btn = new Button("Excluir");
				// final CheckBox cb = new CheckBox();

				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {

						btn.setOnAction((ActionEvent event) -> {
							// // Object objeto = getTableView().getItems().get(getIndex());
							// FlowRateModel batch = (FlowRateModel) getTableView().getItems().get(getIndex());
							// inflateFlowRateDeleteDialog(batch);
						});
						setGraphic(btn);
						setText(null);
					}
				}
			};
			return cell;
		}
	};

	/**
	 * 
	 */
	private void inflateNewProcessConfig() {
		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.NEW_PROCESS_CONFIG);
			sceneFactory.setTitle("Nova configuração de processo");
			NewProcessConfigControllerView childController = sceneFactory.getController();
			childController.setCallback(value -> {
				refreshTable();
			});
			sceneFactory.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void closeView() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	/**
	 * 
	 */
	@FXML
	private void onTableMouseClicked(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			if (event.getClickCount() == 2) {
				Node node = ((Node) event.getTarget()).getParent();
				TableRow row;
				if (node instanceof TableRow) {
					row = (TableRow) node;
				} else {
					row = (TableRow) node.getParent();
				}
				//
				ProcessConfigModel processConfig = (ProcessConfigModel) row.getItem();
				if (processConfig != null) {
					updateView(processConfig);
				}
			}
		}
	}

	/**
	 * 
	 */
	@FXML
	private void onActionButton(ActionEvent event) {

		if (viewAction == ViewActionEnum.NEW) {
			inflateNewProcessConfig();
		} else if (viewAction == ViewActionEnum.SELECT) {
			processConfigCallback.accept(selectedProcessConfig);
			closeView();
		}
	}

	// /**
	// *
	// */
	// @FXML
	// private void onActionCancelButton(ActionEvent event) {
	// closeView();
	// }

	/**
	 * Inicializa a classe controller. Este método é chamado automaticamente após o arquivo fxml ter sido carregado.
	 */
	@FXML
	private void initialize() {

		processConfigService = new ProcessConfigService();

		// Process config table
		columnId.setStyle("-fx-alignment: CENTER;");
		// columnDescription.setStyle("-fx-alignment: CENTER;");
		columnDelete.setStyle("-fx-alignment: CENTER;");

		columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		columnDescription.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		// Select button
		columnSelect.setStyle("-fx-alignment: CENTER;");
		columnSelect.setCellValueFactory(new PropertyValueFactory<>("Button"));
		columnSelect.setCellFactory(cellFactorySelect);
		//
		columnDelete.setCellValueFactory(new PropertyValueFactory<>("Button"));
		columnDelete.setCellFactory(cellFactoryDelete);

		// -----------------------------------------
		refreshTable();

		// -----------------------------------------
		// Flow rate calib table
		columnListFlowRateCalib.setStyle("-fx-alignment: CENTER;");
		// columnListDescriptionCalib.setStyle("-fx-alignment: CENTER;");
		columnListRefMeterCalib.setStyle("-fx-alignment: CENTER;");
		columnListRamalCalib.setStyle("-fx-alignment: CENTER;");
		columnListPumpCalib.setStyle("-fx-alignment: CENTER;");
		columnListFlowRateCalib.setCellValueFactory(new PropertyValueFactory<>("flowRate"));
		columnListDescriptionCalib.setCellValueFactory(new PropertyValueFactory<>("description"));
		columnListRefMeterCalib.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				String refMeter = "";
				if (p.getValue().getRefMeter() != null) {
					refMeter = p.getValue().getRefMeter().getTag();
				}
				return new ReadOnlyObjectWrapper<>(refMeter);
			}
		});
		columnListRamalCalib.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				String ramal = "";
				if (p.getValue().getRamal() != null) {
					ramal = p.getValue().getRamal().getTag();
				}
				return new ReadOnlyObjectWrapper<>(ramal);
			}
		});
		columnListPumpCalib.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				String pump = "";
				if (p.getValue().getPump() != null) {
					pump = p.getValue().getPump().getTag();
				}
				return new ReadOnlyObjectWrapper<>(pump);
			}
		});
		// Flow rate verif
		columnListFlowRateVerif.setStyle("-fx-alignment: CENTER;");
		// columnListDescriptionVerif.setStyle("-fx-alignment: CENTER;");
		columnListRefmMeterVerif.setStyle("-fx-alignment: CENTER;");
		columnListRamalVerif.setStyle("-fx-alignment: CENTER;");
		columnListPumpVerif.setStyle("-fx-alignment: CENTER;");
		columnListFlowRateVerif.setCellValueFactory(new PropertyValueFactory<>("flowRate"));
		columnListDescriptionVerif.setCellValueFactory(new PropertyValueFactory<>("description"));
		columnListRefmMeterVerif.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				String refMeter = "";
				if (p.getValue().getRefMeter() != null) {
					refMeter = p.getValue().getRefMeter().getTag();
				}
				return new ReadOnlyObjectWrapper<>(refMeter);
			}
		});
		columnListRamalVerif.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				String ramal = "";
				if (p.getValue().getRamal() != null) {
					ramal = p.getValue().getRamal().getTag();
				}
				return new ReadOnlyObjectWrapper<>(ramal);
			}
		});
		columnListPumpVerif.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				String pump = "";
				if (p.getValue().getPump() != null) {
					pump = p.getValue().getPump().getTag();
				}
				return new ReadOnlyObjectWrapper<>(pump);
			}
		});

		// Calculated fixed const
		columnFlowRateConst.setStyle("-fx-alignment: CENTER;");
		columnKConst.setStyle("-fx-alignment: CENTER;");
		columnReConst.setStyle("-fx-alignment: CENTER;");
		columnTempConst.setStyle("-fx-alignment: CENTER;");
		//
		columnDescriptionConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>(p.getValue().getDescricao());
			}
		});
		//
		columnFlowRateConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>("" + p.getValue().getFlowrate());
			}
		});
		//
		columnKConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>("" + p.getValue().getK());
			}
		});
		//
		columnReConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>("" + p.getValue().getRe());
			}
		});
		//
		columnTempConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>("" + p.getValue().getTemp());
			}
		});
	}
}