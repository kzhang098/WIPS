package errors;

import java.util.List;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


/**
 * This module will handle all errors related to authentication.
 * Potential errors may include invalid username or invalid password.
 */
public class AuthenticationError extends AbsError{

	/**
	 * Calls the AbsError constructor to make a new empty key list of
	 * AuthenticationErrors.
	 */
	public AuthenticationError() {
		super();
	}
}
