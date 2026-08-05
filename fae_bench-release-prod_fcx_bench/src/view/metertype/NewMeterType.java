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
import java.util.function.Consumer;

import enumerations.SceneTypeEnum;
import exceptions.NoCheckedException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.MeterTypeModel;
import si.dbcomm.service.MeterTypeService;
import util.MaskTextField;
import util.ValidateFields;
import view.controller.SceneFactory;
import view.flowrate.LoadFlowRateControllerView;

/**
 * @author marcos
 *
 */
public class NewMeterType {

	@FXML
	private AnchorPane root;

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

	// Select flowrates
	@FXML
	private Label lbMinFlowrate;
	
	@FXML
	private Label lbTransitionFlowrate;
	
	@FXML
	private Label lbNominalFlowrate;
	
	@FXML
	private Label lbMaxFlowrate;

	@FXML
	private Button btMinFlowrate;

	@FXML
	private Button btTransitionFlowrate;

	@FXML
	private Button btNominalFlowrate;

	@FXML
	private Button btMaxFlowrate;

	//
	@FXML
	private Button btAction;

	private Consumer<MeterTypeModel> meterCallback;

	//
	private Button selectedFlowrateButton;

	private FlowRateModel minFlowrate;

	private FlowRateModel transitionFlowrate;

	private FlowRateModel nominalFlowrate;

	private FlowRateModel maxFlowrate;

	/**
	 * 
	 */
	public void setCallback(Consumer<MeterTypeModel> callback) {
		this.meterCallback = callback;
	}

	/**
	 * 
	 */
	private void updateFlowrateSelectionView(FlowRateModel flowRate) {
		
//		@FXML
//		private Label lbMinFlowrate;
//		@FXML
//		private Label lbTransitionFlowrate;
//		@FXML
//		private Label lbNominalFlowrate;
//		@FXML
//		private Label lbMaxFlowrate;

		if (selectedFlowrateButton == btMinFlowrate) {
			minFlowrate = flowRate;
			lbMinFlowrate.setText(flowRate.getDescription());
		} else if (selectedFlowrateButton == btTransitionFlowrate) {
			transitionFlowrate = flowRate;
			lbTransitionFlowrate.setText(flowRate.getDescription());
		} else if (selectedFlowrateButton == btNominalFlowrate) {
			nominalFlowrate = flowRate;
			lbNominalFlowrate.setText(flowRate.getDescription());
		} else if (selectedFlowrateButton == btMaxFlowrate) {
			maxFlowrate = flowRate;
			lbMaxFlowrate.setText(flowRate.getDescription());
		}
	}

