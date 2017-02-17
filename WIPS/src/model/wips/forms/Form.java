package model.wips.forms;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Wips;
import model.user.EndUser;
import model.wips.Entity;
import model.wips.WorkFlow;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class Form implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This list contains all couples that form is made out of
	 */
	private List<Couple> couples;
	/**
	 * This List contains the message from all entity
	 */
	private List<String> message;
	/**
	 * This stores the name of the form
	 */
	private String formName;
	/**
	 * This is a unique identifier for the fom
	 */
	private int formID;

	private List<Entity> roles;
	private List<EndUser> users;
	private WorkFlow wf;
	private String fromUser = null;

	private Date dateTime;

	/**
	 * This a constructor to make the new form object
	 */
	public Form(String formname, WorkFlow wf) {
		this.formName = formname;
		couples = new ArrayList<Couple>();
		message = new ArrayList<String>();
		roles = new ArrayList<Entity>();
		users = new ArrayList<EndUser>();
		this.wf = wf;
		formID = Wips.getInstance().getIdsOfEveryClass().getFormId();
		// TODO Auto-generated constructor stub
	}

	public WorkFlow getFormWorkFlow() {
		return this.wf;
	}

	public void setWorkFlow(WorkFlow workflow) {
		this.wf = workflow;
	}

	/**
	 * This method will add message to the message list
	 */
	public void addMessage(String message) {
		this.message.add(message);
	}

	public void addCouple(List<Couple> c) {
		for (Couple p : c) {
			couples.add(p);
		}
	}

	public List<Couple> getCouples() {
		return couples;
	}

	public void addRoles(List<Entity> role) {
		for (Entity r : role)
			if (!roles.contains(r))
				roles.add(r);
	}

	public void addRoles(Entity role) {
		if (!roles.contains(role))
			roles.add(role);
	}

	public List<Entity> getRoles() {
		return roles;
	}

	public void addUser(EndUser user) {
		if (!users.contains(user)) {
			users.add(user);
		}
		wf.addUpadte();
	}

	public List<EndUser> getUsers() {
		return this.users;
	}

	public void updateUsers() {
		for (EndUser user : users)
			user.update();
	}

	public String getFormName() {
		return formName;
	}

	public boolean isAllowed() {
		for (Couple c : couples) {
			if (!c.isAllowed())
				return false;
		}
		return true;
	}

	public int getFormId() {
		return formID;
	}

	public void clear() {
		for (int i = 2; i < couples.size(); i++) {
			couples.get(i).clear();
		}
		message.clear();
		users.clear();
		roles.clear();
	}

	public String getFromUser() {
		return this.fromUser;
	}

	public void setFromUser(String fromuser) {
		this.fromUser = fromuser;
	}

	public String getDate() {
		SimpleDateFormat formatDate = new SimpleDateFormat("MM-dd-yyyy");
		return formatDate.format(dateTime);
	}

	public String getTime() {
		SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");
		return formatTime.format(dateTime);
	}

	public void setFormDateTime() {
		this.dateTime = Calendar.getInstance().getTime();
	}
}
