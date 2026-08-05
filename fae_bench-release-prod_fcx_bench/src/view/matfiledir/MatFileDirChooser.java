package view.matfiledir;

import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.ChooserBrowser;

public class MatFileDirChooser {

	@FXML
	private BorderPane root;

	@FXML
	private TextField tfLocalPath;
	
	@FXML
	private TextField tfRemotePath;

	private Consumer<String[]> matFileCallback;

	/**
	 * 
	 */
	public void setFilePath(String fileLocalPath, String fileRemotePath) {
		tfLocalPath.setText(fileLocalPath);
		tfRemotePath.setText(fileRemotePath);
	}

	/**
	 * 
	 */
	public void setCallback(Consumer<String[]> callback) {
		this.matFileCallback = callback;
	}

	/**
	 *
	 */
	@FXML
	private void onChooseLocalPath(ActionEvent event) {
		String file = ChooserBrowser.directoryOpen((Stage) root.getScene().getWindow());
		tfLocalPath.setText(file);
	}
	
	/**
	 *
	 */
	@FXML
	private void onChooseRemotePath(ActionEvent event) {
		String file = ChooserBrowser.directoryOpen((Stage) root.getScene().getWindow());
		tfRemotePath.setText(file);
	}

	// /**
	// *
	// */
	// @FXML
	// private void onCaliConstFileChoose(ActionEvent event) {
	// String file = ChooserBrowser.directoryOpen((Stage) root.getScene().getWindow());
	// tfMatFilePath.setText(file);
	// }

	/**
	 *
	 */
	@FXML
	private void onConfirm(ActionEvent event) {
		if (matFileCallback != null) {

			String[] path = new String[2];
			path[0] = tfLocalPath.getText();
			path[1] = tfRemotePath.getText();
			matFileCallback.accept(path);
		}
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void initialize() {
		System.out.println("initialize");
	}
}