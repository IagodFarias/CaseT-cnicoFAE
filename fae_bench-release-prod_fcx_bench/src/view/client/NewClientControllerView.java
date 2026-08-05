//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file NewFlowRateControllerView.java
*    @author osmenio
*    @date 13 de mar de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.client;

import java.util.function.Consumer;

import enumerations.UFEnum;
import exceptions.NoCheckedException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.ClientModel;
import si.dbcomm.service.ClientService;
import util.MaskTextField;
import view.ComboBoxChoice;

/**
 * @author marcos
 *
 */
public class NewClientControllerView {
	// Style Ok/Error
	private static String styleError = "-fx-border-color: red ; -fx-border-width: 1px ;";
	
	private static String styleOk = "";

	@FXML
	private BorderPane root;

	@FXML
	private TextField textfieldFantasyName;

	@FXML
	private TextField textFieldSocialName;

	@FXML
	private TextField textfieldStateRegistration;

	@FXML
	private TextField textfieldEmail;

	@FXML
	private TextField textfieldCnpj;

	@FXML
	private TextField textfieldAddress;

	@FXML
	private TextField textfieldDistrict;

	@FXML
	private TextField textfieldCity;

	@FXML
	private ComboBox<ComboBoxChoice> comboBoxUF;

	@FXML
	private TextField textfieldCep;

	@FXML
	private TextField textfieldBusinessContact;

	@FXML
	private TextField textfieldTel;

	@FXML
	private TextField textfieldCel;

	@FXML
	private Label labelRequiredFields;

	@FXML
	private Button btnCancel;
	@FXML
	private Button btnSalvar;

	ClientService clientService;
	
	private ClientModel editClient = null;

	private boolean isEditClient = false;

	private Consumer<String> flowRateSelectCallback = null;

	/**
	 * 
	 * 
	 */
	public void setCallback(Consumer<String> callback) {
		this.flowRateSelectCallback = callback;
	}

