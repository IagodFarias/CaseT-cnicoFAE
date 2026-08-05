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

import java.util.Optional;
import java.util.function.Consumer;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import si.dbcomm.model.BatchModel;
import si.dbcomm.service.BatchService;

/**
 * @author marcos
 *
 */
public class SelectBatchControllerView {
	BatchService batchService;

	@FXML
	private Button btnCancel;

	@FXML
	private TableView<BatchModel> batchTable;

	@FXML
	private TableColumn<BatchModel, Long> columnLoteId;
	
	@FXML
	private TableColumn<BatchModel, Integer> columnRadioFreq;

	@FXML
	private TableColumn<BatchModel, Integer> columnQtdMeters;

	@FXML
	private TableColumn<BatchModel, Integer> columnLeftProduce;

	@FXML
	private TableColumn<BatchModel, String> columnClient;

	@FXML
	private TableColumn<BatchModel, String> columnMeterDesc;

	@FXML
	private TableColumn<BatchModel, String> columnManufacturerCode;

	@FXML
	private TableColumn<BatchModel, String> columnLastAssignedSerialSeq;

	private ObservableList<BatchModel> batchesData;

	private Consumer<BatchModel> batchSelectCallback;

	public void setBatchSelectCallback(Consumer<BatchModel> callback) {
		this.batchSelectCallback = callback;
	}

	/**
	 * Inicializa a classe controller. Este método é chamado automaticamente após o arquivo fxml ter sido carregado.
	 */
	@FXML
	private void initialize() {

		columnLoteId.setCellValueFactory(new PropertyValueFactory<>("batchId"));
		columnQtdMeters.setCellValueFactory(new PropertyValueFactory<>("numMeters"));
//		columnRadioFreq.setCellValueFactory(new PropertyValueFactory<>("numMeters"));
		columnLeftProduce.setCellValueFactory(new PropertyValueFactory<>("left"));
		columnManufacturerCode.setCellValueFactory(new PropertyValueFactory<>("manufacturerCode"));
		columnLastAssignedSerialSeq.setCellValueFactory(new PropertyValueFactory<>("lastAssignedSerialSeq"));

		columnLoteId.setStyle("-fx-alignment: CENTER;");
		columnQtdMeters.setStyle("-fx-alignment: CENTER;");
		columnLeftProduce.setStyle("-fx-alignment: CENTER;");
		columnManufacturerCode.setStyle("-fx-alignment: CENTER;");
		columnLastAssignedSerialSeq.setStyle("-fx-alignment: CENTER;");

		columnClient.setStyle("-fx-alignment: CENTER;");
		columnClient.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BatchModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<BatchModel, String> p) {
				return new ReadOnlyObjectWrapper<>(p.getValue().getClient().getNomeFantasia());
			}
		});

		columnMeterDesc.setStyle("-fx-alignment: CENTER;");
		columnMeterDesc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BatchModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<BatchModel, String> p) {
				return new ReadOnlyObjectWrapper<>(p.getValue().getMeterType().getMeterModelDescription());
			}
		});

		batchService = new BatchService();

		batchesData = FXCollections.observableArrayList(batchService.findAll());
		batchTable.setItems(batchesData);

	}

	@FXML
	private void onActionCancelButton(ActionEvent event) {
		// get a handle to the stage
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		stage.close();
	}

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
				BatchModel selectedBatch = (BatchModel) row.getItem();
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confimação");
				alert.setHeaderText("Produção de lote: " + selectedBatch.getBatchId());
				alert.setContentText("Deseja iniciar?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					batchSelectCallback.accept(selectedBatch);
					Stage stage = (Stage) batchTable.getScene().getWindow();
					stage.close();
				}
			}
		}
	}
}
