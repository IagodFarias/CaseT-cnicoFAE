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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;

import exceptions.NoCheckedException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.CalculatedFixedConstModel;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.ProcessConfigModel;
import si.dbcomm.service.CalculatedFixedConstService;
import si.dbcomm.service.FlowRateService;
import si.dbcomm.service.ProcessConfigService;
import util.MaskTextField;
import util.ValidateFields;

/**
 * @author marcos
 *
 */
public class NewProcessConfigControllerView {

	@FXML
	private BorderPane root;
	@FXML
	private TabPane tabPane;

	// -------------------------------------------------------
	@FXML
	private TextField tfDescription;
	// Process
	// @FXML
	// private CheckBox cbRadioWmBus;
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
	// @FXML
	// private TextField tfPurgeFlowRate;
	// @FXML
	// private TextField tfPurgeTime;
	@FXML
	private TextField tfZeroFlowTime;
	// @FXML
	// private TextField tfFlowTime;
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

	// -------------------------------------------------------
	// Calib tab
	@FXML
	private Tab tbFlowRateCalib;

	@FXML
	private TextField tfSearchFlowRateCalib;

	// @FXML
	// private Button btSearchFlowRateCalib;

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

	@FXML
	private TableView<FlowRateModel> tvSelectedFlowRateCalib;

	@FXML
	private TableColumn<FlowRateModel, Double> columnSelectedFlowRateCalib;

	@FXML
	private TableColumn<FlowRateModel, String> columnSelectedDescriptionCalib;

	@FXML
	private TableColumn<FlowRateModel, String> columnSelectedRefMeterCalib;

	@FXML
	private TableColumn<FlowRateModel, String> columnSelectedRamalCalid;

	@FXML
	private TableColumn<FlowRateModel, String> columnSelectedPumpCalib;

	@FXML
	private Button btSelectSingleCalib;

	@FXML
	private Button btSelectAllCalib;

	@FXML
	private Button btUnselectSingleCalib;

	@FXML
	private Button btUnselectAllCalib;

	// -------------------------------------------------------
	// Verif tab
	@FXML
	private Tab tbFlowRateVerif;

	@FXML
	private TextField tfSearchFlowRateVerif;

	// @FXML
	// private Button btSearchFlowRateVerif;

	@FXML
	private TableView<FlowRateModel> tvListFlowRateVerif;

	@FXML
	private TableColumn<FlowRateModel, Double> columnListFlowRateVerif;

	@FXML
	private TableColumn<FlowRateModel, String> columnListDescriptionVerif;

	@FXML
	private TableColumn<FlowRateModel, String> columnListRefMeterVerif;

	@FXML
	private TableColumn<FlowRateModel, String> columnListRamalVerif;

	@FXML
	private TableColumn<FlowRateModel, String> columnListPumpVerif;

	@FXML
	private TableView<FlowRateModel> tvSelectedFlowRateVerif;

	@FXML
	private TableColumn<FlowRateModel, Double> columnSelectedFlowRateVerif;

	@FXML
	private TableColumn<FlowRateModel, String> columnSelectedDescriptionVerif;

	@FXML
	private TableColumn<FlowRateModel, String> columnSelectedRefMeterVerif;

	@FXML
	private TableColumn<FlowRateModel, String> columnSelectedRamalVerif;

	@FXML
	private TableColumn<FlowRateModel, String> columnSelectedPumpVerif;

	@FXML
	private Button btSelectSingleVerif;

	@FXML
	private Button btSelectAllVerif;

	@FXML
	private Button btUnselectSingleVerif;

	@FXML
	private Button btUnselectAllVerif;

	// -------------------------------------------------------
	// Calculated fixed const
	@FXML
	private Tab tbCalculatedFixedConst;

	@FXML
	private TextField tfSearchConst;

	@FXML
	private TableView<CalculatedFixedConstModel> tvListCalculatedFixedConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnListDescriptionConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnListFlowRateConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnListKConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnListReConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnListTempConst;

	@FXML
	private TableView<CalculatedFixedConstModel> tvSelectedCalculatedFixedConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnSelectedDescriptionConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnSelectedFlowRateConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnSelectedKConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnSelectedReConst;

	@FXML
	private TableColumn<CalculatedFixedConstModel, String> columnSelectedTempConst;

	@FXML
	private Button btSelectSingleConst;

	@FXML
	private Button btSelectAllConst;

	@FXML
	private Button btUnselectSingleConst;

	@FXML
	private Button btUnselectAllConst;

	// -------------------------------------------------------
	// private ObservableList<FlowRateModel> flowRateData;

	private HashMap<Long, FlowRateModel> hashListFlowRateCalib;

