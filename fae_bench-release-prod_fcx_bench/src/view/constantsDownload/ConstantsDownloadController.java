//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @fileConsts ConstantsDownloadController.java
*    @author marcos
*    @date 31 de mar de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.constantsDownload;

import java.io.File;

import controller.ConversorController;
import controller.MeterController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import si.dbcomm.model.ConversorModel;
import si.dbcomm.model.MeterModel;
import util.MatFileHandlerUtil;

/**
 * @author marcos
 *
 */
public class ConstantsDownloadController {

	@FXML
    private Button btnChooseCaliLutFile;
	
	@FXML
	private Button btnChooseConfigFile;
	
	@FXML
	private Button btnLoad;
	
	@FXML
	private TextField textFieldChoosenConstsFile;
	
	@FXML
	private TextField textFieldConfigFile;
	
	@FXML
	private ComboBox<String> comboBoxPosicaoMedidor;
	
	private MeterController meterController;
	
	private ConversorController conversorController;
	
	private File fileConsts;
	
	private File fileConfig;
	
	@FXML 
	public void initialize() { 
		conversorController = new ConversorController();
		for(ConversorModel conversor : conversorController.getAllConversors()){
			comboBoxPosicaoMedidor.getItems().add(conversor.getIp());
		}
		
	}
	
	@FXML
	public void btnLoadConstFileAction(){
		FileChooser chooser = new FileChooser();
	    chooser.setTitle("Carregar Arquivo de constantes");
	    fileConsts = chooser.showOpenDialog(new Stage());	
	    if(fileConsts.exists()){
	    	textFieldChoosenConstsFile.setText(fileConsts.getAbsolutePath());
		}else{
			Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Arquivo Inválido!");
            alert.setContentText("Selecione um Arquivo .mat com tabela de calibração.");
            alert.showAndWait();
		}
	    
	}
	
	@FXML
	public void btnChooseConfigFileAction(){
		FileChooser chooser = new FileChooser();
	    chooser.setTitle("Carregar Arquivo de configuração");
	    fileConfig = chooser.showOpenDialog(new Stage());	
	    if(fileConfig.exists()){
	    	textFieldConfigFile.setText(fileConfig.getAbsolutePath());
		}else{
			Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Arquivo Inválido!");
            alert.setContentText("Selecione um Arquivo .mat com dados de configuração do medidor.");
            alert.showAndWait();
		}
	}
	
	@FXML
	public void btnLoadAction(){
		if(fileConsts != null){
			MatFileHandlerUtil matFile = new MatFileHandlerUtil();
			meterController.setMeter(matFile.retrieveConfigFromMatFile(fileConfig.getAbsolutePath()));
			meterController.setCalibConstants(matFile.retrieveConstantsFromMatFile(fileConsts.getAbsolutePath()));
			
			if(this.meterController.sendNonCalcCalibrationData()){
				 Alert alert = new Alert(AlertType.INFORMATION);
	             alert.setTitle("Sucesso");
	             alert.setHeaderText("Dados enviados para medidor na posição com sucesso!");
	             alert.showAndWait();
//	             meterController.stopReading();
	             meterController.disconnectComm();
			}else{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Erro");
				alert.setHeaderText("Erro ao enviar dados para o medidor na posição" );
				alert.showAndWait();
				
			}
			
		}else{
			 Alert alert = new Alert(AlertType.ERROR);
             alert.setTitle("Erro");
             alert.setHeaderText("Arquivo não selecionado!");
             alert.setContentText("Selecione um Arquivo .mat com tabela de calibração para iniciar");
             alert.showAndWait();
		}
	}
	
	
	@FXML
	public void comboBoxSelectedItem(){
				
		meterController = new MeterController();
		MeterModel meter = new MeterModel();
		ConversorModel conversor = conversorController.getConversorFormDbByIp(comboBoxPosicaoMedidor.getSelectionModel().getSelectedItem());
		if(conversor != null){
			meter.setConversor(conversor);
			meterController.setMeter(meter);
			if(meterController.initiateComm()){
				if(meterController.verifiConnection2()){
					meterController.startReading();
				}else{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erro");
					alert.setHeaderText("Não foi possivel verificar medidor");
					alert.setContentText("Não foi possivel verificar a comunicaçpão com o medidor na posição "+comboBoxPosicaoMedidor.getSelectionModel().getSelectedItem());
					alert.showAndWait();
				}
			}else{
				 Alert alert = new Alert(AlertType.ERROR);
	             alert.setTitle("Erro");
	             alert.setHeaderText("Não foi possivel conectar");
	             alert.setContentText("Não foi possivel conectar ao medidor na posição "+comboBoxPosicaoMedidor.getSelectionModel().getSelectedItem());
	             alert.showAndWait();
			}
		}
		
	}
	
}
