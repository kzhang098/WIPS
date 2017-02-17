package model.wips;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Wips;
import model.wips.intermediates.AbsReq;
import model.wips.intermediates.AndReq;
import model.wips.intermediates.OrReq;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class State implements Serializable {

	/**
	 * This is the field containing the name of the State
	 */

	private String name;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This variable will contain a unique id for the state, which will be
	 * unique in the workflow.
	 */
	private int stateID;
	/**
	 * This will be set to true for the start state, otherwise it will be set to
	 * false. Therefore, no other start will be a start state except the first
	 * state.
	 */
	private boolean startState;

	/**
	 * This is the boolean value which states whether this state is the final
	 * state in the workflow.
	 */

	private boolean endState = false;

	/**
	 * This boolean will be set to true if the workflow is in this current
	 * state. Otherwise it will be false.
	 */
	private boolean currentState;
	/**
	 * This variable stores the entity in which the state resides in.
	 */
	private Entity entity;
	/**
	 * If the condition of the state is satisfied then it moves to the next
	 * state by setting the condition value to true.
	 */
	private boolean condition = false;

	private Set<String> distinctVals;

	private List<OrReq> orStartWithMe = null;
	private List<OrReq> orEndWithMe = null;
	private AndReq andr = new AndReq();
	private AndReq andEndWithMe = new AndReq();
	private List<AbsReq> allStates = null;
	private List<AbsReq> allEndStates = null;
	private List<String> tempDistinctVals = new ArrayList<>();

	/**
	 * This constructor sets the unique id for the state.
	 * 
	 * @param id
	 *            int
	 */
	public State(int id, boolean startState, Entity entity) {
		this.stateID = id;
		this.entity = entity;
		this.startState = startState;
		distinctVals = new HashSet<String>();
	}

	public void populate() {
		if (orStartWithMe == null && orEndWithMe == null) {
			orStartWithMe = new ArrayList<OrReq>();
			// AbsReq andS = new AndReq();
			orEndWithMe = new ArrayList<OrReq>();
			// AbsReq andE = new AndReq();
			for (Transition t : Wips.getInstance().getCurrentWorkFlow().getTransition()) {
				if (this.equals(t.getStartState())) {
					OrReq or = new OrReq(t);
					orStartWithMe.add(or);
				}

				if (this.equals(t.getEndState())) {
					OrReq orn = new OrReq(t);
					orEndWithMe.add(orn);
				}
			}
		}
	}

	/**
	 * This method checks whether the condition for the current state satisfies
	 * or not. If it does satisfies then it returns true otherwise it returns
	 * false.
	 * 
	 * @return boolean
	 */
	public boolean isAllowedtoSend() {
		int counter = 0;
		getAllEndWithMe();
		if (allEndStates == null || allEndStates.size() == 0) {
			return true;
		}
		for (int i = 0; i < allEndStates.size() - 1; i++) {
			if (allEndStates.get(i).isAllowed()) {
				counter = 1;
			}
		}

		if (counter == 1 && (allEndStates.get(allEndStates.size() - 1) instanceof AndReq)
				&& allEndStates.get(allEndStates.size() - 1).isAllowed()) {
			return true;
		}

		if (allEndStates.get(allEndStates.size() - 1) instanceof OrReq && allEndStates.get(allEndStates.size() - 1).isAllowed()) {
			return true;
		}else if(counter == 1 && !(allEndStates.get(allEndStates.size() - 1) instanceof AndReq)){
			return true;
		}

		if (allEndStates.size() == 1 && allEndStates.get(0).isAllowed()) {
			return true;
		}
		return false;
	}

	/**
	 * sets the start state
	 * 
	 * @param tf
	 *            boolean
	 */
	public void setStartState(boolean tf) {
		this.startState = tf;
	}

	/**
	 * tells whether a state is start state or not
	 * 
	 * @return boolean
	 */
	public boolean isStartState() {
		return startState;
	}

	/**
	 * sets the current state
	 * 
	 * @param tf
	 *            boolean
	 */
	public void setCurrentState(boolean tf) {
		this.currentState = tf;
	}

	/**
	 * checks whether this state is current state or not
	 * 
	 * @return boolean
	 */
	public boolean isCurrentState() {
		return currentState;
	}

	/**
	 * sets the role of the state
	 * 
	 * @param entity
	 *            Entity
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * @return the role of this state
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * it sets the condition
	 * 
	 * @param tf
	 *            boolean
	 */
	public void setCondition(boolean tf) {
		this.condition = tf;
	}

	/**
	 * returns the condition
	 * 
	 * @return boolean
	 */
	public boolean getCondition() {
		return condition;
	}
	// Maybe List: Transitions list, keeps track
	// of how many transitions are going to this state.
	// 2 transitions means 2 approvals to make this state active.
	// Everytime this state is accessed by another state, then increment
	// approval++
	// if #transitions = approvals, then active state

	/**
	 * @return the stated id
	 */
	public int getID() {
		return stateID;
	}

	public void addDistintVals(List<String> vals) {
		for (String s : vals) {
			distinctVals.add(s);
		}
	}

	public Set<String> getDistinctValues() {
		return distinctVals;
	}

	public List<OrReq> getOrStartWithMe() {
		populate();
		return orStartWithMe;
	}

	public List<AbsReq> getAllStartWithMe() {
		populate();
		allStates = new ArrayList<AbsReq>();
		if (orStartWithMe.size() > 0) {
			for (OrReq or : orStartWithMe)
				allStates.add(or);
		}
		if (andr.size() != 0)
			allStates.add(andr);
		return allStates;
	}

	public List<OrReq> getEndState() {
		populate();
		return orEndWithMe;
	}

	/**
	 * This method returns the name of the state object
	 * 
	 * @return
	 */

	public String getName() {
		return name;
	}

	/**
	 * This method returns a boolean value representing whether or not this
	 * state is a final state in the workflow.
	 * 
	 * @return boolean value which states if this state is final state.
	 */

	public boolean isEndState() {
		return endState;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEndState(boolean endState) {
		this.endState = endState;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof State))
			return false;
		State state = (State) o;
		if (this.getID() == state.getID())
			return true;
		return false;
	}

	@Override
	public String toString() {
		return getName();
	}

	public void addand(Transition t) {
		andr.add(t);
	}

	public AndReq getAnd() {
		return andr;
	}

	public void popAndEndWithMe() {
		andEndWithMe.getAndTransitions().clear();
		for (Transition t : Wips.getInstance().getCurrentWorkFlow().getTransition()) {
			if (this.equals(t.getEndState()) && t.getReq()) {
				andEndWithMe.add(t);
			}
		}
	}

	public void getAllEndWithMe() {
		populate();
		allEndStates = new ArrayList<AbsReq>();
		popAndEndWithMe();
		if (orEndWithMe.size() > 0) {
			for (OrReq or : orEndWithMe)
				allEndStates.add(or);
		}
		if (andEndWithMe.size() > 0) {
			allEndStates.add(andEndWithMe);
		}
	}
	
	public void addTempDistinctVals(String temDistVals) {
		tempDistinctVals.clear();
		String[] tokens = temDistVals.split(",");
		for(int i = 0; i< tokens.length; i++) {
			if(!tokens[i].trim().isEmpty())
				tempDistinctVals.add(tokens[i].trim());
		}
	}
	
	public void finalizeDistinctVals() {
		for(String dVal: tempDistinctVals){
			distinctVals.add(dVal);
		}
	}
	
	public void clearTempVals() {
		tempDistinctVals.clear();
	}
	public List<String> getTempDistVals() {
		return tempDistinctVals;
	}
	
	public String getTempDistValsAsString() {
		StringBuilder sb = new StringBuilder();
		for(String s : tempDistinctVals){
			sb.append(s.trim() + ", ");
		}
		return sb.toString();
	}
}
