package controller.form;


import java.io.IOException;

import helper.OpenScreen;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Wips;
import model.wips.State;
import model.wips.intermediates.AbsReq;
import errors.*;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class SelectStatesController {

	@FXML
	Button backbtn, nextbtn, logoutbtn;
	
	@FXML
	ListView<AbsReq> listview;
	
	private ListView<AbsReq> nextStates;
	private ObservableList<AbsReq> nextStatesOb;
	
	@FXML
	protected void initialize() {
		disableNextButton();
		show();
	}
	
	private void disableNextButton () {
		int i = listview.getSelectionModel().getSelectedIndex();
		if(i < 0) {
			nextbtn.setDisable(true);
		} else {
			nextbtn.setDisable(false);
		}
		
		listview.getSelectionModel().selectedItemProperty()
	     .addListener(new ChangeListener<AbsReq>() {
	       public void changed(ObservableValue<? extends AbsReq> observable,
	    		   AbsReq oldValue, AbsReq newValue) {
	    	   nextbtn.setDisable(false); 
	       }
	     });
	}
	
	public void show() {
		//add to observable list currenstate.getStartWithMe();
		Wips wips = Wips.getInstance();
//		State[] s = {wips.getCurrentWorkFlow().getStartState()};
//		wips.getCurrentWorkFlow().setCurrentState(s);
		State currenState = wips.getCurrentWorkFlow().getCurrentState(wips.getRoleOfCurrentUser());
		
		currenState.populate();
		nextStatesOb = null;
		nextStatesOb = FXCollections.observableArrayList(currenState.getAllStartWithMe());
		listview.setItems(nextStatesOb);
		
	}
	
	public void next() {
		//index of currentState.getStartWithMe();
		Wips.getInstance().setIndexOfNextState(listview.getSelectionModel().getSelectedIndex()); // get index from observable;
	}
	
	private void fromUserToNull() {
		Wips.getInstance().getCurrentWorkFlow().getForm().setFromUser(null);
		Wips.getInstance().setHasPressedBack(true);
	}
	
	public void handle(ActionEvent handler) throws IOException, ClassNotFoundException {
		Button b = (Button) handler.getSource();
		if (b == backbtn) {
			fromUserToNull();
			Parent e = FXMLLoader.load(getClass().getResource("/view/endUser/eformgen.fxml"));
			OpenScreen.openScreen("eformgen.fxml", handler, "Form", e, getClass(),"/view/enduser/eformgen.css");
		} else if (b == nextbtn) {
			if (listview.getSelectionModel().getSelectedItem() != null) {
				next();
				Parent e = FXMLLoader.load(getClass().getResource("/view/endUser/erecipient.fxml"));
				OpenScreen.openScreen("erecipient.fxml", handler, "Recipient Window", e, getClass(),"/view/enduser/erecipient.css");
			} else {
				AbsError e = new InputError();
				e.addError("Please select a state!");
				e.handle();
			}
		} else if (b == logoutbtn) {
			Parent l = FXMLLoader.load(getClass().getResource("/view/session/userlogin.fxml"));
			OpenScreen.openScreen("userlogin.fxml", handler, "Log in", l, getClass(),"/view/session/application.css");
		}
	}
}
