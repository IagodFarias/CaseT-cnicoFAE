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
package view.batelada;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.VerificationErrorModel;
import util.Utilities;

/**
 * @author marcos
 *
 */
public class ShowBateladaResult {

	@FXML
	private AnchorPane root;

	// Position 01
	@FXML
	private ImageView ivMeter01;

	@FXML
	private Label lbStatus01;

	@FXML
	private HBox hbVersion01;

	@FXML
	private Label lbVersion01;

	@FXML
	private VBox vbStatusConfig01;

	@FXML
	private ImageView ivSystemDate01;

	@FXML
	private Label lbSystemDate01;

	@FXML
	private ImageView ivReplaceDate01;

	@FXML
	private Label lbReplaceDate01;

	@FXML
	private ImageView ivWmbus01;

	@FXML
	private Label lbWmbus01;

	@FXML
	private HBox hbError01;

	@FXML
	private Label lbSerialNumber01;

	@FXML
	private Label lbResult01;

	// Position 02
	@FXML
	private ImageView ivMeter02;

	@FXML
	private Label lbStatus02;

	@FXML
	private HBox hbVersion02;

	@FXML
	private Label lbVersion02;

	@FXML
	private VBox vbStatusConfig02;

	@FXML
	private ImageView ivSystemDate02;

	@FXML
	private Label lbSystemDate02;

	@FXML
	private ImageView ivReplaceDate02;

	@FXML
	private Label lbReplaceDate02;

	@FXML
	private ImageView ivWmbus02;

	@FXML
	private Label lbWmbus02;

	@FXML
	private HBox hbError02;

	@FXML
	private Label lbSerialNumber02;

	@FXML
	private Label lbResult02;

	// Position 03
	@FXML
	private ImageView ivMeter03;

	@FXML
	private Label lbStatus03;

	@FXML
	private HBox hbVersion03;

	@FXML
	private Label lbVersion03;

	@FXML
	private VBox vbStatusConfig03;

	@FXML
	private ImageView ivSystemDate03;

	@FXML
	private Label lbSystemDate03;

	@FXML
	private ImageView ivReplaceDate03;

	@FXML
	private Label lbReplaceDate03;

	@FXML
	private ImageView ivWmbus03;

	@FXML
	private Label lbWmbus03;

	@FXML
	private HBox hbError03;

	@FXML
	private Label lbSerialNumber03;

	@FXML
	private Label lbResult03;

	// Position 04
	@FXML
	private ImageView ivMeter04;

	@FXML
	private Label lbStatus04;

	@FXML
	private HBox hbVersion04;

	@FXML
	private Label lbVersion04;

	@FXML
	private VBox vbStatusConfig04;

	@FXML
	private ImageView ivSystemDate04;

	@FXML
	private Label lbSystemDate04;

	@FXML
	private ImageView ivReplaceDate04;

	@FXML
	private Label lbReplaceDate04;

	@FXML
	private ImageView ivWmbus04;

	@FXML
	private Label lbWmbus04;

	@FXML
	private HBox hbError04;

	@FXML
	private Label lbSerialNumber04;

	@FXML
	private Label lbResult04;

	// Position 05
	@FXML
	private ImageView ivMeter05;

	@FXML
	private Label lbStatus05;

	@FXML
	private HBox hbVersion05;

	@FXML
	private Label lbVersion05;

	@FXML
	private VBox vbStatusConfig05;

	@FXML
	private ImageView ivSystemDate05;

	@FXML
	private Label lbSystemDate05;

	@FXML
	private ImageView ivReplaceDate05;

	@FXML
	private Label lbReplaceDate05;

	@FXML
	private ImageView ivWmbus05;

	@FXML
	private Label lbWmbus05;

	@FXML
	private HBox hbError05;

	@FXML
	private Label lbSerialNumber05;

	@FXML
	private Label lbResult05;

	// Position 06
	@FXML
	private ImageView ivMeter06;

	@FXML
	private Label lbStatus06;

	@FXML
	private HBox hbVersion06;

	@FXML
	private Label lbVersion06;

	@FXML
	private VBox vbStatusConfig06;

	@FXML
	private ImageView ivSystemDate06;

	@FXML
	private Label lbSystemDate06;

	@FXML
	private ImageView ivReplaceDate06;

	@FXML
	private Label lbReplaceDate06;

	@FXML
	private ImageView ivWmbus06;

	@FXML
	private Label lbWmbus06;

	@FXML
	private HBox hbError06;

	@FXML
	private Label lbSerialNumber06;

	@FXML
	private Label lbResult06;

	// Position 07
	@FXML
	private ImageView ivMeter07;

	@FXML
	private Label lbStatus07;

	@FXML
	private HBox hbVersion07;

	@FXML
	private Label lbVersion07;

	@FXML
	private VBox vbStatusConfig07;

	@FXML
	private ImageView ivSystemDate07;

	@FXML
	private Label lbSystemDate07;

	@FXML
	private ImageView ivReplaceDate07;

	@FXML
	private Label lbReplaceDate07;

	@FXML
	private ImageView ivWmbus07;

	@FXML
	private Label lbWmbus07;

	@FXML
	private HBox hbError07;

	@FXML
	private Label lbSerialNumber07;

	@FXML
	private Label lbResult07;

	// Position 08
	@FXML
	private ImageView ivMeter08;

	@FXML
	private Label lbStatus08;

	@FXML
	private HBox hbVersion08;

	@FXML
	private Label lbVersion08;

	@FXML
	private VBox vbStatusConfig08;

