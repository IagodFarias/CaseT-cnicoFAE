//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file GraphControl.java
*    @author marcos
*    @date 25 de out de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.util;

import java.util.Set;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

/**
 * @author marcos
 *
 */
public class GraphControl {

	private NumberAxis xAxis;

	private NumberAxis yAxis;

	private SimpleDoubleProperty valueProperty = new SimpleDoubleProperty();

	private int eixoXSamples = 0;

	private boolean circleBuffer = false;

	private Series<Integer, Double> refSeries;

	private Series<Integer, Double> upperLimitSeries;

	private Series<Integer, Double> lowerLimitSeries;

	private LineChart<Integer, Double> refChart;

	private double yUpper = Double.NaN;

	private double yLower = Double.NaN;

	private ObservableList<XYChart.Data<Integer, Double>> refObsList = FXCollections.observableArrayList();

	private ObservableList<XYChart.Data<Integer, Double>> upperLimitObsList = FXCollections.observableArrayList();

	private ObservableList<XYChart.Data<Integer, Double>> lowerLimitObsList = FXCollections.observableArrayList();

	public GraphControl(LineChart<Integer, Double> refChart, NumberAxis x, NumberAxis y) {
		this.refChart = refChart;
		this.xAxis = x;
		this.yAxis = y;
		this.refSeries = new Series<>(refObsList);
		this.lowerLimitSeries = new Series<>(lowerLimitObsList);
		this.upperLimitSeries = new Series<>(upperLimitObsList);
		refChart.getData().add(refSeries);
		refChart.getData().add(lowerLimitSeries);
		refChart.getData().add(upperLimitSeries);
		xAxis.setForceZeroInRange(false);
		yAxis.setForceZeroInRange(false);

		valueProperty.addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						refObsList.add(new XYChart.Data<Integer, Double>(eixoXSamples, (Double) newVal));

						if (!Double.isNaN(yLower)) {
							lowerLimitObsList.add(new XYChart.Data<Integer, Double>(eixoXSamples, (Double) yLower));
						}

						if (!Double.isNaN(yUpper)) {
							upperLimitObsList.add(new XYChart.Data<Integer, Double>(eixoXSamples, (Double) yUpper));
						}

						eixoXSamples++;
						// if (eixoXSamples > 1000) {
						if (eixoXSamples > 500) {
							lowerLimitObsList.clear();
							upperLimitObsList.clear();
							eixoXSamples = 0;
							circleBuffer = true;
						}

						if (circleBuffer) {
							refObsList.remove(refObsList.get(0));
						}
					}
				});
			}
		});

		// setUpGraph();
		// setUpGraphCollors();
	}

	public void clearGraph() {
		// refSeries.getData().clear();
		// lowerLimitSeries.getData().clear();
		// upperLimitSeries.getData().clear();
		// //
		// lowerLimitObsList.clear();
		// upperLimitObsList.clear();
		// refObsList.clear();
		// eixoXSamples = 0;
		// circleBuffer = false;
		//
		// System.gc();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				if (refSeries.getData() != null) {
					if (!refSeries.getData().isEmpty()) {
						refSeries.getData().clear();
					}
				}
				if (lowerLimitSeries.getData() != null) {
					if (!lowerLimitSeries.getData().isEmpty()) {
						lowerLimitSeries.getData().clear();
					}
				}
				if (upperLimitSeries.getData() != null) {
					if (!upperLimitSeries.getData().isEmpty()) {
						upperLimitSeries.getData().clear();
					}
				}

				if (lowerLimitObsList != null) {
					if (!lowerLimitObsList.isEmpty()) {
						lowerLimitObsList.clear();
					}
				}
				if (upperLimitObsList != null) {
					if (!upperLimitObsList.isEmpty()) {
						upperLimitObsList.clear();
					}
				}
				if (refObsList != null) {
					if (!refObsList.isEmpty()) {
						refObsList.clear();
					}
				}
				eixoXSamples = 0;
				circleBuffer = false;

