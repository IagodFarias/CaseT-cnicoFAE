package util;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ValidateFields {

	private static String styleError = "-fx-border-color: red ;";// TODO - Osmenio - mudar css para as classe generalStyleSheet1.css
	private static String styleOk = "";
	private static String styleRadioButtonError = "-fx-outer-border: red ;";
	private static String styleCheckBoxError = "-fx-outer-border: red ;";
	
	/**
	 * 
	 */
	public static boolean setValidateError(Button button) {
		button.setStyle(styleError);
		return true;
	}

	/**
	 * 
	 */
	public static boolean setValidateError(ChoiceBox choiceBox) {
		choiceBox.setStyle(styleError);
		return true;
	}

	/**
	 * 
	 */
	public static boolean setValidateError(TextField textField) {
		textField.setStyle(styleError);
		return true;
	}

	/**
	 * 
	 */
	public static boolean setValidateError(ComboBox comboBox) {
		comboBox.setStyle(styleError);
		return true;
	}

	/**
	 * 
	 */
	public static boolean setValidateError(CheckBox checkBox) {
		checkBox.setStyle(styleCheckBoxError);
		return true;
	}

	/**
	 * 
	 */
	public static boolean setValidateError(TableView tableView) {
		tableView.setStyle(styleError);
		return true;
	}

	/**
	 * 
	 */
	public static boolean clearValidateError(Button button) {
		button.setStyle(styleOk);
		return true;
	}
	
	/**
	 * 
	 */
	public static boolean clearValidateError(ChoiceBox choiceBox) {
		choiceBox.setStyle(styleOk);
		return true;
	}

	/**
	 * 
	 */
	public static boolean clearValidateError(TextField textField) {
		textField.setStyle(styleOk);
		return true;
	}

	/**
	 * 
	 */
	public static boolean clearValidateError(ComboBox comboBox) {
		comboBox.setStyle(styleOk);
		return true;
	}

	/**
	 * 
	 */
	public static boolean clearValidateError(CheckBox checkBox) {
		checkBox.setStyle(styleOk);
		return true;
	}

	/**
	 * 
	 */
	public static boolean clearValidateError(TableView tableView) {
		tableView.setStyle(styleOk);
		return true;
	}

	/**
	 * 
	 */
	public static boolean isEmpty(ChoiceBox choiceBox) {
		boolean validateError = false;

		if (((String) choiceBox.getValue()) == null) {
			choiceBox.setStyle(styleError);
			validateError = true;
		} else if (((String) choiceBox.getValue()).isEmpty()) {
			choiceBox.setStyle(styleError);
			validateError = true;
		} else if (((String) choiceBox.getValue()).length() == 0) {
			choiceBox.setStyle(styleError);
			validateError = true;
		} else {
			choiceBox.setStyle(styleOk);
		}
		return validateError;
	}

	/**
	 * 
	 */
	public static boolean isEmpty(TextField textField) {
		boolean validateError = false;

		if (textField.getText() == null) {
			textField.setStyle(styleError);
			validateError = true;
		} else if (textField.getText().isEmpty()) {
			textField.setStyle(styleError);
			validateError = true;
		} else if (textField.getText().length() == 0) {
			textField.setStyle(styleError);
			validateError = true;
		} else {
			textField.setStyle(styleOk);
		}
		return validateError;
	}

	/**
	 * 
	 */
	public static boolean isEmpty(ComboBox comboBox) {
		boolean validateError = false;

		if (((Object) comboBox.getValue()) == null) {
			comboBox.setStyle(styleError);
			validateError = true;
		} else {
			comboBox.setStyle(styleOk);
		}
		return validateError;
	}

	/**
	 * 
	 */
	public static boolean isEmpty(Label label) {
		boolean validateError = false;

		if (label.getText().isEmpty()) {
			// label.setStyle(styleError);
			validateError = true;
		} else {
			// label.setStyle(styleOk);
		}
		return validateError;
	}

	/**
	 * 
	 */
	public static boolean ipAddress(final TextField textField) {

		boolean result = false;
		String ipAddress = textField.getText();
		String[] arrayStr = ipAddress.split("\\.");
		if (arrayStr.length == 4) {
			ipAddress = "" + Integer.valueOf(arrayStr[0]) + "." + Integer.valueOf(arrayStr[1]) + "." + Integer.valueOf(arrayStr[2]) + "." + Integer.valueOf(arrayStr[3]);
			textField.setText(ipAddress);

			textField.setStyle(styleOk);
			result = false;
		} else {
			textField.setStyle(styleError);
			result = true;
		}
		return result;
	}
}
