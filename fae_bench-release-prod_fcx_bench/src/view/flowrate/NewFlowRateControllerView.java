//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file NewFlowRateControllerView.java
*    @author osmenio
*    @date 13 de mar de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.flowrate;

import java.util.Optional;
import java.util.function.Consumer;

import exceptions.NoCheckedException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.PidConfigModel;
import si.dbcomm.model.PumpModel;
import si.dbcomm.model.RamalModel;
import si.dbcomm.model.RefMeterModel;
import si.dbcomm.service.FlowRateService;
import si.dbcomm.service.PumpService;
import si.dbcomm.service.RamalService;
import si.dbcomm.service.RefMeterService;
import view.ComboBoxChoice;

/**
 * @author marcos
 *
 */
public class NewFlowRateControllerView {
	// Style Ok/Error
	private static String styleError = "-fx-border-color: red ; -fx-border-width: 1px ;";//TODO - Osmenio - mudar css para as classe generalStyleSheet.css
	
	private static String styleOk = "";

	@FXML
	private BorderPane root;

	// -----------------------------------------------------------
	// FlowRate
	@FXML
	private TextField textfieldFlowRate;

	@FXML
	private TextField textFieldDescription;

	@FXML
	private TextField textfieldUpperLimit;

	@FXML
	private TextField textfieldLowerLimit;

	// -----------------------------------------------------------
	// PID controller
	@FXML
	private TextField textfieldKp;

	@FXML
	private TextField textfieldKi;

	@FXML
	private TextField textfieldKd;

	// -----------------------------------------------------------
	// Ramal
	// RadioButton
	@FXML
	private ToggleGroup ramalGroupRadioButton;
	@FXML
	private RadioButton radioButtonRm01;
	@FXML
	private RadioButton radioButtonRm02;
	@FXML
	private RadioButton radioButtonRm03;
	@FXML
	private RadioButton radioButtonRm04;
	@FXML
	private RadioButton radioButtonRm05;
	@FXML
	private RadioButton radioButtonRm06;
	@FXML
	private RadioButton radioButtonRm07;
	@FXML
	private RadioButton radioButtonRm08;
	@FXML
	private RadioButton radioButtonRm09;
	@FXML
	private RadioButton radioButtonRm10;
	// Labels max
	@FXML
	private Label labalMaxRm01;
	@FXML
	private Label labalMaxRm02;
	@FXML
	private Label labalMaxRm03;
	@FXML
	private Label labalMaxRm04;
	@FXML
	private Label labalMaxRm05;
	@FXML
	private Label labalMaxRm06;
	@FXML
	private Label labalMaxRm07;
	@FXML
	private Label labalMaxRm08;
	@FXML
	private Label labalMaxRm09;
	@FXML
	private Label labalMaxRm10;
	// Labels mim
	@FXML
	private Label labalMinRm01;
	@FXML
	private Label labalMinRm02;
	@FXML
	private Label labalMinRm03;
	@FXML
	private Label labalMinRm04;
	@FXML
	private Label labalMinRm05;
	@FXML
	private Label labalMinRm06;
	@FXML
	private Label labalMinRm07;
	@FXML
	private Label labalMinRm08;
	@FXML
	private Label labalMinRm09;
	@FXML
	private Label labalMinRm10;

	// -----------------------------------------------------------
	// Ref meter
	// RadioButton
	@FXML
	private ToggleGroup refMeterGroupRadioButton;
	@FXML
	private RadioButton radioButtonRefMeter01;
	@FXML
	private RadioButton radioButtonRefMeter02;
	@FXML
	private RadioButton radioButtonRefMeter03;
	// Labels max
	@FXML
	private Label labalMaxRefMeter01;
	@FXML
	private Label labalMaxRefMeter02;
	@FXML
	private Label labalMaxRefMeter03;
	// Labels mim
	@FXML
	private Label labalMinRefMeter01;
	@FXML
	private Label labalMinRefMeter02;
	@FXML
	private Label labalMinRefMeter03;

	// -----------------------------------------------------------
	// Pump
	// RadioButton
	@FXML
	private ToggleGroup pumpGroupRadioButton;
	@FXML
	private RadioButton radioButtonPump01;
	@FXML
	private RadioButton radioButtonPump02;
	@FXML
	private RadioButton radioButtonPump03;
	// Labels max
	@FXML
	private Label labalMaxPump01;
	@FXML
	private Label labalMaxPump02;
	@FXML
	private Label labalMaxPump03;
	// Labels mim
	@FXML
	private Label labalMinPump01;
	@FXML
	private Label labalMinPump02;
	@FXML
	private Label labalMinPump03;