//				System.out.println("GraphControl.clearGraph()");
				// System.gc();
			}
		});
	}

	@SuppressWarnings("unused")
	private void setUpGraph() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				refChart.getData().add(refSeries);
				refChart.getData().add(lowerLimitSeries);
				refChart.getData().add(upperLimitSeries);
			}
		});
	}

	@SuppressWarnings("unused")
	private void setUpGraphCollors() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Set<Node> lineNode = refChart.lookupAll(".series0");
				for (final Node line : lineNode) {
					line.setStyle("-fx-stroke: #1FE561; -fx-stroke-width: 2px;");
				}
				Set<Node> lineNodeUpperLimit = refChart.lookupAll(".series1");
				for (final Node line : lineNodeUpperLimit) {
					line.setStyle("-fx-stroke: #FF0000; -fx-stroke-width: 1px;");
				}
				Set<Node> lineNodeLowerLimit = refChart.lookupAll(".series2");
				for (final Node line : lineNodeLowerLimit) {
					line.setStyle("-fx-stroke: #FF0000; -fx-stroke-width: 1px;");
				}
				xAxis.setForceZeroInRange(false);
				yAxis.setForceZeroInRange(false);
			}
		});

	}

	public void addNewData(double data) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				refObsList.add(new XYChart.Data<Integer, Double>(eixoXSamples, data));
			}
		});

	}

	/**
	 * Function returns the valueProperty of attribute refSeries
	 * 
	 * @return the refSeries
	 */
	public Series<Integer, Double> getRefSeries() {
		return refSeries;
	}

	/**
	 * Function returns the valueProperty of attribute valueProperty
	 * 
	 * @return the valueProperty
	 */
	public SimpleDoubleProperty getValueProperty() {
		return valueProperty;
	}

	/**
	 * Function sets the valueProperty for attribute valueProperty
	 * 
	 * @param valueProperty
	 *            the valueProperty to set
	 */
	public void setValueProperty(SimpleDoubleProperty value) {
		this.valueProperty = value;
	}

	public double getValue() {
		return this.valueProperty.get();
	}

	public void setValue(double value) {
		this.valueProperty.set(value);
	}

	/**
	 * Function returns the valueProperty of attribute refObsList
	 * 
	 * @return the refObsList
	 */
	public ObservableList<XYChart.Data<Integer, Double>> getRefObsList() {
		return refObsList;
	}

	/**
	 * Function returns the value of attribute xAxis
	 * 
	 * @return the xAxis
	 */
	public NumberAxis getxAxis() {
		return xAxis;
	}

	/**
	 * Function sets the value for attribute xAxis
	 * 
	 * @param xAxis
	 *            the xAxis to set
	 */
	public void setxAxis(NumberAxis xAxis) {
		this.xAxis = xAxis;
	}

	/**
	 * Function returns the value of attribute yAxis
	 * 
	 * @return the yAxis
	 */
	public NumberAxis getyAxis() {
		return yAxis;
	}

	/**
	 * Function sets the value for attribute yAxis
	 * 
	 * @param yAxis
	 *            the yAxis to set
	 */
	public void setyAxis(NumberAxis yAxis) {
		this.yAxis = yAxis;
	}

	/**
	 * Function sets the valueProperty for attribute refObsList
	 * 
	 * @param refObsList
	 *            the refObsList to set
	 */
	public void setRefObsList(ObservableList<XYChart.Data<Integer, Double>> refObsList) {
		this.refObsList = refObsList;
	}

	/**
	 * Function returns the valueProperty of attribute upperLimitObsList
	 * 
	 * @return the upperLimitObsList
	 */
	public ObservableList<XYChart.Data<Integer, Double>> getUpperLimitObsList() {
		return upperLimitObsList;
	}

	/**
	 * Function sets the valueProperty for attribute upperLimitObsList
	 * 
	 * @param upperLimitObsList
	 *            the upperLimitObsList to set
	 */
	public void setUpperLimitObsList(ObservableList<XYChart.Data<Integer, Double>> upperLimitObsList) {
		this.upperLimitObsList = upperLimitObsList;
	}

	/**
	 * Function returns the valueProperty of attribute lowerLimitObsList
	 * 
	 * @return the lowerLimitObsList
	 */
	public ObservableList<XYChart.Data<Integer, Double>> getLowerLimitObsList() {
		return lowerLimitObsList;
	}

	/**
	 * Function sets the valueProperty for attribute lowerLimitObsList
	 * 
	 * @param lowerLimitObsList
	 *            the lowerLimitObsList to set
	 */
	public void setLowerLimitObsList(ObservableList<XYChart.Data<Integer, Double>> lowerLimitObsList) {
		this.lowerLimitObsList = lowerLimitObsList;
	}

	/**
	 * Function sets the valueProperty for attribute refSeries
	 * 
	 * @param refSeries
	 *            the refSeries to set
	 */
	public void setRefSeries(Series<Integer, Double> refSeries) {
		this.refSeries = refSeries;
	}

	/**
	 * Function returns the valueProperty of attribute upperLimitSeries
	 * 
	 * @return the upperLimitSeries
	 */
	public Series<Integer, Double> getUpperLimitSeries() {
		return upperLimitSeries;
	}

	/**
	 * Function sets the valueProperty for attribute upperLimitSeries
	 * 
	 * @param upperLimitSeries
	 *            the upperLimitSeries to set
	 */
	public void setUpperLimitSeries(Series<Integer, Double> upperLimitSeries) {
		this.upperLimitSeries = upperLimitSeries;
	}

	/**
	 * Function returns the valueProperty of attribute lowerLimitSeries
	 * 
	 * @return the lowerLimitSeries
	 */
	public Series<Integer, Double> getLowerLimitSeries() {
		return lowerLimitSeries;
	}

	/**
	 * Function sets the valueProperty for attribute lowerLimitSeries
	 * 
	 * @param lowerLimitSeries
	 *            the lowerLimitSeries to set
	 */
	public void setLowerLimitSeries(Series<Integer, Double> lowerLimitSeries) {
		this.lowerLimitSeries = lowerLimitSeries;
	}

	/**
	 * Function returns the valueProperty of attribute refChart
	 * 
	 * @return the refChart
	 */
	public LineChart<Integer, Double> getRefChart() {
		return refChart;
	}

	/**
	 * Function sets the valueProperty for attribute refChart
	 * 
	 * @param refChart
	 *            the refChart to set
	 */
	public void setRefChart(LineChart<Integer, Double> refChart) {
		this.refChart = refChart;
	}

	/**
	 * Function returns the value of attribute eixoXSamples
	 * 
	 * @return the eixoXSamples
	 */
	public int getEixoXSamples() {
		return eixoXSamples;
	}

	/**
	 * Function sets the value for attribute eixoXSamples
	 * 
	 * @param eixoXSamples
	 *            the eixoXSamples to set
	 */
	public void setEixoXSamples(int eixoXSamples) {
		this.eixoXSamples = eixoXSamples;
	}

	/**
	 * Function returns the value of attribute yUpper
	 * 
	 * @return the yUpper
	 */
	public double getyUpper() {
		return yUpper;
	}

	/**
	 * Function sets the value for attribute yUpper
	 * 
	 * @param yUpper
	 *            the yUpper to set
	 */
	public void setyUpper(double yUpper) {
		this.yUpper = yUpper;
	}

	/**
	 * Function returns the value of attribute yLower
	 * 
	 * @return the yLower
	 */
	public double getyLower() {
		return yLower;
	}

	/**
	 * Function sets the value for attribute yLower
	 * 
	 * @param yLower
	 *            the yLower to set
	 */
	public void setyLower(double yLower) {
		this.yLower = yLower;
	}
}
