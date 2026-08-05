package util;

import java.io.File;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChooserBrowser {

	public static String fileOpen(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		// fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Firmware Files", "*.a43"));
		File selectedFile = fileChooser.showOpenDialog(stage.getScene().getWindow());

		String filePath = "";
		if (selectedFile != null) {
			filePath = selectedFile.getPath();
		}
		return filePath;
	}

	public static String directoryOpen(Stage stage) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(stage.getScene().getWindow());

		String filePath = "";
		if (selectedDirectory != null) {
			filePath = selectedDirectory.getPath();
		}
		return filePath;
	}
}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//