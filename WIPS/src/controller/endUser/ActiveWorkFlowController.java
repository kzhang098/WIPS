package controller.endUser;

import javafx.scene.control.ListView;
import model.wips.WorkFlow;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class ActiveWorkFlowController {
	/**
	 * This listview will be populated with all the active workflows that the user creates.
	 */
	ListView<WorkFlow> allWorkflows;
	
	/**
	 * This method will populate the listview called allworkflows that a user can join
	 */
	public void showAllActiveWorkFlows() {
		//shows listview
	}
	
	/**
	 * This method will delete the workflow that was passed in. It will also update the 
	 * list of all the active workflows by removing that particular workflow from the list.
	 * @param workflow WorkFlow
	 */
	public void cancel(WorkFlow workflow) {
		workflow.setActive(false);
	}
	
	/**
	 * This method will show the status of the current workflow that was passed in. 
	 * Therefore, the user will be able to see the history of where the form was throughout the process
	 * @param workflow WorkFlow
	 */
	public void status(WorkFlow workflow) {
		
	}
}