	private HashMap<Long, FlowRateModel> hashSelectedFlowRateCalib;

	private HashMap<Long, FlowRateModel> hashListFlowRateVerif;

	private HashMap<Long, FlowRateModel> hashSelectedFlowRateVerif;

	private HashMap<Long, CalculatedFixedConstModel> hashListFixedConst;

	private HashMap<Long, CalculatedFixedConstModel> hashSelectedFixedConst;

	private Consumer<ProcessConfigModel> processConfigCallback;

	//
	private ProcessConfigService processConfigService = new ProcessConfigService();

	private ProcessConfigModel editProcessConfig;

	private boolean isEdition = false;

	private FlowRateService flowRateService;

	private CalculatedFixedConstService calculatedFixedConstService;

	/**
	 * 
	 */
	public void setCallback(Consumer<ProcessConfigModel> callback) {
		this.processConfigCallback = callback;
	}

	/**
	 * 
	 */
	public void setProcessConfig(ProcessConfigModel processConfig) {
		this.editProcessConfig = processConfig;
		isEdition = true;
	}

	/**
	 * @throws NoCheckedException
	 * 
	 */
	private ProcessConfigModel validateAllFields() throws NoCheckedException {
		boolean validateError = false;

		validateError |= ValidateFields.isEmpty(tfDescription);
		// Process
		validateError |= ValidateFields.isEmpty(tfMeterRadioConfigTries);
		validateError |= ValidateFields.isEmpty(tfMeterDateUpdateTries);

		// Calib
		validateError |= ValidateFields.isEmpty(tfInitCalibTemp);
		validateError |= ValidateFields.isEmpty(tfMaxTrimAgcTries);
		validateError |= ValidateFields.isEmpty(tfPercentageToVerif);

		// Pump
		validateError |= ValidateFields.isEmpty(tfLinePressurePumpLoad);
		validateError |= ValidateFields.isEmpty(tfTimeCheckPressure);

		// Times
		validateError |= ValidateFields.isEmpty(tfZeroFlowTime);
		validateError |= ValidateFields.isEmpty(tfFlowStabilizationTime);
		validateError |= ValidateFields.isEmpty(tfFlowStabilyCheckTime);
		validateError |= ValidateFields.isEmpty(tfFlowRateTimeout);

		// Sizes
		validateError |= ValidateFields.isEmpty(tfFlowBuffSize);
		validateError |= ValidateFields.isEmpty(tfWindowSize);

		// Process steps
		if ((!cbZeroFlowEnable.isSelected()) && (!cbCalibration.isSelected()) && (!cbVerification.isSelected())) {
			ValidateFields.setValidateError(cbZeroFlowEnable);
			ValidateFields.setValidateError(cbCalibration);
			ValidateFields.setValidateError(cbVerification);
			validateError = true;
		} else {
			ValidateFields.clearValidateError(cbZeroFlowEnable);
			ValidateFields.clearValidateError(cbCalibration);
			ValidateFields.clearValidateError(cbVerification);
		}

		// List flow rate calib
		if (!validateError) {
			if (cbCalibration.isSelected()) {
				if (hashSelectedFlowRateCalib.isEmpty()) {
					tabPane.getSelectionModel().select(1);
					ValidateFields.setValidateError(tvSelectedFlowRateCalib);
					validateError = true;
				} else {
					ValidateFields.clearValidateError(tvSelectedFlowRateCalib);
					validateError = false;
				}
			}
		}
		// List flow rate verif
		if (!validateError) {
			if (cbVerification.isSelected()) {
				if (hashSelectedFlowRateVerif.isEmpty()) {
					tabPane.getSelectionModel().select(2);
					ValidateFields.setValidateError(tvSelectedFlowRateVerif);
					validateError = true;
				} else {
					ValidateFields.clearValidateError(tvSelectedFlowRateVerif);
					validateError = false;
				}
			}
		}
		// List calculated fixed const
		if (!validateError) {
			// if (cbVerification.isSelected()) {
			if (hashSelectedFixedConst.isEmpty()) {
				tabPane.getSelectionModel().select(3);
				ValidateFields.setValidateError(tvSelectedCalculatedFixedConst);
				validateError = true;
			} else {
				ValidateFields.clearValidateError(tvSelectedCalculatedFixedConst);
				validateError = false;
			}
			// }
		}

		// ----------------------------------------------------
		if (validateError) {
			// tabPane.getSelectionModel().select(0);
			throw new NoCheckedException();
		} else {
			// BenchSettings benchSettings = new BenchSettings();
			// return benchSettings;

			ProcessConfigModel processConfig = new ProcessConfigModel();

			processConfig.setDescricao(tfDescription.getText());
			// Process
			// processConfig.setRadioWmBus(cbRadioWmBus.isSelected());
			processConfig.setChooseQaSamples(cbChooseQaSamples.isSelected());
			processConfig.setMeterRadioConfigTries(Integer.valueOf(tfMeterRadioConfigTries.getText()));
			processConfig.setMeterDateUpdateTries(Integer.valueOf(tfMeterDateUpdateTries.getText()));
			// Process steps
			processConfig.setZeroFlowEnabled(cbZeroFlowEnable.isSelected());
			if (cbCalibration.isSelected()) {
				processConfig.setOnlyVerification(false);
			} else {
				processConfig.setOnlyVerification(true);
			}

			// Calib
			processConfig.setInitCalibTemp(Integer.valueOf(tfInitCalibTemp.getText()));
			processConfig.setTrimAgcMaxTries(Integer.valueOf(tfMaxTrimAgcTries.getText()));
			processConfig.setPercentageToVerif(Double.valueOf(tfPercentageToVerif.getText()));
			// Pump
			processConfig.setLinePressurePumpLoad(Double.valueOf(tfLinePressurePumpLoad.getText()));
			processConfig.setTimeCheckPressurePumpLoad(Integer.valueOf(tfTimeCheckPressure.getText()));
			// Times
			processConfig.setZeroFlowTime(Long.valueOf(tfZeroFlowTime.getText()));
			processConfig.setPreFlowStabilizationTime(Long.valueOf(tfFlowStabilizationTime.getText()));
			processConfig.setFlowStabilityCheckTime(Integer.valueOf(tfFlowStabilyCheckTime.getText()));
			processConfig.setTimeOutFlowRate(Long.valueOf(tfFlowRateTimeout.getText()));
			// Matfile
			processConfig.setSaveMatData(cbSaveMatData.isSelected());
			processConfig.setSaveMatDataReprovedMeters(cbSaveMatDataReprovedMeters.isSelected());
			processConfig.setSaveCalibLutMatData(cbSaveMatDataCalibLut.isSelected());

			processConfig.setFlowBuffSize(Integer.valueOf(tfFlowBuffSize.getText()));
			processConfig.setWindowSize(Integer.valueOf(tfWindowSize.getText()));

			processConfig.getCalibFlowRates().addAll(hashSelectedFlowRateCalib.values());
			processConfig.getVerifFlowRates().addAll(hashSelectedFlowRateVerif.values());
			processConfig.getCalcFixedConsts().addAll(hashSelectedFixedConst.values());
			return processConfig;
		}
	}

