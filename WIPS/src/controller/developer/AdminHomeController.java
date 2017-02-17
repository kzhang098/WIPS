package controller.developer;

import java.io.IOException;

import java.util.List;
import controller.session.LogOutController;
import helper.OpenScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import model.Wips;
import model.user.Developer;
import model.user.EndUser;
import model.wips.WorkFlow;
import errors.*;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah

public class AdminHomeController {
	
	@FXML
	Button createWFBtn, logoutBtn, editbtn,deleteBtn,editUserBtn, undobutton;
	
	@FXML
	ListView<WorkFlow> createdWorkFlows;
	
	ListView<WorkFlow> list = new ListView<WorkFlow>();
	ObservableList<WorkFlow> createdWorkFlowsOb = FXCollections.observableArrayList();
	
	
	@FXML
	protected void initialize() {
		//Do something once the FXML is done
		editbtn.setDisable(true);
		deleteBtn.setDisable(true);
		showFinished();
		createdWorkFlows.getSelectionModel().selectedItemProperty().addListener((ov, oldVal, newVal) -> {
			editbtn.setDisable(false);
			deleteBtn.setDisable(false);
		});
	}
	
	/**
	 * Running this method will redirect the workflow application developer to the window where 
	 * he or she is able to create the workflow.
	 */
	
	public void createWorkFlow() {
		//This method will open 
	}
	
	/**
	 * Running this method will redirect the workflow application developer to the window where
	 * the workflow application.
	 */
	public void showFinished() {
		// show all workflows that has been created to list
		createdWorkFlowsOb = FXCollections.observableArrayList(Wips.getInstance().getCurrentuser().getAllWorkflows());
		createdWorkFlows.setItems(createdWorkFlowsOb);
	}
	
	public void handle(ActionEvent handler) throws IOException, ClassNotFoundException {
		Button b = (Button) handler.getSource();
		if (b == createWFBtn) {
			//If the user is developer then open the following screen
			Parent l = FXMLLoader.load(getClass().getResource("/view/developer/dfilesbrowser.fxml"));
			OpenScreen.openScreen("dfilesbrowser.fxml", handler, "Create Workflow", l, getClass(),"/view/developer/dfilesbrowser.css");
			
		} else if (b== logoutBtn) {
			Parent l = FXMLLoader.load(getClass().getResource("/view/session/userlogin.fxml"));
			OpenScreen.openScreen("userlogin.fxml", handler, "Log in", l, getClass(),"/view/session/application.css");
			LogOutController.logInScreen();
		} else if(b == deleteBtn) {
			if (createdWorkFlows.getSelectionModel().getSelectedItem() != null)	{
				int wrkflow = createdWorkFlows.getSelectionModel().getSelectedItem().getId();
				Wips wips = Wips.getInstance();
				
				//Removes it from the developer
				
				for(int i = 0; i < wips.getCurrentuser().getAllWorkflows().size(); i++) {
					if(wips.getCurrentuser().getAllWorkflows().get(i).getId() == wrkflow) {
						wips.getCurrentuser().getAllWorkflows().remove(i);
						deleteBtn.setDisable(true);
						break;
					}
				}
				
				//Removes it from the WIPS 
				
				for(int i = 0; i < wips.getAllWorkFlows().size(); i++) {
					if(wips.getAllWorkFlows().get(i).getId() == wrkflow) {
						wips.getUndoWorkFlowStack().push(wips.getAllWorkFlows().get(i));
						wips.getAllWorkFlows().remove(i);
						deleteBtn.setDisable(true);
						break;
					}
				}
				
				//Reset the Observable list. 
				
				createdWorkFlowsOb = FXCollections.observableArrayList(Wips.getInstance().getCurrentuser().getAllWorkflows());
				createdWorkFlows.setItems(createdWorkFlowsOb);
			} else {
				AbsError e = new InputError();
				e.addError("No workflow selected for deletion.");
				e.handle();
			}
			deleteBtn.setDisable(true);
			editbtn.setDisable(true);

		} else if (b == editbtn){
			//Open the edit window
			Wips.getInstance().setCurrentWorkFlow(createdWorkFlows.getSelectionModel().getSelectedItem());
			Parent l = FXMLLoader.load(getClass().getResource("/view/developer/deditworkflow.fxml"));
			OpenScreen.openScreen("deditworkflow.fxml", handler, "Edit Workflow", l, getClass(),"/view/developer/deditworkflow.css");
		} else if (b== editUserBtn) {
			Parent l = FXMLLoader.load(getClass().getResource("/view/developer/deditsusers.fxml"));
			OpenScreen.openScreen("deditworkflow.fxml", handler, "Edit Users", l, getClass(),"/view/developer/deditworkflow.css");			
		} else if (b == undobutton) {
			Wips wips = Wips.getInstance();
			if(wips.getUndoWorkFlowStack().size()>0){
				WorkFlow redo = wips.getUndoWorkFlowStack().pop();
				wips.getAllWorkFlows().add(redo);
				wips.getCurrentuser().getAllWorkflows().add(redo);
				showFinished();
			}
		}
	}
}