	/**
	 * 
	 * 
	 */
	private void onCloseStage() {
		if (flowRateSelectCallback != null) {
			flowRateSelectCallback.accept("");
		}
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	/**
		 * 
		 */
	public void setBatch(ClientModel client) {
		editClient = client;
		isEditClient = true;

		// Update view
		textfieldFantasyName.setText(client.getNomeFantasia());
		textFieldSocialName.setText(client.getRazaoSocial());
		textfieldStateRegistration.setText(client.getInscricaoEstadual());
		textfieldCnpj.setText(client.getCnpj());
		textfieldAddress.setText(client.getLogradouro());
		textfieldDistrict.setText(client.getBairro());
		textfieldCity.setText(client.getCidade());
		textfieldCep.setText(client.getCep());
		textfieldBusinessContact.setText(client.getContato());
		textfieldEmail.setText(client.getEmail());
		textfieldTel.setText(client.getTelefone());
		textfieldCel.setText(client.getCelular());

		// UF
		comboBoxUF.getSelectionModel().select(UFEnum.getEnum(client.getUf()).getValue());
		comboBoxUF.getSelectionModel().selectFirst();
		comboBoxUF.setValue(new ComboBoxChoice(UFEnum.getEnum(client.getUf()).getValue(), UFEnum.getEnum(client.getUf()).getName()));

		btnSalvar.setText("Atualizar");
	}

	/**
	 * 
	 */
	private StringConverter<ComboBoxChoice> stringConverter = new StringConverter<ComboBoxChoice>() {
		@Override
		public String toString(ComboBoxChoice object) {
			return object.getName();
		}

		@Override
		public ComboBoxChoice fromString(String string) {
			return null;
		}
	};

	/**
	 * 
	 * 
	 */
	private ClientModel validateAllField() throws NoCheckedException {

		int checkedItem = 0;

		ClientModel clientModel = new ClientModel();

		// Fantasy Name
		if (textfieldFantasyName.getText().length() == 0) {
			textfieldFantasyName.setStyle(styleError);
			textfieldFantasyName.setFocusTraversable(true);
			textfieldFantasyName.requestFocus();

			checkedItem++;
		} else {
			clientModel.setNomeFantasia(textfieldFantasyName.getText());
			textfieldFantasyName.setStyle(styleOk);
		}

		// Social Name
		if (textFieldSocialName.getText().length() == 0) {
			clientModel.setRazaoSocial("");
		} else {
			clientModel.setRazaoSocial(textFieldSocialName.getText());
		}

		// State Registration
		if (textfieldStateRegistration.getText().length() == 0) {
			textfieldStateRegistration.setStyle(styleError);
			textfieldStateRegistration.setFocusTraversable(true);
			textfieldStateRegistration.requestFocus();

			checkedItem++;
		} else {
			clientModel.setInscricaoEstadual(textfieldStateRegistration.getText());
			textfieldStateRegistration.setStyle(styleOk);
		}

		// CNPJ
		if (textfieldCnpj.getText().length() == 0) {
			textfieldCnpj.setStyle(styleError);
			textfieldCnpj.setFocusTraversable(true);
			textfieldCnpj.requestFocus();

			checkedItem++;
		} else {
			clientModel.setCnpj(textfieldCnpj.getText());
			textfieldCnpj.setStyle(styleOk);
		}

		// Address
		if (textfieldAddress.getText().length() == 0) {
			textfieldAddress.setStyle(styleError);
			textfieldAddress.setFocusTraversable(true);
			textfieldAddress.requestFocus();

			checkedItem++;
		} else {
			clientModel.setLogradouro(textfieldAddress.getText());
			textfieldAddress.setStyle(styleOk);
		}

		// District
		if (textfieldDistrict.getText().length() == 0) {
			textfieldDistrict.setStyle(styleError);
			textfieldDistrict.setFocusTraversable(true);
			textfieldDistrict.requestFocus();

			checkedItem++;
		} else {
			clientModel.setBairro(textfieldDistrict.getText());
			textfieldDistrict.setStyle(styleOk);
		}

		// City
		if (textfieldCity.getText().length() == 0) {
			textfieldCity.setStyle(styleError);
			textfieldCity.setFocusTraversable(true);
			textfieldCity.requestFocus();

			checkedItem++;
		} else {
			clientModel.setCidade(textfieldCity.getText());
			textfieldCity.setStyle(styleOk);
		}

		// UF
		if (comboBoxUF.getSelectionModel().isEmpty()) {
			comboBoxUF.setStyle(styleError);
			comboBoxUF.setFocusTraversable(true);
			comboBoxUF.requestFocus();

			checkedItem++;
		} else {
			clientModel.setUf(UFEnum.getEnum((int) comboBoxUF.getSelectionModel().getSelectedItem().getId()).getName());
			comboBoxUF.setStyle(styleOk);
		}

		// CEP
		if (textfieldCep.getText().length() == 0) {
			clientModel.setCep("");
			// textfieldCep.setStyle(styleError);
			// textfieldCep.setFocusTraversable(true);
			// textfieldCep.requestFocus();
			//
			// checkedItem++;
		} else {
			clientModel.setCep(textfieldCep.getText());
			textfieldCep.setStyle(styleOk);
		}

		// Business Contact
		if (textfieldBusinessContact.getText().length() == 0) {
			clientModel.setContato("");
		} else {
			clientModel.setContato(textfieldBusinessContact.getText());
		}

		// Email
		if (textfieldEmail.getText().length() == 0) {
			clientModel.setEmail("");
		} else {
			clientModel.setEmail(textfieldEmail.getText());
		}

		// Tel
		if (textfieldTel.getText().length() == 0) {
			clientModel.setTelefone("");
		} else {
			clientModel.setTelefone(textfieldTel.getText());
		}

		// Cel
		if (textfieldCel.getText().length() == 0) {
			clientModel.setCelular("");
		} else {
			clientModel.setCelular(textfieldCel.getText());
		}

		// Check
		if (checkedItem != 0) {
			throw new NoCheckedException("Some field is empty");
		}

		return clientModel;
	}

	/**
	 * 
	 *
	 */
	@SuppressWarnings("unused")
	private void setNumDigits(TextField textField, int length) {

		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > oldValue.length()) {
					// Check if the new character is greater than LIMIT
					if (textField.getText().length() >= length) {
						textField.setText(textField.getText().substring(0, length));
					}
				}
			}
		});
	}

	/**
	 * 
	 * 
	 */
	private void loadClient() {

		MaskTextField.cnpjField(textfieldCnpj);
		MaskTextField.cepField(textfieldCep);
		MaskTextField.phoneField(textfieldTel);
		MaskTextField.cellPhoneField(textfieldCel);

		ObservableList<ComboBoxChoice> dataUF = FXCollections.observableArrayList();
		for (int i = 0; i < UFEnum.toArrayString().length; i++) {
			dataUF.add(new ComboBoxChoice(i, UFEnum.toArrayString()[i]));
		}
		comboBoxUF.setItems(dataUF);
		comboBoxUF.setConverter(stringConverter);
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onActionSalvar() {
		try {
			ClientModel clientModel = validateAllField();

			if (isEditClient) {
				clientModel.setId(editClient.getId());
				clientService.update(clientModel);
			} else {
				clientService.persist(clientModel);
			}
			onCloseStage();
		} catch (NoCheckedException ex) {
			//TODO - Avaliado - precisamos entender se a melhor abordgem para este tratamento é utilizar um catch com bloco vazio 
		} catch (CrudDatabaseException ex) {
			labelRequiredFields.setVisible(true);
			labelRequiredFields.setText(ex.getMessage());
		}
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onActionCancelButton(ActionEvent event) {
		onCloseStage();
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onFieldFocusValidadeNumeric(KeyEvent event) {
		TextField tx = (TextField) event.getSource();
		if (tx != null) {
			String newValue = tx.getText();
			if (!newValue.matches("\\d*")) {
				tx.setText(newValue.replaceAll("[^\\d]", ""));
			}
		}
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void initialize() {
		clientService = new ClientService();

		labelRequiredFields.setVisible(false);
		loadClient();
	}
}
