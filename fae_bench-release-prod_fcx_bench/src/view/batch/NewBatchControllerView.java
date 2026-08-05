//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file NewBatchControllerView.java
*    @author marcos
*    @date 24 de set de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.batch;

import java.io.IOException;
import java.util.Calendar;
import java.util.function.Consumer;

import enumerations.SceneTypeEnum;
import enumerations.ViewActionEnum;
import exceptions.NoCheckedException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BatchRadioWmBusConfigModel;
import si.dbcomm.model.ClientModel;
import si.dbcomm.model.MeterTypeModel;
import si.dbcomm.model.ProcessConfigModel;
import si.dbcomm.service.BatchRadioWmBusConfigService;
import si.dbcomm.service.BatchService;
import si.dbcomm.service.ClientService;
import si.dbcomm.service.MeterTypeService;
import util.MaskTextField;
import util.ValidateFields;
import view.controller.SceneFactory;
import view.processconfig.LoadProcessConfigControllerView;

/**
 * @author marcos
 *
 */
public class NewBatchControllerView {

	@FXML
	private AnchorPane root;

	@FXML
	private TextField tfBatchNum;

	@FXML
	private TextField tfDescription;

	@FXML
	private TextField tfStartSeq;

	@FXML
	private TextField tfNumberOfMeters;

	@FXML
	private TextField tfYearManufacture;

	@FXML
	private TextField tfManufacturerCode;

	@FXML
	private ComboBox<ClientModel> cbClient;

	@FXML
	private ComboBox<MeterTypeModel> cbMeterType;

	@FXML
	private Label lbLoadProcessConfig;

	@FXML
	private Button btLoadProcessConfig;

	// wM-Bus config
	// @FXML
	// private VBox vbWmbbusConfig;

	@FXML
	private TextField tfStartSeqRadio;

	@FXML
	private TextField tfManufacturerNumberRadio;

	@FXML
	private TextField tfProductCodeRadio;

	@FXML
	private TextField tfVersionRadio;

	@FXML
	private TextField tfTransmitIntervalRadio;

	//
	@FXML
	private Label labelUserMsg;

	// @FXML
	// private Button btnCancel;

	@FXML
	private Button btAction;

	// private ObservableList<ComboBoxChoice> dataMeterType;
	//
	// private ObservableList<ComboBoxChoice> dataClient;

	// private MeterTypeService meterTypeService;
	//
	// private ClientService clientService;
	//
	// private BatchService batchService;

	// private BatchModel editBatch = null;
	//
	// private boolean isEditBatch = false;

	//
	private ProcessConfigModel selectedProcessConfig;

	private Consumer<String> batchSelectCallback = null;

	private BatchModel selectedBatch;

	private ViewActionEnum viewAction = ViewActionEnum.NEW;

	/**
	 * 
	 */
	public void setBatch(BatchModel batchModel) {
		this.selectedBatch = batchModel;
		viewAction = ViewActionEnum.EDIT;
		btAction.setText("Atualizar");
		updateView(selectedBatch);
	}

	/**
	 * 
	 */
	public void setCallback(Consumer<String> callback) {
		this.batchSelectCallback = callback;
	}

	/**
	 * 
	 */
	private void updateView(BatchModel batchModel) {

		//
		tfBatchNum.setText("" + batchModel.getBatchId());
		tfDescription.setText(batchModel.getDescription());
		tfStartSeq.setText("" + batchModel.getStartSerialSeq());
		tfNumberOfMeters.setText("" + batchModel.getNumMeters());
		tfYearManufacture.setText("" + (batchModel.getYearOfManufacture().getYear() - 100));
		tfManufacturerCode.setText("" + batchModel.getManufacturerCode());

		cbClient.getItems().clear();
		cbClient.getItems().add(batchModel.getClient());
		cbClient.setValue(batchModel.getClient());
		cbMeterType.getItems().clear();
		cbMeterType.getItems().add(batchModel.getMeterType());
		cbMeterType.setValue(batchModel.getMeterType());

		//
		tfBatchNum.setEditable(false);
		tfDescription.setEditable(false);
		tfStartSeq.setEditable(false);
		tfNumberOfMeters.setEditable(false);
		tfYearManufacture.setEditable(false);
		tfManufacturerCode.setEditable(false);
	}

