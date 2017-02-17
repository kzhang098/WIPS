package model.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.Wips;
import model.wips.Entity;
import model.wips.WorkFlow;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public abstract class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This stores the roles that user plays in the workflow
	 */
	protected List<Entity> roles;
	/**
	 * This is the name of the user
	 */
	protected String name;
	/**
	 * username of this user
	 */
	protected String username;
	/**
	 * password of this user
	 */
	protected String password;
	
	protected boolean isDeveloper = false;
	
	/**
	* This list will contain all the workflows of that particular user.
	*/
	protected List<WorkFlow> allworkflows;
	/**
	 * This is the unique id for the user
	 */
	protected List<String> values;
	/**
	 * unique identifier for each users
	 */
	protected int id;
	
	protected String email;
	
	/**
	 * This is the constructor that will be call by the concrete classes
	 * @param name String
	 * @param roles list of Entity
	 * @param value list of String
	 */
	public User(String name, List<Entity> roles, List<String> value) {
		this.name = name;
		this.roles = roles;
		values = new ArrayList<String>();
		values.addAll(value);
		id = Wips.getInstance().getIdsOfEveryClass().getUserId();
		generateUsername();
		generatePassword();
		allworkflows = new ArrayList<WorkFlow>();
	}
	public User(String name){
		this.name = name;
		values = new ArrayList<String>();
		roles = new ArrayList<Entity>();
		id = Wips.getInstance().getIdsOfEveryClass().getUserId();
		generateUsername();
		generatePassword();
	}
	public User(String username, boolean b) {
		this.username = username;
		if(b) {
			this.name =username;
			generateUsername();
			generatePassword();
		}
	}
	/**
	 * This method simply returns the id of the user
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	* This method will add the workflow to list of all workflows
	*/
	public void setWorkflow(WorkFlow workflow){
		allworkflows.add(workflow);
	}

	/**
	* This method will return all the workflows of the current user
	*/
	public List<WorkFlow> getAllWorkflows(){
		return allworkflows;
	}
	
	/**
	 * it generates a alphanumeric string to be the username
	 */
	public void generateUsername() {
		String[] first = name.split(" ");
		username = first[0]+ id;
		System.out.println("username is " + username);
	}
	
	/**
	 * it generates a random alphanumeric string as password
	 */
	public void generatePassword() {
		String[] first = name.split(" ");
		int len = first[0].length();
		char[] name = first[0].toCharArray();
		StringBuilder build = new StringBuilder();
		for(int i = 0; i < len; i++) {
			int ran = (int) (Math.random()*len);
			build.append(name[ran]);
		}
		build.append((int)(Math.random()*1000));
		password = build.toString();
		System.out.println("password is: " + password);
	}
	
	public List<String> getVals() {
		return values;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public List<Entity> getRoles() {
		return roles;
	}
	
	public void setDeveloper(boolean b) {
		this.isDeveloper = b;
	}
	
	public boolean isDeveloper() {
		return isDeveloper;
	}
	
	public void setEmail(String email) {
		this.email = email; 
	}
	
	public String getEmail() {
		return email;
	}
	

	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof User))
			return false;
		User user = (User) o;
		if(this.getUsername().equals(user.getUsername()))
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return username;
	}
}
