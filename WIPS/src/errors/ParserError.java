package errors;

import java.util.List;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


/**
 * This module will handle all parsing errors incurred in the process
 * of parsing files. Potential errors include incorrect file format,
 * unrecognized tags, unrecognized attribute names, and syntax errors.
 */
public class ParserError extends AbsError {

	/**
	 * Calls the AbsError constructor to make a new empty key list of
	 * ParserErrors.
	 */
	public ParserError() {
		super();
	}
}