	@FXML
	private ImageView ivSystemDate08;

	@FXML
	private Label lbSystemDate08;

	@FXML
	private ImageView ivReplaceDate08;

	@FXML
	private Label lbReplaceDate08;

	@FXML
	private ImageView ivWmbus08;

	@FXML
	private Label lbWmbus08;

	@FXML
	private HBox hbError08;

	@FXML
	private Label lbSerialNumber08;

	@FXML
	private Label lbResult08;

	// Position 09
	@FXML
	private ImageView ivMeter09;

	@FXML
	private Label lbStatus09;

	@FXML
	private HBox hbVersion09;

	@FXML
	private Label lbVersion09;

	@FXML
	private VBox vbStatusConfig09;

	@FXML
	private ImageView ivSystemDate09;

	@FXML
	private Label lbSystemDate09;

	@FXML
	private ImageView ivReplaceDate09;

	@FXML
	private Label lbReplaceDate09;

	@FXML
	private ImageView ivWmbus09;

	@FXML
	private Label lbWmbus09;

	@FXML
	private HBox hbError09;

	@FXML
	private Label lbSerialNumber09;

	@FXML
	private Label lbResult09;

	// Position 10
	@FXML
	private ImageView ivMeter10;

	@FXML
	private Label lbStatus10;

	@FXML
	private HBox hbVersion10;

	@FXML
	private Label lbVersion10;

	@FXML
	private VBox vbStatusConfig10;

	@FXML
	private ImageView ivSystemDate10;

	@FXML
	private Label lbSystemDate10;

	@FXML
	private ImageView ivReplaceDate10;

	@FXML
	private Label lbReplaceDate10;

	@FXML
	private ImageView ivWmbus10;

	@FXML
	private Label lbWmbus10;

	@FXML
	private HBox hbError10;

	@FXML
	private Label lbSerialNumber10;

	@FXML
	private Label lbResult10;

	// Position 11
	@FXML
	private ImageView ivMeter11;

	@FXML
	private Label lbStatus11;

	@FXML
	private HBox hbVersion11;

	@FXML
	private Label lbVersion11;

	@FXML
	private VBox vbStatusConfig11;

	@FXML
	private ImageView ivSystemDate11;

	@FXML
	private Label lbSystemDate11;

	@FXML
	private ImageView ivReplaceDate11;

	@FXML
	private Label lbReplaceDate11;

	@FXML
	private ImageView ivWmbus11;

	@FXML
	private Label lbWmbus11;

	@FXML
	private HBox hbError11;

	@FXML
	private Label lbSerialNumber11;

	@FXML
	private Label lbResult11;

	// Position 12
	@FXML
	private ImageView ivMeter12;

	@FXML
	private Label lbStatus12;

	@FXML
	private HBox hbVersion12;

	@FXML
	private Label lbVersion12;

	@FXML
	private VBox vbStatusConfig12;

	@FXML
	private ImageView ivSystemDate12;

	@FXML
	private Label lbSystemDate12;

	@FXML
	private ImageView ivReplaceDate12;

	@FXML
	private Label lbReplaceDate12;

	@FXML
	private ImageView ivWmbus12;

	@FXML
	private Label lbWmbus12;

	@FXML
	private HBox hbError12;

	@FXML
	private Label lbSerialNumber12;

	@FXML
	private Label lbResult12;

	// Position 13
	@FXML
	private ImageView ivMeter13;

	@FXML
	private Label lbStatus13;

	@FXML
	private HBox hbVersion13;

	@FXML
	private Label lbVersion13;

	@FXML
	private VBox vbStatusConfig13;

	@FXML
	private ImageView ivSystemDate13;

	@FXML
	private Label lbSystemDate13;

	@FXML
	private ImageView ivReplaceDate13;

	@FXML
	private Label lbReplaceDate13;

	@FXML
	private ImageView ivWmbus13;

	@FXML
	private Label lbWmbus13;

	@FXML
	private HBox hbError13;

	@FXML
	private Label lbSerialNumber13;

	@FXML
	private Label lbResult13;

	// Position 14
	@FXML
	private ImageView ivMeter14;

	@FXML
	private Label lbStatus14;

	@FXML
	private HBox hbVersion14;

	@FXML
	private Label lbVersion14;

	@FXML
	private VBox vbStatusConfig14;

	@FXML
	private ImageView ivSystemDate14;

	@FXML
	private Label lbSystemDate14;

	@FXML
	private ImageView ivReplaceDate14;

	@FXML
	private Label lbReplaceDate14;

	@FXML
	private ImageView ivWmbus14;

	@FXML
	private Label lbWmbus14;

	@FXML
	private HBox hbError14;

	@FXML
	private Label lbSerialNumber14;

	@FXML
	private Label lbResult14;

	// Position 15
	@FXML
	private ImageView ivMeter15;

	@FXML
	private Label lbStatus15;

	@FXML
	private HBox hbVersion15;

	@FXML
	private Label lbVersion15;

	@FXML
	private VBox vbStatusConfig15;

	@FXML
	private ImageView ivSystemDate15;

	@FXML
	private Label lbSystemDate15;

	@FXML
	private ImageView ivReplaceDate15;

	@FXML
	private Label lbReplaceDate15;

	@FXML
	private ImageView ivWmbus15;

	@FXML
	private Label lbWmbus15;

	@FXML
	private HBox hbError15;

	@FXML
	private Label lbSerialNumber15;

	@FXML
	private Label lbResult15;

	// Position 16
	@FXML
	private ImageView ivMeter16;

