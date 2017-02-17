package model.wips;

import java.io.Serializable;
import java.util.List;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This field will hold the role of the user.
	 */
	private String role;
	/**
	 * This list will contain the distinct values related to the user. Values will be a set of
	 * strings that a user is currently related to. For instance, a professor can have multiple 
	 * classes that he/she teaches.
	 */
	private List<String> distinctValues;

	/**
	 * This method will set the user role that is passed in as a string.
	 * @param role String
	 */
	public Entity(String role) {
		this.role = role;
	}
	
	/**
	 * This method will compare the objects, if they are equal then it return true otherwise it return false
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Entity))
			return false;
		Entity entity = (Entity) o;
		if(this.role.equalsIgnoreCase(entity.role))
			return true;
		return false;
	}
	
	public String getRole() {
		return role;
	}
	@Override
	public String toString() {
		return getRole();
	}
}
