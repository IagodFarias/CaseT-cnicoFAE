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

/**
 * @author marcos
 *
 */
public class AddFlowRateControllerView {

	FlowRateService flowRateService;

	@FXML
	private BorderPane root;

	@FXML
	private TableView<FlowRateModel> flowRateTable;

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
	private TableColumn columnCheckBox;

	private ObservableList<FlowRateModel> flowRateData;

	
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
	private void onTableMouseClicked(MouseEvent event) {
		
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
		// Delete button
		columnCheckBox = new TableColumn("Excluir");
		columnCheckBox.setStyle("-fx-alignment: CENTER;");
		columnCheckBox.setCellValueFactory(new PropertyValueFactory<>("Button"));
		columnCheckBox.setCellFactory(cellFactoryCheckBox);
		flowRateTable.getColumns().add(columnCheckBox);

		// -----------------------------------------
		flowRateService = new FlowRateService();
		flowRateData = FXCollections.observableArrayList(flowRateService.findAll());
		flowRateTable.setItems(flowRateData);
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