	/**
	 * 
	 */
	private void inflateSelectFlowrateView() {
		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.LOAD_FLOW_RATE);
			sceneFactory.setTitle("Select Flowrate");
			LoadFlowRateControllerView childController = sceneFactory.getController();
			childController.setCallback(value -> {
				// selectedFlowrateLabel.setText(value.getDescription());
				updateFlowrateSelectionView(value);
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
	 * @throws NoCheckedException
	 * 
	 */
	private MeterTypeModel validateAllFields() throws NoCheckedException {
		boolean validateError = false;

		validateError |= ValidateFields.isEmpty(tfDescription);
		validateError |= ValidateFields.isEmpty(tfMeteringClass);
		validateError |= ValidateFields.isEmpty(tfManufacturer);
		validateError |= ValidateFields.isEmpty(tfCodProduct);
		validateError |= ValidateFields.isEmpty(tfRange);
		validateError |= ValidateFields.isEmpty(tfInmetroQnId);
		validateError |= ValidateFields.isEmpty(tfCarcassLength);
		validateError |= ValidateFields.isEmpty(tfUltraSoundPathLengh);
		validateError |= ValidateFields.isEmpty(tfNominalDiameter);
		validateError |= ValidateFields.isEmpty(tfReductionDiameter);
		validateError |= ValidateFields.isEmpty(tfLowCutoff);
		validateError |= ValidateFields.isEmpty(tfHighCutoff);
		validateError |= ValidateFields.isEmpty(tfMinFlowRatePermissibleError);
		validateError |= ValidateFields.isEmpty(tfNominalFlowRatePermissibleError);
		validateError |= ValidateFields.isEmpty(tfMaxFlowRatePermissibleError);
		validateError |= ValidateFields.isEmpty(tfTransitionFlowRatePermissibleError);

		// Min flowrate
		ValidateFields.clearValidateError(btMinFlowrate);
		if (ValidateFields.isEmpty(lbMinFlowrate)) {
			ValidateFields.setValidateError(btMinFlowrate);
			validateError = true;
		}
		// Transition flowrate
		ValidateFields.clearValidateError(btTransitionFlowrate);
		if (ValidateFields.isEmpty(lbTransitionFlowrate)) {
			ValidateFields.setValidateError(btTransitionFlowrate);
			validateError = true;
		}
		// Nominal flowrate
		ValidateFields.clearValidateError(btNominalFlowrate);
		if (ValidateFields.isEmpty(lbNominalFlowrate)) {
			ValidateFields.setValidateError(btNominalFlowrate);
			validateError = true;
		}
		// Max flowrate
		ValidateFields.clearValidateError(btMaxFlowrate);
		if (ValidateFields.isEmpty(lbMaxFlowrate)) {
			ValidateFields.setValidateError(btMaxFlowrate);
			validateError = true;
		}

		// ----------------------------------------------------
		if (validateError) {
			// tabPane.getSelectionModel().select(0);
			throw new NoCheckedException();
		} else {

			MeterTypeModel meterType = new MeterTypeModel();

			meterType.setMeterModelDescription(tfDescription.getText());
			meterType.setMeteringClass(tfMeteringClass.getText());
			meterType.setManufacturer(tfManufacturer.getText());
			meterType.setCodProduto(tfCodProduct.getText());
			meterType.setRange(Integer.valueOf(tfRange.getText()));
			meterType.setInmetroQnId(tfInmetroQnId.getText());
			meterType.setCarcassLength(Double.valueOf(tfCarcassLength.getText()));
			meterType.setRadioWmBus(cbRadioWmBus.isSelected());
			meterType.setUltraSoundPathLengh(Double.valueOf(tfUltraSoundPathLengh.getText()));
			meterType.setNominalDiameter(Double.valueOf(tfNominalDiameter.getText()));
			meterType.setReductionDiameter(Double.valueOf(tfReductionDiameter.getText()));
			meterType.setLowCutoff(Double.valueOf(tfLowCutoff.getText()));
			meterType.setHighCutoff(Double.valueOf(tfHighCutoff.getText()));
			meterType.setMinFlowRatePermissibleError(Double.valueOf(tfMinFlowRatePermissibleError.getText()));
			meterType.setNominalFlowRatePermissibleError(Double.valueOf(tfNominalFlowRatePermissibleError.getText()));
			meterType.setMaxFlowRatePermissibleError(Double.valueOf(tfMaxFlowRatePermissibleError.getText()));
			meterType.setTransitionFlowRatePermissibleError(Double.valueOf(tfTransitionFlowRatePermissibleError.getText()));
			// Flowrates
			meterType.setMinFlowrate(minFlowrate);
			meterType.setTransitionFlowrate(transitionFlowrate);
			meterType.setNominalFlowrate(nominalFlowrate);
			meterType.setMaxFlowrate(maxFlowrate);

			return meterType;
		}
	}

	/**
	 * 
	 */
	@FXML
	private void onSelectFlowrate(ActionEvent event) {

		// if (event.getSource() == btMinFlowrate) {
		// selectedFlowrateLabel = lbMinFlowrate;
		// } else if (event.getSource() == btTransitionFlowrate) {
		// selectedFlowrateLabel = lbTransitionFlowrate;
		// } else if (event.getSource() == btNominalFlowrate) {
		// selectedFlowrateLabel = lbNominalFlowrate;
		// } else if (event.getSource() == btMaxFlowrate) {
		// selectedFlowrateLabel = lbMaxFlowrate;
		// }

		selectedFlowrateButton = (Button) event.getSource();
		//
		inflateSelectFlowrateView();
	}

	/**
	 * 
	 */
	@FXML
	private void onActionButton(ActionEvent event) {

		try {
			MeterTypeModel meterType = validateAllFields();

			MeterTypeService meterTypeService = new MeterTypeService();
			meterTypeService.persist(meterType);
			//
			meterCallback.accept(meterType);
			closeView();
		} catch (NoCheckedException e) {
			e.printStackTrace();
		} catch (CrudDatabaseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicializa a classe controller. Este método é chamado automaticamente após o arquivo fxml ter sido carregado.
	 */
	@FXML
	private void initialize() {

		MaskTextField.numericField(tfRange, 6);
		MaskTextField.maxField(tfInmetroQnId, 1);
		MaskTextField.decimalField(tfCarcassLength, 7, 3);
		MaskTextField.decimalField(tfUltraSoundPathLengh, 7, 3);
		MaskTextField.decimalField(tfNominalDiameter, 7, 3);
		MaskTextField.decimalField(tfReductionDiameter, 7, 3);
		MaskTextField.decimalField(tfLowCutoff, 7, 3);
		MaskTextField.decimalField(tfHighCutoff, 7, 3);
		MaskTextField.decimalField(tfMinFlowRatePermissibleError, 7, 3);
		MaskTextField.decimalField(tfNominalFlowRatePermissibleError, 7, 3);
		MaskTextField.decimalField(tfMaxFlowRatePermissibleError, 7, 3);
		MaskTextField.decimalField(tfTransitionFlowRatePermissibleError, 7, 3);
	}
}