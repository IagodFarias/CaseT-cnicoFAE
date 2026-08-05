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

import enumerations.SceneTypeEnum;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.service.FlowRateService;
import view.controller.SceneFactory;

/**
 * @author marcos
 *
 */
public class SelectFlowRate {

	FlowRateService flowRateService;

	@FXML
	private BorderPane root;

	// -------------------------------------------------------
	// FlowRate Table Calib
	@FXML
	private TableView<FlowRateModel> flowRateTableCalib;

	@FXML
	private TableColumn<FlowRateModel, Long> columnFlowRateCalib;

	@FXML
	private TableColumn<FlowRateModel, Integer> columnDescriptionCalib;

	@FXML
	private TableColumn<FlowRateModel, Integer> columnUpperLimitCalib;

	@FXML
	private TableColumn<FlowRateModel, String> columnLowerLimitCalib;

	@FXML
	private TableColumn<FlowRateModel, String> columnRamalCalib;

	@FXML
	private TableColumn<FlowRateModel, String> columnRefMeterCalib;

	@FXML
	private TableColumn<FlowRateModel, String> columnPumpCalib;

	@SuppressWarnings("rawtypes")
	private TableColumn columnCheckBoxCalib;

	private ObservableList<FlowRateModel> flowRateDataCalib;

	// -------------------------------------------------------
	// FlowRate Table Calib
	@FXML
	private TableView<FlowRateModel> flowRateTableVerific;

	@FXML
	private TableColumn<FlowRateModel, Long> columnFlowRateVerific;

	@FXML
	private TableColumn<FlowRateModel, Integer> columnDescriptionVerific;

	@FXML
	private TableColumn<FlowRateModel, Integer> columnUpperLimitVerific;

	@FXML
	private TableColumn<FlowRateModel, String> columnLowerLimitVerific;

	@FXML
	private TableColumn<FlowRateModel, String> columnRamalVerific;

	@FXML
	private TableColumn<FlowRateModel, String> columnRefMeterVerific;

	@FXML
	private TableColumn<FlowRateModel, String> columnPumpVerific;

	/**
	 * 
	 * 
	 */
	private void onCloseStage() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onTableCalibMouseClicked(MouseEvent event) {
		// if (event.getButton().equals(MouseButton.PRIMARY)) {
		// if (event.getClickCount() == 2) {
		// Node node = ((Node) event.getTarget()).getParent();
		// TableRow row;
		// if (node instanceof TableRow) {
		// row = (TableRow) node;
		// } else {
		// row = (TableRow) node.getParent();
		// }
		// //
		// inflateFlowRate((FlowRateModel) row.getItem());
		// }
		// }
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onTableVerificMouseClicked(MouseEvent event) {
		// if (event.getButton().equals(MouseButton.PRIMARY)) {
		// if (event.getClickCount() == 2) {
		// Node node = ((Node) event.getTarget()).getParent();
		// TableRow row;
		// if (node instanceof TableRow) {
		// row = (TableRow) node;
		// } else {
		// row = (TableRow) node.getParent();
		// }
		// //
		// inflateFlowRate((FlowRateModel) row.getItem());
		// }
		// }
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onActionAdicionar() {
		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.ADD_FLOW_RATE);
			sceneFactory.setTitle("Adiciona vazões");
			sceneFactory.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onActionSalvar() {

	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onActionCancel(ActionEvent event) {
		onCloseStage();
	}

	/**
	 * Inicializa a classe controller. Este método é chamado automaticamente após o arquivo fxml ter sido carregado.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	private void initialize() {

		// ----------------------------------------------------------------
		// Load all flowRate calib
		columnFlowRateCalib.setCellValueFactory(new PropertyValueFactory<>("flowRate"));
		columnDescriptionCalib.setCellValueFactory(new PropertyValueFactory<>("description"));
		columnUpperLimitCalib.setCellValueFactory(new PropertyValueFactory<>("upperLimit"));
		columnLowerLimitCalib.setCellValueFactory(new PropertyValueFactory<>("lowerLimit"));

		columnFlowRateCalib.setStyle("-fx-alignment: CENTER;");
		columnDescriptionCalib.setStyle("-fx-alignment: CENTER;");
		columnUpperLimitCalib.setStyle("-fx-alignment: CENTER;");
		columnLowerLimitCalib.setStyle("-fx-alignment: CENTER;");
		columnRamalCalib.setStyle("-fx-alignment: CENTER;");
		columnRefMeterCalib.setStyle("-fx-alignment: CENTER;");
		columnPumpCalib.setStyle("-fx-alignment: CENTER;");

		columnRamalCalib.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				ObservableValue<String> objectWrapper = null;
				if (p.getValue().getRamal() != null)
					objectWrapper = new ReadOnlyObjectWrapper<>(p.getValue().getRamal().getTag());

				return objectWrapper;
			}
		});

		columnRefMeterCalib.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				ObservableValue<String> objectWrapper = null;
				if (p.getValue().getRamal() != null)
					objectWrapper = new ReadOnlyObjectWrapper<>(p.getValue().getRefMeter().getTag());

				return objectWrapper;
			}
		});

		columnPumpCalib.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				ObservableValue<String> objectWrapper = null;
				if (p.getValue().getRamal() != null)
					objectWrapper = new ReadOnlyObjectWrapper<>(p.getValue().getPump().getTag());

				return objectWrapper;
			}
		});

		// -------------------------------------
		// Delete button
		columnCheckBoxCalib = new TableColumn("Excluir");
		columnCheckBoxCalib.setStyle("-fx-alignment: CENTER;");
		columnCheckBoxCalib.setCellValueFactory(new PropertyValueFactory<>("Button"));
		columnCheckBoxCalib.setCellFactory(cellFactoryCheckBox);
		flowRateTableCalib.getColumns().add(columnCheckBoxCalib);

		// -----------------------------------------
		flowRateService = new FlowRateService();
		flowRateDataCalib = FXCollections.observableArrayList(flowRateService.findAll());
		flowRateTableCalib.setItems(flowRateDataCalib);
	}

	/**
	 * 
	 * 
	 */
	private Callback<TableColumn<Object, String>, TableCell<Object, String>> cellFactoryCheckBox = new Callback<TableColumn<Object, String>, TableCell<Object, String>>() {
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
}
