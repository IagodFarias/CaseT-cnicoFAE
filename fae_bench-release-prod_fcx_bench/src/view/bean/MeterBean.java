//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MeterBean.java
*    @author marcos
*    @date 24 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.bean;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import services.CalibrationService;
import si.dbcomm.model.MeterDataModel;
import si.dbcomm.model.MeterModel;

/**
 * @author marcos
 *
 */
public class MeterBean implements Observer {

	// static final Image IMAGE_LED_AMARELO = new Image("File:../../rsrc/images/ledAmarelo.png");
	// static final Image IMAGE_LED_PRETO = new Image("File:../../rsrc/images/ledOff.png");
	// static final Image IMAGE_LED_VERMELHO = new Image("File:../../rsrc/images/ledVermelho.png");
	// static final Image IMAGE_LED_VERDE = new Image("File:../../rsrc/images/ledVerde.png");

	static final Image IMAGE_LED_AMARELO = new Image("/view/images/ledAmarelo.png");

	static final Image IMAGE_LED_PRETO = new Image("/view/images/ledOff.png");

	static final Image IMAGE_LED_VERMELHO = new Image("/view/images/ledVermelho.png");

	static final Image IMAGE_LED_VERDE = new Image("/view/images/ledVerde.png");
	
	static final Image IMAGE_LED_AZUL = new Image("/view/images/ledAzul.png");

	private ObservableList<XYChart.Data<Integer, Double>> velocitiesCalcAtMeter = FXCollections.observableArrayList();

	private ObservableList<XYChart.Data<Integer, Double>> velocitiesCalcAtSw = FXCollections.observableArrayList();

	private Series<Integer, Double> velMeterSeries;

	private Series<Integer, Double> velSwSeries;

	private ImageView ledPosition;

	private Label statusLabelPos;
	//
	// private NumberAxis xAxis;
	//
	// private NumberAxis yAxis;

	static int series = 0;

	LineChart<Integer, Double> chartReference;

	private CalibrationService calibService = new CalibrationService();

	private MeterModel meter;

	private int eixoX = 0;

	private boolean circleBuffer = false;

	public MeterBean(MeterModel meter) {
		this.meter = meter;
		velMeterSeries = new XYChart.Series<>(velocitiesCalcAtMeter);
		velSwSeries = new XYChart.Series<>(velocitiesCalcAtSw);
	}

	/**
	 * Function returns the value of attribute velMeterSeries
	 * 
	 * @return the velMeterSeries
	 */
	public Series<Integer, Double> getVelMeterSeries() {
		return velMeterSeries;
	}

	/**
	 * Function sets the value for attribute velMeterSeries
	 * 
	 * @param velMeterSeries
	 *            the velMeterSeries to set
	 */
	public void setVelMeterSeries(Series<Integer, Double> velMeterSeries) {
		this.velMeterSeries = velMeterSeries;
	}

	/**
	 * Function returns the value of attribute velSwSeries
	 * 
	 * @return the velSwSeries
	 */
	public Series<Integer, Double> getVelSwSeries() {
		return velSwSeries;
	}

	/**
	 * Function sets the value for attribute velSwSeries
	 * 
	 * @param velSwSeries
	 *            the velSwSeries to set
	 */
	public void setVelSwSeries(Series<Integer, Double> velSwSeries) {
		this.velSwSeries = velSwSeries;
	}

	/**
	 * Function returns the value of attribute chartReference
	 * 
	 * @return the chartReference
	 */
	public LineChart<Integer, Double> getChartReference() {
		return chartReference;
	}

	/**
	 * Function sets the value for attribute chartReference
	 * 
	 * @param chartReference
	 *            the chartReference to set
	 */
	public void setChartReference(final LineChart<Integer, Double> chartReference) {
		this.chartReference = chartReference;
	}

	/**
	 * Function returns the value of attribute ledPosition
	 * 
	 * @return the ledPosition
	 */
	public ImageView getLedPosition() {
		return ledPosition;
	}

	/**
	 * Function sets the value for attribute ledPosition
	 * 
	 * @param ledPosition
	 *            the ledPosition to set
	 */
	public void setLedPosition(ImageView ledPosition) {
		this.ledPosition = ledPosition;
	}

