package model.parser;

import model.wips.*;
import model.wips.Entity;

import org.w3c.dom.*;

import model.parser.intermediates.GenInter;
import model.parser.intermediates.WorkFlowInter;
import model.user.EndUser;
import model.user.User;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class UserParser extends Parser {
	
	boolean errorFound = false;
	
	/**
	 * This list contains all usernames that have been found in the XML parser
	 */
		
	ArrayList<String> userNames;
	
	/**
	 * This hashmap contains a String regarding the "type of" error that was encountered while parsing.
	 */
	
	HashMap<String,Boolean> keyMap; 
	
	/**
	 * This contains the intermediate object which contains the list of all
	 * States and Entities associated with the "to-be-created" workflow.
	 */

	WorkFlowInter<Entity, State> wfi;

	/**
	 * This contains the intermediate object which contains all user information
	 * parsed from the user xml file.
	 */

	GenInter<User> usersInter = new GenInter<User>();

	/**
	 * it creates new user parser object
	 */
	public UserParser(File userFile) {
		super(userFile);
		userNames = new ArrayList<String>();
		keyMap = new HashMap<String, Boolean>();
		keyMap.put("incorrectUserTag", false);
		keyMap.put("duplicateUserName", false);
		keyMap.put("roleNotFound", false);
		keyMap.put("incorrectSubTag", false);
		keyMap.put("incorrectRoleTag", false);
		keyMap.put("incorrectValueTag", false);
		keyMap.put("emptyRoleValueAttr", false);
		keyMap.put("emptyValueValueAttr", false);
		keyMap.put("invalidEmail", false);

		// TODO Auto-generated constructor stub
	}

	/**
	 * This method is responsible for parsing the file containing all
	 * information regarding the user and stores the parsed information in a
	 * GenInter<User> object to be used later.
	 * 
	 */
	
	@Override

	public void parse() {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(fileName);
			doc.getDocumentElement().normalize();

			NodeList userList = doc.getElementsByTagName("user");
			
			if(userList.getLength() == 0) {
				keyMap.put("incorrectUserTag",true);
				errorFound = true;
			} else {
				extractUsers(userList);
			}
			
			if (keyMap.containsValue(true)) {
				List<String> errors = new ArrayList<String>();
				if (keyMap.get("duplicateUserName"))
					errors.add("There is a duplicate username in the User XML file.");
				if (keyMap.get("roleNotFound"))
					errors.add("There is an invalid role in the User XML file.");
				if (keyMap.get("incorrectSubTag"))
					errors.add("User sub tags should only be roles or values. Please check your user file.");
				if (keyMap.get("incorrectUserTag")) 
					errors.add("Your User XML file does not contain user tags. Please try again!!");
				if (keyMap.get("incorrectRoleTag"))
					errors.add("One tags under <roles> isn't <role>. Please check.");
				if (keyMap.get("incorrectValueTag"))
					errors.add("One tags under <values> isn't <value>. Please check.");
				if (keyMap.get("emptyRoleValueAttr")) 
					errors.add("One of the value attributes for the <role> tag is empty");
				if (keyMap.get("emptyValueValueAttr")) 
					errors.add("One of the value attributes for the <value> tag is empty");
				if (keyMap.get("invalidEmail")) 
					errors.add("Invalid email detected!!!");
				
				
				this.getError(errors).handle();
			}
			
			if(errorFound) {
				usersInter = null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void extractUsers(NodeList userList) {
		
		for (int i = 0; i < userList.getLength(); i++) {
			Node userNode = userList.item(i);

			String username = "";
			String email = "";

			if (userNode.getNodeType() == Node.ELEMENT_NODE) {
				Element userElement = (Element) userNode;
				username = userElement.getAttribute("name");
				email = userElement.getAttribute("email"); 
				
				if(!isValidEmailAddress(email)) {
					keyMap.put("invalidEmail", true);
					errorFound = true;
				} 
				
				NodeList children = userNode.getChildNodes();
				ArrayList<Node> childNodes = new ArrayList<Node>();
				
				if (!userNames.contains(username)) {
					userNames.add(username);
				} else {
					keyMap.put("duplicateUserName", true);
					errorFound = true;
				}
				
				for(int j = 0; j < children.getLength(); j++) {
					if(children.item(j).getNodeType() == Node.ELEMENT_NODE) {
						childNodes.add(children.item(j));
					}
				}
				
				Node node1 = childNodes.get(0);
				Node node2 = childNodes.get(1);
				
				//If the roles come first then I order node1 and then node2.
				//Else then I would order node2 (Which should contain roles) and then node1 which should
				//contain values. I'm doing this because the code handles roles first and then values. 
			
				if (node1.getNodeName().equals("roles")) {
					handleChildren(node1,node2, username, email);
				} else if(node1.getNodeName().equals("values")) {
					handleChildren(node2,node1, username, email);
				} else {
					keyMap.put("incorrectSubTag", true);
					errorFound = true;
				}
			}
		}
	}
	
	private void handleChildren(Node node1, Node node2, String name, String email) {
		
		User user;
		
		ArrayList<Entity> roleArrayList = new ArrayList<Entity>();
		ArrayList<String> valuesArrayList = new ArrayList<String>();
		
		String username = name;
		String role = "";
		
		if (node1.getNodeType() == Node.ELEMENT_NODE) {
			NodeList roleList = node1.getChildNodes();
			Node roleNode;

			for (int j = 0; j < roleList.getLength(); j++) {
				roleNode = roleList.item(j);
				if (roleNode.getNodeType() == Node.ELEMENT_NODE) {
					
					if(!roleNode.getNodeName().equals("role")) {
						keyMap.put("incorrectRoleTag", true);
						errorFound = true;
					}
					
					Element roleElement = (Element) roleNode;
					role = roleElement.getAttribute("value");
					
					if(role.equals("")) {
						keyMap.put("emptyRoleValueAttr", true);
						errorFound = true;
					}
					
					roleArrayList.add(new Entity(role));
				}
			}
		}
		
		String value;

		if (node2.getNodeType() == Node.ELEMENT_NODE) {
			NodeList valuesList = node2.getChildNodes();
			Node valueNode;

			for (int j = 0; j < valuesList.getLength(); j++) {
				valueNode = valuesList.item(j);
				if (valueNode.getNodeType() == Node.ELEMENT_NODE) {
					
					if(!valueNode.getNodeName().equals("value")) {
						keyMap.put("incorrectValueTag", true);
						errorFound = true;
					}
					
					Element valueElement = (Element) valueNode;
					value = valueElement.getAttribute("value");
					
					if(value.equals("")) {
						keyMap.put("emptyValueValueAttr", true);
						errorFound = true;
					}
						
					valuesArrayList.add(value);
				}
			}
		}
	
		user = new EndUser(username, roleArrayList, valuesArrayList);
		user.setEmail(email);
		usersInter.addAttr(user);
	}
	

	/**
	 * This returns the GenInter<User> object where the users parsed from the
	 * user file are stored.
	 * 
	 * @return GenInter<User> usersInter.
	 */

	@Override
	public Object getInters() {
		// TODO Auto-generated method stub
		return this.usersInter;
	}
	
	public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
	}
}