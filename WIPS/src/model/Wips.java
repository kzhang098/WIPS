package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import model.user.EndUser;
import model.user.User;
import model.wips.Entity;
import model.wips.WorkFlow;
import model.wips.forms.Form;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class Wips implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String storeUser = "data";
	
	private boolean hasPressedBack = false;
	private static Wips wips = null;
	private Stack<WorkFlow> undoWorkFlow = new Stack<>();
	private Stack<User> undoUsers = new Stack<>();
	/**
	 * This list will have all the workflows that ever created.
	 */
	private List<WorkFlow> workflow = new ArrayList<>();
	/**
	 * This list will have all the users that ever created.
	 */
	
	public List<Form> forms; 
	
	private List<User> users;
	/**
	 * Current logged in user will be set to this variable.
	 */
	private User currentUser;
	/**
	* Current workflow the user is working on.
	**/
	private WorkFlow currentWorkflow;
	private int indexOfNextState = -1;
	private IdsOfEveryClass idsOfEveryClass;
	private Entity roleOfCurrentUser = null;
	
	private Wips() {
		users = new ArrayList<User>();
		idsOfEveryClass = new IdsOfEveryClass();
	}
	public List<EndUser> getEndUser() {
		List<EndUser> endusers = new ArrayList<EndUser>();  
		for(User u: users){
			if(u instanceof EndUser)
				endusers.add((EndUser)u);
		}
		return endusers;
	}
	
	public void addUser(User user) {
		if(!users.contains(user))
			users.add(user);
	}
	
	public List<WorkFlow> getAllWorkFlows() {
		return this.workflow; 
	}
	
	public void addWorkFlow(WorkFlow f) {
		workflow.add(f);
	}
	public  List<User> getUsers() {
		return users; 
	}
	
	public List<Form> getForms() {
		return forms; 
	}
	
	public void setCurrentWorkFlow(WorkFlow f) {
		this.currentWorkflow = f;
	}
	
	public WorkFlow getCurrentWorkFlow() {
		return currentWorkflow;
	}
	
	public void setIndexOfNextState(int index) {
		indexOfNextState = index;
	}
	
	public int getIndexOfNextState() {
		return indexOfNextState;
	}
	
	public User getCurrentuser() {
		return currentUser;
	}
	
	public void setCurrentUser(User user) {
		this.currentUser = user;
	}
	public static Wips getInstance() {
		if(wips == null)
			wips = new Wips();
		return wips;
	}

	public void setRoleOfCurrentUser(Entity e) {
		this.roleOfCurrentUser = e;
	}
	
	public Entity getRoleOfCurrentUser() {
		return this.roleOfCurrentUser;
	}
	public IdsOfEveryClass getIdsOfEveryClass() {
		return idsOfEveryClass;
	}
	
	public void setHasPressedBack(boolean b) {
		this.hasPressedBack = b;
	}
	
	public boolean getHasPressedBack() {
		return this.hasPressedBack;
	}
	
	public Stack<WorkFlow> getUndoWorkFlowStack() {
		return undoWorkFlow;
	}
	
	public Stack<User> getUndoUsers() {
		return undoUsers;
	}
	public void make () throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeUser + File.separator + "wips" + "/wips"));
		oos.writeObject(this);
		oos.close();
	}

	public static Wips remake()throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/wips/wips"));
		 wips = (Wips)ois.readObject();
		 ois.close();
		 return wips; 
	}
}
