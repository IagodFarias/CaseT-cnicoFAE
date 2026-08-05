//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BatchControllerView.java
*    @author marcos
*    @date 17 de set de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.batch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import enumerations.SceneTypeEnum;
import enumerations.ViewActionEnum;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BatchRadioWmBusConfigModel;
import si.dbcomm.service.BatchRadioWmBusConfigService;
import si.dbcomm.service.BatchService;
import si.dbcomm.service.QaAssuredService;
import view.controller.SceneFactory;

/**
 * @author marcos
 *
 */
public class LoadBatchControllerView {

	@FXML
	private AnchorPane root;

	@FXML
	private Button btnAction;

	// Table
	@FXML
	private TableView<BatchModel> batchTable;

	@FXML
	private TableColumn columnSelect;

	@FXML
	private TableColumn<BatchModel, Long> columnLoteId;

	@FXML
	private TableColumn<BatchModel, String> columnDescription;

	@FXML
	private TableColumn columnActive;

	@FXML
	private TableColumn columnDelete;

	// Detail
	@FXML
	private CheckBox cbRadioWmBus;

	@FXML
	private Label lbNumberOfMeters;

	@FXML
	private Label lbStartSeq;

	@FXML
	private Label lbLastAssignedSerialSeq;

	@FXML
	private Label lbLeftProduce;

	@FXML
	private Label lbManufacturerCode;

	@FXML
	private Label lbYearOfManufacture;

	@FXML
	private Label lbClient;

	@FXML
	private Label lbMeterType;

	// wM-Bus config
	@FXML
	private VBox vbWmbbusConfig;

	@FXML
	private Label lbStartSeqRadio;

	@FXML
	private Label lbLastAssignedSerialSeqRadio;

	@FXML
	private Label lbLeftProduceRadio;

	@FXML
	private Label lbManufacturerNumberRadio;

	@FXML
	private Label lbProductCodeRadio;

	@FXML
	private Label lbVersionRadio;

	@FXML
	private Label lbTransmitIntervalRadio;

	// REWORK
	// Table
	@FXML
	private TableView<BatchModel> batchTableRw;

	@FXML
	private TableColumn columnSelectRw;

	@FXML
	private TableColumn<BatchModel, Long> columnLoteIdRw;

	@FXML
	private TableColumn<BatchModel, String> columnDescriptionRw;

	@FXML
	private TableColumn columnActiveRw;

	@FXML
	private TableColumn columnDeleteRw;
	// Detail
	@FXML
	private CheckBox cbRadioWmBusRw;

	@FXML
	private Label lbNumberOfMetersRw;

	@FXML
	private Label lbStartSeqRw;

	@FXML
	private Label lbLastAssignedSerialSeqRw;

	@FXML
	private Label lbLeftProduceRw;

	@FXML
	private Label lbManufacturerCodeRw;

	@FXML
	private Label lbYearOfManufactureRw;

	@FXML
	private Label lbClientRw;

	@FXML
	private Label lbMeterTypeRw;

	// wM-Bus config
	@FXML
	private VBox vbWmbbusConfigRw;

	@FXML
	private Label lbStartSeqRadioRw;

	@FXML
	private Label lbLastAssignedSerialSeqRadioRw;

	@FXML
	private Label lbLeftProduceRadioRw;

	@FXML
	private Label lbManufacturerNumberRadioRw;

	@FXML
	private Label lbProductCodeRadioRw;

	@FXML
	private Label lbVersionRadioRw;

	@FXML
	private Label lbTransmitIntervalRadioRw;

	//
	@FXML
	private Label labelUserMsg;

	private BatchService batchService;

	@SuppressWarnings("unused")
	private Consumer<BatchModel> batchSelectCallback;

	private BatchModel selectedBatch;

	//
	private HashMap<CheckBox, BatchModel> hashCheck = new HashMap<>();
	private HashMap<CheckBox, BatchModel> hashCheckRw = new HashMap<>();

	private ViewActionEnum viewAction = ViewActionEnum.NEW;

	private ToggleGroup group = new ToggleGroup();

	/**
	 * 
	 */
	public void setCallback(Consumer<BatchModel> callback) {
		this.batchSelectCallback = callback;
	}

