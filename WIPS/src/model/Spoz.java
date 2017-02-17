package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class Spoz extends Application{

	/**
	 * primary stag
	 */
	public static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
	    Spoz.primaryStage = primaryStage;
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/session/userlogin.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/view/session/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Log in");
			//primaryStage.setResizable(false);
			primaryStage.show();
			root.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Spoz() {
	}
	
	/**
	 * @param args String
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
