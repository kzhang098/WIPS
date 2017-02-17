package helper;

import java.io.IOException;

import controller.session.LogOutController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class OpenScreen {

	public OpenScreen() {
		// TODO Auto-generated constructor stub
	}

	public static void openScreen(String ss, ActionEvent e, String title, Parent p, Class classname, String cssFile) throws IOException {
		Scene s = new Scene(p);
		s.getStylesheets().add(classname.getResource(cssFile).toExternalForm());
		Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
		st.setScene(s);
		st.setTitle(title);
		st.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				if (ss.equalsIgnoreCase("dhomescreen.fxml")) {
					//Seralize everything before you close this window
					System.out.println("DEVELOPER HOME SCREEN CLOSED");
					LogOutController.logInScreen();
				} else if (ss.equalsIgnoreCase("ehomescreen.fxml")) {
					//Seralize everything before you close this window
					System.out.println("ENDUSER HOME SCREEN CLOSED");
					LogOutController.logInScreen();
				}
			}
		});
		st.setResizable(true);
		st.show();
	}
}
