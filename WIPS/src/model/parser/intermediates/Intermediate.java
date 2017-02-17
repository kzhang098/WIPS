package model.parser.intermediates;

import java.util.ArrayList;
import java.util.List;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public abstract class Intermediate <T>{
	/**
	 * it stores all the attributes for the type T
	 */
	protected List<T> tempAttr = new ArrayList<>();
	
	/**
	 * it adds the attributes to the temporary list
	 * @param e T
	 */
	public boolean addAttr(T e) {
		if(!tempAttr.contains(e)) {
			tempAttr.add(e); 
			return true; 
		}
		return false;
	}

	/**
	 * it returns the temporary list of attributes
	 * @return list
	 */
	public List<T> getTempAttr() {
		return tempAttr;
	}
	
}
