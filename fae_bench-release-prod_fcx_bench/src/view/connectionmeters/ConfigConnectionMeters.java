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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.ConversorModel;
import si.dbcomm.service.ConversorService;
import si.dbcomm.util.ConversorNumbers;

/**
 * @author marcos
 *
 */
public class ConfigConnectionMeters {

	@FXML
	private BorderPane root;

	// C
	@FXML
	private CheckBox cbPos01;

	@FXML
	private CheckBox cbPos02;

	@FXML
	private CheckBox cbPos03;

	@FXML
	private CheckBox cbPos04;

	@FXML
	private CheckBox cbPos05;

	@FXML
	private CheckBox cbPos06;

	@FXML
	private CheckBox cbPos07;

	@FXML
	private CheckBox cbPos08;

	@FXML
	private CheckBox cbPos09;

	@FXML
	private CheckBox cbPos10;

	@FXML
	private CheckBox cbPos11;

	@FXML
	private CheckBox cbPos12;

	@FXML
	private CheckBox cbPos13;

	@FXML
	private CheckBox cbPos14;

	@FXML
	private CheckBox cbPos15;

	@FXML
	private CheckBox cbPos16;

	@FXML
	private CheckBox cbPos17;

	@FXML
	private CheckBox cbPos18;

	@FXML
	private CheckBox cbPos19;

	@FXML
	private CheckBox cbPos20;

	ConversorService conversorService = new ConversorService();

	/**
	*
	*/
	private void updateView() {
		cbPos01.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR1).isEnabled());
		cbPos02.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR2).isEnabled());
		cbPos03.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR3).isEnabled());
		cbPos04.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR4).isEnabled());
		cbPos05.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR5).isEnabled());
		cbPos06.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR6).isEnabled());
		cbPos07.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR7).isEnabled());
		cbPos08.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR8).isEnabled());
		cbPos09.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR9).isEnabled());
		cbPos10.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR10).isEnabled());
		cbPos11.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR11).isEnabled());
		cbPos12.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR12).isEnabled());
		cbPos13.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR13).isEnabled());
		cbPos14.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR14).isEnabled());
		cbPos15.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR15).isEnabled());
		cbPos16.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR16).isEnabled());
		cbPos17.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR17).isEnabled());
		cbPos18.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR18).isEnabled());
		cbPos19.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR19).isEnabled());
		cbPos20.setSelected(conversorService.findByName(ConversorNumbers.CONVERSOR20).isEnabled());
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
	private void onActionSaveButton(ActionEvent event) {
		try {
			// CONVERSOR1 - CONVERSOR5
			updateConversorIfExists(ConversorNumbers.CONVERSOR1, cbPos01.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR2, cbPos02.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR3, cbPos03.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR4, cbPos04.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR5, cbPos05.isSelected());
			
			// CONVERSOR6 - CONVERSOR10
			updateConversorIfExists(ConversorNumbers.CONVERSOR6, cbPos06.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR7, cbPos07.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR8, cbPos08.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR9, cbPos09.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR10, cbPos10.isSelected());
			
			// CONVERSOR11 - CONVERSOR15
			updateConversorIfExists(ConversorNumbers.CONVERSOR11, cbPos11.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR12, cbPos12.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR13, cbPos13.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR14, cbPos14.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR15, cbPos15.isSelected());
			
			// CONVERSOR16 - CONVERSOR20
			updateConversorIfExists(ConversorNumbers.CONVERSOR16, cbPos16.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR17, cbPos17.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR18, cbPos18.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR19, cbPos19.isSelected());
			updateConversorIfExists(ConversorNumbers.CONVERSOR20, cbPos20.isSelected());
			
			// Call onClose after successful update
			onClose();
		} catch (Exception e) {
			System.err.println("Error saving conversor settings: " + e.getMessage());
			// Don't close the dialog if there was an error
		}
	}

	/**
	 * Helper method to safely update a conversor if it exists
	 */
	private void updateConversorIfExists(ConversorNumbers conversorNumber, boolean enabled) {
		try {
			ConversorModel conversorModel = conversorService.findByName(conversorNumber);
			if (conversorModel != null && conversorModel.getId() != null) {
				conversorModel.setEnabled(enabled);
				conversorService.update(conversorModel);
			} else {
				System.out.println("Skipping update for " + conversorNumber + " - not found or no ID");
			}
		} catch (Exception e) {
			System.err.println("Error updating " + conversorNumber + ": " + e.getMessage());
		}
	}

	/**
	 * Inicializa a classe controller. Este m�todo � chamado automaticamente ap�s o arquivo fxml ter sido carregado.
	 */
	@FXML
	private void initialize() {
		updateView();
	}
}
