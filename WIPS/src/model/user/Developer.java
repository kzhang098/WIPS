package model.user;

import java.util.List;

import model.wips.Entity;
import model.wips.WorkFlow;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class Developer extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates the developer object
	 */
	public Developer(String name, List<Entity> role, List<String> val) {
		super(name, role, val);
		setDeveloper(true);
		// TODO Auto-generated constructor stub
	}

	public Developer(String username) {
		super(username, false);
		setDeveloper(true);
	}
	/**
	 * It deletes a specific workflow based the index provided
	 */
	public void deleteWorkFlow(int index) {

	}

	/**
	 * @return list of all the workflow that this developer has created
	 */
	public List<WorkFlow> getWorkFlows() {
		return getAllWorkflows();
	}

}
