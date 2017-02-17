package model.wips.forms;

import java.io.Serializable;

import javafx.scene.control.TextArea;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class Couple implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This will be a unique id for the couple object
	 */
	private int coupleId;
	
	String heading;
	boolean isrequired;
	boolean userField;
	String contentOfTextArea;
	
	/**
	 * This is the constructor that creates the new couple object
	 */
	public Couple(String heading, boolean isrequired, boolean userField) {
		this.heading = heading;
		this.isrequired = isrequired;
		this.userField = userField;
		//coupleId++;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public boolean isRequired() {
		return isrequired;
	}

	public void setIsrequired(boolean isrequired) {
		this.isrequired = isrequired;
	}

	public boolean isUserField() {
		return userField;
	}

	public void setUserField(boolean userField) {
		this.userField = userField;
	}
	
	public void setContentOfTextArea(String value) {
		contentOfTextArea = value;
	}
	
	public String getContentOfTextArea() {
		return contentOfTextArea;
	}
	
	public boolean isAllowed() {
		if(isRequired() && !contentOfTextArea.isEmpty()) {
			return true;
		}
		if(!isRequired()) {
			return true;
		}
		return false;
	}
	
	public void clear() {
		setContentOfTextArea("");
	}
}
