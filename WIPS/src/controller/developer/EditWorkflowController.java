package controller.developer;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

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
import model.wips.State;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class EditWorkflowController {

	@FXML
	Button addnewuser, backbtn, logoutbtn, savebtn, addbtn, deletebtn;

	@FXML
	ListView<State> allstates;
	
	@FXML
	TextField nametextfield, emailtextfield;
	
	@FXML
	ListView<EndUser> allusers;

	@FXML
	TextArea distinctstatevalues;

	@FXML
	TabPane tabpane;

	@FXML
	Tab workflowtab, usertab, newUser;

	private ObservableList<State> allstatesOb;
	private ObservableList<EndUser> allusersOb;

	@FXML
	protected void initialize() {
		tabListeners();
		showStates();
	}

	public EditWorkflowController() {
		// TODO Auto-generated constructor stub
	}

	public void tabListeners() {
		tabpane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
			if (newTab.equals(workflowtab)) {
				showStates();
			} 
		});
	}

	public void showStates() {
		allstatesOb = FXCollections.observableArrayList(Wips.getInstance().getCurrentWorkFlow().getState());
		allstates.setItems(allstatesOb);
		allstates.getSelectionModel().selectedItemProperty().addListener((ov, oldVal, newVal) -> {
			if (oldVal != null) {
				oldVal.addTempDistinctVals(distinctstatevalues.getText());
			}
			distinctVals(newVal);

		});
	}

	public void distinctVals(State state) {
		distinctstatevalues.clear();
		StringBuilder sb = new StringBuilder();
		if (state != null) {
			for (String dVal : state.getDistinctValues()) {
				sb.append(dVal);
				sb.append(", ");
			}
			if (state.getTempDistVals().size() != 0) {
				distinctstatevalues.setText(state.getTempDistValsAsString());
			} else {
				distinctstatevalues.setText(sb.toString());
			}
		}
	}

	public void confirm() {
		for (State s : Wips.getInstance().getCurrentWorkFlow().getState()) {
			s.finalizeDistinctVals();
		}
	}
	
	public void discard() {
		for (State s : Wips.getInstance().getCurrentWorkFlow().getState()) {
			s.clearTempVals();;
		}
	}
	public void handle(ActionEvent handler) throws IOException, ClassNotFoundException {
		Button b = (Button) handler.getSource();
		if (b == addnewuser) {

		} else if (b == backbtn) {
			//confirm();
			discard();
			Parent l = FXMLLoader.load(getClass().getResource("/view/developer/dhomescreen.fxml"));
			OpenScreen.openScreen("dhomescreen.fxml", handler, "Developer", l, getClass(),
					"/view/developer/dhomescreen.css");
		} else if (b == logoutbtn) {
			Parent l = FXMLLoader.load(getClass().getResource("/view/session/userlogin.fxml"));
			OpenScreen.openScreen("userlogin.fxml", handler, "Log in", l, getClass(), "/view/session/application.css");
		} else if (b == savebtn) {
			confirm();
		} else if (b == deletebtn) {
			User u = allusers.getSelectionModel().getSelectedItem();
			Wips.getInstance().getEndUser().remove(u);
			allusersOb.remove(u);
			allusers.setItems(allusersOb);
		}
	}

}
