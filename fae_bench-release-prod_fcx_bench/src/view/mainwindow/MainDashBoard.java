//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MainDashBoard.java
*    @author marcos
*    @date 1 de set de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.mainwindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import bciapi.impl.BenchControlImpl;
import bciapi.socket.BCISocketComm;
import controller.BenchDataController;
import controller.MeterController;
import controller.ProcessController;
import enumerations.MeterConnectionStatus;
import enumerations.SceneTypeEnum;
import enumerations.UserDialogEnum;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import si.dbcomm.dao.DataBasePersistence;
import si.dbcomm.enumerations.CalibrationTypeEnum;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.ConversorModel;
import si.dbcomm.model.LogChangeProcessBatchModel;
import si.dbcomm.service.BatchService;
import si.dbcomm.service.ConversorService;
import si.dbcomm.service.LogChangeProcessBatchService;
import si.dbcomm.util.ConversorNumbers;
import util.MainMachineStateEnum;
import util.PreferencesHandler;
import util.StandardProcessStatesEnum;
import util.ViewStatesUtil;
import view.batch.LoadBatchControllerView;
import view.batch.NewBatchControllerView;
import view.batelada.ShowBateladaResult;
import view.bean.BenchBean;
import view.configDataBase.ConfigDataBase;
import view.connectionmeters.ConnectSingleMeter;
import view.controller.SceneFactory;
import view.dialog.UserDialog;
import view.matfiledir.MatFileDirChooser;
import view.util.GraphControl;
import view.util.LedViewUtil;
import view.util.ViewData;
import view.util.ViewDataUtil;
import view.util.WorkIndicatorDialog;
import javafx.scene.text.Font;

/**
 * @author marcos
 *
 */
public class MainDashBoard {

	ArrayList<ConversorNumbers> conversores = new ArrayList<>();

	ObservableList<XYChart.Data<Integer, Double>> refFlowrates = FXCollections.observableArrayList();
	ObservableList<XYChart.Data<Integer, Double>> refFlowratesUpper = FXCollections.observableArrayList();
	ObservableList<XYChart.Data<Integer, Double>> refFlowratesLower = FXCollections.observableArrayList();
	ObservableList<XYChart.Data<Integer, Double>> refTempMontante = FXCollections.observableArrayList();
	ObservableList<XYChart.Data<Integer, Double>> refTempJusante = FXCollections.observableArrayList();

	// BenchDataModel data;
	ViewData data;
	@SuppressWarnings("unused")
	private int lastObservedSize = 0;

	int eixoX = 0;

	double auxFlowRate = 0; // auxiliar flow rate to change line chart

	@FXML
	private AnchorPane root;

	@FXML
	private MenuItem mnProcessConfig;

	@FXML
	private NumberAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	Series<Integer, Double> refFlowSeries;

	Series<Integer, Double> refFlowSeriesUpperLimit;

	Series<Integer, Double> refFlowSeriesLowerLimit;

	@FXML
	LineChart<Integer, Double> refFlowRateChart;

	@FXML
	LineChart<Integer, Double> metersVelocitieChart;

	@FXML
	LineChart<Integer, Double> refPressureChart;

	@FXML
	LineChart<Integer, Double> stdDevRefFlowRateChart;

	@FXML
	private NumberAxis xAxisStdDev;

	@FXML
	private NumberAxis yAxisStdDev;

	@FXML
	private NumberAxis xAxisVelMeter;

	@FXML
	private NumberAxis yAxisVelMeter;

	@FXML
	private NumberAxis xAxisTemp;

	@FXML
	private NumberAxis yAxisTemp;

	@FXML
	private NumberAxis xAxisPress;

	@FXML
	private NumberAxis yAxisPress;

	Series<Integer, Double> refTempSeries;
	Series<Integer, Double> refTempJusSeries;

	@FXML
	LineChart<Integer, Double> refTemperatureChart;

	@FXML
	LineChart<Integer, Double> refPressChart;

	@FXML
	HBox gaugeHbox;

	@FXML
	BorderPane mainBorderPane;

	// ProgressBar progressBar;
	// TextArea txtArea;
	// Console console;
	// PrintStream ps;

	@FXML
	ImageView faeImageLogo;

	@FXML
	ImageView imageLedBciConect;

	@FXML
	ImageView imageLedSampling;

	@FXML
	ImageView imageLedLeak;

	@FXML
	ImageView imageLedAdjustFlow;

	@FXML
	ImageView imageLedEmerg;

	@FXML
	ImageView imageLedAirPress;

	@FXML
	ImageView imageLedInferiorReserv;

	@FXML
	ImageView imageLedBp1;

	@FXML
	ImageView imageLedBp2;

	@FXML
	ImageView imageLedBrep;
	
	@FXML
	RadioButton fullProdRadio;
	
	@FXML
	RadioButton estimatedPointsRadio;
	
//	@FXML
//	RadioButton fixedPointsRadio;
	
	@FXML
	ToggleGroup calibrationGroup;

	@FXML
	ImageView imagePos1;
	@FXML
	ImageView imagePos2;
	@FXML
	ImageView imagePos3;
	@FXML
	ImageView imagePos4;
	@FXML
	ImageView imagePos5;
	@FXML
	ImageView imagePos6;
	@FXML
	ImageView imagePos7;
	@FXML
	ImageView imagePos8;
	@FXML
	ImageView imagePos9;
	@FXML
	ImageView imagePos10;
	@FXML
	ImageView imagePos11;
	@FXML
	ImageView imagePos12;
	@FXML
	ImageView imagePos13;
	@FXML
	ImageView imagePos14;
	@FXML
	ImageView imagePos15;
	@FXML
	ImageView imagePos16;
	@FXML
	ImageView imagePos17;
	@FXML
	ImageView imagePos18;
	@FXML
	ImageView imagePos19;
	@FXML
	ImageView imagePos20;

	@FXML
	Label pos1ReportLabel;
	@FXML
	Label pos2ReportLabel;
	@FXML
	Label pos3ReportLabel;
	@FXML
	Label pos4ReportLabel;
	@FXML
	Label pos5ReportLabel;
	@FXML
	Label pos6ReportLabel;
	@FXML
	Label pos7ReportLabel;
	@FXML
	Label pos8ReportLabel;
	@FXML
	Label pos9ReportLabel;
	@FXML
	Label pos10ReportLabel;
	@FXML
	Label pos11ReportLabel;
	@FXML
	Label pos12ReportLabel;
	@FXML
	Label pos13ReportLabel;
	@FXML
	Label pos14ReportLabel;
	@FXML
	Label pos15ReportLabel;
	@FXML
	Label pos16ReportLabel;
	@FXML
	Label pos17ReportLabel;
	@FXML
	Label pos18ReportLabel;
	@FXML
	Label pos19ReportLabel;
	@FXML
	Label pos20ReportLabel;