	/**
	 * 
	 */
	private void initAllValues() {
		// Process
		// cbRadioWmBus.setSelected(false);
		cbChooseQaSamples.setSelected(true);
		tfMeterRadioConfigTries.setText("20");
		tfMeterDateUpdateTries.setText("20");
		// Process steps
		cbZeroFlowEnable.setSelected(true);
		cbCalibration.setSelected(true);
		cbVerification.setSelected(true);
		// Calib
		tfInitCalibTemp.setText("30");
		tfMaxTrimAgcTries.setText("50");
		tfPercentageToVerif.setText("0.3");
		// Pump
		tfLinePressurePumpLoad.setText("40");
		tfTimeCheckPressure.setText("5000");
		// Times
		tfZeroFlowTime.setText("20000");
		tfFlowStabilizationTime.setText("15000");
		tfFlowStabilyCheckTime.setText("10000");
		tfFlowRateTimeout.setText("180000");
		// Matfile
		cbSaveMatData.setSelected(true);
		cbSaveMatDataReprovedMeters.setSelected(true);
		cbSaveMatDataCalibLut.setSelected(true);
		// Sizes
		tfFlowBuffSize.setText("40");
		tfWindowSize.setText("1000");
	}

	/**
	 * 
	 */
	private void updateListCalculatedFixedConst(String like) {
		ArrayList<CalculatedFixedConstModel> constList;
		// Load from db
		if (like.isEmpty()) {
			constList = (ArrayList<CalculatedFixedConstModel>) calculatedFixedConstService.findAll();
		} else {
			// constList = (ArrayList<CalculatedFixedConstModel>) calculatedFixedConstService.findByFlowRateLike(like);
			constList = (ArrayList<CalculatedFixedConstModel>) calculatedFixedConstService.findByFlowRateLike(like);
		}

		// if (tableView == tvListFlowRateCalib) {
		hashListFixedConst.clear();
		for (CalculatedFixedConstModel fixedConst : constList) {
			hashListFixedConst.put(fixedConst.getId(), fixedConst);
		}
		// Update tableview
		tvListCalculatedFixedConst.getItems().clear();
		tvListCalculatedFixedConst.setItems(FXCollections.observableArrayList(new ArrayList<CalculatedFixedConstModel>(hashListFixedConst.values())));
		// tvListCalculatedFixedConst.getSortOrder().add(columnListFlowRateCalib);
	}

