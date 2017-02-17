package helper;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class Pops {
	private Pops(){}
	
	public static Alert pop(Alert.AlertType c ,String message, String title) {
		Alert alertBox = new Alert(c, message, ButtonType.OK, ButtonType.CANCEL);
		alertBox.setTitle(title);
		alertBox.showAndWait();
		return alertBox;
	}
	
	public static void openScreen(String name, ActionEvent e, String title, Parent p) throws IOException {
		Scene s = new Scene(p);
	//	s.getStylesheets().add(PhotoAlbum.class.getResource("application.css").toExternalForm());;
		Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
		st.setScene(s);
		st.setTitle(title);
		st.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				if (name.equalsIgnoreCase("useralbums.fxml")) {
				} else if (name.equalsIgnoreCase("adminscreen.fxml")) {
				}
			}
		});
		st.setResizable(false);
		st.show();
	}
}