	// @FXML
	// Label flowRateStateLabel;

	@FXML
	Label contadorProducaoLabel;

	@FXML
	VBox vboxPos1;

	@FXML
	VBox vboxLeftInfo;

	// @FXML
	// Pane ledPanePos1;

	@FXML
	Pane rightPane;

	@FXML
	Pane leftPane;

	@FXML
	Pane centerPane;

	@FXML
	Label processStateLabel;

	@FXML
	Label setPointFlowRateLabel;

	@FXML
	Label setPointUpperFlowRateLabel;

	@FXML
	Label setPointLowerFlowRateLabel;

	@FXML
	Label lblEditBatelada;

	@FXML
	Label bateladaLabel;

	@FXML
	Label numeroLoteLabel;

	@FXML
	Label conexaoBci;

	@FXML
	ToolBar toolBar;

	@FXML
	Button btnStart;

	@FXML
	Button btnStop;

	@FXML
	Button btnPurge;

	@FXML
	Button btnBatch;

	@FXML
	Button btnSkipRep;

	@FXML
	Button btnRefreshMedidores;

	@FXML
	Button btnDownloadConsts;

	@FXML
	Rectangle recPos1;
	@FXML
	Rectangle recPos2;
	@FXML
	Rectangle recPos3;
	@FXML
	Rectangle recPos4;
	@FXML
	Rectangle recPos5;
	@FXML
	Rectangle recPos6;
	@FXML
	Rectangle recPos7;
	@FXML
	Rectangle recPos8;
	@FXML
	Rectangle recPos9;
	@FXML
	Rectangle recPos10;
	@FXML
	Rectangle recPos11;
	@FXML
	Rectangle recPos12;
	@FXML
	Rectangle recPos13;
	@FXML
	Rectangle recPos14;
	@FXML
	Rectangle recPos15;
	@FXML
	Rectangle recPos16;
	@FXML
	Rectangle recPos17;
	@FXML
	Rectangle recPos18;
	@FXML
	Rectangle recPos19;
	@FXML
	Rectangle recPos20;

	@FXML
	CheckBox uncertaintyFlowRepeatCheckBox;

	@FXML
	CheckBox onlyVerificationCheckBox;

	//
	@FXML
	Label lblShowResult;

	//
	@FXML
	Button btnPos1;

	@FXML
	Button btnPos2;

	@FXML
	Button btnPos3;

	@FXML
	Button btnPos4;

	@FXML
	Button btnPos5;

	@FXML
	Button btnPos6;

	@FXML
	Button btnPos7;

	@FXML
	Button btnPos8;

	@FXML
	Button btnPos9;

	@FXML
	Button btnPos10;

	@FXML
	Button btnPos11;

	@FXML
	Button btnPos12;

	@FXML
	Button btnPos13;

	@FXML
	Button btnPos14;

	@FXML
	Button btnPos15;

	@FXML
	Button btnPos16;

	@FXML
	Button btnPos17;

	@FXML
	Button btnPos18;

	@FXML
	Button btnPos19;

	@FXML
	Button btnPos20;
	
	//status da conex�o do banco
	@FXML
	private Label lbStatus;

	//
	ViewStatesUtil mainState;

	private BenchBean benchBean = BenchBean.getInstance();
	
	private Gauge pressaoDiferencial;
	private Gauge pressaoMontante;
	private Gauge pressaoJusante;
	private Gauge temperaturaMontante;
	private Gauge temperaturaJusante;
	private Gauge temperaturaResevInferior;
	private Gauge temperaturaResevSup;
	private Gauge waterLevel;
	private Gauge vazaoDn02;
	private Gauge vazaoDn08;
	private Gauge vazaoDn32;

	private ArrayList<ImageView> ledPositions = new ArrayList<>();
	private ArrayList<Label> labelReports = new ArrayList<>();
	@SuppressWarnings("rawtypes")
	private WorkIndicatorDialog wd = null;
	boolean flag = false;

	// private BatchController batchController = new BatchController();
	//
//	private BateladaService bateladaService = new BateladaService();
	
	private BatchModel selectedBatch;

	//private BatchController batchController = new BatchController();

	// private ConversorService conversorService;
	private ConversorService conversorService = new ConversorService();
	
	private LogChangeProcessBatchService logChangeProcessBatchService = new LogChangeProcessBatchService();

	// private boolean flagConnect;
	// private boolean connectedMeters;

	// private boolean flagInit;

	private boolean flagPurge;

	private LedViewUtil bciConnectLed;
	private LedViewUtil fullProdLed;
	private LedViewUtil pointEstimatedLed;

	// private LedViewUtil pos1;

	private GraphControl graphRefFlowRateDn08;
	private GraphControl graphRefFlowRateDn32;
	private GraphControl graphRefFlowRateDn02;

	private GraphControl graphRefTempMont;
	private GraphControl graphRefTempJus;

	private GraphControl graphRefPressJus;
	private GraphControl graphRefPressMon;

	private GraphControl graphStd02DevRefFlowrate;
	private GraphControl graphStd08DevRefFlowrate;
	private GraphControl graphStd32DevRefFlowrate;

	//
	private String[] matFileDir = new String[2];

