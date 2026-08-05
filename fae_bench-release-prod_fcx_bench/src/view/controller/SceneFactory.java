//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file SceneFactory.java
*    @author osmenio
*    @date 27 de mar de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.controller;

import java.io.IOException;

import enumerations.SceneTypeEnum;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by osmenio on 20/03/2017.
 */
public class SceneFactory {

	private FXMLLoader loader;
	// private Scene scene;
	private Parent root;
	private String title = "";

	public SceneFactory(SceneTypeEnum type) throws IOException {

		switch (type) {
			case SELECT_BATCH:
				loader = new FXMLLoader(getClass().getResource("/view/batch/SelectBatch.fxml"));
				root = loader.load();
			break;

			case LOAD_BATCH:
				loader = new FXMLLoader(getClass().getResource("/view/batch/LoadBatch.fxml"));
				root = loader.load();
			break;

			case NEW_BATCH:
				loader = new FXMLLoader(getClass().getResource("/view/batch/NewBatch.fxml"));
				root = loader.load();
			break;

			case LOAD_FLOW_RATE:
				loader = new FXMLLoader(getClass().getResource("/view/flowrate/LoadFlowRate.fxml"));
				root = loader.load();
			break;

			case NEW_FLOW_RATE:
				loader = new FXMLLoader(getClass().getResource("/view/flowrate/NewFlowRate.fxml"));
				root = loader.load();
			break;

			case SELECT_FLOW_RATE:
				loader = new FXMLLoader(getClass().getResource("/view/flowrate/SelectFlowRate.fxml"));
				root = loader.load();
			break;

			case ADD_FLOW_RATE:
				loader = new FXMLLoader(getClass().getResource("/view/flowrate/AddFlowRate.fxml"));
				root = loader.load();
			break;

			case LOAD_PROCESS_CONFIG:
				loader = new FXMLLoader(getClass().getResource("/view/processconfig/LoadProcessConfig.fxml"));
				root = loader.load();
			break;

			case NEW_PROCESS_CONFIG:
				loader = new FXMLLoader(getClass().getResource("/view/processconfig/NewProcessConfig.fxml"));
				root = loader.load();
			break;

			case LOAD_CLIENT:
				loader = new FXMLLoader(getClass().getResource("/view/client/LoadClient.fxml"));
				root = loader.load();
			break;

			case NEW_CLIENT:
				loader = new FXMLLoader(getClass().getResource("/view/client/NewClient.fxml"));
				root = loader.load();
			break;

			case CONSTANTS_DOWNLOAD:
				loader = new FXMLLoader(getClass().getResource("/view/constantsDownload/ConstantsDownloadView.fxml"));
				root = loader.load();
			break;

			case LOAD_METER_TYPE:
				loader = new FXMLLoader(getClass().getResource("/view/metertype/LoadMeterType.fxml"));
				root = loader.load();
			break;

			case NEW_METER_TYPE:
				loader = new FXMLLoader(getClass().getResource("/view/metertype/NewMeterType.fxml"));
				root = loader.load();
			break;

			//
			case CONFIG_CONNECTION_METERS:
				loader = new FXMLLoader(getClass().getResource("/view/connectionmeters/ConfigConnectionMeters.fxml"));
				root = loader.load();
			break;

			case CONNECT_SINGLE_METER:
				loader = new FXMLLoader(getClass().getResource("/view/connectionmeters/ConnectSingleMeter.fxml"));
				root = loader.load();
			break;
			
			case DATABASE_SETTINGS:
				loader = new FXMLLoader(getClass().getResource("/view/configDataBase/ConfigDataBase.fxml"));
				root = loader.load();
			break;

			//
			case SHOW_BATELADA_RESULT:
				loader = new FXMLLoader(getClass().getResource("/view/batelada/ShowBateladaResult.fxml"));
				root = loader.load();
			break;
			
			//
			case MATFILE_DIR:
				loader = new FXMLLoader(getClass().getResource("/view/matfiledir/MatFileDirChooser.fxml"));
				root = loader.load();
				break;

			default:
			break;
		}
	}

	/**
	 * @throws IOException
	 *
	 */
	public void show() throws IOException {

		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle(title);
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
		// stage.showAndWait();
	}

	/**
	 * @throws IOException
	 *
	 */
	public void showModal() throws IOException {

		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle(title);
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
		// stage.showAndWait();
	}

	/**
	 *
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param <T>
	 *
	 */
	public <T> T getController() {
		if (loader != null) {
			return loader.getController();
		}
		return null;
	}

	/**
	 *
	 */
	public FXMLLoader getLoader() {
		return loader;
	}
}
