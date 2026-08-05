//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MainStartView.java
*    @author marcos
*    @date 27 de set de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.mainwindow;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.util.WorkIndicatorDialog;

/**
 * @author marcos
 *
 */
public class MainStartView extends Application implements Runnable {

	public static MainDashBoard controller;
//	 FXMLLoader guiLoader = new FXMLLoader(getClass().getResource("MainDashboard2.fxml"));
	FXMLLoader guiLoader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
	static Parent rootNode;
	private boolean flagInit;

	@SuppressWarnings("rawtypes")
	private WorkIndicatorDialog wd = null;

	Thread mainGui;

	/**
	 * Creates a object for class MainStartView.java
	 */
	public MainStartView() {
		mainGui = new Thread(this);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			rootNode = (Parent) guiLoader.load();
//			rootNode.setScaleX(0.8);
//			rootNode.setScaleY(0.8);
//			rootNode.setTranslateX(-100);
//			rootNode.setTranslateY(-100);
			Scene scene = new Scene(rootNode);
			controller = (MainDashBoard) guiLoader.getController();
			// primaryStage.getIcons().add(new Image("File:../../rsrc/images/logo_fae_3.png"));
			primaryStage.getIcons().add(new Image("/view/images/logo_fae_3.png"));
			primaryStage.setTitle("Bancada Fluxus");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void modalInitializa() {
		flagInit = true;
		wd = new WorkIndicatorDialog(null, "Iniciando periféricos e serviços");
		wd.addTaskEndNotification(result -> {
			flagInit = false;
			wd = null;
		});

		wd.exec("123", inputParam -> {
			while (flagInit) {
				try {
					Thread.currentThread().setName("Modal Init Periféricos - Thread");
					Thread.currentThread().sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return new Integer(1);
		});
	}

	/**
	 * Function returns the value of attribute controller
	 * 
	 * @return the controller
	 */
	public MainDashBoard getController() {
		return controller;
	}

	/**
	 * Function sets the value for attribute controller
	 * 
	 * @param controller
	 *            the controller to set
	 */
	public void setController(MainDashBoard controller) {
		MainStartView.controller = controller;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Thread.currentThread().setName("MainStartView - Thread");
		MainStartView.launch();
	}

	public void startView() {
		mainGui.start();
	}
}
