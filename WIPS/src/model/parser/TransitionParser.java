package model.parser;

import org.w3c.dom.*;

import model.parser.intermediates.GenInter;
import model.parser.intermediates.WorkFlowInter;
import model.wips.Entity;
import model.wips.State;
import model.wips.Transition;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class TransitionParser extends Parser {
	
	boolean errorFound = false;
	
	
	 HashMap<String,Boolean> keyMap;
	
	/**
	 * This contains the workflow intermediate object which contains all the states and entities which 
	 * contains  
	 */
	
	WorkFlowInter<Entity,State> wfi; 
	
	
	/**
	 * This is the intermediate object which will store transition objects that have been extracted from the
	 * transitions XML file
	 */
	
	GenInter<Transition> transitions = new GenInter<Transition>();
	
	/**
	 * This is the constructor for the TransitionParser module. This constructor takes two arguments:
	 * A File transitionFile which stores the XML file to be parsed and the WorkFlowInter wfi
	 * object which will be used in the parse() method to reference created states and entities.
	 */
	public TransitionParser(File transitionFile, WorkFlowInter<Entity,State> wfi2) {
		super(transitionFile);
		this.wfi = wfi2; 
		this.keyMap = new HashMap<String, Boolean>(); 
		keyMap.put("incorrectTransitionTag", false);
		keyMap.put("invalidStateID", false);
		keyMap.put("startStateIdError", false);
		keyMap.put("endStateIdError", false);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * This method is responsible for parsing the file that was passed through when this module during
	 * instantiation. This method is also responsible for storing the extracted information into the
	 * GenInter<Transition> object. 
	 */
	
	public void parse() {
		try{
	
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(fileName);
			doc.getDocumentElement().normalize();
			
		
			NodeList transitionList = doc.getElementsByTagName("transition");
			
			if(transitionList.getLength() == 0) {
				keyMap.put("incorrectTransitionTag", true);
				errorFound = true;
			} else {
				extract(transitionList);
			}
			
			if (keyMap.containsValue(true)) {
				List<String> errors = new ArrayList<String>();
				if (keyMap.get("incorrectTransitionTag"))
					errors.add("Your transition XML file does not contain transition tags. Please try again");
				if (keyMap.get("invalidStateID"))
					errors.add("There is an invalid state ID in the Transition XML file.");
				
				this.getError(errors).handle();
			}
			
			if(errorFound) {
				transitions = null;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void extract(NodeList transitionList) {
		
		for(int i = 0; i< transitionList.getLength(); i++) {
			Node tranNode = transitionList.item(i);
			String startStateStr; 
			String endStateStr; 
			
			if(tranNode.getNodeType() == Node.ELEMENT_NODE) {
				
				if(!tranNode.getNodeName().equals("transition")) {
					keyMap.put("incorrectTransitionTag", true);
					errorFound = true;
				}
				
				Element transElement = (Element) tranNode; 
				
				//This will hold state id. 
				
				startStateStr = transElement.getAttribute("startstate");
				endStateStr = transElement.getAttribute("endstate");
				
				if(startStateStr.equals("")){
					keyMap.put("startStateIdError", true);
					startStateStr = "0";
					errorFound = true;

				}
				if(endStateStr.equals("")) {
					keyMap.put("endStateIdError", true);
					endStateStr = "0";
					errorFound = true;

				}
				
				//This contains that actual id values for states 
				
				ArrayList<State> temp_states = (ArrayList<State>) wfi.getTempStates();
				addTransIfExists(temp_states, startStateStr, endStateStr);
			}
		}
	}
	
	private void addTransIfExists(ArrayList<State> temp_states, String startStateStr, String endStateStr) {
		
		Transition transition;
		
		State startState = null;
		State endState = null;
		
		int startStateID = Integer.parseInt(startStateStr);
		int endStateID = Integer.parseInt(endStateStr);
		
		boolean startFound = false; 
		boolean endFound = false;
		
		for(int j = 0; j < temp_states.size(); j++) {
			if(temp_states.get(j).getID() == startStateID) {
				startState= temp_states.get(j);
				startFound = true;
			} else if(temp_states.get(j).getID() == endStateID) { 
				endState = temp_states.get(j);
				endFound = true;
			}
		}
		
		if(!startFound || !endFound) {
			keyMap.put("invalidStateID", true);
			errorFound = true;
		} else {
			transition = new Transition(startState, endState);
			transitions.getTempAttr().add(transition); 
		}
	}
	
	/**
	 * This method returns the intermediate object in which the extracted information from the XML
	 * file is stored.
	 * @return The object containing the extracted information 
	 */
	
	public GenInter<Transition> getTransitions() {
		return this.transitions;
	}
	
	
	@Override
	public Object getInters() {
		// TODO Auto-generated method stub
		return this.transitions;
	}

}
