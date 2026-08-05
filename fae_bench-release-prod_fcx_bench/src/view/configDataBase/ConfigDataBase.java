package view.configDataBase;

import java.util.function.Consumer;

import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import si.dbcomm.dao.DataBasePersistence;
import util.PreferencesHandler;

public class ConfigDataBase {

	@FXML
	private BorderPane root;

	@FXML
	private JFXTextField tfUrlDataBase;
	
	@FXML
	private JFXTextField tfUserDataBase;
	
	@FXML
	private JFXTextField tfPasswordDataBase;
	
	@FXML
	private Label lbStatusDataBase;
	
	private Consumer<String> lbStatusCallback;


	/**
	 *
	 */
	@FXML
	private void onConfigDataBase(ActionEvent event) {
		String passwordDataBase = "";
		PreferencesHandler.saveDataBaseUrl(getTfUrlDataBase().getText());
		PreferencesHandler.saveDataBaseUserName(getTfUserDataBase().getText());
		if(!getTfPasswordDataBase().getText().equals("")){
			PreferencesHandler.saveDataBasePassword(getTfPasswordDataBase().getText());			
			passwordDataBase = PreferencesHandler.readDataBasePassword();
		}else{
			passwordDataBase = PreferencesHandler.readDataBasePassword();
		}
		String urlDataBase = PreferencesHandler.readDataBaseUrl();
		String userDataBase = PreferencesHandler.readDataBaseUserName();
		boolean sucessfull = DataBasePersistence.initialize(urlDataBase, userDataBase, passwordDataBase);
		if(!sucessfull){
			lbStatusDataBase.setText("Não foi possível conectar com o banco.");
			lbStatusCallback.accept(lbStatusDataBase.getText());
		}else{
			lbStatusDataBase.setText("Banco de dados conectado");
			lbStatusCallback.accept(lbStatusDataBase.getText());
			onClose(event);
		}
	}

	/**
	 *
	 */
	@FXML
	private void onClose(ActionEvent event) {
		
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void initialize() {
		System.out.println("initialize");
	}
	
	public JFXTextField getTfUrlDataBase() {
		return tfUrlDataBase;
	}

	public void setTfUrlDataBase(String tfUrlDataBase) {
		this.tfUrlDataBase.setText(tfUrlDataBase);
	}

	public JFXTextField getTfUserDataBase() {
		return tfUserDataBase;
	}

	public void setTfUserDataBase(String tfUserDataBase) {
		this.tfUserDataBase.setText(tfUserDataBase);
	}

	public JFXTextField getTfPasswordDataBase() {
		return tfPasswordDataBase;
	}

	public void setTfPasswordDataBase(String tfPasswordDataBase) {
		this.tfPasswordDataBase.setText(tfPasswordDataBase);
	}

	/**
	 * @return the lbStatusCallback
	 */
	public Consumer<String> getLbStatusCallback() {
		return lbStatusCallback;
	}

	/**
	 * @param lbStatusCallback the lbStatusCallback to set
	 */
	public void setLbStatusCallback(Consumer<String> lbStatusCallback) {
		this.lbStatusCallback = lbStatusCallback;
	}

}
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////