package model.wips;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Wips;
import model.user.EndUser;
import model.user.User;
import model.wips.forms.Form;


//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class WorkFlow implements Serializable{
	/**
	 * This field contains the name of the workflow
	 */
	
	private String name;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This is the workflows unique id. This will help the system identify this particular workflow. 
	 */
	private int id;
	private int cloneId;
	/**
	 * This is the field which informs the system that this workflow is active. True if active. False if not active.
	 */
	private boolean isActive = false;
	/**
	 *  This list stores all states associated with the workflow.
	 */
	private List<State> state;
	/**
	 * This list stores all transitions associated with the workflow.
	 */
	private List<Transition> transition;
	/**
	 * This list stores all entities associated with the workflow.
	 */
	private List<Entity> entity;
	
	private List<EndUser> users;
	/**
	 * This will store the form associated with the workflow
	 */
	private Form form;

	/**
	 * this stores the start 
	 */
	private State startState = null;
	private List<State> currentStates = new ArrayList<>();
	private List<Boolean> hasUpdate;
	/**
	 *The empty constructor
	 */
	public WorkFlow() {
		id = Wips.getInstance().getIdsOfEveryClass().getWorkFlowId();
		this.state = new ArrayList<State>();
		this.entity = new ArrayList<Entity>();
		this.transition = new ArrayList<Transition>();
		this.users = new ArrayList<EndUser>();
		hasUpdate = new ArrayList<Boolean>();
	}

	/**
	 * This is the constructor for the workflow object. It will set the active boolean 
	 * field to true. It will also store all passed through lists in their respective lists.
	 * @param id int
	 * @param states list of States
	 * @param entities list of Entities
	 * @param transition list of Transtions
	 * @param form  Form
	 */
	public WorkFlow(List<State> states, List<Entity> entities, List<Transition> transition, int shouldIncrementId) {
		if(shouldIncrementId == 1) {
			id = Wips.getInstance().getIdsOfEveryClass().getWorkFlowId();
		}else {
			cloneId = Wips.getInstance().getIdsOfEveryClass().getWorkFlowCloneId();
		}
		this.state = states;
		this.entity = entities;
		this.transition = transition;
		this.users = new ArrayList<EndUser>();
		hasUpdate = new ArrayList<Boolean>();
	}
	
	/**
	 * Changes the active boolean value from true to false, telling the system that this workflow is inactive
	 * @param b boolean
	 */
	public void setActive(boolean b) {
		this.isActive = b;
	}

	public boolean isActive() {
		return this.isActive;
	}
	/**
	 * Returns the states list.
	 * @return list of states
	 */
	public List<State> getState() {
		return state;
	}

	/**
	 * Returns the transition list
	 * @return list of transitions
	 */
	public List<Transition> getTransition() {
		return transition;
	}

	/**
	 * Returns the entities list.
	 * @return list of entities
	 */
	public List<Entity> getEntity() {
		return entity;
	}

	/**
	 * Returns the forms.
	 * @return forms
	 */
	public Form getForm() {
		return form;
	}

	public void setStartState(State s){
		this.startState = s;
	}
	
	/**
	 * @return the start state
	 */
	public State getStartState() {
		return this.startState;
	}
	
	/**
	 * checks if given entity belongs to this workflow
	 * @param e Entity
	 * @return boolean
	 */
	public boolean hasRole(Entity e) {
		if(entity.contains(e))
			return true;
		return false;
	}
	
	public void setForm(Form form) {
		this.form = form;
	}
	
	public void addUser(List<User> user) {
		for(User u: user){
			if(u instanceof EndUser)
				users.add((EndUser)u);
		}
	}
	
	public List<EndUser> getUsers() {
		return users;
	}
	
	public int getId() {
		return id;
	}
	
	public int getCloneId() {
		return cloneId;
	}
	public void setCurrentState(List<State> s) {
		for(int i = 0; i < s.size(); i++) {
			currentStates.add(s.get(i));
		}
		
	}
	
	public State getCurrentState(Entity e) {
		for(State s: currentStates) {
			if(s.getEntity().equals(e))
				return s;
		}
		return null;
	}
	
	public void setWorkFlowName(String name) {
		this.name = name;
	}
	
	public String getWorkFlowName() {
		return name;
	}
	
	public List<State> getCurrentStates() {
		return currentStates;
	}
	
	public void setHasUpdate(EndUser user, boolean b) {
		int index = form.getUsers().indexOf(user);
		if(form.getUsers().size() > 0)
			this.hasUpdate.set(index, b);
	}
	
	public void addUpadte() {
		hasUpdate.add(true);
		for(int i = 0; i < hasUpdate.size(); i++) {
			hasUpdate.set(i, true);
		}
	}
	public boolean getHasUpdate(EndUser user) {
		return hasUpdate.get(form.getUsers().indexOf(user));
	}
	
	@Override
	public String toString() {
		int index = form.getUsers().indexOf(Wips.getInstance().getCurrentuser());
		return name + (index >= 0 && hasUpdate.get(index) ? " ( Updated )" : "" );
	}
	
	@Override
	public Object clone() {
		//WorkFlow wf = allwflist.getSelectionModel().getSelectedItem();
		WorkFlow newWf = new WorkFlow(this.getState(), this.getEntity(), this.getTransition(), 0);
		newWf.setWorkFlowName(this.getWorkFlowName());
		Form f = new Form(this.getForm().getFormName(), newWf);
		f.addCouple(this.getForm().getCouples());
//		f.clear();
		newWf.setForm(f);
		newWf.setCurrentState(this.getCurrentStates());
		newWf.setStartState(this.getStartState());
		return newWf;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof WorkFlow))
			return false;
		WorkFlow f = (WorkFlow) o;
		if(this.getId() == f.getId() && this.getCloneId() == f.getCloneId()){
			return true;
		}
		return false;
	}
}