	/**
	 * 
	 */
	private void closeView() {
		if (batchSelectCallback != null) {
			batchSelectCallback.accept("");
		}
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	/**
	 * @throws NoCheckedException
	 *
	 */
	private void validateAllFields() throws NoCheckedException {
		boolean validateError = false;

		validateError |= ValidateFields.isEmpty(tfBatchNum);
		validateError |= ValidateFields.isEmpty(tfDescription);
		validateError |= ValidateFields.isEmpty(tfStartSeq);
		validateError |= ValidateFields.isEmpty(tfNumberOfMeters);
		validateError |= ValidateFields.isEmpty(tfYearManufacture);
		validateError |= ValidateFields.isEmpty(tfManufacturerCode);
		validateError |= ValidateFields.isEmpty(cbClient);
		validateError |= ValidateFields.isEmpty(cbMeterType);

		ValidateFields.clearValidateError(btLoadProcessConfig);
		if (ValidateFields.isEmpty(lbLoadProcessConfig)) {
			ValidateFields.setValidateError(btLoadProcessConfig);
			validateError = true;
		}

		// wM-Bus config
		if (((MeterTypeModel) cbMeterType.getValue()).isRadioWmBus()) {
			validateError |= ValidateFields.isEmpty(tfStartSeqRadio);
			validateError |= ValidateFields.isEmpty(tfTransmitIntervalRadio);
		}

		if (validateError) {
			throw new NoCheckedException("Preencha todos os campos selecionados.");
		}
	}

	/**
	 * 
	 */
	private ChangeListener<MeterTypeModel> listener = new ChangeListener<MeterTypeModel>() {
		@Override
		public void changed(ObservableValue arg0, MeterTypeModel arg1, MeterTypeModel arg2) {
			if (arg2.isRadioWmBus()) {
				tfStartSeqRadio.setDisable(false);
				tfManufacturerNumberRadio.setDisable(false);
				tfProductCodeRadio.setDisable(false);
				tfVersionRadio.setDisable(false);
				tfTransmitIntervalRadio.setDisable(false);
			} else {
				tfStartSeqRadio.setDisable(true);
				tfManufacturerNumberRadio.setDisable(true);
				tfProductCodeRadio.setDisable(true);
				tfVersionRadio.setDisable(true);
				tfTransmitIntervalRadio.setDisable(true);
			}
		}
	};

	/**
	 * 
	 */
	@FXML
	private void onLoadProcessConfig() {
		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.LOAD_PROCESS_CONFIG);
			sceneFactory.setTitle("Configuração de Processo");
			LoadProcessConfigControllerView childController = sceneFactory.getController();
			childController.setCallback(value -> {
				if (value != null) {
					selectedProcessConfig = value;
					lbLoadProcessConfig.setText(value.getDescricao());
				}
			});
			sceneFactory.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 
	 */
	@FXML
	private void onActionSalvar() {

		// --------------------------------------------
		labelUserMsg.setText("");
		try {
			validateAllFields();

			BatchRadioWmBusConfigModel batchRadioConfig = null;
			BatchModel batch = new BatchModel();

			batch.setBatchId(tfBatchNum.getText());
			batch.setDescription(tfDescription.getText());
			batch.setStartSerialSeq(Integer.valueOf(tfStartSeq.getText()));
			batch.setNumMeters(Integer.valueOf(tfNumberOfMeters.getText()));
			//
			int year = Integer.valueOf(tfYearManufacture.getText());
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			batch.setYearOfManufacture(calendar.getTime());
			batch.setManufacturerCode(tfManufacturerCode.getText());
			//
			batch.setClient((ClientModel) cbClient.getValue());
			MeterTypeModel meterType = (MeterTypeModel) cbMeterType.getValue();
			batch.setMeterType(meterType);
			batch.setIdxProcessConfig(selectedProcessConfig.getId());
			batch.setFinished(false);

			if (meterType.isRadioWmBus()) {
				// wM-Bus config
				batchRadioConfig = new BatchRadioWmBusConfigModel();
				batchRadioConfig.setStartSerialSeq(Integer.valueOf(tfStartSeqRadio.getText()));
				batchRadioConfig.setManufacNumber(tfManufacturerNumberRadio.getText());
				batchRadioConfig.setProductCode(tfProductCodeRadio.getText());
				batchRadioConfig.setVersion(tfVersionRadio.getText());
				batchRadioConfig.setTransmitInterval(Short.valueOf(tfTransmitIntervalRadio.getText()));
			}
			//
			BatchService batchService = new BatchService();
			BatchRadioWmBusConfigService wmBusService = new BatchRadioWmBusConfigService();

			if (viewAction == ViewActionEnum.NEW) {
				// batchService.persist(batch);
				// //
				// batchRadioConfig.setBatch(batch);
				// wmBusService.persist(batchRadioConfig);

				if (meterType.isRadioWmBus()) {
//					batchService.saveBatchWithRadio(batch, batchRadioConfig);
					
					batchRadioConfig.setBatch(batch);
					wmBusService.persist(batchRadioConfig);
					batchRadioConfig.setBatch(batch);
				} else {
					batchService.persist(batch);
				}
			} else if (viewAction == ViewActionEnum.EDIT) {
				// selectedBatch.setIdxProcessConfig(batch.getIdxProcessConfig());
				// batchService.update(selectedBatch);
			}
			closeView();
		} catch (NoCheckedException ex) {
			// e.printStackTrace();
			labelUserMsg.setText(ex.getMessage());
		} catch (CrudDatabaseException ex) {
			// e.printStackTrace();
			labelUserMsg.setText(ex.getMessage());
		}

		// --------------------------------------------
		// try {
		// validateAllField();
		//
		// BatchModel newBatch = new BatchModel();
		// ClientModel client = clientService.findById(comboBoxClient.getSelectionModel().getSelectedItem().getId());
		// newBatch.setClient(client);
		// MeterTypeModel meterType = meterTypeService.findById(comboBoxTipoMedi.getSelectionModel().getSelectedItem().getId());
		// newBatch.setMeterType(meterType);
		// newBatch.setNumMeters(Integer.parseInt(textFieldQtdMedi.getText()));
		// newBatch.setStartSerialSeq(Integer.parseInt(textFieldNumSer.getText()));
		// newBatch.setBatchId(textfieldBatchNum.getText());
		// newBatch.setManufacturerCode(textFieldManufacturerCode.getText());
		//
		// int year = Integer.valueOf(textfieldYearManufacture.getText().toString());
		// Calendar calendar = Calendar.getInstance();
		// calendar.set(Calendar.YEAR, year);
		// Date dateYear = calendar.getTime();
		// newBatch.setYearOfManufacture(dateYear);
		// newBatch.setFinished(false);
		//
		// if (isEditBatch) {
		// newBatch.setId(editBatch.getId());
		// newBatch.setBatchId(editBatch.getBatchId());
		// batchService.update(newBatch);
		// } else {
		// batchService.persist(newBatch);
		// }
		//
		// onCloseStage();
		// } catch (NoCheckedException ex) {
		// labelUserMsg.setVisible(true);
		// labelUserMsg.setText(ex.getMessage());
		// } catch (CrudDatabaseException ex) {
		// labelUserMsg.setVisible(true);
		// labelUserMsg.setText(ex.getMessage());
		// }
	}

	/**
	 * 
	 */
	@FXML
	private void initialize() {
		MeterTypeService meterTypeService = new MeterTypeService();
		ClientService clientService = new ClientService();
		// batchService = new BatchService();

		//
		// MaskTextField.numericField(tfBatchNum, 6);
		MaskTextField.numericField(tfStartSeq, 6);
		MaskTextField.numericField(tfNumberOfMeters, 6);

		//
		cbClient.getItems().addAll(clientService.findAll());
		cbMeterType.getItems().addAll(meterTypeService.findAll());
		cbMeterType.valueProperty().addListener(listener);

		// wM-Bus cofig.
		tfStartSeqRadio.setDisable(true);
		tfManufacturerNumberRadio.setDisable(true);
		tfProductCodeRadio.setDisable(true);
		tfVersionRadio.setDisable(true);
		tfTransmitIntervalRadio.setDisable(true);

		labelUserMsg.setText("");

		viewAction = ViewActionEnum.NEW;
		btAction.setText("Salvar");
	}
}