	// @FXML
	// private TextField textFieldYearManufacture;
	// @FXML
	// private DatePicker datePickerYearManufacture;

	@FXML
	private Label labelRequiredFields;

	@FXML
	private Button btnCancel;
	@FXML
	private Button btnSalvar;

	private FlowRateService flowRateService;
	private RamalService ramalService;
	private RefMeterService refMeterService;
	private PumpService pumpService;

	private FlowRateModel editFlowRate = null;
	private boolean isEditFlowRate = false;

	private Consumer<String> flowRateSelectCallback = null;

	private RamalModel ramalModelRadioButtonSelected;
	private RefMeterModel refMeterModelRadioButtonSelected;
	private PumpModel pumpModelRadioButtonSelected;

	/**
	 * 
	 * 
	 */
	public void setCallback(Consumer<String> callback) {
		this.flowRateSelectCallback = callback;
	}

	/**
	 * 
	 * 
	 */
	private void onCloseStage() {
		if (flowRateSelectCallback != null) {
			flowRateSelectCallback.accept("");
		}
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	/**
	 * 
	 */
	private void setViewSelectedRamal(RamalModel ramal) {
		switch ((int) ramal.getId()) {
			case 1:
				radioButtonRm01.setText(ramal.getTag());
				radioButtonRm01.setUserData(ramal);
				radioButtonRm01.setToggleGroup(ramalGroupRadioButton);
				radioButtonRm01.setSelected(true);
				labalMaxRm01.setText("" + ramal.getMaxFlowRate());
				labalMinRm01.setText("" + ramal.getMinFlowRate());
			break;
			case 2:
				radioButtonRm02.setText(ramal.getTag());
				radioButtonRm02.setUserData(ramal);
				radioButtonRm02.setToggleGroup(ramalGroupRadioButton);
				radioButtonRm02.setSelected(true);
				labalMaxRm02.setText("" + ramal.getMaxFlowRate());
				labalMinRm02.setText("" + ramal.getMinFlowRate());
			break;
			case 3:
				radioButtonRm03.setText(ramal.getTag());
				radioButtonRm03.setUserData(ramal);
				radioButtonRm03.setToggleGroup(ramalGroupRadioButton);
				radioButtonRm03.setSelected(true);
				labalMaxRm03.setText("" + ramal.getMaxFlowRate());
				labalMinRm03.setText("" + ramal.getMinFlowRate());
			break;
			case 4:
				radioButtonRm04.setText(ramal.getTag());
				radioButtonRm04.setUserData(ramal);
				radioButtonRm04.setToggleGroup(ramalGroupRadioButton);
				radioButtonRm04.setSelected(true);
				labalMaxRm04.setText("" + ramal.getMaxFlowRate());
				labalMinRm04.setText("" + ramal.getMinFlowRate());
			break;
			case 5:
				radioButtonRm05.setText(ramal.getTag());
				radioButtonRm05.setUserData(ramal);
				radioButtonRm05.setToggleGroup(ramalGroupRadioButton);
				radioButtonRm05.setSelected(true);
				labalMaxRm05.setText("" + ramal.getMaxFlowRate());
				labalMinRm05.setText("" + ramal.getMinFlowRate());
			break;
			case 6:
				radioButtonRm06.setText(ramal.getTag());
				radioButtonRm06.setUserData(ramal);
				radioButtonRm06.setToggleGroup(ramalGroupRadioButton);
				radioButtonRm06.setSelected(true);
				labalMaxRm06.setText("" + ramal.getMaxFlowRate());
				labalMinRm06.setText("" + ramal.getMinFlowRate());
			break;
			case 7:
				radioButtonRm07.setText(ramal.getTag());
				radioButtonRm07.setUserData(ramal);
				radioButtonRm07.setToggleGroup(ramalGroupRadioButton);
				radioButtonRm07.setSelected(true);
				labalMaxRm07.setText("" + ramal.getMaxFlowRate());
				labalMinRm07.setText("" + ramal.getMinFlowRate());
			break;
			case 8:
				radioButtonRm08.setText(ramal.getTag());
				radioButtonRm08.setUserData(ramal);
				radioButtonRm08.setToggleGroup(ramalGroupRadioButton);
				radioButtonRm08.setSelected(true);
				labalMaxRm08.setText("" + ramal.getMaxFlowRate());
				labalMinRm08.setText("" + ramal.getMinFlowRate());
			break;
			case 9:
				radioButtonRm09.setText(ramal.getTag());
				radioButtonRm09.setUserData(ramal);
				radioButtonRm09.setToggleGroup(ramalGroupRadioButton);
				radioButtonRm09.setSelected(true);
				labalMaxRm09.setText("" + ramal.getMaxFlowRate());
				labalMinRm09.setText("" + ramal.getMinFlowRate());
			break;
			case 10:
				radioButtonRm10.setText(ramal.getTag());
				radioButtonRm10.setUserData(ramal);
				radioButtonRm10.setToggleGroup(ramalGroupRadioButton);
				radioButtonRm10.setSelected(true);
				labalMaxRm10.setText("" + ramal.getMaxFlowRate());
				labalMinRm10.setText("" + ramal.getMinFlowRate());
			break;
		}
	}

	/**
	 * 
	 */
	private void setViewSelectedRefMeter(RefMeterModel refMeter) {
		switch ((int) refMeter.getId()) {
			case 1:
				radioButtonRefMeter01.setText(refMeter.getTag());
				radioButtonRefMeter01.setUserData(refMeter);
				radioButtonRefMeter01.setToggleGroup(refMeterGroupRadioButton);
				radioButtonRefMeter01.setSelected(true);
				labalMaxRefMeter01.setText("" + refMeter.getMaxFlowRate());
				labalMinRefMeter01.setText("" + refMeter.getMinFlowRate());
			break;
			case 2:
				radioButtonRefMeter02.setText(refMeter.getTag());
				radioButtonRefMeter02.setUserData(refMeter);
				radioButtonRefMeter02.setToggleGroup(refMeterGroupRadioButton);
				radioButtonRefMeter02.setSelected(true);
				labalMaxRefMeter02.setText("" + refMeter.getMaxFlowRate());
				labalMinRefMeter02.setText("" + refMeter.getMinFlowRate());
			break;
			case 3:
				radioButtonRefMeter03.setText(refMeter.getTag());
				radioButtonRefMeter03.setUserData(refMeter);
				radioButtonRefMeter03.setToggleGroup(refMeterGroupRadioButton);
				radioButtonRefMeter03.setSelected(true);
				labalMaxRefMeter03.setText("" + refMeter.getMaxFlowRate());
				labalMinRefMeter03.setText("" + refMeter.getMinFlowRate());
			break;
		}
	}

	/**
	 * 
	 */
	private void setViewSelectedPump(PumpModel pump) {
		switch ((int) pump.getId()) {
			case 1:
				radioButtonPump01.setText(pump.getTag());
				radioButtonPump01.setUserData(pump);
				radioButtonPump01.setToggleGroup(pumpGroupRadioButton);
				radioButtonPump01.setSelected(true);
				labalMaxPump01.setText("" + pump.getMaxFlowRate());
				labalMinPump01.setText("" + pump.getMinFlowRate());
			break;
			case 2:
				radioButtonPump02.setText(pump.getTag());
				radioButtonPump02.setUserData(pump);
				radioButtonPump02.setToggleGroup(pumpGroupRadioButton);
				radioButtonPump02.setSelected(true);
				labalMaxPump02.setText("" + pump.getMaxFlowRate());
				labalMinPump02.setText("" + pump.getMinFlowRate());
			break;
			case 3:
				radioButtonPump03.setText(pump.getTag());
				radioButtonPump03.setUserData(pump);
				radioButtonPump03.setToggleGroup(pumpGroupRadioButton);
				radioButtonPump03.setSelected(true);
				labalMaxPump03.setText("" + pump.getMaxFlowRate());
				labalMinPump03.setText("" + pump.getMinFlowRate());
			break;
		}
	}

	/**
		 * 
		 */
	public void setBatch(FlowRateModel flowRate) {
		editFlowRate = flowRate;
		isEditFlowRate = true;

		// FlowRate
		textfieldFlowRate.setText("" + flowRate.getFlowRate());
		textFieldDescription.setText("" + flowRate.getDescription());
		textfieldUpperLimit.setText("" + flowRate.getUpperLimit());
		textfieldLowerLimit.setText("" + flowRate.getLowerLimit());

		// PID controller
		if (flowRate.getPidConfig() != null) {
			textfieldKp.setText("" + flowRate.getPidConfig().getProportionalKp());
			textfieldKi.setText("" + flowRate.getPidConfig().getIntegrativeTm());
			textfieldKd.setText("" + flowRate.getPidConfig().getDerivativeTv());
		}

		// Ramal
		if (flowRate.getRamal() != null)
			setViewSelectedRamal(flowRate.getRamal());

		// RefMeter
		if (flowRate.getRefMeter() != null)
			setViewSelectedRefMeter(flowRate.getRefMeter());

		// Pump
		if (flowRate.getPump() != null)
			setViewSelectedPump(flowRate.getPump());

		// Update view
		// textfieldBatchNum.setEditable(false);
		btnSalvar.setText("Atualizar");
	}

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private StringConverter<ComboBoxChoice> stringConverter = new StringConverter<ComboBoxChoice>() {
		//TODO - Osmenio - metodo nao utilizado verificar possibilidade de remoção
		@Override
		public String toString(ComboBoxChoice object) {
			return object.getName();
		}

		@Override
		public ComboBoxChoice fromString(String string) {
			return null;
		}
	};

	/**
	 * 
	 * 
	 */
	private void validateAllField() throws NoCheckedException {

		// int checkedItem = 0;
		boolean checkedFlowRate = false;
		boolean checkedDescription = false;
		boolean checkedUpperLimit = false;
		boolean checkedLowerLimit = false;
		boolean checkedRamal = false;
		boolean checkedRefMeter = false;
		boolean checkedPump = false;
		@SuppressWarnings("unused")
		String fieldName = "";

		// FlowRate
		if (textfieldFlowRate.getText().length() == 0) {
			textfieldFlowRate.setStyle(styleError);
			textfieldFlowRate.setFocusTraversable(true);
			textfieldFlowRate.requestFocus();

			// checkedItem++;
			checkedFlowRate = false;
			fieldName = "No FlowRate filled";
		} else {
			checkedFlowRate = true;
			textfieldFlowRate.setStyle(styleOk);
		}

		// Description
		if (textFieldDescription.getText().length() == 0) {
			textFieldDescription.setStyle(styleError);
			textFieldDescription.setFocusTraversable(true);
			textFieldDescription.requestFocus();

			// checkedItem++;
			checkedDescription = false;
			fieldName = "No Description filled";
		} else {
			checkedDescription = true;
			textFieldDescription.setStyle(styleOk);
		}

		// Upper Limit
		if (textfieldUpperLimit.getText().length() == 0) {
			textfieldUpperLimit.setStyle(styleError);
			textfieldUpperLimit.setFocusTraversable(true);
			textfieldUpperLimit.requestFocus();

			// checkedItem++;
			checkedUpperLimit = false;
			fieldName = "No Upper Limit filled";
		} else {
			checkedUpperLimit = true;
			textfieldUpperLimit.setStyle(styleOk);
		}

		// Lower Limit
		if (textfieldLowerLimit.getText().length() == 0) {
			textfieldLowerLimit.setStyle(styleError);
			textfieldLowerLimit.setFocusTraversable(true);
			textfieldLowerLimit.requestFocus();

			// checkedItem++;
			checkedLowerLimit = false;
			fieldName = "No Lower Limit filled";
		} else {
			checkedLowerLimit = true;
			textfieldLowerLimit.setStyle(styleOk);
		}

		// // Kp
		// if (textfieldKp.getText().length() == 0) {
		// textfieldKp.setStyle(styleError);
		// textfieldKp.setFocusTraversable(true);
		// textfieldKp.requestFocus();
		//
		// checkedItem++;
		// fieldName = "No Kp filled";
		// } else {
		// textfieldKp.setStyle(styleOk);
		// }
		//
		// // Ki
		// if (textfieldKi.getText().length() == 0) {
		// textfieldKi.setStyle(styleError);
		// textfieldKi.setFocusTraversable(true);
		// textfieldKi.requestFocus();
		//
		// checkedItem++;
		// fieldName = "No Ki filled";
		// } else {
		// textfieldKi.setStyle(styleOk);
		// }
		//
		// // Kd
		// if (textfieldKd.getText().length() == 0) {
		// textfieldKd.setStyle(styleError);
		// textfieldKd.setFocusTraversable(true);
		// textfieldKd.requestFocus();
		//
		// checkedItem++;
		// fieldName = "No Kd filled";
		// } else {
		// textfieldKd.setStyle(styleOk);
		// }

		// Ramal
		if (ramalModelRadioButtonSelected == null) {
			// checkedItem++;
			checkedRamal = false;
			fieldName = "No Ramal selected";
		} else {
			checkedRamal = true;
		}
		// Ref meter
		if (refMeterModelRadioButtonSelected == null) {
			// checkedItem++;
			checkedRefMeter = false;
			fieldName = "No Ref Meter selected";
		} else {
			checkedRefMeter = true;
		}

		// Pump
		if (pumpModelRadioButtonSelected == null) {
			// checkedItem++;
			checkedPump = false;
			fieldName = "No Pump selected";
		} else {
			checkedPump = true;
		}

		// if (checkedItem != 0) {
		if ((!checkedFlowRate) || (!checkedDescription) || (!checkedUpperLimit) || (!checkedLowerLimit)) {

			labelRequiredFields.setVisible(true);
			labelRequiredFields.setText("Todos os campos devem ser preenchidos");
			// throw new NoCheckedException(fieldName);
			throw new NoCheckedException("Some field is empty");

		} else if ((!checkedRamal) || (!checkedRefMeter) || (!checkedPump)) {
			inflateCheckEmptyField();
		} else {
			labelRequiredFields.setVisible(false);
		}
	}

	/**
	 * @throws NoCheckedException
	 * 
	 * 
	 */
	private void inflateCheckEmptyField() throws NoCheckedException {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Salvar Vazão");
		alert.setHeaderText("Um dos campos estão vazios. \n - Ramal \n - Medidor de ref. \n - Bomba ");
		alert.setContentText("Deseja salvar assim mesmo?");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			// try {
			// // Delete selected batch
			// flowRateService.delete(batch);
			// } catch (CrudDatabaseException e) {
			// e.printStackTrace();
			// }
		} else if (result.get() == ButtonType.CANCEL) {
			throw new NoCheckedException("Some field is empty");
		}
	}