	@FXML
	private Label lbStatus16;

	@FXML
	private HBox hbVersion16;

	@FXML
	private Label lbVersion16;

	@FXML
	private VBox vbStatusConfig16;

	@FXML
	private ImageView ivSystemDate16;

	@FXML
	private Label lbSystemDate16;

	@FXML
	private ImageView ivReplaceDate16;

	@FXML
	private Label lbReplaceDate16;

	@FXML
	private ImageView ivWmbus16;

	@FXML
	private Label lbWmbus16;

	@FXML
	private HBox hbError16;

	@FXML
	private Label lbSerialNumber16;

	@FXML
	private Label lbResult16;

	// Position 17
	@FXML
	private ImageView ivMeter17;

	@FXML
	private Label lbStatus17;

	@FXML
	private HBox hbVersion17;

	@FXML
	private Label lbVersion17;

	@FXML
	private VBox vbStatusConfig17;

	@FXML
	private ImageView ivSystemDate17;

	@FXML
	private Label lbSystemDate17;

	@FXML
	private ImageView ivReplaceDate17;

	@FXML
	private Label lbReplaceDate17;

	@FXML
	private ImageView ivWmbus17;

	@FXML
	private Label lbWmbus17;

	@FXML
	private HBox hbError17;

	@FXML
	private Label lbSerialNumber17;

	@FXML
	private Label lbResult17;

	// Position 18
	@FXML
	private ImageView ivMeter18;

	@FXML
	private Label lbStatus18;

	@FXML
	private HBox hbVersion18;

	@FXML
	private Label lbVersion18;

	@FXML
	private VBox vbStatusConfig18;

	@FXML
	private ImageView ivSystemDate18;

	@FXML
	private Label lbSystemDate18;

	@FXML
	private ImageView ivReplaceDate18;

	@FXML
	private Label lbReplaceDate18;

	@FXML
	private ImageView ivWmbus18;

	@FXML
	private Label lbWmbus18;

	@FXML
	private HBox hbError18;

	@FXML
	private Label lbSerialNumber18;

	@FXML
	private Label lbResult18;

	// Position 19
	@FXML
	private ImageView ivMeter19;

	@FXML
	private Label lbStatus19;

	@FXML
	private HBox hbVersion19;

	@FXML
	private Label lbVersion19;

	@FXML
	private VBox vbStatusConfig19;

	@FXML
	private ImageView ivSystemDate19;

	@FXML
	private Label lbSystemDate19;

	@FXML
	private ImageView ivReplaceDate19;

	@FXML
	private Label lbReplaceDate19;

	@FXML
	private ImageView ivWmbus19;

	@FXML
	private Label lbWmbus19;

	@FXML
	private HBox hbError19;

	@FXML
	private Label lbSerialNumber19;

	@FXML
	private Label lbResult19;

	// Position 20
	@FXML
	private ImageView ivMeter20;

	@FXML
	private Label lbStatus20;

	@FXML
	private HBox hbVersion20;

	@FXML
	private Label lbVersion20;

	@FXML
	private VBox vbStatusConfig20;

	@FXML
	private ImageView ivSystemDate20;

	@FXML
	private Label lbSystemDate20;

	@FXML
	private ImageView ivReplaceDate20;

	@FXML
	private Label lbReplaceDate20;

	@FXML
	private ImageView ivWmbus20;

	@FXML
	private Label lbWmbus20;

	@FXML
	private HBox hbError20;

	@FXML
	private Label lbSerialNumber20;

	@FXML
	private Label lbResult20;

	/**
	 *
	 */
	public void fillListError(HBox hbox, List<VerificationErrorModel> listError) {

		VBox vbBoxKey = new VBox();
		VBox vbBoxValue = new VBox();
		Label lbKey;
		Label lbValue;
		Collections.sort(listError, VerificationErrorModel.comparator);
		for (VerificationErrorModel verificationError : listError) {

			lbKey = new Label("" + verificationError.getExpectedFlowRate());
			lbValue = new Label("" + Utilities.getDoubleFormat().format(verificationError.getError()));
			// lbValue = new Label("" + verificationError.getError());
			//
			vbBoxKey.getChildren().add(lbKey);
			vbBoxValue.getChildren().add(lbValue);
		}
		//
		hbox.getChildren().add(vbBoxKey);
		hbox.getChildren().add(vbBoxValue);
	}