	/**
	 *
	 */
	@FXML
	private void onMenuExitClick(ActionEvent event) {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	/**
	 *
	 *
	 */
	@FXML
	private void onMenuBatchClick(ActionEvent event) {
		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.LOAD_BATCH);
			sceneFactory.setTitle("Cadastro de Lotes");
			LoadBatchControllerView childController = sceneFactory.getController();
			childController.setCallback(batch -> {
				BatchService batchService = new BatchService();
				selectedBatch = batchService.findByBatchId(batch.getBatchId());
				if (selectedBatch != null) {
					numeroLoteLabel.setText(selectedBatch.getBatchId());
					contadorProducaoLabel.setText("" + selectedBatch.getNumMeters());
					//
					btnRefreshMedidores.setDisable(false);
					lblEditBatelada.setDisable(false);
				}
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
	@FXML
	private void onMenuClientClick(ActionEvent event) {
		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.LOAD_CLIENT);
			sceneFactory.setTitle("Cadastro de Clientes");
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
	private void onMenuMeterTypeClick(ActionEvent event) {
		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.LOAD_METER_TYPE);
			sceneFactory.setTitle("Cadastro de tipos de medidores");
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
	private void onMenuFlowRateClick(ActionEvent event) {
		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.LOAD_FLOW_RATE);
			sceneFactory.setTitle("Cadastro de Vaz�es");
			sceneFactory.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 *
	 */
	@FXML
	private void onMenuProcessConfigClick(ActionEvent event) {

		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.LOAD_PROCESS_CONFIG);
			sceneFactory.setTitle("Configura��o de Processo");
			sceneFactory.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 *
	 */
	@FXML
	private void onMenuConnectionMeter(ActionEvent event) {

		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.CONFIG_CONNECTION_METERS);
			sceneFactory.setTitle("Conex�o com medidores");
			sceneFactory.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 *
	 */
	@FXML
	private void onMenuMatFile(ActionEvent event) {

		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.MATFILE_DIR);
			// sceneFactory.setTitle("Conex�o com medidores");
			MatFileDirChooser controller = sceneFactory.getController();
			controller.setFilePath(matFileDir[0], matFileDir[1]);
			controller.setCallback(value -> {
				matFileDir = value;
				PreferencesHandler.saveMatFileLocalDir(matFileDir[0]);
				PreferencesHandler.saveMatFileRemoteDir(matFileDir[1]);
			});
			sceneFactory.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@FXML
	private void onMenuConfigDataBase(ActionEvent event) {
		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.DATABASE_SETTINGS);
			ConfigDataBase childController = sceneFactory.getController();
			String urlDataBase = PreferencesHandler.readDataBaseUrl();
			String userDataBase = PreferencesHandler.readDataBaseUserName();
			childController.setTfUrlDataBase(urlDataBase);
			childController.setTfUserDataBase(userDataBase);
			childController.setLbStatusCallback(value -> {
				lbStatus.setText(value);
				lbStatus.setTextFill(Color.GREEN);
			});
			sceneFactory.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	@FXML
	private void onMenuAboutClick(ActionEvent event) {

		// loadProcessConfig(selectedBatch);

		// try {
		// SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.LOAD_PROCESS_CONFIG);
		// // ProcessConfigControllerView childController = sceneFactory.getController();
		// sceneFactory.setTitle("Config. de processo");
		// sceneFactory.show();
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		//
		// // try {
		// // SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.LOAD_FLOW_RATE);
		// // LoadFlowRateControllerView childController = sceneFactory.getController();
		// // childController.setFlowRateSelectCallback(callback);
		// // childController.setViewAction(ViewActionEnum.TO_VIEW);
		// // sceneFactory.setTitle("Config. de processo");
		// // sceneFactory.show();
		// // } catch (IOException e1) {
		// // e1.printStackTrace();
		// // }
	}

	/**
	 *
	 */
	@FXML
	private void onConnectSingleMeter(ActionEvent event) {
		ConversorNumbers conversorNum = ConversorNumbers.CONVERSOR1;
		if (event.getSource() == btnPos1) {
			conversorNum = ConversorNumbers.CONVERSOR1;
		} else if (event.getSource() == btnPos2) {
			conversorNum = ConversorNumbers.CONVERSOR2;
		} else if (event.getSource() == btnPos3) {
			conversorNum = ConversorNumbers.CONVERSOR3;
		} else if (event.getSource() == btnPos4) {
			conversorNum = ConversorNumbers.CONVERSOR4;
		} else if (event.getSource() == btnPos5) {
			conversorNum = ConversorNumbers.CONVERSOR5;
		} else if (event.getSource() == btnPos6) {
			conversorNum = ConversorNumbers.CONVERSOR6;
		} else if (event.getSource() == btnPos7) {
			conversorNum = ConversorNumbers.CONVERSOR7;
		} else if (event.getSource() == btnPos8) {
			conversorNum = ConversorNumbers.CONVERSOR8;
		} else if (event.getSource() == btnPos9) {
			conversorNum = ConversorNumbers.CONVERSOR9;
		} else if (event.getSource() == btnPos10) {
			conversorNum = ConversorNumbers.CONVERSOR10;
		} else if (event.getSource() == btnPos11) {
			conversorNum = ConversorNumbers.CONVERSOR11;
		} else if (event.getSource() == btnPos12) {
			conversorNum = ConversorNumbers.CONVERSOR12;
		} else if (event.getSource() == btnPos13) {
			conversorNum = ConversorNumbers.CONVERSOR13;
		} else if (event.getSource() == btnPos14) {
			conversorNum = ConversorNumbers.CONVERSOR14;
		} else if (event.getSource() == btnPos15) {
			conversorNum = ConversorNumbers.CONVERSOR15;
		} else if (event.getSource() == btnPos16) {
			conversorNum = ConversorNumbers.CONVERSOR16;
		} else if (event.getSource() == btnPos17) {
			conversorNum = ConversorNumbers.CONVERSOR17;
		} else if (event.getSource() == btnPos18) {
			conversorNum = ConversorNumbers.CONVERSOR18;
		} else if (event.getSource() == btnPos19) {
			conversorNum = ConversorNumbers.CONVERSOR19;
		} else if (event.getSource() == btnPos20) {
			conversorNum = ConversorNumbers.CONVERSOR20;
		}

		//
		if (conversorService.findByName(conversorNum).isEnabled()) {
			try {
				SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.CONNECT_SINGLE_METER);
				sceneFactory.setTitle("Conex�o de medidores");
				ConnectSingleMeter controller = sceneFactory.getController();
				controller.setConversorNumber(conversorNum);
				sceneFactory.showModal();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			try {
				UserDialog userDialog = new UserDialog();
				userDialog.setDialogType(UserDialogEnum.NEGATIVE_TYPE);
				userDialog.setTitle("Conversor n�o habilitado!");
				userDialog.setContentText("Habilite o conversor conrrespondente para conectar este medidor.");
				userDialog.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	@FXML
	public void checkFlowUncertaintyRepetition(ActionEvent event) {
		if (uncertaintyFlowRepeatCheckBox.isSelected()) {
			benchBean.setRepeatFlowUncertainty(true);
		} else {
			benchBean.setRepeatFlowUncertainty(false);
		}
	}

	@FXML
	public void checkOnlyVerification(ActionEvent event) {
		if (onlyVerificationCheckBox.isSelected()) {
			benchBean.setOnlyVerification(true);
		} else {
			benchBean.setOnlyVerification(false);
		}
	}
	
	@FXML
	public void checkChangeFullProd(ActionEvent event){
		
	}

	@SuppressWarnings("rawtypes")
	@FXML
	public void initialize() {
		try {
			new LedViewUtil(imagePos1).turnLedOff();
			new LedViewUtil(imagePos2).turnLedOff();
			new LedViewUtil(imagePos3).turnLedOff();
			new LedViewUtil(imagePos4).turnLedOff();
			new LedViewUtil(imagePos5).turnLedOff();
			new LedViewUtil(imagePos6).turnLedOff();
			new LedViewUtil(imagePos7).turnLedOff();
			new LedViewUtil(imagePos8).turnLedOff();
			new LedViewUtil(imagePos9).turnLedOff();
			new LedViewUtil(imagePos10).turnLedOff();
			new LedViewUtil(imagePos11).turnLedOff();
			new LedViewUtil(imagePos12).turnLedOff();
			new LedViewUtil(imagePos13).turnLedOff();
			new LedViewUtil(imagePos14).turnLedOff();
			new LedViewUtil(imagePos15).turnLedOff();
			new LedViewUtil(imagePos16).turnLedOff();
			new LedViewUtil(imagePos17).turnLedOff();
			new LedViewUtil(imagePos18).turnLedOff();
			new LedViewUtil(imagePos19).turnLedOff();
			new LedViewUtil(imagePos20).turnLedOff();

			ledPositions.add(imagePos1);
			ledPositions.add(imagePos2);
			ledPositions.add(imagePos3);
			ledPositions.add(imagePos4);
			ledPositions.add(imagePos5);
			ledPositions.add(imagePos6);
			ledPositions.add(imagePos7);
			ledPositions.add(imagePos8);
			ledPositions.add(imagePos9);
			ledPositions.add(imagePos10);
			ledPositions.add(imagePos11);
			ledPositions.add(imagePos12);
			ledPositions.add(imagePos13);
			ledPositions.add(imagePos14);
			ledPositions.add(imagePos15);
			ledPositions.add(imagePos16);
			ledPositions.add(imagePos17);
			ledPositions.add(imagePos18);
			ledPositions.add(imagePos19);
			ledPositions.add(imagePos20);

			labelReports.add(pos1ReportLabel);
			labelReports.add(pos2ReportLabel);
			labelReports.add(pos3ReportLabel);
			labelReports.add(pos4ReportLabel);
			labelReports.add(pos5ReportLabel);
			labelReports.add(pos6ReportLabel);
			labelReports.add(pos7ReportLabel);
			labelReports.add(pos8ReportLabel);
			labelReports.add(pos9ReportLabel);
			labelReports.add(pos10ReportLabel);
			labelReports.add(pos11ReportLabel);
			labelReports.add(pos12ReportLabel);
			labelReports.add(pos13ReportLabel);
			labelReports.add(pos14ReportLabel);
			labelReports.add(pos15ReportLabel);
			labelReports.add(pos16ReportLabel);
			labelReports.add(pos17ReportLabel);
			labelReports.add(pos18ReportLabel);
			labelReports.add(pos19ReportLabel);
			labelReports.add(pos20ReportLabel);

			BenchBean.getInstance().setIsSampling(new LedViewUtil(imageLedSampling));

			BenchBean.getInstance().setIsAdjustingFlow(new LedViewUtil(imageLedAdjustFlow));

			BenchBean.getInstance().setIsLeaking(new LedViewUtil(imageLedLeak));

			BenchBean.getInstance().setEmergencyLed(new LedViewUtil(imageLedEmerg));

			BenchBean.getInstance().setPressLed(new LedViewUtil(imageLedAirPress));

			BenchBean.getInstance().setInferiorLevel(new LedViewUtil(imageLedInferiorReserv));

			BenchBean.getInstance().setBp1Led(new LedViewUtil(imageLedBp1));

			BenchBean.getInstance().setBp2Led(new LedViewUtil(imageLedBp2));

			BenchBean.getInstance().setBrepLed(new LedViewUtil(imageLedBrep));

			bciConnectLed = new LedViewUtil(imageLedBciConect);
			BenchBean.getInstance().setBciConnectLed(bciConnectLed);

			if (BenchControlImpl.getInstance().checkConnectionWithBci()) {
				BenchBean.getInstance().getBciConnectLed().turnLedGreen();
			} else {
				BenchBean.getInstance().getBciConnectLed().turnLedRed();
			}
			
			fullProdRadio.setSelected(true);		
			fullProdRadio.setDisable(true);
			estimatedPointsRadio.setDisable(true);
//			fixedPointsRadio.setDisable(true);
			
			fullProdRadio.setUserData(CalibrationTypeEnum.FULL_PROD.name());
			estimatedPointsRadio.setUserData(CalibrationTypeEnum.ESTIMATED_CONST.name());
//			fixedPointsRadio.setUserData(CalibrationTypeEnum.FIXED_CONST.name());

			StringProperty calibrationTypeProperty = new SimpleStringProperty();
			
			calibrationGroup.selectedToggleProperty().addListener((obserableValue, old_toggle, new_toggle) -> {
			    if (calibrationGroup.getSelectedToggle() != null) {		        
			        if(calibrationGroup.getSelectedToggle().getUserData().toString() == CalibrationTypeEnum.FULL_PROD.name()){
			        	calibrationTypeProperty.setValue(CalibrationTypeEnum.FULL_PROD.name());		   
			        }else if(calibrationGroup.getSelectedToggle().getUserData().toString() == CalibrationTypeEnum.ESTIMATED_CONST.name()){
			        	calibrationTypeProperty.setValue(CalibrationTypeEnum.ESTIMATED_CONST.name());	
			        }else if(calibrationGroup.getSelectedToggle().getUserData().toString() == CalibrationTypeEnum.FIXED_CONST.name()){
			        	calibrationTypeProperty.setValue(CalibrationTypeEnum.FIXED_CONST.name());	
			        }
			        benchBean.setCalibrationTypeProperty(calibrationTypeProperty);
			    }
			});  

			graphRefFlowRateDn02 = new GraphControl(refFlowRateChart, xAxis, yAxis);
			graphRefFlowRateDn08 = new GraphControl(refFlowRateChart, xAxis, yAxis);
			graphRefFlowRateDn32 = new GraphControl(refFlowRateChart, xAxis, yAxis);

			graphRefFlowRateDn02.getValueProperty().bind(benchBean.getRefMeterDN02().getFlowRateProperty());
			graphRefFlowRateDn08.getValueProperty().bind(benchBean.getRefMeterDN08().getFlowRateProperty());
			graphRefFlowRateDn32.getValueProperty().bind(benchBean.getRefMeterDN32().getFlowRateProperty());

			benchBean.getRefMeterDN02().setViewGraph(graphRefFlowRateDn02);
			benchBean.getRefMeterDN08().setViewGraph(graphRefFlowRateDn08);
			benchBean.getRefMeterDN32().setViewGraph(graphRefFlowRateDn32);

			graphRefTempJus = new GraphControl(refTemperatureChart, xAxisTemp, yAxisTemp);
			graphRefTempMont = new GraphControl(refTemperatureChart, xAxisTemp, yAxisTemp);

			graphRefTempJus.getValueProperty().bind(benchBean.getTempTTLIProperty());
			graphRefTempMont.getValueProperty().bind(benchBean.getTempTTLOProperty());

			graphRefPressJus = new GraphControl(refPressureChart, xAxisPress, yAxisPress);
			graphRefPressMon = new GraphControl(refPressureChart, xAxisPress, yAxisPress);

			graphRefPressJus.getValueProperty().bind(benchBean.getPressPTLOProperty());
			graphRefPressMon.getValueProperty().bind(benchBean.getPressPTLIProperty());

			// STD deviation grapStupe
			graphStd02DevRefFlowrate = new GraphControl(stdDevRefFlowRateChart, xAxisStdDev, yAxisStdDev);
			graphStd02DevRefFlowrate.getValueProperty().bind(benchBean.getRefMeterDN02().getStdDevRefFlowRateProperty());

			graphStd08DevRefFlowrate = new GraphControl(stdDevRefFlowRateChart, xAxisStdDev, yAxisStdDev);
			graphStd08DevRefFlowrate.getValueProperty().bind(benchBean.getRefMeterDN08().getStdDevRefFlowRateProperty());

			graphStd32DevRefFlowrate = new GraphControl(stdDevRefFlowRateChart, xAxisStdDev, yAxisStdDev);
			graphStd32DevRefFlowrate.getValueProperty().bind(benchBean.getRefMeterDN32().getStdDevRefFlowRateProperty());

			benchBean.getRefMeterDN02().setStdGraph(graphStd02DevRefFlowrate);
			benchBean.getRefMeterDN08().setStdGraph(graphStd08DevRefFlowrate);
			benchBean.getRefMeterDN32().setStdGraph(graphStd32DevRefFlowrate);
			// end std setup

			// Process labels setup
			bateladaLabel.textProperty().bind(Bindings.convert(benchBean.getBateladaCountProperty()));
			processStateLabel.textProperty().bind(Bindings.convert(benchBean.getRunningStateProperty()));
			setPointFlowRateLabel.textProperty().bind(Bindings.convert(benchBean.getRunningFlowRateProperty()));
			setPointUpperFlowRateLabel.textProperty().bind(Bindings.convert(benchBean.getRunningUpperFlowRateProperty()));
			setPointLowerFlowRateLabel.textProperty().bind(Bindings.convert(benchBean.getRunningLowerFlowRateProperty()));
			// End process lables setup

			// graphRefTempJus = new GraphControl(refTemperatureChart, xAxisTemp, yAxisTemp);
			// graphRefTempJus.getValueProperty().bind(benchBean.getTempTTLIProperty());

			// ConversorService conversorService = new ConversorService();
			try {
				// Set colors for position indicators based on converters
				recPos1.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR1).getColorCode()));
				recPos2.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR2).getColorCode()));
				recPos3.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR3).getColorCode()));
				recPos4.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR4).getColorCode()));
				recPos5.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR5).getColorCode()));
				recPos6.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR6).getColorCode()));
				recPos7.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR7).getColorCode()));
				recPos8.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR8).getColorCode()));
				recPos9.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR9).getColorCode()));
				recPos10.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR10).getColorCode()));
				recPos11.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR11).getColorCode()));
				recPos12.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR12).getColorCode()));
				recPos13.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR13).getColorCode()));
				recPos14.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR14).getColorCode()));
				recPos15.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR15).getColorCode()));
				recPos16.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR16).getColorCode()));
				recPos17.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR17).getColorCode()));
				recPos18.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR18).getColorCode()));
				recPos19.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR19).getColorCode()));
				recPos20.setFill(Color.web(conversorService.findByName(ConversorNumbers.CONVERSOR20).getColorCode()));
			} catch (Exception e) {
				System.err.println("Error setting converter colors: " + e.getMessage());
			}

			try {
				GaugeBuilder gaugeBuilder = GaugeBuilder.create().skinType(SkinType.SPACE_X);
				gaugeBuilder.lcdVisible(true);
				gaugeBuilder.subTitleColor(Color.AZURE);
				gaugeBuilder.foregroundBaseColor(Color.BLACK);
				gaugeBuilder.animationDuration(180);

				pressaoMontante = gaugeBuilder.decimals(3).maxValue(42).unit("Bar").build();
				pressaoMontante.setAnimated(true);
				pressaoMontante.valueProperty().bind(benchBean.getPressPTLIProperty());
				VBox pressaoMontBox = getTopicBox("Presso Montante", Color.rgb(153, 0, 0), pressaoMontante);

				pressaoJusante = gaugeBuilder.decimals(3).maxValue(42).unit("Bar").build();
				pressaoJusante.setAnimated(true);
				pressaoJusante.valueProperty().bind(benchBean.getPressPTLOProperty());
				VBox pressaoJusBox = getTopicBox("Presso Jusante", Color.rgb(153, 0, 0), pressaoJusante);

				pressaoDiferencial = gaugeBuilder.decimals(3).maxValue(42).unit("Bar").build();
				pressaoDiferencial.setAnimated(true);
				pressaoDiferencial.valueProperty().bind(benchBean.getPressPTDIFProperty());
				VBox difPressaoBox = getTopicBox("Presso Diferencial", Color.rgb(153, 0, 0), pressaoDiferencial);

				temperaturaMontante = gaugeBuilder.decimals(2).maxValue(60).unit("C").build();
				temperaturaMontante.setAnimated(true);
				temperaturaMontante.valueProperty().bind(benchBean.getTempTTLIProperty());
				VBox tempMontanteBox = getTopicBox("Temperatura Montante - ", Color.rgb(14, 64, 203), temperaturaMontante);

				temperaturaJusante = gaugeBuilder.decimals(2).maxValue(60).unit("C").build();
				temperaturaJusante.setAnimated(true);
				temperaturaJusante.valueProperty().bind(benchBean.getTempTTLOProperty());
				VBox tempJusanteBox = getTopicBox("Temperatura Jusante - ", Color.rgb(14, 169, 16), temperaturaJusante);

				temperaturaResevInferior = gaugeBuilder.decimals(2).maxValue(60).unit("C").build();
				temperaturaResevInferior.setAnimated(true);
				temperaturaResevInferior.valueProperty().bind(benchBean.getTempTTRIProperty());
				VBox tempReservBox = getTopicBox("Temperatura Reservatório Inferior - ", Color.rgb(14, 64, 203), temperaturaResevInferior);

				temperaturaResevSup = gaugeBuilder.decimals(2).maxValue(60).unit("C").build();
				temperaturaResevSup.setAnimated(true);
				temperaturaResevSup.valueProperty().bind(benchBean.getTempTTRSProperty());
				VBox tempReservSupBox = getTopicBox("Temperatura Reservatrio Superior - ", Color.rgb(14, 64, 203), temperaturaResevSup);

				vazaoDn02 = gaugeBuilder.decimals(5).maxValue(100).unit("L/h").build();
				vazaoDn02.setAnimated(true);
				vazaoDn02.valueProperty().bind(benchBean.getRefMeterDN02().getFlowRateProperty());
				VBox vazaoDn02Box = getTopicBox("Vazo DN02", Color.rgb(31, 229, 97), vazaoDn02);

				vazaoDn08 = gaugeBuilder.decimals(5).maxValue(1600).unit("L/h").build();
				vazaoDn08.setAnimated(true);
				vazaoDn08.valueProperty().bind(benchBean.getRefMeterDN08().getFlowRateProperty());
				VBox vazaoDn08Box = getTopicBox("Vazo DN08", Color.rgb(31, 229, 97), vazaoDn08);

				vazaoDn32 = gaugeBuilder.decimals(5).maxValue(16000).unit("L/h").build();
				vazaoDn32.setAnimated(true);
				vazaoDn32.valueProperty().bind(benchBean.getRefMeterDN32().getFlowRateProperty());
				VBox vazaoDn32Box = getTopicBox("Vazo DN32", Color.rgb(31, 229, 97), vazaoDn32);

				gaugeHbox.getChildren().add(vazaoDn32Box);
				gaugeHbox.getChildren().add(vazaoDn08Box);
				gaugeHbox.getChildren().add(vazaoDn02Box);
				gaugeHbox.getChildren().add(pressaoJusBox);
				gaugeHbox.getChildren().add(pressaoMontBox);
				gaugeHbox.getChildren().add(difPressaoBox);
				gaugeHbox.getChildren().add(tempMontanteBox);
				gaugeHbox.getChildren().add(tempJusanteBox);
				gaugeHbox.getChildren().add(tempReservBox);
				gaugeHbox.getChildren().add(tempReservSupBox);
				mainBorderPane.setLeft(leftPane);

			} catch (Exception e) {
				e.printStackTrace();
			}

			// --------------------------------------------------------------------------

			//
			String localPath = PreferencesHandler.readMatFileLocalDir();
			String remotePath = PreferencesHandler.readMatFileRemoteDir();
			matFileDir[0] = localPath;
			matFileDir[1] = remotePath;
			System.out.println("MatFileLocalDir: " + localPath);
			System.out.println("MatFileRemoteDir: " + remotePath);

			// --------------------------------------------------------------------------
			// State process listener
			benchBean.getRunningStateProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

					// String state = arg0.getValue();
					// System.out.println("StateProcess.listener: " + state);
					// StandardProcessStatesEnum stateEnum = StandardProcessStatesEnum.getEnum(state);

					switch (StandardProcessStatesEnum.getEnum(arg0.getValue())) {

						case WAIT:
							btnStart.setDisable(true);
							btnStop.setDisable(true);
							btnBatch.setDisable(false);
							btnRefreshMedidores.setDisable(false);
							btnDownloadConsts.setDisable(false);
							btnPurge.setDisable(false);
							lblEditBatelada.setDisable(false);
							// mnProcessConfig.setDisable(false);
							onlyVerificationCheckBox.setDisable(false);
							lblShowResult.setDisable(false);
							enableAllConnectionMeterButtons(true);

						//
						// showBateladaResultDialog();
						break;

						default:
						break;
					}
				}
			});

			// --------------------------------------------------------------------------
			// Buttons click
			btnStart.setDisable(true);
			btnStop.setDisable(true);
			btnRefreshMedidores.setDisable(true);
			lblEditBatelada.setDisable(true);
			// mnProcessConfig.setDisable(false);
			onlyVerificationCheckBox.setSelected(false);
			onlyVerificationCheckBox.setDisable(true);
			lblShowResult.setDisable(true);
			enableAllConnectionMeterButtons(false);

			btnStart.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (ViewStatesUtil.minMeter) {
						ViewStatesUtil.mainStates = MainMachineStateEnum.RUN;

						btnStart.setDisable(true);
						btnStop.setDisable(false);
						btnBatch.setDisable(true);
						btnRefreshMedidores.setDisable(true);
						btnDownloadConsts.setDisable(true);
						btnPurge.setDisable(true);
						lblEditBatelada.setDisable(true);
						// mnProcessConfig.setDisable(true);
						onlyVerificationCheckBox.setDisable(true);
						lblShowResult.setDisable(true);
						enableAllConnectionMeterButtons(false);
						fullProdRadio.setDisable(true);
						estimatedPointsRadio.setDisable(true);
	//					fixedPointsRadio.setDisable(true);
						
					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Informao");
						alert.setHeaderText("No foi identificada conexo com Medidor.");
						alert.setContentText("Reconecte os medidores e tente novamente");
						alert.showAndWait();
					}
				}
			});

			btnStop.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Parar calibrao");
					alert.setHeaderText("Deseja parar a calibrao?");
					// alert.setContentText("Selecione um lote para iniciar");
					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == ButtonType.OK) {
						ViewStatesUtil.mainStates = MainMachineStateEnum.STOP;
						btnStart.setDisable(false);
						btnStop.setDisable(true);
					}
				}
			});

			btnBatch.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					try {
						SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.LOAD_BATCH);
						sceneFactory.setTitle("Seleo de lote");
						LoadBatchControllerView childController = sceneFactory.getController();
						childController.setCallback(batch -> {
							BatchService batchService = new BatchService();
							// selectedBatch = batchService.findByBatchId(batch.getBatchId());
							selectedBatch = batch;
							if (selectedBatch != null) {
								numeroLoteLabel.setText(selectedBatch.getBatchId());
								contadorProducaoLabel.setText("" + selectedBatch.getNumMeters());
								//
								btnRefreshMedidores.setDisable(false);
								lblEditBatelada.setDisable(false);
								enableAllConnectionMeterButtons(true);
								BateladaModel lastBatelada = batchService.getBatchLastBatelada(selectedBatch);
								LogChangeProcessBatchModel logChangeProcessBatchModel = logChangeProcessBatchService.findByBatch(selectedBatch);
								if(logChangeProcessBatchModel != null && lastBatelada != null){
									CalibrationTypeEnum calibrationType = logChangeProcessBatchModel.getCalibrationTypeEnum();
									if(lastBatelada.getCalibrationType() == CalibrationTypeEnum.FULL_PROD && calibrationType.name().equals(CalibrationTypeEnum.FULL_PROD.name())){
										fullProdRadio.setDisable(true);
										estimatedPointsRadio.setDisable(true);
	//									fixedPointsRadio.setDisable(true);
									}else if(lastBatelada.getCalibrationType() == CalibrationTypeEnum.FULL_PROD && calibrationType == CalibrationTypeEnum.ESTIMATED_CONST){
										fullProdRadio.setSelected(true);
										Alert alert = new Alert(AlertType.WARNING);
										alert.setTitle("Informao");
										alert.setHeaderText("Lote est apto para mudar para Pontos estimados.");
										alert.setContentText("Marque a opo de Pontos estimados em Tipo de calibrao.");
										alert.showAndWait();
										fullProdRadio.setDisable(false);
										estimatedPointsRadio.setDisable(false);
	//									fixedPointsRadio.setDisable(false);
									}else if(lastBatelada.getCalibrationType() == CalibrationTypeEnum.ESTIMATED_CONST && calibrationType == CalibrationTypeEnum.FULL_PROD){
										estimatedPointsRadio.setSelected(true);
										Alert alert = new Alert(AlertType.WARNING);
										alert.setTitle("Informao");
										alert.setHeaderText("Desvio padro fora de limite aceitvel. Por favor retornar para modo calibrao completa ou criar novo lote.");
										alert.setContentText("Marque a opo de Todos os pontos em Tipo de calibrao.");
										alert.showAndWait();
										fullProdRadio.setDisable(false);
										estimatedPointsRadio.setDisable(false);
	//									fixedPointsRadio.setDisable(false);
									}else if(lastBatelada.getCalibrationType() == CalibrationTypeEnum.ESTIMATED_CONST && calibrationType == CalibrationTypeEnum.ESTIMATED_CONST){
										estimatedPointsRadio.setSelected(true);
										fullProdRadio.setDisable(false);
										estimatedPointsRadio.setDisable(false);
	//									fixedPointsRadio.setDisable(false);
									}else if(lastBatelada.getCalibrationType() == CalibrationTypeEnum.FIXED_CONST){
	//									fixedPointsRadio.setSelected(true);
										fullProdRadio.setDisable(false);
										estimatedPointsRadio.setDisable(false);
	//									fixedPointsRadio.setDisable(false);
										Alert alert = new Alert(AlertType.WARNING);
										alert.setTitle("Informao");
										alert.setHeaderText("Lote est rodando com pontos fixos.");
										alert.setContentText("Caso o indice de reprovao da batelada for maior que 50%, recomendado marcar tipo da calibrao pontos estimados.");
										alert.showAndWait();
									}
								}else{
									Alert alert = new Alert(AlertType.INFORMATION);
									alert.setTitle("Informao");
									alert.setHeaderText("Lote selecionado no est no log de mudanas de lote ainda.");
									alert.setContentText("Continue rodando a batelada, mas avise a engenharia.");
									alert.showAndWait();
								}
								
							}
						});
						sceneFactory.show();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					//
					// showBateladaResultDialog();
				}
			});

			btnRefreshMedidores.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					if (selectedBatch != null) {
						connectMeters(event);
						btnStart.setDisable(false);
						onlyVerificationCheckBox.setDisable(false);
						lblEditBatelada.setDisable(false);
						// mnProcessConfig.setDisable(false);
					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Informao");
						alert.setHeaderText("Nenhum Lote foi selecionado ainda.");
						alert.setContentText("Selecione um lote para iniciar");
						alert.showAndWait();
					}
				}
			});

			btnDownloadConsts.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.CONSTANTS_DOWNLOAD);
						sceneFactory.setTitle("Download de constantes");
						sceneFactory.show();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});

			// lblEditBatelada.setDisable(false);
			lblEditBatelada.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					// try {
					// SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.SELECT_FLOW_RATE);
					// sceneFactory.setTitle("Seleo de vazes");
					// sceneFactory.show();
					// } catch (IOException e1) {
					// e1.printStackTrace();
					// }
					try {
						SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.NEW_BATCH);
						sceneFactory.setTitle("Seleo de Vazes");
						NewBatchControllerView controller = sceneFactory.getController();
						controller.setBatch(selectedBatch);
						// controller.setCallback(value -> {
						// refreshBathTable();
						// });
						sceneFactory.show();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});

			btnPurge.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					ViewStatesUtil.mainStates = MainMachineStateEnum.PURGE;

					// flagConnect = true;
					wd = new WorkIndicatorDialog(((Node) event.getTarget()).getScene().getWindow(), "Purgando...");
					wd.exec("123", inputParam -> {
						while (ViewStatesUtil.mainStates == MainMachineStateEnum.PURGE) {
							try {
								Thread.currentThread().setName("Modal Purge - Thread");
								Thread.sleep(50);
								// counterTimeOut++;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						return new Integer(1);
					});
				}
			});

			lblShowResult.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					showBateladaResultDialog();
				}
			});

			// this.flagInit = false;

			initializeConverters();
		} catch (Exception e) {
			System.err.println("Error during dashboard initialization: " + e.getMessage());
		}
	}

	private void initializeConverters() {
		try {
			ConversorModel conv1 = conversorService.findByName(ConversorNumbers.CONVERSOR1);
			ConversorModel conv2 = conversorService.findByName(ConversorNumbers.CONVERSOR2);
			// ... any other converters
			
			// Only use converters if they were successfully loaded
			if (conv1 != null) {
				// Make sure colorCode is valid for JavaFX
				if (conv1.getColorCode() == null || conv1.getColorCode().isEmpty()) {
					conv1.setColorCode("#808080");
				}
			} else {
				// Handle missing converter
				System.out.println("WARNING: Converter 1 could not be loaded");
			}
			
			if (conv2 != null) {
				// Make sure colorCode is valid for JavaFX
				if (conv2.getColorCode() == null || conv2.getColorCode().isEmpty()) {
					conv2.setColorCode("#808080");
				}
			} else {
				// Handle missing converter
				System.out.println("WARNING: Converter 2 could not be loaded");
			}
		} catch (Exception e) {
			// Log error and continue without converters
			System.err.println("Could not initialize converters: " + e.getMessage());
			// Maybe disable converter-related UI elements
		}
	}

	public void showBateladaResultDialog() {

		BateladaModel batelada = benchBean.getBatelada();
		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.SHOW_BATELADA_RESULT);
			sceneFactory.setTitle("Resultado da batelada " + batelada.getId());
			ShowBateladaResult controller = sceneFactory.getController();
			controller.setBatelada(batelada);
			sceneFactory.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Changes the color of the indicating led to YELLOW for CONECTED, BLACK/OFF for DISCONNECTED
	 * 
	 * @param conversores
	 *            - the conversores to set
	 */
	public void setConnectedConversores(ArrayList<ConversorNumbers> conversores) {
		Image imageAmarelo = new Image("/view/images/ledAmarelo.png");
		Image imagePreto = new Image("/view/images/ledOff.png");
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j <= conversores.size(); j++) {
				if (conversores.size() == 0) {
					ledPositions.get(i).setImage(imagePreto);
				} else {
					// Avoid index out of bounds
					if (j == conversores.size()) {
						break;
					}
					if (ConversorNumbers.getIndexValue(conversores.get(j)) == i) {
						ledPositions.get(i).setImage(imageAmarelo);
						ViewDataUtil.meterBeans.get(conversores.get(j)).configureChartReference(metersVelocitieChart, xAxisVelMeter, yAxisVelMeter);
						ViewDataUtil.meterBeans.get(conversores.get(j)).setLedPosition(ledPositions.get(i));// sets the
						ViewDataUtil.meterBeans.get(conversores.get(j)).setStatusLabelPos(labelReports.get(i));
						break;
					} else {
						ledPositions.get(i).setImage(imagePreto);
					}
				}
			}
		}
	}

	/**
	 * Changes the color of the indicating led to YELLOW for CONECTED, BLACK/OFF for DISCONNECTED
	 * 
	 * @param conversor
	 *            - the conversor to set
	 */
	public void addConnectedConversor(ConversorNumbers conversor) {
		Image imageAmarelo = new Image("/view/images/ledAmarelo.png");

		int idx = conversor.ordinal();
		ledPositions.get(idx).setImage(imageAmarelo);
		ViewDataUtil.meterBeans.get(conversor).configureChartReference(metersVelocitieChart, xAxisVelMeter, yAxisVelMeter);
		ViewDataUtil.meterBeans.get(conversor).setLedPosition(ledPositions.get(idx));// sets the
		ViewDataUtil.meterBeans.get(conversor).setStatusLabelPos(labelReports.get(idx));
	}

	/**
	 * Changes the color of the indicating led to YELLOW for CONECTED, BLACK/OFF for DISCONNECTED
	 * 
	 * @param conversor
	 *            - the conversor to set
	 */
	public void updateConversorView(ConversorNumbers conversor, MeterConnectionStatus meterConnectionStatus) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Image imageAmarelo = new Image("/view/images/ledAmarelo.png");
				Image imageVermelho = new Image("/view/images/ledVermelho.png");

				int idx = conversor.ordinal();
				if (meterConnectionStatus == MeterConnectionStatus.CONNECTED) {
					ledPositions.get(idx).setImage(imageAmarelo);
					labelReports.get(idx).setText("");
					ViewDataUtil.meterBeans.get(conversor).configureChartReference(metersVelocitieChart, xAxisVelMeter, yAxisVelMeter);
					ViewDataUtil.meterBeans.get(conversor).setLedPosition(ledPositions.get(idx));// sets the
					ViewDataUtil.meterBeans.get(conversor).setStatusLabelPos(labelReports.get(idx));
				} else {
					ledPositions.get(idx).setImage(imageVermelho);
					labelReports.get(idx).setText("DIFF_FW");
				}
			}
		});
	}

	/**
	 * 
	 */
	public void connectMeters(ActionEvent event) {
		// ViewStatesUtil.mainStates = MainMachineStateEnum.SETUP;
		ViewStatesUtil.mainStates = MainMachineStateEnum.CONNECT_METERS;
		modalConnectMeters(event);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void modalConnectMeters(Event event) {
		wd = new WorkIndicatorDialog(((Node) event.getTarget()).getScene().getWindow(), "Conectando Medidores...");
		wd.exec("123", inputParam -> {
			while ((ViewStatesUtil.mainStates == MainMachineStateEnum.SETUP) || (ViewStatesUtil.mainStates == MainMachineStateEnum.CONNECT_METERS)) {
				try {
					Thread.currentThread().setName("Modal Connect Medidores - Thread");
					Thread.currentThread().sleep(50);
					// counterTimeOut++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// connectedMeters = true;
			return new Integer(1);
		});
	}

	// @SuppressWarnings({ "rawtypes", "unchecked" })
	// public void modalInitializa() {
	// flagInit = true;
	// wd = new WorkIndicatorDialog(null, "Iniciando perif�ricos e servi�os");
	// wd.addTaskEndNotification(result -> {
	// flagInit = false;
	// wd = null;
	// });
	// wd.exec("123", inputParam -> {
	// while (flagInit) {
	// try {
	// Thread.sleep(50);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// return new Integer(1);
	// });
	// }

	private VBox getTopicBox(final String TEXT, final Color COLOR, final Gauge GAUGE) {
		Rectangle bar = new Rectangle(180, 5);
		bar.setArcWidth(6);
		bar.setArcHeight(6);
		bar.setFill(COLOR);
		Label label = new Label(TEXT);
		label.setTextFill(COLOR);
		label.setAlignment(Pos.CENTER);
		label.setPadding(new Insets(5, 0, 0, 0));
		GAUGE.setBarColor(COLOR);
		GAUGE.setBarBackgroundColor(Color.rgb(39, 44, 50));
		GAUGE.setAnimated(true);
		VBox vBox = new VBox(bar, label, GAUGE);
		vBox.setPadding(new Insets(5, 2, 10, 2));
		vBox.setSpacing(0);
		vBox.setAlignment(Pos.CENTER);
		return vBox;
	}

	private void enableAllConnectionMeterButtons(boolean enable) {
		enable = !enable;
		btnPos1.setDisable(enable);
		btnPos2.setDisable(enable);
		btnPos3.setDisable(enable);
		btnPos4.setDisable(enable);
		btnPos5.setDisable(enable);
		btnPos6.setDisable(enable);
		btnPos7.setDisable(enable);
		btnPos8.setDisable(enable);
		btnPos9.setDisable(enable);
		btnPos10.setDisable(enable);
		btnPos11.setDisable(enable);
		btnPos12.setDisable(enable);
		btnPos13.setDisable(enable);
		btnPos14.setDisable(enable);
		btnPos15.setDisable(enable);
		btnPos16.setDisable(enable);
		btnPos17.setDisable(enable);
		btnPos18.setDisable(enable);
		btnPos19.setDisable(enable);
		btnPos20.setDisable(enable);
	}

	/**
	 * Function returns the value of attribute selectedBatch
	 * 
	 * @return the selectedBatch
	 */
	public BatchModel getSelectedBatch() {
		return selectedBatch;
	}

	/**
	 * Function sets the value for attribute selectedBatch
	 * 
	 * @param selectedBatch
	 *            the selectedBatch to set
	 */
	public void setSelectedBatch(BatchModel selectedBatch) {
		this.selectedBatch = selectedBatch;
	}

	/**
	 * Function returns the value of attribute conversores
	 * 
	 * @return the conversores
	 */
	public ArrayList<ConversorNumbers> getConversores() {
		return conversores;
	}

	/**
	 * Function sets the value for attribute conversores
	 * 
	 * @param conversores
	 *            the conversores to set
	 */
	public void setConversores(ArrayList<ConversorNumbers> conversores) {
		this.conversores = conversores;
	}

	// /**
	// *
	// */
	// public void flagMeterConnect(boolean flag) {
	// this.connectedMeters = flag;
	// }

	/**
	 * Function returns the value of attribute flagPurge
	 * 
	 * @return the flagPurge
	 */
	public boolean isFlagPurge() {
		return flagPurge;
	}

	/**
	 * Function sets the value for attribute flagPurge
	 * 
	 * @param flagPurge
	 *            the flagPurge to set
	 */
	public void setFlagPurge(boolean flagPurge) {
		this.flagPurge = flagPurge;
	}
}
