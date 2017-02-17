package model.wips;

import java.io.Serializable;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class Transition implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This field will store the origin of the transition 
	 */
	private State startState;
	/**
	 * This field will store the destination of the transition 
	 */
	private State endState;
	
	private boolean isReq = false;
	private boolean isActive = false;
	
	/**
	 * This is the constructor of the Transition module. It will take two arguments, 
	 * which will be the start state and the end state for the transition.
	 * @param startState State
	 * @param endState State
	 */
	public Transition(State startState, State endState) {
		this.startState = startState;
		this.endState = endState;
	}
	
	public State getEndState() {
		return endState;
	}
	
	public State getStartState() {
		return startState;
	}
	
	public String toString() {
		return "S: " + getStartState() + " E: " + getEndState(); 
	}
	
	public void setReq(boolean b) {
		isReq = b;
	}
	
	public void setIsActive(boolean b) {
		isActive = b;
	}
	
	public boolean getReq() {
		return isReq;
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	
	//@Override
//	public boolean equals(Object o) {
//		if(o == null || !(o instanceof Transition))
//			return false;
//		Transition transition = (Transition) o;
//		if(this.startState.equals(transition.startState)&& this.endState.equals(transition.endState))
//			return true;
//		return false;
//	}
}
