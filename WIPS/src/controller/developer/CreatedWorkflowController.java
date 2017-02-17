package controller.developer;

import java.util.List;

import javafx.scene.control.ListView;
import model.Wips;
import model.user.Developer;
import model.wips.WorkFlow;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah

public class CreatedWorkflowController {
	/**
	 * List of all the created workflows by the developer
	 */
	public ListView<WorkFlow> allWorkflowsDisplay;
	Developer admin = (Developer) Wips.getInstance().getCurrentuser();
	
	public CreatedWorkflowController() {
		// TODO Auto-generated constructor stub
	}
	
	public List<WorkFlow> getCreatedWorkflow () {
		return admin.getWorkFlows();
	}

}
