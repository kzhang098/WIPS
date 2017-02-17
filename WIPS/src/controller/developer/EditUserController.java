package controller.developer;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import errors.AbsError;
import errors.InputError;
import helper.AutoEmail;
import helper.OpenScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Wips;
import model.user.EndUser;
import model.user.User;
import model.wips.Entity;
import model.wips.WorkFlow;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class EditUserController {

	@FXML
	Button backbtn, deletebtn, addbtn, logoutbtn, savebutton, undobutton;

	@FXML
	ListView<EndUser> allusers, listofallusers;

	@FXML
	TextArea userroles, uservalues, rolestextfield, valuestextfield;

	@FXML
	TabPane tabpane;

	@FXML
	Tab editTab, addTab;

	@FXML
	TextField nametextfield, emailtextfield;

	String tabOpened = "editTab";

	private ObservableList<EndUser> allusersOb;

	@FXML
	protected void initialize() {
		tabListeners();
		showUsers();
		deletebtn.setDisable(true);
		// deleteBtn.setDisable(true);
	}

	public void tabListeners() {
		tabpane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
			if (newTab.equals(editTab)) {

				allusers.getSelectionModel().clearSelection();
				listofallusers.getSelectionModel().clearSelection();
				deletebtn.setDisable(true);
				savebutton.setDisable(false);
				showUsers();
				tabOpened = "editTab";
			} else if (newTab.equals(addTab)) {
				// deletebtn.setDisable(true);
				allusers.getSelectionModel().clearSelection();
				listofallusers.getSelectionModel().clearSelection();
				deletebtn.setDisable(true);
				savebutton.setDisable(true);
				showUsers2();
				tabOpened = "addTab";
			}
		});

	}

	public void showUsers() {
		allusersOb = FXCollections.observableArrayList(Wips.getInstance().getEndUser());
		allusers.setItems(allusersOb);
		allusers.getSelectionModel().selectedItemProperty().addListener((ov, oldVal, newVal) -> {
			deletebtn.setDisable(false);
			if (oldVal != null) {
				oldVal.addTempRoles(userroles.getText());
				oldVal.addTempVals(uservalues.getText());
			}
			showRoles(newVal);
			showValues(newVal);

			// deleteBtn.setDisable(false);
		});
	}

	public void showUsers2() {
		allusersOb = FXCollections.observableArrayList(Wips.getInstance().getEndUser());
		listofallusers.setItems(allusersOb);
		listofallusers.getSelectionModel().selectedItemProperty().addListener((e) -> {
			deletebtn.setDisable(false);
		});
	}

	public void showRoles(EndUser user) {
		userroles.clear();
		StringBuilder sb = new StringBuilder();
		if (user != null) {
			for (Entity e : user.getRoles()) {
				sb.append(e.toString() + ",");
			}
			if (user.getTempRoles().size() != 0) {
				userroles.setText(user.getTempRolesAsString());
			} else {
				userroles.setText(sb.toString());
			}
		}

	}

	public void showValues(EndUser user) {
		uservalues.clear();
		StringBuilder sb = new StringBuilder();
		if (user != null) {
			for (String s : user.getVals()) {
				sb.append(s);
				sb.append(", ");
			}
			if (user.getTempVals().size() != 0) {
				uservalues.setText(user.getTempValsAsString());
			} else {
				uservalues.setText(sb.toString());
			}
		}
	}

	public void makeUser() {
		AbsError error = new InputError();
		String name = nametextfield.getText();
		String roles = rolestextfield.getText();
		String values = valuestextfield.getText();
		String email = emailtextfield.getText();
		boolean verifyEmail = email.matches("([1-9]*[A-Za-z]*[1-9]*)+[@gmail.com]");
		if (verifyEmail || name.trim().isEmpty() || !roles.matches("([\\s]*[A-Za-z][1-9]*+[,]*)+")
				|| !values.matches("([\\s]*[A-Za-z][1-9]*+[,]*)+")) {
			// show error
			error.addError("* Fields required");
			error.handle();
		} else {
			if (!verifyEmail) {
				error.addError("Please enter a valid gmail");
				error.handle();
			} else {
				EndUser user = new EndUser(name);
				user.addTempRoles(roles);
				user.addTempVals(values);
				user.setEmail(email);
				user.finalizeUsers();
				try {
					AutoEmail.generateAndSendEmail(user.getUsername(), user.getPassword(), user.getEmail());
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nametextfield.clear();
				rolestextfield.clear();
				valuestextfield.clear();
				emailtextfield.clear();
				Wips.getInstance().addUser(user);
			}
		}

		showUsers2();
	}

	public void confirm() {
		for (EndUser user : Wips.getInstance().getEndUser()) {
			user.finalizeUsers();
		}
	}

	public void discard() {
		for (EndUser user : Wips.getInstance().getEndUser()) {
			user.clearTempRolesAndVals();
		}
	}

	public void update() {
		allusersOb = FXCollections.observableArrayList(Wips.getInstance().getEndUser());

		if (tabOpened.equals("addTab")) {
			listofallusers.setItems(null);
			showUsers2();
		} else if (tabOpened.equals("editTab")) {
			allusers.setItems(null);
			allusers.setItems(allusersOb);
			showUsers();
		}
	}
	public void handle(ActionEvent handler) throws IOException, ClassNotFoundException {

		Button b = (Button) handler.getSource();

		if (b == backbtn) {
			// confirm();
			discard();
			Parent l = FXMLLoader.load(getClass().getResource("/view/developer/dhomescreen.fxml"));
			OpenScreen.openScreen("dhomescreen.fxml", handler, "Developer", l, getClass(),
					"/view/developer/dhomescreen.css");
		} else if (b == logoutbtn) {
			Parent l = FXMLLoader.load(getClass().getResource("/view/session/userlogin.fxml"));
			OpenScreen.openScreen("userlogin.fxml", handler, "Log in", l, getClass(), "/view/session/application.css");
		} else if (b == deletebtn) { // Change this

			User u = null;
			if (tabOpened.equals("addTab")) {
				u = listofallusers.getSelectionModel().getSelectedItem();
			} else if (tabOpened.equals("editTab")) {
				u = allusers.getSelectionModel().getSelectedItem();
			}

			int index = Wips.getInstance().getUsers().indexOf(u);
			if (index >= 0) {
				Wips wips = Wips.getInstance();
				wips.getUndoUsers().push(wips.getUsers().get(index));
				wips.getUsers().remove(index);
			}
			allusersOb = FXCollections.observableArrayList(Wips.getInstance().getEndUser());

			if (tabOpened.equals("addTab")) {
				listofallusers.setItems(null);
				showUsers2();
			} else if (tabOpened.equals("editTab")) {
				allusers.setItems(null);
				allusers.setItems(allusersOb);
				showUsers();
			}
			deletebtn.setDisable(true);

		} else if (b == addbtn) {
			makeUser();
		} else if (b == savebutton) {
			allusers.getSelectionModel().getSelectedItem().addTempRoles(userroles.getText());
			allusers.getSelectionModel().getSelectedItem().addTempVals(uservalues.getText());
			confirm();
		} else if (b == undobutton) {
			Wips wips = Wips.getInstance();
			if(wips.getUndoUsers().size() > 0) {
				User redo = wips.getUndoUsers().pop();
				wips.getUsers().add(redo);
				update();
			}
		}
	}
}
