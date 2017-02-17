package errors;

import helper.Pops;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert.AlertType;

/**
 * The abstract class which all specific error classes will inherit
 * their methods and fields from. The purpose of this class is so
 * that in each module which can throw an error, we can reference
 * the error using this abstract type.
 */

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah

public abstract class AbsError {
	
	/**
	 * The list of errors for this specific error.
	 */
	protected List<String> keyList;
	
	/**
	 * The constructor which will create a new empty key list of 
	 * errors to store all new errors for this type of error.
	 */
	public AbsError() {
		keyList = new ArrayList<String>();
	}
	
	/**
	 * This method will add any new error that occurs to the list
	 * of errors for this specific error. Errors will be printed
	 * or displayed at a later time via the handle method.
	 * @param newError
	 */
	public void addError(String newError) 
	{
		if (newError != null && !newError.isEmpty() && !keyList.contains(newError))
			keyList.add(newError);
	}
	
	/**
	 * This method will be called to access and handle the list of
	 * errors that have occurred for this specific error. The list
	 * of errors will be printed along with information about where
	 * each specific error occurred.
	 */
	public void handle()
	{
		//Print all errors in the list
		StringBuilder sb = new StringBuilder();
		for (String currentError : keyList)
		{
			sb.append(currentError);
			sb.append("\n");
			//print error message
			//do substring(7) on class name to omit "errors." part of the class name
				//Display message for current error
		}
		Pops.pop(AlertType.ERROR, sb.toString(), this.getClass().getName().substring(7));
		keyList.clear();
	}
}
