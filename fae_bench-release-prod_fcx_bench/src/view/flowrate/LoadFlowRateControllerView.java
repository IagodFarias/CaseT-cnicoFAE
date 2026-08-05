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
package view.flowrate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import enumerations.SceneTypeEnum;
import enumerations.ViewActionEnum;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.service.FlowRateService;
import view.controller.SceneFactory;

/**
 * @author marcos
 *
 */
public class LoadFlowRateControllerView {

	@FXML
	private AnchorPane root;

	// @FXML
	// private Button btnCancel;

	@FXML
	private Button btAction;

	@FXML
	private TableView<FlowRateModel> flowRateTable;

	@FXML
	private TableColumn columnSelect;

	@FXML
	private TableColumn<FlowRateModel, Long> columnFlowRate;

	@FXML
	private TableColumn<FlowRateModel, Integer> columnDescription;

	@FXML
	private TableColumn<FlowRateModel, Integer> columnUpperLimit;

	@FXML
	private TableColumn<FlowRateModel, String> columnLowerLimit;

	@FXML
	private TableColumn<FlowRateModel, String> columnRamal;

	@FXML
	private TableColumn<FlowRateModel, String> columnRefMeter;

	@FXML
	private TableColumn<FlowRateModel, String> columnPump;

	@SuppressWarnings("rawtypes")
	private TableColumn columnDeleteButton;

	// private ObservableList<FlowRateModel> flowRateData;

	@SuppressWarnings("unused")
	private Consumer<FlowRateModel> flowRateSelectCallback;

	// private ArrayList<FlowRateModel> listFlowRate = new ArrayList<>();
	//
	private HashMap<CheckBox, Boolean> hashCheck = new HashMap<>();

	private ViewActionEnum viewAction = ViewActionEnum.NEW;

	private FlowRateModel selectedFlowRate;

	private FlowRateService flowRateService;

	// /**
	// *
	// */
	// public void setListFlowRate(ArrayList<FlowRateModel> list) {
	// listFlowRate.clear();
	// listFlowRate.addAll(list);
	// }

	// /**
	// *
	// */
	// public void setViewAction(ViewActionEnum action) {
	//
	// btnNew.setVisible(true);
	// columnDeleteButton.setVisible(true);
	// switch (action) {
	//
	// // case EDIT:
	// // break;
	//
	// case LOAD:
	// flowRateData = FXCollections.observableArrayList(flowRateService.findAll());
	// flowRateTable.setItems(flowRateData);
	// break;
	//
	// case VIEW:
	// flowRateData = FXCollections.observableArrayList(listFlowRate);
	// flowRateTable.setItems(flowRateData);
	// btnNew.setVisible(false);
	// columnDeleteButton.setVisible(false);
	// break;
	//
	// default:
	// break;
	// }
	// }

