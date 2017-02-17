package model;

import java.io.Serializable;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class IdsOfEveryClass implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int workFlowId = 1;
	private int UserId = 1;
	private int formId = 1;
	private int workFlowCloneId = 1;
	public int getWorkFlowId() {
		return workFlowId++;
	}
	public int getUserId() {
		return UserId++;
	}
	public int getFormId() {
		return formId++;
	}
	
	public int getWorkFlowCloneId() {
		return workFlowCloneId++;
	}
}
