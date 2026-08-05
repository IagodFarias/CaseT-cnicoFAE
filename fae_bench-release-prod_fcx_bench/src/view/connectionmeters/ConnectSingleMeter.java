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
package view.connectionmeters;

import enumerations.MeterConnectionStatus;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import si.dbcomm.service.ConversorService;
import si.dbcomm.util.ConversorNumbers;
import util.MainMachineStateEnum;
import util.ViewStatesUtil;

/**
 * @author marcos
 *
 */
public class ConnectSingleMeter {

	@FXML
	private BorderPane root;

	@FXML
	private Label lbTitle;

	@FXML
	private ProgressIndicator piProcessing;

	@FXML
	private ImageView ivStatusImage;

	@FXML
	private Label lbUserMsg;

	@FXML
	private Button tbCancel;

	// private ConversorService conversorService = new ConversorService();
	//
	// private ConversorNumbers conversorNum;

	private boolean isCanceled = false;

	private ThreadConnectSingleMeter threadConnectSingleMeter;

	/**
	 * Function sets the value for attribute conversorNum
	 * 
	 * @param conversorNum
	 *            the conversorNum to set
	 */
	public void setConversorNumber(ConversorNumbers conversorNum) {
		// this.conversorNum = conversorNum;
		//
		lbTitle.setText(conversorNum.name());
		ViewStatesUtil.conversorNum = conversorNum;
		ViewStatesUtil.resultConnectSingleMeter = MeterConnectionStatus.DISCONNECTED;
		ViewStatesUtil.mainStates = MainMachineStateEnum.CONNECT_SINGLE_METER;

		threadConnectSingleMeter.setLoopThread(true);
		threadConnectSingleMeter.start();

	}

	/**
	 * 
	 */
	private void onClose() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	/**
	 * 
	 */
	@FXML
	private void onCancelButton() {
		if (!isCanceled) {
			isCanceled = true;
			threadConnectSingleMeter.setLoopThread(false);
		} else {
			onClose();
		}
	}

	/**
	 * 
	 */
	@FXML
	private void onOkButton(ActionEvent event) {
		// onClose();
		if (ViewStatesUtil.resultConnectSingleMeter != MeterConnectionStatus.DISCONNECTED) {
			onClose();
		}
	}

	/**
	 * Inicializa a classe controller. Este método é chamado automaticamente após o arquivo fxml ter sido carregado.
	 */
	@FXML
	private void initialize() {

		// stage = (Stage) root.getScene().getWindow();
		// stage.setOnCloseRequest((WindowEvent event1) -> {
		// // drugs.clear();
		// // loadDrug();
		// System.out.println("ConnectSingleMeter.stop");
		// });

		piProcessing.setVisible(true);
		ivStatusImage.setVisible(false);
		lbUserMsg.setText("Conectando com o medidor");

		threadConnectSingleMeter = new ThreadConnectSingleMeter();
		threadConnectSingleMeter.setName("ThreadConnectSingleMeter");
	}

	// --------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------
	private class ThreadConnectSingleMeter extends Thread implements Runnable {

		private boolean loopThread = false;

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while (loopThread) {
				while ((ViewStatesUtil.mainStates == MainMachineStateEnum.CONNECT_SINGLE_METER)) {
					// while ((loopThread) && (ViewStatesUtil.mainStates == MainMachineStateEnum.CONNECT_SINGLE_METER)) {
					try {
						Thread.currentThread().sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (ViewStatesUtil.resultConnectSingleMeter != MeterConnectionStatus.DISCONNECTED) {
					break;
				} else {
					System.out.println("ConnectSingleMeter.setConversorNumber.Thread.CONNECT_SINGLE_METER");
					ViewStatesUtil.mainStates = MainMachineStateEnum.CONNECT_SINGLE_METER;
				}
			}

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Result thread
					if (ViewStatesUtil.resultConnectSingleMeter == MeterConnectionStatus.CONNECTED) {
						piProcessing.setVisible(false);
						ivStatusImage.setVisible(true);
						ivStatusImage.setImage(new Image("/view/images/icon_check_50.png"));
						lbUserMsg.setText("Sucesso ao conectar com o medidor.");
					} else if (ViewStatesUtil.resultConnectSingleMeter == MeterConnectionStatus.DIFF_FW_VERSION) {
						piProcessing.setVisible(false);
						ivStatusImage.setVisible(true);
						ivStatusImage.setImage(new Image("/view/images/icon_uncheck_50.png"));
						lbUserMsg.setText("Medidor com versão de fw diferente do lote.");
					} else {
						piProcessing.setVisible(false);
						ivStatusImage.setVisible(true);
						ivStatusImage.setImage(new Image("/view/images/icon_uncheck_50.png"));
						lbUserMsg.setText("Falha ao conectar com o medidor.");
					}
					tbCancel.setText("Fechar");
				}
			});
		}

		/**
		 * Function returns the value of attribute loopThread
		 * 
		 * @return the loopThread
		 */
		public boolean isLoopThread() {
			return loopThread;
		}

		/**
		 * Function sets the value for attribute loopThread
		 * 
		 * @param loopThread
		 *            the loopThread to set
		 */
		public void setLoopThread(boolean loopThread) {
			this.loopThread = loopThread;
		}
	}
}