	/**
	 * Function returns the value of attribute statusLabelPos
	 * 
	 * @return the statusLabelPos
	 */
	public Label getStatusLabelPos() {
		return statusLabelPos;
	}

	/**
	 * Function sets the value for attribute statusLabelPos
	 * 
	 * @param statusLabelPos
	 *            the statusLabelPos to set
	 */
	public void setStatusLabelPos(Label statusLabelPos) {
		this.statusLabelPos = statusLabelPos;
	}

	public void configureChartReference(final LineChart<Integer, Double> chart, NumberAxis x, NumberAxis y) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				chartReference = chart;
				chartReference.getData().add(velMeterSeries);

				Set<Node> lineMainMeterVel = chartReference.lookupAll(".series" + series);
				for (final Node line : lineMainMeterVel) {
					line.setStyle("-fx-stroke: " + meter.getConversor().getColorCode() + "; -fx-stroke-width: 2px;");
				}
				series++;

				chartReference.getData().add(velSwSeries);

				Set<Node> lineMainSwVel = chartReference.lookupAll(".series" + series);
				for (final Node line : lineMainSwVel) {
					line.setStyle("-fx-stroke: " + meter.getConversor().getColorCode() + "; -fx-stroke-width: 2px; -fx-stroke-dash-array: 5;");
				}
				series++;

				x.setForceZeroInRange(false);
				y.setForceZeroInRange(false);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof MeterDataModel) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					MeterDataModel pack = (MeterDataModel) arg;
					eixoX++;
					double velCalcSw = calibService.ttToVel(pack.getTtstd(), pack.getTtrev(), meter);
					velocitiesCalcAtSw.add(new XYChart.Data<Integer, Double>(eixoX, velCalcSw));
					velocitiesCalcAtMeter.add(new XYChart.Data<Integer, Double>(eixoX, pack.getVel()));

					if (eixoX > 150) {
						velocitiesCalcAtSw.clear();
						eixoX = 0;
						circleBuffer = true;
					}

					if (circleBuffer) {
						// velocitiesCalcAtSw.remove(velocitiesCalcAtSw.get(0));
						velocitiesCalcAtMeter.remove(velocitiesCalcAtMeter.get(0));
					}
				}
			});
		}

	}

	public void setLedToYellow() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ledPosition.setImage(IMAGE_LED_AMARELO);
			}
		});
	}

	public void setLedToOff() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ledPosition.setImage(IMAGE_LED_PRETO);
			}
		});
	}

	public void setLedToRed() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ledPosition.setImage(IMAGE_LED_VERMELHO);
			}
		});
	}

	public void setLedToGreen() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ledPosition.setImage(IMAGE_LED_VERDE);
			}
		});
	}
	
	public void setLedToBlue() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ledPosition.setImage(IMAGE_LED_AZUL);
			}
		});
	}

	public void setStatusLabelPos(String value) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				statusLabelPos.setText(value);
			};
		});
	}

	public void clearGraph() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				if (velMeterSeries.getData() != null) {
					if (!velMeterSeries.getData().isEmpty()) {
						velMeterSeries.getData().clear();
					}
				}

				if (velSwSeries.getData() != null) {
					if (!velSwSeries.getData().isEmpty()) {
						velSwSeries.getData().clear();
					}
				}

				if (velocitiesCalcAtMeter != null) {
					if (!velocitiesCalcAtMeter.isEmpty()) {
						velocitiesCalcAtMeter.clear();
					}
				}

				if (velocitiesCalcAtSw != null) {
					if (!velocitiesCalcAtSw.isEmpty()) {
						velocitiesCalcAtSw.clear();
					}
				}
				eixoX = 0;
				circleBuffer = false;

//				System.out.println("MeterBean.clearGraph()");
//				System.gc();
			};
		});

		// velMeterSeries.getData().clear();
		// velSwSeries.getData().clear();
		//
		// velocitiesCalcAtMeter.clear();
		// velocitiesCalcAtSw.clear();
		// eixoX = 0;
		// circleBuffer = false;
		//
		// System.gc();
	}
}
