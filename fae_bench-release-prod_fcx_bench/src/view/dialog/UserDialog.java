package view.dialog;

import java.io.IOException;
import java.util.function.Consumer;

import enumerations.UserDialogEnum;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/****
 * 
 * @author Administrador
 */
public class UserDialog {

	public static int POSITIVE_TYPE = 0;
	public static int NEGATIVE_TYPE = 1;
	public static int NEUTRAL_TYPE = 2;

	private static double xOffset = 0;
	private static double yOffset = 0;

	private FXMLLoader loader;
	private Parent root;
	private String title = "Alert !";
	private String contentText = "Content text";

	private Consumer<String> onPositiveListener;

	private Consumer<String> onNegativeListener;

	private UserDialogEnum dialogType = UserDialogEnum.POSITIVE_TYPE;

	//
	private Stage stage;

	/**
	 * @throws IOException
	 *
	 */
	public UserDialog() throws IOException {
		loader = new FXMLLoader(getClass().getResource("/view/dialog/Dialog.fxml"));
		root = loader.load();
	}

	/**
	 *
	 */
	public void setDialogType(UserDialogEnum type) {
		this.dialogType = type;
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
	}

	/**
	 *
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 *
	 */
	public void setContentText(String text) {
		this.contentText = text;
	}

	/**
	 *
	 */
	public void close() {
		stage.close();
	}

	/**
	 *
	 */
	public void show() {

		Scene scene = new Scene(root);
		// Stage stage = new Stage();
		stage = new Stage();

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				xOffset = stage.getX() - mouseEvent.getScreenX();
				yOffset = stage.getY() - mouseEvent.getScreenY();
			}
		});

		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.setX(event.getScreenX() + xOffset);
				stage.setY(event.getScreenY() + yOffset);
			}
		});

		// Controller
		Dialog controller = (Dialog) loader.getController();
		controller.setTitle(title);
		controller.setContentText(contentText);
		controller.setDialogType(dialogType);
		if (onPositiveListener != null) {
			controller.setOnPositiveListener(onPositiveListener);
		}
		if (onNegativeListener != null) {
			controller.setOnNegativeListener(onNegativeListener);
		}

		// scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.setTitle(title);
		// stage.getIcons().add(new Image("/view/images/fae/icon_logo_fae1.png"));
		stage.getIcons().add(new Image("/view/images/icon_logo_fae.png"));
		stage.setResizable(false);
		// stage.initStyle(StageStyle.TRANSPARENT);
		// stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
	}
}
