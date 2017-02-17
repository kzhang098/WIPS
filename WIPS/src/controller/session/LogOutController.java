package controller.session;

import java.io.IOException;

import model.Wips;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class LogOutController {
	
	/**
	 * This method will take the user back to the login screen.
	 */
	public static void logInScreen() {
		//opens Window
		Wips.getInstance().setCurrentUser(null);
		Wips.getInstance().setRoleOfCurrentUser(null);
		Wips.getInstance().getUndoUsers().clear();
		Wips.getInstance().getUndoWorkFlowStack().clear();
		serialize();
		//Goes back to the login screen
	}
	
	/**
	 * This  method will serialize the wips class before logout. Internally, it will call the login screen. 
	 */
	private static void serialize() {
		try {
			Wips.getInstance().make();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