	/**
	 * 
	 *
	 */
	ChangeListener<Toggle> listenerRamalRadioGroup = new ChangeListener<Toggle>() {
		public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
			if (ramalGroupRadioButton.getSelectedToggle() != null) {
				ramalModelRadioButtonSelected = (RamalModel) ramalGroupRadioButton.getSelectedToggle().getUserData();
			}
		}
	};

	/**
	 * 
	 *
	 */
	ChangeListener<Toggle> listenerRefMeterRadioGroup = new ChangeListener<Toggle>() {
		public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
			if (refMeterGroupRadioButton.getSelectedToggle() != null) {
				refMeterModelRadioButtonSelected = (RefMeterModel) refMeterGroupRadioButton.getSelectedToggle().getUserData();
			}
		}
	};

	/**
	 * 
	 *
	 */
	ChangeListener<Toggle> listenerPumpRadioGroup = new ChangeListener<Toggle>() {
		public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
			if (pumpGroupRadioButton.getSelectedToggle() != null) {
				pumpModelRadioButtonSelected = (PumpModel) pumpGroupRadioButton.getSelectedToggle().getUserData();
			}
		}
	};

	/**
	 * 
	 *
	 */
	private void setNumDigits(TextField textField, int length) {

		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > oldValue.length()) {
					// Check if the new character is greater than LIMIT
					if (textField.getText().length() >= length) {
						textField.setText(textField.getText().substring(0, length));
					}
				}
			}
		});
	}

	/**
	 * 
	 * 
	 */
	private void setMaxValueFields() {
		setNumDigits(textfieldFlowRate, 5);
		// setNumDigits(textFieldDescription, 7);
		setNumDigits(textfieldUpperLimit, 7);
		setNumDigits(textfieldLowerLimit, 7);
	}

	/**
	 * 
	 * 
	 */
	private void loadNewBatch() {
		flowRateService = new FlowRateService();
		ramalService = new RamalService();
		refMeterService = new RefMeterService();
		pumpService = new PumpService();

		// ------------------------------------------------------------
		// Ramal
		for (RamalModel ramalItem : ramalService.findAll()) {
			switch ((int) ramalItem.getId()) {
				case 1:
					radioButtonRm01.setText(ramalItem.getTag());
					radioButtonRm01.setUserData(ramalItem);
					radioButtonRm01.setToggleGroup(ramalGroupRadioButton);
					labalMaxRm01.setText("" + ramalItem.getMaxFlowRate());
					labalMinRm01.setText("" + ramalItem.getMinFlowRate());
				break;
				case 2:
					radioButtonRm02.setText(ramalItem.getTag());
					radioButtonRm02.setUserData(ramalItem);
					radioButtonRm02.setToggleGroup(ramalGroupRadioButton);
					labalMaxRm02.setText("" + ramalItem.getMaxFlowRate());
					labalMinRm02.setText("" + ramalItem.getMinFlowRate());
				break;
				case 3:
					radioButtonRm03.setText(ramalItem.getTag());
					radioButtonRm03.setUserData(ramalItem);
					radioButtonRm03.setToggleGroup(ramalGroupRadioButton);
					labalMaxRm03.setText("" + ramalItem.getMaxFlowRate());
					labalMinRm03.setText("" + ramalItem.getMinFlowRate());
				break;
				case 4:
					radioButtonRm04.setText(ramalItem.getTag());
					radioButtonRm04.setUserData(ramalItem);
					radioButtonRm04.setToggleGroup(ramalGroupRadioButton);
					labalMaxRm04.setText("" + ramalItem.getMaxFlowRate());
					labalMinRm04.setText("" + ramalItem.getMinFlowRate());
				break;
				case 5:
					radioButtonRm05.setText(ramalItem.getTag());
					radioButtonRm05.setUserData(ramalItem);
					radioButtonRm05.setToggleGroup(ramalGroupRadioButton);
					labalMaxRm05.setText("" + ramalItem.getMaxFlowRate());
					labalMinRm05.setText("" + ramalItem.getMinFlowRate());
				break;
				case 6:
					radioButtonRm06.setText(ramalItem.getTag());
					radioButtonRm06.setUserData(ramalItem);
					radioButtonRm06.setToggleGroup(ramalGroupRadioButton);
					labalMaxRm06.setText("" + ramalItem.getMaxFlowRate());
					labalMinRm06.setText("" + ramalItem.getMinFlowRate());
				break;
				case 7:
					radioButtonRm07.setText(ramalItem.getTag());
					radioButtonRm07.setUserData(ramalItem);
					radioButtonRm07.setToggleGroup(ramalGroupRadioButton);
					labalMaxRm07.setText("" + ramalItem.getMaxFlowRate());
					labalMinRm07.setText("" + ramalItem.getMinFlowRate());
				break;
				case 8:
					radioButtonRm08.setText(ramalItem.getTag());
					radioButtonRm08.setUserData(ramalItem);
					radioButtonRm08.setToggleGroup(ramalGroupRadioButton);
					labalMaxRm08.setText("" + ramalItem.getMaxFlowRate());
					labalMinRm08.setText("" + ramalItem.getMinFlowRate());
				break;
				case 9:
					radioButtonRm09.setText(ramalItem.getTag());
					radioButtonRm09.setUserData(ramalItem);
					radioButtonRm09.setToggleGroup(ramalGroupRadioButton);
					labalMaxRm09.setText("" + ramalItem.getMaxFlowRate());
					labalMinRm09.setText("" + ramalItem.getMinFlowRate());
				break;
				case 10:
					radioButtonRm10.setText(ramalItem.getTag());
					radioButtonRm10.setUserData(ramalItem);
					radioButtonRm10.setToggleGroup(ramalGroupRadioButton);
					labalMaxRm10.setText("" + ramalItem.getMaxFlowRate());
					labalMinRm10.setText("" + ramalItem.getMinFlowRate());
				break;
			}
		}
		ramalGroupRadioButton.selectedToggleProperty().addListener(listenerRamalRadioGroup);

		// ------------------------------------------------------------
		// Ref meter
		for (RefMeterModel refMeter : refMeterService.findAll()) {
			switch ((int) refMeter.getId()) {
				case 1:
					radioButtonRefMeter01.setText(refMeter.getTag());
					radioButtonRefMeter01.setUserData(refMeter);
					radioButtonRefMeter01.setToggleGroup(refMeterGroupRadioButton);
					labalMaxRefMeter01.setText("" + refMeter.getMaxFlowRate());
					labalMinRefMeter01.setText("" + refMeter.getMinFlowRate());
				break;
				case 2:
					radioButtonRefMeter02.setText(refMeter.getTag());
					radioButtonRefMeter02.setUserData(refMeter);
					radioButtonRefMeter02.setToggleGroup(refMeterGroupRadioButton);
					labalMaxRefMeter02.setText("" + refMeter.getMaxFlowRate());
					labalMinRefMeter02.setText("" + refMeter.getMinFlowRate());
				break;
				case 3:
					radioButtonRefMeter03.setText(refMeter.getTag());
					radioButtonRefMeter03.setUserData(refMeter);
					radioButtonRefMeter03.setToggleGroup(refMeterGroupRadioButton);
					labalMaxRefMeter03.setText("" + refMeter.getMaxFlowRate());
					labalMinRefMeter03.setText("" + refMeter.getMinFlowRate());
				break;
			}
		}
		refMeterGroupRadioButton.selectedToggleProperty().addListener(listenerRefMeterRadioGroup);

		// ------------------------------------------------------------
		// Pump
		for (PumpModel pump : pumpService.findAll()) {
			switch ((int) pump.getId()) {
				case 1:
					radioButtonPump01.setText(pump.getTag());
					radioButtonPump01.setUserData(pump);
					radioButtonPump01.setToggleGroup(pumpGroupRadioButton);
					labalMaxPump01.setText("" + pump.getMaxFlowRate());
					labalMinPump01.setText("" + pump.getMinFlowRate());
				break;
				case 2:
					radioButtonPump02.setText(pump.getTag());
					radioButtonPump02.setUserData(pump);
					radioButtonPump02.setToggleGroup(pumpGroupRadioButton);
					labalMaxPump02.setText("" + pump.getMaxFlowRate());
					labalMinPump02.setText("" + pump.getMinFlowRate());
				break;
				case 3:
					radioButtonPump03.setText(pump.getTag());
					radioButtonPump03.setUserData(pump);
					radioButtonPump03.setToggleGroup(pumpGroupRadioButton);
					labalMaxPump03.setText("" + pump.getMaxFlowRate());
					labalMinPump03.setText("" + pump.getMinFlowRate());
				break;
			}
		}
		pumpGroupRadioButton.selectedToggleProperty().addListener(listenerPumpRadioGroup);

		labelRequiredFields.setVisible(false);
		//
		setMaxValueFields();
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onActionSalvar() {

		try {
			validateAllField();

			PidConfigModel pidConfigModel = new PidConfigModel();
			if (textfieldKp.getText().length() != 0)
				pidConfigModel.setProportionalKp(Double.parseDouble(textfieldKp.getText().toString()));
			if (textfieldKi.getText().length() != 0)
				pidConfigModel.setIntegrativeTm(Double.parseDouble(textfieldKi.getText().toString()));
			if (textfieldKd.getText().length() != 0)
				pidConfigModel.setDerivativeTv(Double.parseDouble(textfieldKd.getText().toString()));

			FlowRateModel flowRate = new FlowRateModel();
			flowRate.setFlowRate(Double.parseDouble(textfieldFlowRate.getText().toString()));
			flowRate.setDescription(textFieldDescription.getText().toString());
			flowRate.setUpperLimit(Double.parseDouble(textfieldUpperLimit.getText().toString()));
			flowRate.setLowerLimit(Double.parseDouble(textfieldLowerLimit.getText().toString()));

			flowRate.setPidConfig(pidConfigModel);
			flowRate.setRamal(ramalModelRadioButtonSelected);
			flowRate.setRefMeter(refMeterModelRadioButtonSelected);
			flowRate.setPump(pumpModelRadioButtonSelected);

			// Edit
			if (isEditFlowRate) {
				flowRate.setId(editFlowRate.getId());
				flowRate.setFlowRate(editFlowRate.getFlowRate());
				flowRateService.update(flowRate);
			}
			// Save new
			else {
				flowRateService.persist(flowRate);
			}

			onCloseStage();
		} catch (NoCheckedException ex) {
			// labelRequiredFields.setVisible(true);
			// labelRequiredFields.setText(ex.getMessage());

		} catch (CrudDatabaseException ex) {
			labelRequiredFields.setVisible(true);
			labelRequiredFields.setText(ex.getMessage());
		}
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onActionCancelButton(ActionEvent event) {
		onCloseStage();
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onFieldFocusValidadeNumeric(KeyEvent event) {
		TextField tx = (TextField) event.getSource();
		if (tx != null) {
			String newValue = tx.getText();
			if (!newValue.matches("\\d*")) {
				tx.setText(newValue.replaceAll("[^\\d]", ""));
			}
		}
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void initialize() {
		loadNewBatch();
	}
}
