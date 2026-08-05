package view.dialog;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import enumerations.UserDialogEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by osmenio on 29/03/2017.
 */
public class Dialog implements Initializable {

	@FXML
	private BorderPane root;

	@FXML
	private ImageView ivIcon;

	@FXML
	private Label lbTitle;

	@FXML
	private Label lbContentText;

	@FXML
	private Button btConfirm;

	@FXML
	private Button btCancel;

	private Consumer<String> onPositiveListener;

	private Consumer<String> onNegativeListener;

	/**
	 *
	 */
	private void closeDialog() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	/**
	 *
	 */
	public void setDialogType(UserDialogEnum type) {

		switch (type) {

			case POSITIVE_TYPE:
				ivIcon.setImage(new Image("/view/images/icon_check_50.png"));
			break;

			case NEGATIVE_TYPE:
				ivIcon.setImage(new Image("/view/images/icon_uncheck_50.png"));
			break;

			case NEUTRAL_TYPE:
				ivIcon.setImage(new Image("/view/images/icon_caution_50.png"));
			break;
		}
	}

	/**
	 *
	 */
	public void setOnPositiveListener(Consumer<String> listener) {
		this.onPositiveListener = listener;
	}

	/**
	 *
	 */
	public void setOnNegativeListener(Consumer<String> listener) {
		this.onNegativeListener = listener;
		btCancel.setVisible(true);
	}

	/**
	 *
	 */
	public void setContentText(String text) {
		lbContentText.setText(text);
	}

	/**
	 *
	 */
	public void setTitle(String title) {
		lbTitle.setText(title);
	}

	/**
	 *
	 */
	@FXML
	private void onClose(ActionEvent event) {
		closeDialog();
	}

	/**
	 *
	 */
	@FXML
	private void onConfirm(ActionEvent event) {
		if (onPositiveListener != null) {
			onPositiveListener.accept("");
		}
		closeDialog();
	}

	/**
	 *
	 */
	@FXML
	private void onCancel(ActionEvent event) {
		if (onNegativeListener != null) {
			onNegativeListener.accept("");
		}
		closeDialog();
	}

	/**
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btCancel.setVisible(false);
	}
}
