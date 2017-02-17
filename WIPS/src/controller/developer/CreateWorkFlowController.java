package controller.developer;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import errors.AbsError;
import errors.FileError;
import errors.InputError;
import helper.AutoEmail;
import helper.OpenScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Wips;
import model.parser.Parser;
import model.parser.TransitionParser;
import model.parser.UserParser;
import model.parser.WorkFlowParser;
import model.parser.intermediates.GenInter;
import model.parser.intermediates.WorkFlowInter;
import model.user.User;
import model.wips.Entity;
import model.wips.State;
import model.wips.Transition;
import model.wips.WorkFlow;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class CreateWorkFlowController {
	
	@FXML
	Button wBrowse, tBrowse, uBrowse, backBtn, nextBtn, logoutBtn;
	
	@FXML
	Label wFileName, tFileName, uFileName;

	@FXML
	TextField workflowname;
	
	/**
	 * Abstract Error class that maybe called during workflow, transition, user errors.
	 */
	AbsError e;
	/**
	 * This parser will be used for workflows, transitions, and users to parse the xml file.
	 */
	Parser parser;
	
	WorkFlowInter<Entity, State> wfi = null;
	GenInter<User> users;
	GenInter<Transition> transitions;
	
	@FXML
	protected void initialize() {
		enableDisableBtn(true, true, true, true);

		wfi = new WorkFlowInter<Entity, State>();
		//Do something once the FXML is done
		//Enable this in the final product
		workflowname.textProperty().addListener((observable, oldValue, newValue) -> {
			String s = newValue.trim();
			if (s.length() >= 1) {
				enableDisableBtn(false, true, true, true);
				
			} else {
				enableDisableBtn(true, true, true, true);

			}
		});
	}
		
	private void enableDisableBtn (boolean wbtn, boolean tBtn, boolean uBtn, boolean nxtBtn) {
		wBrowse.setDisable(wbtn);
		tBrowse.setDisable(tBtn);
		uBrowse.setDisable(uBtn);
		nextBtn.setDisable(nxtBtn);
	}
	
	public CreateWorkFlowController() {

	}
	/**
	 * This will pass the file argument to the Workflow parser class to be parsed 
	 * @param file File
	 */
	public void workFlowXml(File file){
		//uploadworkflow button
		parser = new WorkFlowParser(file);
		parser.parse();
		Object o = parser.getInters();
		if(o instanceof WorkFlowInter)
			wfi = (WorkFlowInter<Entity, State>)o;
		System.out.println("is wfi null " + wfi == null ? "yes " :"no ");
	}
	
	/**
	 * This will pass the file argument to the TransitionParser class to be parsed
	 * @param file File
	 */
	public void transitionXml(File file) {
		//upload transition button
		parser = new TransitionParser(file,wfi);
		parser.parse();
		Object o = parser.getInters();
		transitions = (o instanceof GenInter ? (GenInter<Transition>) o : null);	
	}
	
	/**
	 * This will redirect the workflow application developer to the window where 
	 * he/she can generate forms
	 */
	public void generateForms() {
		//chodu will on it
	}
	
	/**
	 * This will pass the file argument into the userParser class
	 * @param file File
	 */
	public void userXmlParser(File file) {
		
		UserParser up = new UserParser(file);
		up.parse();
		Object o = up.getInters();
		users = (o instanceof GenInter ? (GenInter<User>) o : null);
		
		if(users != null && wfi != null && transitions != null) {
			addUsersToWips(users.getTempAttr());
			sendEmails(users);
		}
	}
	
	public void addUsersToWips(List<User> users) {
		for(User user: users) {
			Wips.getInstance().addUser(user);
		}
	}
	/**
	 * This will redirect the workflow application developer to the window where 
	 * he/she  can set permissions for states.
	 */
	public void setStatePermission() {
		//open new windows
	}
	
	/**
	 * This method will finalize all changes made to this workflow and generate a
	 * workflow object. It will access the intermediate model classes as well as 
	 * add all forms created and users associated with this workflow and store the 
	 * Workflow object in the workflows list in the workflow application�s class. 
	 */
	public void finish() {
		
		WorkFlow wf = new WorkFlow(wfi.getTempStates(), wfi.getTempAttr(), transitions.getTempAttr(),1);
		wf.setWorkFlowName(workflowname.getText());
		
	//	Wips.getInstance().getAllWorkFlows().add(wf);
		setStartState(wf);
		Wips.getInstance().setCurrentWorkFlow(wf);
		//wf.addUser(users.getTempAttr());
		reset();
		
	}
	
	public void setStartState(WorkFlow f) {
		for(State s: f.getState()){
			if(s.isStartState()){
				f.setStartState(s);
				List<State> b = new ArrayList<>();
				b.add(s);
				f.setCurrentState(b);
			}
				
		}
		Wips.getInstance().setCurrentWorkFlow(f);
	}

	public void reset() {
		e = null;
		users = null;
		transitions = null;
	}
	/**
	* This method will get the xml file and returns the file object
	*/
	public File getFile(ActionEvent e) throws IOException, NoSuchAlgorithmException {
		Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select an XML File");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		//fileChooser.getExtensionFilters()
			//	.addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
		File file = fileChooser.showOpenDialog(st);
		return file;
	}
		
	public void handle(ActionEvent handler) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
		Button b = (Button) handler.getSource();
		if (b == wBrowse) {
			File f = getFile(handler);	
			if(f != null){
				String fileName = f.getName();
				if (fileName.length() > 3 && fileName.substring(fileName.length() - 4, fileName.length()).toLowerCase().equals(".xml")){
					wFileName.setText(f.getName());
					workFlowXml(f);
					
					enableDisableBtn(false, false, true, true);
					
					//If file exist then call workflowxml parser
					//workFlowXml(f);
				}
				else {
					e = new FileError();
					e.addError("That is not a valid file for the Workflow XML file.");
					e.handle();
				}
			}			
		} else if (b == tBrowse) {
			File f = getFile(handler);
			if(f != null){
				String fileName = f.getName();
				if (fileName.length() > 3 && fileName.substring(fileName.length() - 4, fileName.length()).toLowerCase().equals(".xml")){
					tFileName.setText(f.getName());
					enableDisableBtn(false, false, false, false);
					//If file exist then call trnasition parser
					transitionXml(f);
				}
				else {
					e = new FileError();
					e.addError("That is not a valid file for the Transition XML file.");
					e.handle();
				}
			}			
		} else if (b == uBrowse) {
			File f = getFile(handler);
			if(f != null){
				String fileName = f.getName();
				if (fileName.length() > 3 && fileName.substring(fileName.length() - 4, fileName.length()).toLowerCase().equals(".xml")){
					uFileName.setText(f.getName());
					enableDisableBtn(false, false, false, false);

					//If file exist then call userxml parser
					userXmlParser(f);
				}
				else {
					e = new FileError();
					e.addError("That is not a valid file for the User XML file.");
					e.handle();
				}
			} 			
		} else if (b == backBtn) {
			//Goes back to the admin home screen
			Parent l = FXMLLoader.load(getClass().getResource("/view/developer/dhomescreen.fxml"));
			OpenScreen.openScreen("dhomescreen.fxml", handler, "Developer", l, getClass(),"/view/developer/dhomescreen.css");
		} else if (b == nextBtn) {
			if (wFileName.getText().equals("Browse Workflow XML File")
					|| tFileName.getText().equals("Browse Transition XML File")) {
				e = new InputError();
				e.addError("You are missing one or more of the required XML files.");
				e.handle();
			} else if (wfi == null || transitions == null || users == null) {
				e = new FileError();
				e.addError("Errors exist in your files. Please reupload");
				e.handle();
			} else {
				//Goes to state permission window
				finish();
				Parent l = FXMLLoader.load(getClass().getResource("/view/developer/dstatepscreen.fxml"));
				OpenScreen.openScreen("dstatepscreen.fxml", handler, "State Permission", l, getClass(),"/view/developer/dstatepscreen.css");
			}
		} else if (b == logoutBtn) {
			//Goes back to the user login window
			Parent l = FXMLLoader.load(getClass().getResource("/view/session/userlogin.fxml"));
			OpenScreen.openScreen("userlogin.fxml", handler, "Log in", l, getClass(),"/view/session/application.css");
		}
	}
	
	private static void sendEmails(GenInter<User> users) {
		for(int i = 0; i < users.getTempAttr().size(); i++) {
			String username = users.getTempAttr().get(i).getUsername();
			String password = users.getTempAttr().get(i).getPassword();
			String email = users.getTempAttr().get(i).getEmail();
			
			try {
				AutoEmail.generateAndSendEmail(username, password, email);
			} catch(Exception e) {
				
			}
			
		}
	}
	
	
	
	
}