	/**
	 * 
	 */
	public void setCallback(Consumer<FlowRateModel> callback) {
		this.flowRateSelectCallback = callback;
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
								selectedFlowRate = (FlowRateModel) getTableView().getItems().get(getIndex());
								btAction.setText("Selecionar");
								viewAction = ViewActionEnum.SELECT;
							} else if (viewAction == ViewActionEnum.SELECT) {
								if (cb.isSelected()) {
									for (Map.Entry<CheckBox, Boolean> entry : hashCheck.entrySet()) {
										entry.getKey().setSelected(false);
									}
									cb.setSelected(true);
								} else {
									btAction.setText("Novo");
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
							// Object objeto = getTableView().getItems().get(getIndex());
							FlowRateModel batch = (FlowRateModel) getTableView().getItems().get(getIndex());
							inflateFlowRateDeleteDialog(batch);
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
	 * 
	 */
	private void inflateFlowRate(FlowRateModel batch) {

		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.NEW_FLOW_RATE);
			sceneFactory.setTitle("Atualização de Vazão");
			NewFlowRateControllerView childController = sceneFactory.getController();
			if (batch != null) {
				childController.setBatch(batch);
			}
			childController.setCallback(value -> {
				refreshFlowRateTable();
			});
			sceneFactory.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 */
	private void inflateFlowRateDeleteDialog(FlowRateModel batch) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Deletar Vazão");
		alert.setHeaderText("Vazão: " + batch.getFlowRate());
		alert.setContentText("Tem certeza que deseja excluir esta vazão?");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			try {
				// Delete selected batch
				flowRateService.delete(batch);
				refreshFlowRateTable();
			} catch (CrudDatabaseException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 
	 */
	private void refreshFlowRateTable() {
		// flowRateData = FXCollections.observableArrayList(flowRateService.findAll());
		// flowRateTable.setItems(flowRateData);

		flowRateTable.setItems(FXCollections.observableArrayList(flowRateService.findAll()));
	}

	// /**
	// *
	// *
	// */
	// @FXML
	// private void onActionCancelButton(ActionEvent event) {
	// // get a handle to the stage
	// Stage stage = (Stage) btnCancel.getScene().getWindow();
	// stage.close();
	// }

	/**
	 * 
	 */
	private void closeView() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	/**
	 * 
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
				inflateFlowRate((FlowRateModel) row.getItem());
			}
		}
	}

	/**
	 * 
	 */
	@FXML
	private void onActionButton(ActionEvent event) {
		// inflateFlowRate(null);

		if (viewAction == ViewActionEnum.NEW) {
			inflateFlowRate(null);
		} else if (viewAction == ViewActionEnum.SELECT) {
			if (flowRateSelectCallback != null) {
				flowRateSelectCallback.accept(selectedFlowRate);
			}
			closeView();
		}
	}

	/**
	 * Inicializa a classe controller. Este método é chamado automaticamente após o arquivo fxml ter sido carregado.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	private void initialize() {

		flowRateService = new FlowRateService();

		columnFlowRate.setCellValueFactory(new PropertyValueFactory<>("flowRate"));
		columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
		columnUpperLimit.setCellValueFactory(new PropertyValueFactory<>("upperLimit"));
		columnLowerLimit.setCellValueFactory(new PropertyValueFactory<>("lowerLimit"));

		columnFlowRate.setStyle("-fx-alignment: CENTER;");
		columnDescription.setStyle("-fx-alignment: CENTER;");
		columnUpperLimit.setStyle("-fx-alignment: CENTER;");
		columnLowerLimit.setStyle("-fx-alignment: CENTER;");
		columnRamal.setStyle("-fx-alignment: CENTER;");
		columnRefMeter.setStyle("-fx-alignment: CENTER;");
		columnPump.setStyle("-fx-alignment: CENTER;");

		columnRamal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				ObservableValue<String> objectWrapper = null;
				if (p.getValue().getRamal() != null)
					objectWrapper = new ReadOnlyObjectWrapper<>(p.getValue().getRamal().getTag());

				return objectWrapper;
			}
		});

		columnRefMeter.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				ObservableValue<String> objectWrapper = null;
				if (p.getValue().getRamal() != null)
					objectWrapper = new ReadOnlyObjectWrapper<>(p.getValue().getRefMeter().getTag());

				return objectWrapper;
			}
		});

		columnPump.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				ObservableValue<String> objectWrapper = null;
				if (p.getValue().getRamal() != null)
					objectWrapper = new ReadOnlyObjectWrapper<>(p.getValue().getPump().getTag());

				return objectWrapper;
			}
		});

		// -------------------------------------
		// Select button
		columnSelect.setStyle("-fx-alignment: CENTER;");
		columnSelect.setCellValueFactory(new PropertyValueFactory<>("Button"));
		columnSelect.setCellFactory(cellFactorySelect);
		// Delete button
		columnDeleteButton = new TableColumn("Excluir");
		columnDeleteButton.setStyle("-fx-alignment: CENTER;");
		columnDeleteButton.setCellValueFactory(new PropertyValueFactory<>("Button"));
		columnDeleteButton.setCellFactory(cellFactoryExcluir);
		flowRateTable.getColumns().add(columnDeleteButton);

		// // -----------------------------------------
		// flowRateService = new FlowRateService();
		// flowRateData = FXCollections.observableArrayList(flowRateService.findAll());
		// flowRateTable.setItems(flowRateData);
		refreshFlowRateTable();
	}
}