	/**
	 * 
	 */
	private void inflateInactiveDialog(BatchModel batch) {
		labelUserMsg.setVisible(false);

		Alert alert = new Alert(AlertType.CONFIRMATION);
		if (!batch.isFinished()) {
			alert.setTitle("Inativar lote");
			alert.setHeaderText("Lote: " + batch.getBatchId());
			alert.setContentText("Tem certeza que deseja INATIVAR este lote?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				try {
					//
					batch.setFinished(true);
					batchService.update(batch);
					refreshTables();
				} catch (CrudDatabaseException ex) {
					// e.printStackTrace();
					labelUserMsg.setVisible(true);
					labelUserMsg.setText(ex.getMessage());
				}
			}
		} else {
			alert.setTitle("Inativar lote");
			alert.setHeaderText("Lote: " + batch.getBatchId());
			alert.setContentText("Lote não pode ser ativado");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
			}
		}
	}

	/**
	 * 
	 */
	private void inflateDeleteDialog(BatchModel batch) {
		labelUserMsg.setVisible(false);

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Deletar lote");
		alert.setHeaderText("Lote: " + batch.getBatchId());
		alert.setContentText("Tem certeza que deseja excluir este lote?");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			try {
				// Delete selected batch
				batchService.delete(batch);
				refreshTables();
			} catch (CrudDatabaseException ex) {
				// e.printStackTrace();
				labelUserMsg.setVisible(true);
				labelUserMsg.setText(ex.getMessage());
			}
		}
	}

	/**
	 * 
	 * 
	 */
	private void refreshTables() {
//		batchTable.setItems(FXCollections.observableArrayList(batchService.findAll()));
		batchTable.setItems(FXCollections.observableArrayList(batchService.findAllActive()));

		QaAssuredService qaService = new QaAssuredService();
		batchTableRw.setItems(FXCollections.observableArrayList(qaService.findAllDistinctBatch()));
	}

	/**
	 *
	 */
	private Callback<TableColumn<Object, String>, TableCell<Object, String>> cellFactorySelectRw = new Callback<TableColumn<Object, String>, TableCell<Object, String>>() {
		@Override
		public TableCell<Object, String> call(final TableColumn<Object, String> param) {
			final TableCell<Object, String> cell = new TableCell<Object, String>() {

				final CheckBox cb = new CheckBox();

				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {

						BatchModel hashBatch = (BatchModel) getTableView().getItems().get(getIndex());
						hashCheckRw.put(cb, hashBatch);
						if (hashBatch.isSelected()) {
							cb.setSelected(true);
						} else {
							cb.setSelected(false);
						}

						cb.setOnAction((ActionEvent event) -> {
							BatchModel batch = (BatchModel) getTableView().getItems().get(getIndex());

							// if (batch.isFinished()) {
							// batch.setSelected(false);
							// cb.setSelected(false);
							// } else {
							if (batch.isSelected()) {
								batch.setSelected(false);
								cb.setSelected(false);
								btnAction.setText("Novo");
								viewAction = ViewActionEnum.NEW;
							} else {
								for (Map.Entry<CheckBox, BatchModel> entry : hashCheck.entrySet()) {
									entry.getValue().setSelected(false);
									entry.getKey().setSelected(false);
									btnAction.setText("Novo");
									viewAction = ViewActionEnum.NEW;
								}
								for (Map.Entry<CheckBox, BatchModel> entry : hashCheckRw.entrySet()) {
									if (entry.getKey() != cb) {
										entry.getValue().setSelected(false);
										entry.getKey().setSelected(false);
										btnAction.setText("Novo");
										viewAction = ViewActionEnum.NEW;
									}
								}
								batch.setSelected(true);
								cb.setSelected(true);
								btnAction.setText("Selecionar");
								viewAction = ViewActionEnum.SELECT;
								//
								batch.setReWork(true);
								selectedBatch = batch;
							}
							// }
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
	private Callback<TableColumn<Object, String>, TableCell<Object, String>> cellFactorySelect = new Callback<TableColumn<Object, String>, TableCell<Object, String>>() {
		@Override
		public TableCell<Object, String> call(final TableColumn<Object, String> param) {
			final TableCell<Object, String> cell = new TableCell<Object, String>() {

				final CheckBox cb = new CheckBox();

				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {

						BatchModel hashBatch = (BatchModel) getTableView().getItems().get(getIndex());
						hashCheck.put(cb, hashBatch);
						if (hashBatch.isSelected()) {
							cb.setSelected(true);
						} else {
							cb.setSelected(false);
						}

						cb.setOnAction((ActionEvent event) -> {
							BatchModel batch = (BatchModel) getTableView().getItems().get(getIndex());

							if (batch.isFinished()) {
								batch.setSelected(false);
								cb.setSelected(false);
							} else {
								if (batch.isSelected()) {
									batch.setSelected(false);
									cb.setSelected(false);
									btnAction.setText("Novo");
									viewAction = ViewActionEnum.NEW;
								} else {
									for (Map.Entry<CheckBox, BatchModel> entry : hashCheckRw.entrySet()) {
										entry.getValue().setSelected(false);
										entry.getKey().setSelected(false);
										btnAction.setText("Novo");
										viewAction = ViewActionEnum.NEW;
									}
									for (Map.Entry<CheckBox, BatchModel> entry : hashCheck.entrySet()) {
										if (entry.getKey() != cb) {
											entry.getValue().setSelected(false);
											entry.getKey().setSelected(false);
											btnAction.setText("Novo");
											viewAction = ViewActionEnum.NEW;
										}
									}
									batch.setSelected(true);
									cb.setSelected(true);
									btnAction.setText("Selecionar");
									viewAction = ViewActionEnum.SELECT;
									//
									selectedBatch = batch;
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
	private Callback<TableColumn<Object, String>, TableCell<Object, String>> cellFactoryStatus = new Callback<TableColumn<Object, String>, TableCell<Object, String>>() {
		@Override
		public TableCell<Object, String> call(final TableColumn<Object, String> param) {
			final TableCell<Object, String> cell = new TableCell<Object, String>() {

				final Button btn = new Button();

				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {

						//
						btn.setMinWidth(60);

						BatchModel batch = (BatchModel) getTableView().getItems().get(getIndex());
						if (batch.isFinished()) {
							btn.setText("Inativo");
						} else {
							btn.setText("Ativo");
						}

						btn.setOnAction((ActionEvent event) -> {
							BatchModel batchSelected = (BatchModel) getTableView().getItems().get(getIndex());
							inflateInactiveDialog(batchSelected);
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
	private Callback<TableColumn<Object, String>, TableCell<Object, String>> cellFactoryExcluir = new Callback<TableColumn<Object, String>, TableCell<Object, String>>() {
		@Override
		public TableCell<Object, String> call(final TableColumn<Object, String> param) {
			final TableCell<Object, String> cell = new TableCell<Object, String>() {

				final Button btn = new Button("Excluir");

				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {

						btn.setOnAction((ActionEvent event) -> {
							BatchModel batch = (BatchModel) getTableView().getItems().get(getIndex());
							inflateDeleteDialog(batch);
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
	private void updateViewDetailRw(BatchModel batch) {
		cbRadioWmBusRw.setSelected(batch.getMeterType().isRadioWmBus());
		lbNumberOfMetersRw.setText("" + batch.getNumMeters());
		lbStartSeqRw.setText("" + batch.getStartSerialSeq());
		lbLastAssignedSerialSeqRw.setText("" + batch.getLastAssignedSerialSeq());
		lbLeftProduceRw.setText("" + batch.getLeftProduce());
		lbManufacturerCodeRw.setText(batch.getManufacturerCode());
		lbYearOfManufactureRw.setText("" + batch.getYearOfManufacture().getYear());
		lbClientRw.setText("" + batch.getClient().getNomeFantasia());
		lbMeterTypeRw.setText("" + batch.getMeterType().getMeterModelDescription());
		// ArrayList<BateladaModel> bateladaList = new ArrayList<>(batch.getBateladas());
		// tfProcessConfig.setText("" + batch.getBateladas().getClass());

		if (batch.getMeterType().isRadioWmBus()) {
			vbWmbbusConfigRw.setVisible(true);

			BatchRadioWmBusConfigService batchRadioConfigService = new BatchRadioWmBusConfigService();
			BatchRadioWmBusConfigModel batchRadioConfig = batchRadioConfigService.findByBatchId(batch.getId());

			lbStartSeqRadioRw.setText("" + batchRadioConfig.getStartSerialSeq());
			lbLastAssignedSerialSeqRadioRw.setText("" + batchRadioConfig.getLastAssignedSerial());
			int leftProduce = batchRadioConfig.getStartSerialSeq();
			if (batchRadioConfig.getLastAssignedSerial() != -1) {
				leftProduce = (batchRadioConfig.getStartSerialSeq() + batch.getNumMeters()) - batchRadioConfig.getLastAssignedSerial();
			}
			lbLeftProduceRadioRw.setText("" + leftProduce);
			lbManufacturerNumberRadioRw.setText(batchRadioConfig.getManufacNumber());
			lbProductCodeRadioRw.setText(batchRadioConfig.getProductCode());
			lbVersionRadioRw.setText(batchRadioConfig.getVersion());
			lbTransmitIntervalRadioRw.setText("" + batchRadioConfig.getTransmitInterval());
		} else {
			vbWmbbusConfigRw.setVisible(false);
		}
	}

	/**
	 * 
	 */
	private void updateViewDetail(BatchModel batch) {
		cbRadioWmBus.setSelected(batch.getMeterType().isRadioWmBus());
		lbNumberOfMeters.setText("" + batch.getNumMeters());
		lbStartSeq.setText("" + batch.getStartSerialSeq());
		lbLastAssignedSerialSeq.setText("" + batch.getLastAssignedSerialSeq());
		lbLeftProduce.setText("" + batch.getLeftProduce());
		lbManufacturerCode.setText(batch.getManufacturerCode());
		lbYearOfManufacture.setText("" + batch.getYearOfManufacture().getYear());
		lbClient.setText("" + batch.getClient().getNomeFantasia());
		lbMeterType.setText("" + batch.getMeterType().getMeterModelDescription());
		// ArrayList<BateladaModel> bateladaList = new ArrayList<>(batch.getBateladas());
		// tfProcessConfig.setText("" + batch.getBateladas().getClass());

		if (batch.getMeterType().isRadioWmBus()) {
			vbWmbbusConfig.setVisible(true);

			BatchRadioWmBusConfigService batchRadioConfigService = new BatchRadioWmBusConfigService();
			BatchRadioWmBusConfigModel batchRadioConfig = batchRadioConfigService.findByBatchId(batch.getId());

			lbStartSeqRadio.setText("" + batchRadioConfig.getStartSerialSeq());
			lbLastAssignedSerialSeqRadio.setText("" + batchRadioConfig.getLastAssignedSerial());
			int leftProduce = batchRadioConfig.getStartSerialSeq();
			if (batchRadioConfig.getLastAssignedSerial() != -1) {
				leftProduce = (batchRadioConfig.getStartSerialSeq() + batch.getNumMeters()) - batchRadioConfig.getLastAssignedSerial();
			}
			lbLeftProduceRadio.setText("" + leftProduce);
			lbManufacturerNumberRadio.setText(batchRadioConfig.getManufacNumber());
			lbProductCodeRadio.setText(batchRadioConfig.getProductCode());
			lbVersionRadio.setText(batchRadioConfig.getVersion());
			lbTransmitIntervalRadio.setText("" + batchRadioConfig.getTransmitInterval());
		} else {
			vbWmbbusConfig.setVisible(false);
		}
	}

	/**
	 * 
	 */
	private void inflateNewBatch() {
		labelUserMsg.setVisible(false);
		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.NEW_BATCH);
			sceneFactory.setTitle("Atualização de Lotes");
			NewBatchControllerView controller = sceneFactory.getController();
			controller.setCallback(value -> {
				refreshTables();
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
	@SuppressWarnings("rawtypes")
	@FXML
	private void onTableRwMouseClicked(MouseEvent event) {
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
				// inflateBatch((BatchModel) row.getItem());
				// updateView((BatchModel) row.getItem());
				BatchModel batch = (BatchModel) row.getItem();
				if (batch != null) {
					updateViewDetailRw(batch);
				}
			}
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
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
				// inflateBatch((BatchModel) row.getItem());
				// updateView((BatchModel) row.getItem());
				BatchModel batch = (BatchModel) row.getItem();
				if (batch != null) {
					updateViewDetail(batch);
				}
			}
		}
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onActionNewButton(ActionEvent event) {
		// inflateBatch(null);
		if (viewAction == ViewActionEnum.NEW) {
			inflateNewBatch();
		} else if (viewAction == ViewActionEnum.SELECT) {
			batchSelectCallback.accept(selectedBatch);
			closeView();
		}
	}

	/**
	 * Inicializa a classe controller. Este método é chamado automaticamente após o arquivo fxml ter sido carregado.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@FXML
	private void initialize() {

		vbWmbbusConfig.setVisible(false);
		vbWmbbusConfigRw.setVisible(false);
		labelUserMsg.setVisible(false);

		// ---------------------------------
		// Table
		columnLoteId.setCellValueFactory(new PropertyValueFactory<>("batchId"));
		columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

		// Select button
		columnSelect.setStyle("-fx-alignment: CENTER;");
		columnSelect.setCellFactory(cellFactorySelect);

		// Status button
		columnActive.setStyle("-fx-alignment: CENTER;");
		columnActive.setCellFactory(cellFactoryStatus);

		// Delete button
		columnDelete.setStyle("-fx-alignment: CENTER;");
		columnDelete.setCellFactory(cellFactoryExcluir);

		// ---------------------------------
		// Table Rework
		columnLoteIdRw.setCellValueFactory(new PropertyValueFactory<>("batchId"));
		columnDescriptionRw.setCellValueFactory(new PropertyValueFactory<>("description"));

		// Select button
		columnSelectRw.setStyle("-fx-alignment: CENTER;");
		columnSelectRw.setCellFactory(cellFactorySelectRw);

		// -----------------------------------------
		batchService = new BatchService();
		refreshTables();
	}
}