	/**
	 * 
	 */
	private void updateListFlowRate(TableView<FlowRateModel> tableView, String like) {
		ArrayList<FlowRateModel> flowRateList;
		// Load from db
		if (like.isEmpty()) {
			flowRateList = (ArrayList<FlowRateModel>) flowRateService.findAll();
		} else {
			flowRateList = (ArrayList<FlowRateModel>) flowRateService.findByFlowRateLike(like);
		}

		if (tableView == tvListFlowRateCalib) {
			hashListFlowRateCalib.clear();
			for (FlowRateModel flowRate : flowRateList) {
				hashListFlowRateCalib.put(flowRate.getId(), flowRate);
			}
			// Update tableview
			tvListFlowRateCalib.getItems().clear();
			tvListFlowRateCalib.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashListFlowRateCalib.values())));
			tvListFlowRateCalib.getSortOrder().add(columnListFlowRateCalib);

		} else if (tableView == tvListFlowRateVerif) {
			hashListFlowRateVerif.clear();
			for (FlowRateModel flowRate : flowRateList) {
				hashListFlowRateVerif.put(flowRate.getId(), flowRate);
			}
			// Update tableview
			tvListFlowRateVerif.getItems().clear();
			tvListFlowRateVerif.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashListFlowRateVerif.values())));
			tvListFlowRateVerif.getSortOrder().add(columnListFlowRateVerif);
		}
	}

	/**
	 * 
	 */
	private void loadLists() {

		hashListFlowRateCalib = new HashMap<>();
		hashSelectedFlowRateCalib = new HashMap<>();
		hashListFlowRateVerif = new HashMap<>();
		hashSelectedFlowRateVerif = new HashMap<>();
		hashListFixedConst = new HashMap<>();
		hashSelectedFixedConst = new HashMap<>();
		ArrayList<FlowRateModel> flowRateList = (ArrayList<FlowRateModel>) flowRateService.findAll();
		ArrayList<CalculatedFixedConstModel> constList = (ArrayList<CalculatedFixedConstModel>) calculatedFixedConstService.findAll();

		for (FlowRateModel flowRate : flowRateList) {
			hashListFlowRateCalib.put(flowRate.getId(), flowRate);
			hashListFlowRateVerif.put(flowRate.getId(), flowRate);
		}
		for (CalculatedFixedConstModel fixedConst : constList) {
			hashListFixedConst.put(fixedConst.getId(), fixedConst);
		}
	}

	/**
	 * 
	 */
	@FXML
	private void onSelectConst(ActionEvent event) {

		// Select flow rate verif buttons
		if (event.getSource() == btSelectSingleConst) {
			CalculatedFixedConstModel fixedConst = tvListCalculatedFixedConst.getSelectionModel().getSelectedItem();
			if (fixedConst != null) {
				// Add selected item to hashSelectedFlowRateCalib
				hashSelectedFixedConst.put(fixedConst.getId(), fixedConst);
				// Remove selected item from hashListFlowRateCalib
				hashListFixedConst.remove(fixedConst.getId());

				// Update tableview
				tvListCalculatedFixedConst.getItems().clear();
				tvListCalculatedFixedConst.setItems(FXCollections.observableArrayList(new ArrayList<CalculatedFixedConstModel>(hashListFixedConst.values())));
				tvListCalculatedFixedConst.getSortOrder().add(columnListFlowRateConst);
				// Update tableview
				tvSelectedCalculatedFixedConst.getItems().clear();
				tvSelectedCalculatedFixedConst.setItems(FXCollections.observableArrayList(new ArrayList<CalculatedFixedConstModel>(hashSelectedFixedConst.values())));
				tvSelectedCalculatedFixedConst.getSortOrder().add(columnSelectedFlowRateConst);
			}
		} else if (event.getSource() == btSelectAllConst) {

			// Add all selected item to hashSelectedFlowRateCalib
			for (Entry<Long, CalculatedFixedConstModel> hash : hashListFixedConst.entrySet()) {
				hashSelectedFixedConst.put(hash.getKey(), hash.getValue());
			}
			// Remove all selected item from hashListFlowRateCalib
			hashListFixedConst.clear();
			// Update tableview
			tvListCalculatedFixedConst.getItems().clear();
			// Update tableview
			tvSelectedCalculatedFixedConst.getItems().clear();
			tvSelectedCalculatedFixedConst.setItems(FXCollections.observableArrayList(new ArrayList<CalculatedFixedConstModel>(hashSelectedFixedConst.values())));
			tvSelectedCalculatedFixedConst.getSortOrder().add(columnSelectedFlowRateConst);

		} else if (event.getSource() == btUnselectSingleConst) {
			CalculatedFixedConstModel fixedConst = tvSelectedCalculatedFixedConst.getSelectionModel().getSelectedItem();
			if (fixedConst != null) {
				// Add selected item to hashSelectedFlowRateCalib
				hashListFixedConst.put(fixedConst.getId(), fixedConst);
				// Remove selected item from hashListFlowRateCalib
				hashSelectedFixedConst.remove(fixedConst.getId());

				// Update tableview
				tvListCalculatedFixedConst.getItems().clear();
				tvListCalculatedFixedConst.setItems(FXCollections.observableArrayList(new ArrayList<CalculatedFixedConstModel>(hashListFixedConst.values())));
				tvListCalculatedFixedConst.getSortOrder().add(columnListFlowRateConst);
				// Update tableview
				tvSelectedCalculatedFixedConst.getItems().clear();
				tvSelectedCalculatedFixedConst.setItems(FXCollections.observableArrayList(new ArrayList<CalculatedFixedConstModel>(hashSelectedFixedConst.values())));
				tvSelectedCalculatedFixedConst.getSortOrder().add(columnSelectedFlowRateConst);
			}
		} else if (event.getSource() == btUnselectAllConst) {

			// Add all selected item to hashSelectedFlowRateCalib
			for (Entry<Long, CalculatedFixedConstModel> hash : hashSelectedFixedConst.entrySet()) {
				hashListFixedConst.put(hash.getKey(), hash.getValue());
			}
			// Remove all selected item from hashSelectedFlowRateCalib
			hashSelectedFixedConst.clear();
			// Update tableview
			tvListCalculatedFixedConst.getItems().clear();
			tvListCalculatedFixedConst.setItems(FXCollections.observableArrayList(new ArrayList<CalculatedFixedConstModel>(hashListFixedConst.values())));
			tvListCalculatedFixedConst.getSortOrder().add(columnListFlowRateConst);
			// Update tableview
			tvSelectedCalculatedFixedConst.getItems().clear();
		}
	}

	/**
	 * 
	 */
	@FXML
	private void onSelectFlowRateVerif(ActionEvent event) {

		// Select flow rate verif buttons
		if (event.getSource() == btSelectSingleVerif) {
			FlowRateModel flowRate = tvListFlowRateVerif.getSelectionModel().getSelectedItem();
			if (flowRate != null) {
				// Add selected item to hashSelectedFlowRateCalib
				hashSelectedFlowRateVerif.put(flowRate.getId(), flowRate);
				// Remove selected item from hashListFlowRateCalib
				hashListFlowRateVerif.remove(flowRate.getId());

				// Update tableview
				tvListFlowRateVerif.getItems().clear();
				tvListFlowRateVerif.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashListFlowRateVerif.values())));
				tvListFlowRateVerif.getSortOrder().add(columnListFlowRateVerif);
				// Update tableview
				tvSelectedFlowRateVerif.getItems().clear();
				tvSelectedFlowRateVerif.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashSelectedFlowRateVerif.values())));
				tvSelectedFlowRateVerif.getSortOrder().add(columnSelectedFlowRateVerif);
			}
		} else if (event.getSource() == btSelectAllVerif) {

			// Add all selected item to hashSelectedFlowRateCalib
			for (Entry<Long, FlowRateModel> hash : hashListFlowRateVerif.entrySet()) {
				hashSelectedFlowRateVerif.put(hash.getKey(), hash.getValue());
			}
			// Remove all selected item from hashListFlowRateCalib
			hashListFlowRateVerif.clear();
			// Update tableview
			tvListFlowRateVerif.getItems().clear();
			// Update tableview
			tvSelectedFlowRateVerif.getItems().clear();
			tvSelectedFlowRateVerif.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashSelectedFlowRateVerif.values())));
			tvSelectedFlowRateVerif.getSortOrder().add(columnSelectedFlowRateVerif);

		} else if (event.getSource() == btUnselectSingleVerif) {
			FlowRateModel flowRate = tvSelectedFlowRateVerif.getSelectionModel().getSelectedItem();
			if (flowRate != null) {
				// Add selected item to hashSelectedFlowRateCalib
				hashListFlowRateVerif.put(flowRate.getId(), flowRate);
				// Remove selected item from hashListFlowRateCalib
				hashSelectedFlowRateVerif.remove(flowRate.getId());

				// Update tableview
				tvListFlowRateVerif.getItems().clear();
				tvListFlowRateVerif.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashListFlowRateVerif.values())));
				tvListFlowRateVerif.getSortOrder().add(columnListFlowRateVerif);
				// Update tableview
				tvSelectedFlowRateVerif.getItems().clear();
				tvSelectedFlowRateVerif.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashSelectedFlowRateVerif.values())));
				tvSelectedFlowRateVerif.getSortOrder().add(columnSelectedFlowRateVerif);
			}
		} else if (event.getSource() == btUnselectAllVerif) {

			// Add all selected item to hashSelectedFlowRateCalib
			for (Entry<Long, FlowRateModel> hash : hashSelectedFlowRateVerif.entrySet()) {
				hashListFlowRateVerif.put(hash.getKey(), hash.getValue());
			}
			// Remove all selected item from hashSelectedFlowRateCalib
			hashSelectedFlowRateVerif.clear();
			// Update tableview
			tvListFlowRateVerif.getItems().clear();
			tvListFlowRateVerif.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashListFlowRateVerif.values())));
			tvListFlowRateVerif.getSortOrder().add(columnListFlowRateVerif);
			// Update tableview
			tvSelectedFlowRateVerif.getItems().clear();
		}
	}

	/**
	 * 
	 */
	@FXML
	private void onSelectFlowRateCalib(ActionEvent event) {

		// Select flow rate calib buttons
		if (event.getSource() == btSelectSingleCalib) {
			FlowRateModel flowRate = tvListFlowRateCalib.getSelectionModel().getSelectedItem();
			if (flowRate != null) {
				// Add selected item to hashSelectedFlowRateCalib
				hashSelectedFlowRateCalib.put(flowRate.getId(), flowRate);
				// Remove selected item from hashListFlowRateCalib
				hashListFlowRateCalib.remove(flowRate.getId());

				// Update tableview
				tvListFlowRateCalib.getItems().clear();
				tvListFlowRateCalib.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashListFlowRateCalib.values())));
				tvListFlowRateCalib.getSortOrder().add(columnListFlowRateCalib);
				// Update tableview
				tvSelectedFlowRateCalib.getItems().clear();
				tvSelectedFlowRateCalib.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashSelectedFlowRateCalib.values())));
				tvSelectedFlowRateCalib.getSortOrder().add(columnSelectedFlowRateCalib);
			}
		} else if (event.getSource() == btSelectAllCalib) {

			// Add all selected item to hashSelectedFlowRateCalib
			for (Entry<Long, FlowRateModel> hash : hashListFlowRateCalib.entrySet()) {
				hashSelectedFlowRateCalib.put(hash.getKey(), hash.getValue());
			}
			// Remove all selected item from hashListFlowRateCalib
			hashListFlowRateCalib.clear();
			// Update tableview
			tvListFlowRateCalib.getItems().clear();
			// Update tableview
			tvSelectedFlowRateCalib.getItems().clear();
			tvSelectedFlowRateCalib.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashSelectedFlowRateCalib.values())));
			tvSelectedFlowRateCalib.getSortOrder().add(columnSelectedFlowRateCalib);

		} else if (event.getSource() == btUnselectSingleCalib) {
			FlowRateModel flowRate = tvSelectedFlowRateCalib.getSelectionModel().getSelectedItem();
			if (flowRate != null) {
				// Add selected item to hashSelectedFlowRateCalib
				hashListFlowRateCalib.put(flowRate.getId(), flowRate);
				// Remove selected item from hashListFlowRateCalib
				hashSelectedFlowRateCalib.remove(flowRate.getId());

				// Update tableview
				tvListFlowRateCalib.getItems().clear();
				tvListFlowRateCalib.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashListFlowRateCalib.values())));
				tvListFlowRateCalib.getSortOrder().add(columnListFlowRateCalib);
				// Update tableview
				tvSelectedFlowRateCalib.getItems().clear();
				tvSelectedFlowRateCalib.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashSelectedFlowRateCalib.values())));
				tvSelectedFlowRateCalib.getSortOrder().add(columnSelectedFlowRateCalib);
			}
		} else if (event.getSource() == btUnselectAllCalib) {

			// Add all selected item to hashSelectedFlowRateCalib
			for (Entry<Long, FlowRateModel> hash : hashSelectedFlowRateCalib.entrySet()) {
				hashListFlowRateCalib.put(hash.getKey(), hash.getValue());
			}
			// Remove all selected item from hashSelectedFlowRateCalib
			hashSelectedFlowRateCalib.clear();
			// Update tableview
			tvListFlowRateCalib.getItems().clear();
			tvListFlowRateCalib.setItems(FXCollections.observableArrayList(new ArrayList<FlowRateModel>(hashListFlowRateCalib.values())));
			tvListFlowRateCalib.getSortOrder().add(columnListFlowRateCalib);
			// Update tableview
			tvSelectedFlowRateCalib.getItems().clear();
		}
	}

	/**
	 * 
	 */
	@FXML
	private void onProcessStepsEnable(ActionEvent event) {
		// System.out.println("onFlowRateEnable");
		if (cbCalibration.isSelected()) {
			tbFlowRateCalib.setDisable(false);
			ValidateFields.clearValidateError(tvSelectedFlowRateCalib);
		} else {
			tbFlowRateCalib.setDisable(true);
		}

		if (cbVerification.isSelected()) {
			tbFlowRateVerif.setDisable(false);
			ValidateFields.clearValidateError(tvSelectedFlowRateVerif);
		} else {
			tbFlowRateVerif.setDisable(true);
		}
	}

	/**
	 * 
	 */
	@FXML
	private void onActionSave() {
		try {
			ProcessConfigModel processConfig = validateAllFields();
			processConfigService.persist(processConfig);

			processConfigCallback.accept(processConfig);
			Stage stage = (Stage) root.getScene().getWindow();
			stage.close();
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

		flowRateService = new FlowRateService();
		calculatedFixedConstService = new CalculatedFixedConstService();

		// tbFlowRateCalib.setDisable(true);
		// tbFlowRateVerif.setDisable(true);

		//
		// MaskTextField.numericField(tfPurgeFlowRate, 6);
		// MaskTextField.numericField(tfPurgeTime, 6);
		MaskTextField.numericField(tfZeroFlowTime, 6);
		// MaskTextField.numericField(tfFlowTime, 6);
		MaskTextField.numericField(tfFlowStabilizationTime, 6);
		MaskTextField.numericField(tfFlowStabilyCheckTime, 6);
		MaskTextField.numericField(tfFlowRateTimeout, 6);
		MaskTextField.numericField(tfLinePressurePumpLoad, 6);
		MaskTextField.numericField(tfTimeCheckPressure, 6);
		MaskTextField.numericField(tfInitCalibTemp, 6);
		MaskTextField.numericField(tfMaxTrimAgcTries, 6);
		MaskTextField.numericField(tfPercentageToVerif, 6);
		MaskTextField.numericField(tfFlowBuffSize, 6);
		MaskTextField.numericField(tfWindowSize, 6);

		MaskTextField.numericField(tfSearchFlowRateCalib, 5);
		MaskTextField.numericField(tfSearchFlowRateVerif, 5);
		// MaskTextField.decimalField(tfSearchFlowRateCalib, 5, 1);
		// MaskTextField.decimalField(tfSearchFlowRateVerif, 5, 1);

		// -------------------------------------------------------------------------------------
		// Flow rate calib
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
		//
		columnSelectedFlowRateCalib.setStyle("-fx-alignment: CENTER;");
		// columnSelectedDescriptionCalib.setStyle("-fx-alignment: CENTER;");
		columnSelectedRefMeterCalib.setStyle("-fx-alignment: CENTER;");
		columnSelectedRamalCalid.setStyle("-fx-alignment: CENTER;");
		columnSelectedPumpCalib.setStyle("-fx-alignment: CENTER;");
		columnSelectedFlowRateCalib.setCellValueFactory(new PropertyValueFactory<>("flowRate"));
		columnSelectedDescriptionCalib.setCellValueFactory(new PropertyValueFactory<>("description"));
		columnSelectedRefMeterCalib.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				String refMeter = "";
				if (p.getValue().getRefMeter() != null) {
					refMeter = p.getValue().getRefMeter().getTag();
				}
				return new ReadOnlyObjectWrapper<>(refMeter);
			}
		});
		columnSelectedRamalCalid.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				String ramal = "";
				if (p.getValue().getRamal() != null) {
					ramal = p.getValue().getRamal().getTag();
				}
				return new ReadOnlyObjectWrapper<>(ramal);
			}
		});
		columnSelectedPumpCalib.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				String pump = "";
				if (p.getValue().getPump() != null) {
					pump = p.getValue().getPump().getTag();
				}
				return new ReadOnlyObjectWrapper<>(pump);
			}
		});

		// -------------------------------------------------------------------------------------
		// Flow rate verif
		columnListFlowRateVerif.setStyle("-fx-alignment: CENTER;");
		// columnListDescriptionVerif.setStyle("-fx-alignment: CENTER;");
		columnListRefMeterVerif.setStyle("-fx-alignment: CENTER;");
		columnListRamalVerif.setStyle("-fx-alignment: CENTER;");
		columnListPumpVerif.setStyle("-fx-alignment: CENTER;");
		columnListFlowRateVerif.setCellValueFactory(new PropertyValueFactory<>("flowRate"));
		columnListDescriptionVerif.setCellValueFactory(new PropertyValueFactory<>("description"));
		columnListRefMeterVerif.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
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
		//
		columnSelectedFlowRateVerif.setStyle("-fx-alignment: CENTER;");
		// columnSelectedDescriptionVerif.setStyle("-fx-alignment: CENTER;");
		columnSelectedRefMeterVerif.setStyle("-fx-alignment: CENTER;");
		columnSelectedRamalVerif.setStyle("-fx-alignment: CENTER;");
		columnSelectedPumpVerif.setStyle("-fx-alignment: CENTER;");
		columnSelectedFlowRateVerif.setCellValueFactory(new PropertyValueFactory<>("flowRate"));
		columnSelectedDescriptionVerif.setCellValueFactory(new PropertyValueFactory<>("description"));
		columnSelectedRefMeterVerif.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				String refMeter = "";
				if (p.getValue().getRefMeter() != null) {
					refMeter = p.getValue().getRefMeter().getTag();
				}
				return new ReadOnlyObjectWrapper<>(refMeter);
			}
		});
		columnSelectedRamalVerif.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				String ramal = "";
				if (p.getValue().getRamal() != null) {
					ramal = p.getValue().getRamal().getTag();
				}
				return new ReadOnlyObjectWrapper<>(ramal);
			}
		});
		columnSelectedPumpVerif.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FlowRateModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<FlowRateModel, String> p) {
				String pump = "";
				if (p.getValue().getPump() != null) {
					pump = p.getValue().getPump().getTag();
				}
				return new ReadOnlyObjectWrapper<>(pump);
			}
		});

		// -------------------------------------------------------------------------------------
		// Calculated fixed const
		// columnListDescriptionConst.setStyle("-fx-alignment: CENTER;");
		columnListFlowRateConst.setStyle("-fx-alignment: CENTER;");
		columnListKConst.setStyle("-fx-alignment: CENTER;");
		columnListReConst.setStyle("-fx-alignment: CENTER;");
		columnListTempConst.setStyle("-fx-alignment: CENTER;");
		columnListDescriptionConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>(p.getValue().getDescricao());
			}
		});
		columnListFlowRateConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>("" + p.getValue().getFlowrate());
			}
		});
		columnListKConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>("" + p.getValue().getK());
			}
		});
		columnListReConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>("" + p.getValue().getRe());
			}
		});
		columnListTempConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>("" + p.getValue().getTemp());
			}
		});

		// columnSelectedDescriptionConst.setStyle("-fx-alignment: CENTER;");
		columnSelectedFlowRateConst.setStyle("-fx-alignment: CENTER;");
		columnSelectedKConst.setStyle("-fx-alignment: CENTER;");
		columnSelectedReConst.setStyle("-fx-alignment: CENTER;");
		columnSelectedTempConst.setStyle("-fx-alignment: CENTER;");
		columnSelectedDescriptionConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>(p.getValue().getDescricao());
			}
		});
		columnSelectedFlowRateConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>("" + p.getValue().getFlowrate());
			}
		});
		columnSelectedKConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>("" + p.getValue().getK());
			}
		});
		columnSelectedReConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>("" + p.getValue().getRe());
			}
		});
		columnSelectedTempConst.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CalculatedFixedConstModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<CalculatedFixedConstModel, String> p) {
				return new ReadOnlyObjectWrapper<>("" + p.getValue().getTemp());
			}
		});

		// -------------------------------------------------------------------------------------
		// Load list
		tvListFlowRateCalib.setItems(FXCollections.observableArrayList(flowRateService.findAll()));
		tvListFlowRateVerif.setItems(FXCollections.observableArrayList(flowRateService.findAll()));
		tvListCalculatedFixedConst.setItems(FXCollections.observableArrayList(calculatedFixedConstService.findAll()));

		// load list
		loadLists();

		// Search flow rate calib
		tfSearchFlowRateCalib.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				if (newValue.intValue() != oldValue.intValue()) {
					String like = tfSearchFlowRateCalib.getText();
					updateListFlowRate(tvListFlowRateCalib, like);
				}
			}
		});

		// Search flow rate verif
		tfSearchFlowRateVerif.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				if (newValue.intValue() != oldValue.intValue()) {
					String like = tfSearchFlowRateVerif.getText();
					updateListFlowRate(tvListFlowRateVerif, like);
				}
			}
		});

		// Search calculated fixed const
		tfSearchConst.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				if (newValue.intValue() != oldValue.intValue()) {
					String like = tfSearchConst.getText();
					updateListCalculatedFixedConst(like);
				}
			}
		});

		//
		initAllValues();
	}
}