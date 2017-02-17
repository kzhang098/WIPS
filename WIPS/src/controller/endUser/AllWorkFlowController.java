package controller.endUser;

import java.util.ArrayList;
import java.util.List;

import model.Wips;
import model.user.EndUser;
import model.wips.WorkFlow;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class AllWorkFlowController {
	/**
	 * This listview will show the list of all workflows that the user can join
	 */
	private List<WorkFlow> allWorkFlows;
	
	public AllWorkFlowController() {
		allWorkFlows = new ArrayList<WorkFlow>();
	}
	/**
	 * This method will contain the searchworkflows method. The purpose of 
	 * the filter method is to call searchworkflow method and update the list and listview.
	 */
	private void filterAllWorkFlow() {
		Wips wips = Wips.getInstance();
		for(WorkFlow f: wips.getAllWorkFlows()) {
			if(f.getStartState() != null && f.getStartState().getEntity().equals(wips.getRoleOfCurrentUser())) {
				allWorkFlows.add(f);
			}
		}
	}
	
	public List<WorkFlow> filterJoinedWorkFlows() {
		List<WorkFlow> joinedwfFilter = new ArrayList<>();
		Wips wips = Wips.getInstance();
		for(WorkFlow f: wips.getCurrentuser().getAllWorkflows()){
			int index = f.getForm().getUsers().indexOf(wips.getCurrentuser());
			if(f.getForm().getUsers().contains(wips.getCurrentuser())) {
				if(f.getForm().getRoles().get(index).equals(wips.getRoleOfCurrentUser())){
					joinedwfFilter.add(f);
				}
			}
		}
		return joinedwfFilter;
	}
	
	public List<WorkFlow> filterNotifWorkFlows() {
		List<WorkFlow> notifwfFilter = new ArrayList<>();
		Wips wips = Wips.getInstance();
		
		EndUser user = (EndUser) wips.getCurrentuser();
		for(WorkFlow f: user.getRecievedForm()){
			int index = f.getForm().getUsers().indexOf(wips.getCurrentuser());
			if(f.getForm().getUsers().contains(wips.getCurrentuser())) {
				if(f.getForm().getRoles().get(index).equals(wips.getRoleOfCurrentUser())){
					
					notifwfFilter.add(f);
				}
			}
		}
		return notifwfFilter;
	}
	/**
	 * This method will update the listview with the workflows the user can join
	 */
	public List<WorkFlow> getAllWorkFlowsCanJoin() {
		if(allWorkFlows.size() == 0)
			filterAllWorkFlow();
		return allWorkFlows;
	}
}