	/**
	 *
	 */
	public void setBatelada(BateladaModel batelada) {

		// VerificationErrorService verificationErrorService = new VerificationErrorService();
		ArrayList<MeterModel> listMeters = new ArrayList<MeterModel>(batelada.getMeters());
		ArrayList<VerificationErrorModel> listError;
		for (MeterModel meter : listMeters) {

			listError = new ArrayList<>(meter.getErrors());
			boolean statusOk = true;
			switch (meter.getConversor().getName()) {

				//
				case CONVERSOR1:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus01.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus01.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus01.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus01.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus01.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus01.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus01.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus01.setText("SN");
						} else {
							lbStatus01.setText("VERIF");
							// Error
							hbError01.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								// fillListError(hbError01, listError);
								fillListError(hbError01, listError);
							}
						}
						statusOk = false;
					}
					hbVersion01.setVisible(true);
					lbVersion01.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig01.setVisible(true);
						hbError01.setVisible(true);
						lbResult01.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError01, listError);
						}
						// Serial number
						lbSerialNumber01.setVisible(true);
						lbSerialNumber01.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig01.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate01.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate01.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate01.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate01.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus01.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus01.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus01.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter01.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult01.setText("QA");
								} else {
									lbResult01.setText("EMBALA");
								}
							} else {
								ivMeter01.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult01.setText("CONFIG");
							}
						} else {
							ivMeter01.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult01.setText("QA");
							} else {
								lbResult01.setText("EMBALA");
							}
						}
					} else {
						ivMeter01.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;

				//
				case CONVERSOR2:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus02.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus02.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus02.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus02.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus02.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus02.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus02.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus02.setText("SN");
						} else {
							lbStatus02.setText("VERIF");
							// Error
							hbError02.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError02, listError);
							}
						}
						statusOk = false;
					}
					hbVersion02.setVisible(true);
					lbVersion02.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig02.setVisible(true);
						hbError02.setVisible(true);
						lbResult02.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError02, listError);
						}
						// Serial number
						lbSerialNumber02.setVisible(true);
						lbSerialNumber02.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig02.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate02.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate02.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate02.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate02.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus02.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus02.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus02.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter02.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult02.setText("QA");
								} else {
									lbResult02.setText("EMBALA");
								}
							} else {
								ivMeter02.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult02.setText("CONFIG");
							}
						} else {
							ivMeter02.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult02.setText("QA");
							} else {
								lbResult02.setText("EMBALA");
							}
						}
					} else {
						ivMeter02.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR3:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus03.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus03.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus03.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus03.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus03.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus03.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus03.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus03.setText("SN");
						} else {
							lbStatus03.setText("VERIF");
							// Error
							hbError03.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError03, listError);
							}
						}
						statusOk = false;
					}
					hbVersion03.setVisible(true);
					lbVersion03.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig03.setVisible(true);
						hbError03.setVisible(true);
						lbResult03.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError03, listError);
						}
						// Serial number
						lbSerialNumber03.setVisible(true);
						lbSerialNumber03.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig03.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate03.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate03.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate03.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate03.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus03.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus03.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus03.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter03.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult03.setText("QA");
								} else {
									lbResult03.setText("EMBALA");
								}
							} else {
								ivMeter03.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult03.setText("CONFIG");
							}
						} else {
							ivMeter03.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult03.setText("QA");
							} else {
								lbResult03.setText("EMBALA");
							}
						}
					} else {
						ivMeter03.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR4:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus04.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus04.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus04.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus04.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus04.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus04.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus04.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus04.setText("SN");
						} else {
							lbStatus04.setText("VERIF");
							// Error
							hbError04.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError04, listError);
							}
						}
						statusOk = false;
					}
					hbVersion04.setVisible(true);
					lbVersion04.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig04.setVisible(true);
						hbError04.setVisible(true);
						lbResult04.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError04, listError);
						}
						// Serial number
						lbSerialNumber04.setVisible(true);
						lbSerialNumber04.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig04.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate04.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate04.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate04.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate04.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus04.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus04.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus04.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter04.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult04.setText("QA");
								} else {
									lbResult04.setText("EMBALA");
								}
							} else {
								ivMeter04.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult04.setText("CONFIG");
							}
						} else {
							ivMeter04.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult04.setText("QA");
							} else {
								lbResult04.setText("EMBALA");
							}
						}
					} else {
						ivMeter04.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR5:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus05.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus05.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus05.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus05.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus05.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus05.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus05.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus05.setText("SN");
						} else {
							lbStatus05.setText("VERIF");
							// Error
							hbError05.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError05, listError);
							}
						}
						statusOk = false;
					}
					hbVersion05.setVisible(true);
					lbVersion05.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig05.setVisible(true);
						hbError05.setVisible(true);
						lbResult05.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError05, listError);
						}
						// Serial number
						lbSerialNumber05.setVisible(true);
						lbSerialNumber05.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig05.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate05.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate05.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate05.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate05.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus05.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus05.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus05.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter05.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult05.setText("QA");
								} else {
									lbResult05.setText("EMBALA");
								}
							} else {
								ivMeter05.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult05.setText("CONFIG");
							}
						} else {
							ivMeter05.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult05.setText("QA");
							} else {
								lbResult05.setText("EMBALA");
							}
						}
					} else {
						ivMeter05.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR6:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus06.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus06.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus06.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus06.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus06.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus06.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus06.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus06.setText("SN");
						} else {
							lbStatus06.setText("VERIF");
							// Error
							hbError06.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError06, listError);
							}
						}
						statusOk = false;
					}
					hbVersion06.setVisible(true);
					lbVersion06.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig06.setVisible(true);
						hbError06.setVisible(true);
						lbResult06.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError06, listError);
						}
						// Serial number
						lbSerialNumber06.setVisible(true);
						lbSerialNumber06.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig06.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate06.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate06.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate06.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate06.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus06.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus06.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus06.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter06.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult06.setText("QA");
								} else {
									lbResult06.setText("EMBALA");
								}
							} else {
								ivMeter06.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult06.setText("CONFIG");
							}
						} else {
							ivMeter06.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult06.setText("QA");
							} else {
								lbResult06.setText("EMBALA");
							}
						}
					} else {
						ivMeter06.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR7:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus07.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus07.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus07.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus07.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus07.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus07.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus07.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus07.setText("SN");
						} else {
							lbStatus07.setText("VERIF");
							// Error
							hbError07.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError07, listError);
							}
						}
						statusOk = false;
					}
					hbVersion07.setVisible(true);
					lbVersion07.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig07.setVisible(true);
						hbError07.setVisible(true);
						lbResult07.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError07, listError);
						}
						// Serial number
						lbSerialNumber07.setVisible(true);
						lbSerialNumber07.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig07.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate07.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate07.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate07.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate07.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus07.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus07.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus07.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter07.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult07.setText("QA");
								} else {
									lbResult07.setText("EMBALA");
								}
							} else {
								ivMeter07.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult07.setText("CONFIG");
							}
						} else {
							ivMeter07.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult07.setText("QA");
							} else {
								lbResult07.setText("EMBALA");
							}
						}
					} else {
						ivMeter07.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR8:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus08.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus08.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus08.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus08.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus08.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus08.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus08.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus08.setText("SN");
						} else {
							lbStatus08.setText("VERIF");
							// Error
							hbError08.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError08, listError);
							}
						}
						statusOk = false;
					}
					hbVersion08.setVisible(true);
					lbVersion08.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig08.setVisible(true);
						hbError08.setVisible(true);
						lbResult08.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError08, listError);
						}
						// Serial number
						lbSerialNumber08.setVisible(true);
						lbSerialNumber08.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig08.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate08.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate08.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate08.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate08.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus08.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus08.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus08.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter08.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult08.setText("QA");
								} else {
									lbResult08.setText("EMBALA");
								}
							} else {
								ivMeter08.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult08.setText("CONFIG");
							}
						} else {
							ivMeter08.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult08.setText("QA");
							} else {
								lbResult08.setText("EMBALA");
							}
						}
					} else {
						ivMeter08.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR9:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus09.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus09.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus09.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus09.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus09.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus09.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus09.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus09.setText("SN");
						} else {
							lbStatus09.setText("VERIF");
							// Error
							hbError09.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError09, listError);
							}
						}
						statusOk = false;
					}
					hbVersion09.setVisible(true);
					lbVersion09.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig09.setVisible(true);
						hbError09.setVisible(true);
						lbResult09.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError09, listError);
						}
						// Serial number
						lbSerialNumber09.setVisible(true);
						lbSerialNumber09.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig09.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate09.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate09.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate09.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate09.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus09.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus09.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus09.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter09.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult09.setText("QA");
								} else {
									lbResult09.setText("EMBALA");
								}
							} else {
								ivMeter09.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult09.setText("CONFIG");
							}
						} else {
							ivMeter09.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult09.setText("QA");
							} else {
								lbResult09.setText("EMBALA");
							}
						}
					} else {
						ivMeter09.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR10:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus10.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus10.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus10.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus10.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus10.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus10.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus10.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus10.setText("SN");
						} else {
							lbStatus10.setText("VERIF");
							// Error
							hbError10.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError10, listError);
							}
						}
						statusOk = false;
					}
					hbVersion10.setVisible(true);
					lbVersion10.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig10.setVisible(true);
						hbError10.setVisible(true);
						lbResult10.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError10, listError);
						}
						// Serial number
						lbSerialNumber10.setVisible(true);
						lbSerialNumber10.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig10.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate10.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate10.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate10.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate10.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus10.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus10.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus10.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter10.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult10.setText("QA");
								} else {
									lbResult10.setText("EMBALA");
								}
							} else {
								ivMeter10.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult10.setText("CONFIG");
							}
						} else {
							ivMeter10.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult10.setText("QA");
							} else {
								lbResult10.setText("EMBALA");
							}
						}
					} else {
						ivMeter10.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR11:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus11.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus11.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus11.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus11.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus11.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus11.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus11.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus11.setText("SN");
						} else {
							lbStatus11.setText("VERIF");
							// Error
							hbError11.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError11, listError);
							}
						}
						statusOk = false;
					}
					hbVersion11.setVisible(true);
					lbVersion11.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig11.setVisible(true);
						hbError11.setVisible(true);
						lbResult11.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError11, listError);
						}
						// Serial number
						lbSerialNumber11.setVisible(true);
						lbSerialNumber11.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig11.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate11.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate11.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate11.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate11.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus11.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus11.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus11.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter11.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult11.setText("QA");
								} else {
									lbResult11.setText("EMBALA");
								}
							} else {
								ivMeter11.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult11.setText("CONFIG");
							}
						} else {
							ivMeter11.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult11.setText("QA");
							} else {
								lbResult11.setText("EMBALA");
							}
						}
					} else {
						ivMeter11.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR12:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus12.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus12.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus12.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus12.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus12.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus12.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus12.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus12.setText("SN");
						} else {
							lbStatus12.setText("VERIF");
							// Error
							hbError12.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError12, listError);
							}
						}
						statusOk = false;
					}
					hbVersion12.setVisible(true);
					lbVersion12.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig12.setVisible(true);
						hbError12.setVisible(true);
						lbResult12.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError12, listError);
						}
						// Serial number
						lbSerialNumber12.setVisible(true);
						lbSerialNumber12.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig12.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate12.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate12.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate12.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate12.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus12.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus12.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus12.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter12.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult12.setText("QA");
								} else {
									lbResult12.setText("EMBALA");
								}
							} else {
								ivMeter12.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult12.setText("CONFIG");
							}
						} else {
							ivMeter12.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult12.setText("QA");
							} else {
								lbResult12.setText("EMBALA");
							}
						}
					} else {
						ivMeter12.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR13:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus13.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus13.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus13.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus13.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus13.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus13.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus13.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus13.setText("SN");
						} else {
							lbStatus13.setText("VERIF");
							// Error
							hbError13.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError13, listError);
							}
						}
						statusOk = false;
					}
					hbVersion13.setVisible(true);
					lbVersion13.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig13.setVisible(true);
						hbError13.setVisible(true);
						lbResult13.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError13, listError);
						}
						// Serial number
						lbSerialNumber13.setVisible(true);
						lbSerialNumber13.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig13.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate13.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate13.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate13.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate13.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus13.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus13.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus13.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter13.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult13.setText("QA");
								} else {
									lbResult13.setText("EMBALA");
								}
							} else {
								ivMeter13.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult13.setText("CONFIG");
							}
						} else {
							ivMeter13.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult13.setText("QA");
							} else {
								lbResult13.setText("EMBALA");
							}
						}
					} else {
						ivMeter13.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR14:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus14.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus14.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus14.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus14.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus14.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus14.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus14.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus14.setText("SN");
						} else {
							lbStatus14.setText("VERIF");
							// Error
							hbError14.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError14, listError);
							}
						}
						statusOk = false;
					}
					hbVersion14.setVisible(true);
					lbVersion14.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig14.setVisible(true);
						hbError14.setVisible(true);
						lbResult14.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError14, listError);
						}
						// Serial number
						lbSerialNumber14.setVisible(true);
						lbSerialNumber14.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig14.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate14.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate14.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate14.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate14.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus14.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus14.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus14.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter14.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult14.setText("QA");
								} else {
									lbResult14.setText("EMBALA");
								}
							} else {
								ivMeter14.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult14.setText("CONFIG");
							}
						} else {
							ivMeter14.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult14.setText("QA");
							} else {
								lbResult14.setText("EMBALA");
							}
						}
					} else {
						ivMeter14.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR15:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus15.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus15.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus15.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus15.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus15.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus15.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus15.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus15.setText("SN");
						} else {
							lbStatus15.setText("VERIF");
							// Error
							hbError15.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError15, listError);
							}
						}
						statusOk = false;
					}
					hbVersion15.setVisible(true);
					lbVersion15.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig15.setVisible(true);
						hbError15.setVisible(true);
						lbResult15.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError15, listError);
						}
						// Serial number
						lbSerialNumber15.setVisible(true);
						lbSerialNumber15.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig15.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate15.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate15.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate15.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate15.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus15.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus15.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus15.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter15.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult15.setText("QA");
								} else {
									lbResult15.setText("EMBALA");
								}
							} else {
								ivMeter15.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult15.setText("CONFIG");
							}
						} else {
							ivMeter15.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult15.setText("QA");
							} else {
								lbResult15.setText("EMBALA");
							}
						}
					} else {
						ivMeter15.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR16:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus16.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus16.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus16.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus16.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus16.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus16.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus16.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus16.setText("SN");
						} else {
							lbStatus16.setText("VERIF");
							// Error
							hbError16.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError16, listError);
							}
						}
						statusOk = false;
					}
					hbVersion16.setVisible(true);
					lbVersion16.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig16.setVisible(true);
						hbError16.setVisible(true);
						lbResult16.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError16, listError);
						}
						// Serial number
						lbSerialNumber16.setVisible(true);
						lbSerialNumber16.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig16.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate16.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate16.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate16.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate16.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus16.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus16.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus16.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter16.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult16.setText("QA");
								} else {
									lbResult16.setText("EMBALA");
								}
							} else {
								ivMeter16.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult16.setText("CONFIG");
							}
						} else {
							ivMeter16.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult16.setText("QA");
							} else {
								lbResult16.setText("EMBALA");
							}
						}
					} else {
						ivMeter16.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR17:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus17.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus17.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus17.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus17.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus17.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus17.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus17.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus17.setText("SN");
						} else {
							lbStatus17.setText("VERIF");
							hbError17.setVisible(true);
							// Error
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError17, listError);
							}
						}
						statusOk = false;
					}
					hbVersion17.setVisible(true);
					lbVersion17.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig17.setVisible(true);
						hbError17.setVisible(true);
						lbResult17.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError17, listError);
						}
						// Serial number
						lbSerialNumber17.setVisible(true);
						lbSerialNumber17.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig17.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate17.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate17.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate17.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate17.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus17.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus17.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus17.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter17.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult17.setText("QA");
								} else {
									lbResult17.setText("EMBALA");
								}
							} else {
								ivMeter17.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult17.setText("CONFIG");
							}
						} else {
							ivMeter17.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult17.setText("QA");
							} else {
								lbResult17.setText("EMBALA");
							}
						}
					} else {
						ivMeter17.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR18:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus18.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus18.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus18.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus18.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus18.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus18.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus18.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus18.setText("SN");
						} else {
							lbStatus18.setText("VERIF");
							// Error
							hbError18.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError18, listError);
							}
						}
						statusOk = false;
					}
					hbVersion18.setVisible(true);
					lbVersion18.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig18.setVisible(true);
						hbError18.setVisible(true);
						lbResult18.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError18, listError);
						}
						// Serial number
						lbSerialNumber18.setVisible(true);
						lbSerialNumber18.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig18.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate18.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate18.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate18.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate18.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus18.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus18.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus18.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter18.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult18.setText("QA");
								} else {
									lbResult18.setText("EMBALA");
								}
							} else {
								ivMeter18.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult18.setText("CONFIG");
							}
						} else {
							ivMeter18.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult18.setText("QA");
							} else {
								lbResult18.setText("EMBALA");
							}
						}
					} else {
						ivMeter18.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR19:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus19.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus19.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus19.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus19.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus19.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus19.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus19.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus19.setText("SN");
						} else {
							lbStatus19.setText("VERIF");
							// Error
							hbError19.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError19, listError);
							}
						}
						statusOk = false;
					}
					hbVersion19.setVisible(true);
					lbVersion19.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig19.setVisible(true);
						hbError19.setVisible(true);
						lbResult19.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError19, listError);
						}
						// Serial number
						lbSerialNumber19.setVisible(true);
						lbSerialNumber19.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig19.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate19.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate19.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate19.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate19.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus19.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus19.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus19.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter19.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult19.setText("QA");
								} else {
									lbResult19.setText("EMBALA");
								}
							} else {
								ivMeter19.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult19.setText("CONFIG");
							}
						} else {
							ivMeter19.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult19.setText("QA");
							} else {
								lbResult19.setText("EMBALA");
							}
						}
					} else {
						ivMeter19.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
				//
				case CONVERSOR20:

					// Comm down
					if (meter.isCommDown()) {
						lbStatus20.setText("COMM");
						statusOk = false;
					}
					// Agc Trim
					else if (!meter.isApprovedAgcTrim()) {
						lbStatus20.setText("TRIM");
						statusOk = false;
					}
					// Calculate Zero flow
					else if (!meter.isCalculatedZeroFlow()) {
						lbStatus20.setText("DESV");
						statusOk = false;
					}
					// Download Zero flow
					else if (!meter.isDownloadedZeroFlow()) {
						lbStatus20.setText("DWZERO");
						statusOk = false;
					}
					// Calculate constants
					else if (!meter.isCalculatedConstants()) {
						lbStatus20.setText("CALC");
						statusOk = false;
					}
					// Download constants
					else if (!meter.isDownloadedConstants()) {
						lbStatus20.setText("DOWN");
						statusOk = false;
					}
					// Calculate errors
					else if (!meter.isCalculatedErrors()) {
						lbStatus20.setText("CALC_VERIF");
						statusOk = false;
					}
					// Verif
					else if (!meter.isApprovedVerification()) {
						if (!meter.isAvailableSN()) {
							lbStatus20.setText("SN");
						} else {
							lbStatus20.setText("VERIF");
							// Error
							hbError20.setVisible(true);
							// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
							if (!listError.isEmpty()) {
								fillListError(hbError20, listError);
							}
						}
						statusOk = false;
					}
					hbVersion20.setVisible(true);
					lbVersion20.setText(meter.getFirmware().getVersion());

					if (statusOk) {
						// vbStatusConfig20.setVisible(true);
						hbError20.setVisible(true);
						lbResult20.setVisible(true);

						// Error
						// List<VerificationErrorModel> listError = verificationErrorService.findByMeter(meter);
						if (!listError.isEmpty()) {
							fillListError(hbError20, listError);
						}
						// Serial number
						lbSerialNumber20.setVisible(true);
						lbSerialNumber20.setText(meter.getSerialNumber());

						if (Utilities.parseVersionToLong(meter.getFirmware().getVersion()) >= Utilities.parseVersionToLong("2.0.0.0")) {
							vbStatusConfig20.setVisible(true);
							// Meter system date
							if (meter.isMeterSystemDateConfigured()) {
								ivSystemDate20.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivSystemDate20.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// Meter replace date
							if (meter.isMeterReplaceDateConfigured()) {
								ivReplaceDate20.setImage(new Image("/view/images/icon_check_20.png"));
							} else {
								ivReplaceDate20.setImage(new Image("/view/images/icon_uncheck_20.png"));
								statusOk = false;
							}
							// WmBus config.
							if (meter.getMeterTypeModel().isRadioWmBus()) {
								if (meter.isWmBusConfigured()) {
									ivWmbus20.setImage(new Image("/view/images/icon_check_20.png"));
								} else {
									ivWmbus20.setImage(new Image("/view/images/icon_uncheck_20.png"));
									statusOk = false;
								}
							} else {
								ivWmbus20.setVisible(false);
							}
							//
							if (statusOk) {
								ivMeter20.setImage(new Image("/view/images/water_meter_green.png"));
								if (meter.isQa()) {
									lbResult20.setText("QA");
								} else {
									lbResult20.setText("EMBALA");
								}
							} else {
								ivMeter20.setImage(new Image("/view/images/water_meter_blue.png"));
								lbResult20.setText("CONFIG");
							}
						} else {
							ivMeter20.setImage(new Image("/view/images/water_meter_green.png"));
							if (meter.isQa()) {
								lbResult20.setText("QA");
							} else {
								lbResult20.setText("EMBALA");
							}
						}
					} else {
						ivMeter20.setImage(new Image("/view/images/water_meter_red.png"));
					}
				break;
			}
		}
	}

	/**
	 *
	 */
	private void closeDialog() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	/**
	*
	*/
	@FXML
	private void onAction() {
		closeDialog();
	}

	/**
	 * 
	 */
	@FXML
	private void initialize() {
		// lbSerialNumber01;hbError01

		// Pos 01
		ivMeter01.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus01.setText("-");
		hbVersion01.setVisible(false);
		vbStatusConfig01.setVisible(false);
		hbError01.setVisible(false);
		lbSerialNumber01.setVisible(false);
		lbResult01.setVisible(false);
		// Pos 02
		ivMeter02.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus02.setText("-");
		hbVersion02.setVisible(false);
		vbStatusConfig02.setVisible(false);
		hbError02.setVisible(false);
		lbSerialNumber02.setVisible(false);
		lbResult02.setVisible(false);
		// Pos 03
		ivMeter03.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus03.setText("-");
		hbVersion03.setVisible(false);
		vbStatusConfig03.setVisible(false);
		hbError03.setVisible(false);
		lbSerialNumber03.setVisible(false);
		lbResult03.setVisible(false);
		// Pos 04
		ivMeter04.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus04.setText("-");
		hbVersion04.setVisible(false);
		vbStatusConfig04.setVisible(false);
		hbError04.setVisible(false);
		lbSerialNumber04.setVisible(false);
		lbResult04.setVisible(false);
		// Pos 05
		ivMeter05.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus05.setText("-");
		hbVersion05.setVisible(false);
		vbStatusConfig05.setVisible(false);
		hbError05.setVisible(false);
		lbSerialNumber05.setVisible(false);
		lbResult05.setVisible(false);
		// Pos 06
		ivMeter06.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus06.setText("-");
		hbVersion06.setVisible(false);
		vbStatusConfig06.setVisible(false);
		hbError06.setVisible(false);
		lbSerialNumber06.setVisible(false);
		lbResult06.setVisible(false);
		// Pos 07
		ivMeter07.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus07.setText("-");
		hbVersion07.setVisible(false);
		vbStatusConfig07.setVisible(false);
		hbError07.setVisible(false);
		lbSerialNumber07.setVisible(false);
		lbResult07.setVisible(false);
		// Pos 08
		ivMeter08.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus08.setText("-");
		hbVersion08.setVisible(false);
		vbStatusConfig08.setVisible(false);
		hbError08.setVisible(false);
		lbSerialNumber08.setVisible(false);
		lbResult08.setVisible(false);
		// Pos 09
		ivMeter09.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus09.setText("-");
		hbVersion09.setVisible(false);
		vbStatusConfig09.setVisible(false);
		hbError09.setVisible(false);
		lbSerialNumber09.setVisible(false);
		lbResult09.setVisible(false);
		// Pos 10
		ivMeter10.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus10.setText("-");
		hbVersion10.setVisible(false);
		vbStatusConfig10.setVisible(false);
		hbError10.setVisible(false);
		lbSerialNumber10.setVisible(false);
		lbResult10.setVisible(false);
		// Pos 11
		ivMeter11.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus11.setText("-");
		hbVersion11.setVisible(false);
		vbStatusConfig11.setVisible(false);
		hbError11.setVisible(false);
		lbSerialNumber11.setVisible(false);
		lbResult11.setVisible(false);
		// Pos 12
		ivMeter12.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus12.setText("-");
		hbVersion12.setVisible(false);
		vbStatusConfig12.setVisible(false);
		hbError12.setVisible(false);
		lbSerialNumber12.setVisible(false);
		lbResult12.setVisible(false);
		// Pos 13
		ivMeter13.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus13.setText("-");
		hbVersion13.setVisible(false);
		vbStatusConfig13.setVisible(false);
		hbError13.setVisible(false);
		lbSerialNumber13.setVisible(false);
		lbResult13.setVisible(false);
		// Pos 14
		ivMeter14.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus14.setText("-");
		hbVersion14.setVisible(false);
		vbStatusConfig14.setVisible(false);
		hbError14.setVisible(false);
		lbSerialNumber14.setVisible(false);
		lbResult14.setVisible(false);
		// Pos 15
		ivMeter15.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus15.setText("-");
		hbVersion15.setVisible(false);
		vbStatusConfig15.setVisible(false);
		hbError15.setVisible(false);
		lbSerialNumber15.setVisible(false);
		lbResult15.setVisible(false);
		// Pos 16
		ivMeter16.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus16.setText("-");
		hbVersion16.setVisible(false);
		vbStatusConfig16.setVisible(false);
		hbError16.setVisible(false);
		lbSerialNumber16.setVisible(false);
		lbResult16.setVisible(false);
		// Pos 17
		ivMeter17.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus17.setText("-");
		hbVersion17.setVisible(false);
		vbStatusConfig17.setVisible(false);
		hbError17.setVisible(false);
		lbSerialNumber17.setVisible(false);
		lbResult17.setVisible(false);
		// Pos 18
		ivMeter18.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus18.setText("-");
		hbVersion18.setVisible(false);
		vbStatusConfig18.setVisible(false);
		hbError18.setVisible(false);
		lbSerialNumber18.setVisible(false);
		lbResult18.setVisible(false);
		// Pos 19
		ivMeter19.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus19.setText("-");
		hbVersion19.setVisible(false);
		vbStatusConfig19.setVisible(false);
		hbError19.setVisible(false);
		lbSerialNumber19.setVisible(false);
		lbResult19.setVisible(false);
		// Pos 20
		ivMeter20.setImage(new Image("/view/images/water_meter_black.png"));
		lbStatus20.setText("-");
		hbVersion20.setVisible(false);
		vbStatusConfig20.setVisible(false);
		hbError20.setVisible(false);
		lbSerialNumber20.setVisible(false);
		lbResult20.setVisible(false);
	}
}
