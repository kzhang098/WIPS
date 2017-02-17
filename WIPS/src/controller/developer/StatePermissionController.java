package controller.developer;

import java.io.IOException;

import errors.AbsError;
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
import model.Wips;
import model.wips.State;
import model.wips.Transition;
import model.wips.WorkFlow;
import model.wips.intermediates.OrReq;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class StatePermissionController {
	
	@FXML
	Button addBtn, removeBtn, nextBtn, backBtn, logoutBtn;
	
	@FXML
	ListView<State> allState;
	@FXML
	ListView<Transition> reqState;
	@FXML
	ListView<OrReq> incomingStates;

//	/**
//	 * This is the object which will display all states that are associated with 
//	 * this workflow. Each state listed in this view will be associated with the 
//	 * state’s unique id.
//	 */
//	ListView<State> states;
	
	/**
	 * This is list of states that will help the listview
	 */
//	List<State> state;
	/**
	 * This will store the user inputted value for required value. 
	 */
	int requiredValue;
	/**
	 * This will hold the stateID of the state chosen.  
	 */
//	int stateId;
	/**
	 * This object will handle all the errors
	 */
	AbsError e;
	
	private ObservableList<State> allStatesOb;
	private ObservableList<OrReq> incomingOb;
	private ObservableList<Transition> reqStatesOb;
	private Wips wips;
	private WorkFlow wf;
	
	@FXML
	protected void initialize() {
		//Do something once the FXML is done
		enableDisableBtn(true, true);	
		populate();
	}
	
	private void enableDisableBtn (boolean aBtn, boolean rBtn) {
		addBtn.setDisable(aBtn);
		removeBtn.setDisable(rBtn);
	}
	
	/**
	 * This method will get all the transitions associated with the chosen state 
	 * and update the label’s value. This method will access the TransitionIntermediateModel 
	 * class and iterate through the list to find all transitions who have a destination 
	 * state which matches the chosen state.
	 */
	public void getTransitions(State state) {
		//gets all the transitions of each state
		incomingOb = FXCollections.observableArrayList();
		incomingOb.addAll(state.getOrStartWithMe());
		incomingStates.setItems(incomingOb);

	}
	
	/**
	 * Displays in the window all information associated with the chosen state. 
	 */
	public void displayInfo() {
		//number of transition coming in
		
	}
	
	/**
	 * This updates the required value field in the chosen State object
	 */
	public void updateReq(State state) {
		//update the required field in each state
		reqStatesOb = FXCollections.observableArrayList();
		int indexOfIncomingStates = incomingStates.getSelectionModel().getSelectedIndex();
		if(indexOfIncomingStates >= 0) {
			OrReq t = incomingStates.getSelectionModel().getSelectedItem();
			
			t.getTransition().setReq(true);
			state.addand(t.getTransition());
			state.getOrStartWithMe().remove(indexOfIncomingStates);
		}
		
		reqStatesOb.addAll(state.getAnd().getAndTransitions());
		reqState.setItems(reqStatesOb);
		getTransitions(state);
		
		if(incomingStates.getSelectionModel().getSelectedItems().size() <= 0)
			enableDisableBtn(true, true);
		
	}
	
	/**
	 * Checks the if the inputted value is less than or equal to the number of transitions 
	 * which lead to that state
	 */
	public void remove(Transition t) {
		t.setReq(false);
		State state = allState.getSelectionModel().getSelectedItem();
		state.getOrStartWithMe().add(new OrReq(t));
		state.getAnd().getAndTransitions().remove(t);
		getTransitions(state);
		updateReq(state);
		if(reqState.getSelectionModel().getSelectedItems().size() <= 0)
			enableDisableBtn(true, true);
	}

	/**
	 * This method will be called when theres user errors
	 */
	public void getError(){	
	}
	
	public void handle(ActionEvent handler) throws IOException, ClassNotFoundException {
		Button b = (Button) handler.getSource();
		if (b == addBtn) {
			//adds the selected state
			updateReq(incomingStates.getSelectionModel().getSelectedItem().getTransition().getStartState());
			
		} else if (b == removeBtn) {
			//remove slected index from the added list
			remove(reqState.getSelectionModel().getSelectedItem());
			
		} else if (b == nextBtn) {
			//Go to the Form builder screen
			Parent l = FXMLLoader.load(getClass().getResource("/view/developer/dformcreate.fxml"));
			OpenScreen.openScreen("dformcreate.fxml", handler, "Create Form", l, getClass(),"/view/developer/dformcreate.css");
			
		} else if (b == backBtn) {
			//Go back to the xml file browser
			Parent l = FXMLLoader.load(getClass().getResource("/view/developer/dfilesbrowser.fxml"));
			OpenScreen.openScreen("dfilesbrowser.fxml", handler, "Create Workflow", l, getClass(),"/view/developer/dfilesbrowser.css");
			
		} else if (b == logoutBtn) {
			//Go back to the login screen
			Parent l = FXMLLoader.load(getClass().getResource("/view/session/userlogin.fxml"));
			OpenScreen.openScreen("userlogin.fxml", handler, "Log in", l, getClass(),"/view/session/application.css");
			
		}
	}
	
	public void populate() {
		
		wips = Wips.getInstance();
		wf = wips.getCurrentWorkFlow();
		//wf.getState().add(new State (1,true, new Entity("prof")));
		allStatesOb = FXCollections.observableArrayList(wf.getState());
		allState.setItems(allStatesOb);
		
		

		allState.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				getTransitions(newValue);
				updateReq(newValue);
				enableDisableBtn(true, true);
			}
		});
		
		incomingStates.getSelectionModel().selectedItemProperty().addListener(listner ->{
			enableDisableBtn(false, true);
		});
		
		reqState.getSelectionModel().selectedItemProperty().addListener(listner -> {
			enableDisableBtn(true, false);
		});
	}
	
	public void setAllEndWithReq() {
		for(State s: Wips.getInstance().getCurrentWorkFlow().getState())
			s.getAllEndWithMe();
	}
}
