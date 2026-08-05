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
package view.metertype;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.MeterTypeModel;
import si.dbcomm.service.MeterTypeService;
import view.controller.SceneFactory;

/**
 * @author marcos
 *
 */
public class LoadMeterType {

	@FXML
	private AnchorPane root;

	// Select table
	@FXML
	private TableView<MeterTypeModel> meterTypeTable;

	@FXML
	private TableColumn columnSelect;

	// @FXML
	// private TableColumn<ProcessConfigModel, Long> columnId;

	@FXML
	private TableColumn<MeterTypeModel, String> columnDescription;

	@FXML
	private TableColumn columnDelete;

	// Detail
	@FXML
	private TextField tfDescription;

	@FXML
	private TextField tfMeteringClass;

	@FXML
	private TextField tfManufacturer;

	@FXML
	private TextField tfCodProduct;

	@FXML
	private TextField tfRange;

	@FXML
	private TextField tfInmetroQnId;

	@FXML
	private TextField tfCarcassLength;

	@FXML
	private CheckBox cbRadioWmBus;

	@FXML
	private TextField tfUltraSoundPathLengh;

	@FXML
	private TextField tfNominalDiameter;

	@FXML
	private TextField tfReductionDiameter;

	@FXML
	private TextField tfLowCutoff;

	@FXML
	private TextField tfHighCutoff;

	@FXML
	private TextField tfMinFlowRatePermissibleError;

	@FXML
	private TextField tfNominalFlowRatePermissibleError;

	@FXML
	private TextField tfMaxFlowRatePermissibleError;

	@FXML
	private TextField tfTransitionFlowRatePermissibleError;

	//
	@FXML
	private Button btAction;

	//
	private HashMap<CheckBox, Boolean> hashCheck = new HashMap<>();

	private MeterTypeModel selectedMeterType;

	private ViewActionEnum viewAction = ViewActionEnum.NEW;

	/**
	 * 
	 */
	private void inflateDeleteDialog(MeterTypeModel meterType) {
		// labelUserMsg.setVisible(false);

		MeterTypeService meterTypeService = new MeterTypeService();

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Deletar tipo de medidor");
		// alert.setHeaderText("Lote: " + meterType.getBatchId());
		alert.setContentText("Tem certeza que deseja excluir?");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			try {
				// Delete selected batch
				meterTypeService.delete(meterType);
				refreshTable();
			} catch (CrudDatabaseException ex) {
				ex.printStackTrace();
				// labelUserMsg.setVisible(true);
				// labelUserMsg.setText(ex.getMessage());
			}
		}
	}

	/**
	 * 
	 */
	private void refreshTable() {
		MeterTypeService meterTypeService = new MeterTypeService();
		meterTypeTable.setItems(FXCollections.observableArrayList(meterTypeService.findAll()));
	}

	/**
	*
	*/
	private void updateView(MeterTypeModel meterType) {

		tfDescription.setText(meterType.getMeterModelDescription());
		tfMeteringClass.setText(meterType.getMeteringClass());
		tfManufacturer.setText(meterType.getManufacturer());
		tfCodProduct.setText(meterType.getCodProduto());
		tfRange.setText("" + meterType.getRange());
		tfInmetroQnId.setText("" + meterType.getInmetroQnId());
		tfCarcassLength.setText("" + meterType.getCarcassLength());
		cbRadioWmBus.setSelected(meterType.isRadioWmBus());
		tfUltraSoundPathLengh.setText("" + meterType.getUltraSoundPathLengh());
		tfNominalDiameter.setText("" + meterType.getNominalDiameter());
		tfReductionDiameter.setText("" + meterType.getReductionDiameter());
		tfLowCutoff.setText("" + meterType.getLowCutoff());
		tfHighCutoff.setText("" + meterType.getHighCutoff());
		tfMinFlowRatePermissibleError.setText("" + meterType.getMinFlowRatePermissibleError());
		tfNominalFlowRatePermissibleError.setText("" + meterType.getNominalFlowRatePermissibleError());
		tfMaxFlowRatePermissibleError.setText("" + meterType.getMaxFlowRatePermissibleError());
		tfTransitionFlowRatePermissibleError.setText("" + meterType.getTransitionFlowRatePermissibleError());
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
								selectedMeterType = (MeterTypeModel) getTableView().getItems().get(getIndex());
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
							MeterTypeModel meterType = (MeterTypeModel) getTableView().getItems().get(getIndex());
							inflateDeleteDialog(meterType);
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
	private void inflateNewMeterType() {
		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.NEW_METER_TYPE);
			sceneFactory.setTitle("Novo tipo de medidor");
			NewMeterType childController = sceneFactory.getController();
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
				updateView((MeterTypeModel) row.getItem());
			}
		}
	}

	/**
	 * 
	 */
	@FXML
	private void onActionButton(ActionEvent event) {
		if (viewAction == ViewActionEnum.NEW) {
			inflateNewMeterType();
		} else if (viewAction == ViewActionEnum.SELECT) {
			// processConfigCallback.accept(selectedProcessConfig);
			closeView();
		}
	}

	/**
	 * Inicializa a classe controller. Este método é chamado automaticamente após o arquivo fxml ter sido carregado.
	 */
	@FXML
	private void initialize() {

		// Meter type table
		columnDelete.setStyle("-fx-alignment: CENTER;");
		columnDescription.setCellValueFactory(new PropertyValueFactory<>("meterModelDescription"));
		// Select button
		columnSelect.setStyle("-fx-alignment: CENTER;");
		columnSelect.setCellValueFactory(new PropertyValueFactory<>("Button"));
		columnSelect.setCellFactory(cellFactorySelect);
		//
		columnDelete.setCellValueFactory(new PropertyValueFactory<>("Button"));
		columnDelete.setCellFactory(cellFactoryDelete);

		// -----------------------------------------
		// MeterTypeService meterTypeService = new MeterTypeService();
		// meterTypeTable.setItems(FXCollections.observableArrayList(meterTypeService.findAll()));
		refreshTable();

	}
}