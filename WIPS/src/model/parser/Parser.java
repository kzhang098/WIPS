package model.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import errors.AbsError;
import errors.ParserError;
import model.parser.intermediates.WorkFlowInter;
import model.wips.Entity;
import model.wips.State;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


/**
 * @author Deep, Hassan, Kenneth
 *
 */
public abstract class Parser {
	
	/**
	 * This will contain the File object that will be parsed. 
	 */
	
	protected File fileName; 
	
	/**
	 * This list will contain the keys for all errors that have been encountered by the parser dring runtime.
	 * Each key will be associated with a particular error message which will be handled by the errors module.
	 */
	
	
	List<String> tempError;
	/**
	 * This constructor takes a File object as an argument and saves it in the module. 
	 * This file object will be the file chosen by the workflow application developer 
	 * and will be associated with one of the workflow, transitions, or users xml files.
	 */
	public Parser(File f) {
		this.fileName = f; 
	}

	/**
	 * This will begin the process for parsing the XML file. Takes a file as an argument, 
	 * which will be the file that will be parsed. This method will also store the extracted 
	 * information from the file into their intermediate model modules. 
	 * @return nothing
	 */
	
	public abstract void parse();
	
	/**
	 * If any error is found while parsing the file. 
	 * @param errors List of String
	 * @return ParserError
	 */
	public AbsError getError(List<String> errors) {
		AbsError parser = new ParserError();
		for(String e: errors) {
			parser.addError(e);
		}
		return parser;
	}
	
	/**
	 * This method returns the intermediate object in which the extracted information from the XML
	 * file is stored.
	 * @return The object containing the extracted information 
	 */
	public abstract Object getInters();
	
}
