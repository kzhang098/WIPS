package model.parser.intermediates;

import java.util.ArrayList;
import java.util.List;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class WorkFlowInter<T, S> extends Intermediate<T> { // T = entity, S = state

	
	/**
	 * This will be the list that stores all of the State objects which have been passed from the WorkFlowParser class
	 */
	protected List<S> tempStates = new ArrayList<>();
	
	/**
	 * it adds the states to the temporary states list
	 * @param s typs S
	 */
	public void addStates(S s) {
		tempStates.add(s);
	}

	/**
	 * This will return the list of states stored in this class
	 * @return list of states
	 */
	public List<S> getTempStates() {
		return tempStates;
	}
}
