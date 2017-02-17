package controller.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import errors.AbsError;
import errors.InputError;
import helper.OpenScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Wips;
import model.user.EndUser;
import model.wips.WorkFlow;
import model.wips.forms.Couple;
import model.wips.forms.Form;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class FormController {

	@FXML
	Button sendbtn, backbutton, logoutbtn;

	@FXML
	TextField requiredsign;

	@FXML
	Label title, description;

	@FXML
	CheckBox checkbox;

	@FXML
	TextArea optionalmessage;

	@FXML
	VBox vbox, usernamevbox;

	@FXML
	ScrollPane sp;

	boolean optionalM = false;

	List<TextArea> textAreas = new ArrayList<>();

	/**
	 * This method will show the form to the user
	 */
	public void showForm() {
		// Once we have the form we show the form using GUI
	}

	/**
	 * Accesses the static user variable in the WIPS class and adds this form
	 * object to that user’s sent list. This also accesses the recipient user’s
	 * received queue and adds this form to their received queue. //
	 */
	// public void send(Form f) {
	// EndUser enduser = (EndUser) Wips.currentUser;
	// //enduser.send(f,);
	// }

	@FXML
	protected void initialize() {
		sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		vbox.setFillWidth(true);
		// Assume we have couples from the form object
		// Wips.getInstance().setCurrentWorkFlow(Wips.getInstance().getAllWorkFlows().get(0));
		List<Couple> dummyC = Wips.getInstance().getCurrentWorkFlow().getForm().getCouples();
		WorkFlow wf = Wips.getInstance().getCurrentWorkFlow();
		if (!wf.isActive()) {
			sendbtn.setDisable(true);
		}
		// for (int i = 0; i < 20; i++) {
		// boolean req = getRandomBoolean();
		// boolean f = getRandomBoolean();
		// if(f == true && req == true) {
		// f = false;
		// }
		// Couple dummyCouple = new Couple("Title"+"["+i+"]",req, f);
		// dummyC.add(dummyCouple);
		// }

		// Generate the form that was send to the user
		// Set the title
		Couple temp = dummyC.get(0);
		title.setText(temp.getHeading());
		// Set the description
		temp = dummyC.get(1);
		description.setText(temp.getHeading());

		for (int i = 2; i < dummyC.size(); i++) {
			vbox.getChildren().add(createCoupleRow(dummyC.get(i)));
		}

		vbox.setSpacing(15);
		createSenderLabel();
	}

	private void createSenderLabel() {
		if (Wips.getInstance().getCurrentWorkFlow().getForm().getFromUser() != null) {
			Label label = new Label();
			label.setFont(new Font(15));
			label.setText("Sender: " + Wips.getInstance().getCurrentWorkFlow().getForm().getFromUser());
			label.setPadding(new Insets(10, 10, 0, 10));

			Label dateTime = new Label();
			dateTime.setFont(new Font(15));
			dateTime.setText("Sent on: " + Wips.getInstance().getCurrentWorkFlow().getForm().getDate() + ", "
					+ Wips.getInstance().getCurrentWorkFlow().getForm().getTime());
			dateTime.setPadding(new Insets(10));

			usernamevbox.getChildren().add(label);
			usernamevbox.getChildren().add(dateTime);
		}
	}

	private GridPane createCoupleRow(Couple couple) {
		GridPane gridpane = new GridPane();
		List<EndUser> endUsers = Wips.getInstance().getCurrentWorkFlow().getForm().getUsers();
		// If required is true and user field is false
		if (couple.isRequired() && !couple.isUserField()) {
			RowConstraints row1 = new RowConstraints();
			row1.setVgrow(Priority.ALWAYS);

			ColumnConstraints col1 = new ColumnConstraints();
			col1.setPercentWidth(30);
			ColumnConstraints col2 = new ColumnConstraints();
			col2.setPercentWidth(55);
			ColumnConstraints col3 = new ColumnConstraints();
			col3.setPercentWidth(15);

			Label label = new Label(couple.getHeading());
			label.setFont(new Font("", 15));
			label.setWrapText(true);

			TextArea textArea = new TextArea();
			if (endUsers.size() != 0) {
				textArea.setText(couple.getContentOfTextArea());
				// textArea.setDisable(true);
				textArea.setEditable(false);
				textArea.setStyle("-fx-background-color: white");
			}

			if (Wips.getInstance().getHasPressedBack()) {
				if (endUsers.size() == 0) {
					textArea.setText(couple.getContentOfTextArea());
					// textArea.setDisable(true);
					textArea.setEditable(true);
					textArea.setStyle("-fx-background-color: white");
				}
			}

			textArea.setWrapText(true);
			textArea.setPrefSize(Double.MIN_VALUE, Double.MIN_VALUE);
			this.textAreas.add(textArea);

			Label reqLabel = new Label("*required");

			gridpane.add(label, 0, 1);
			gridpane.add(textArea, 1, 1);
			gridpane.add(reqLabel, 2, 1);

			gridpane.getRowConstraints().addAll(row1);
			gridpane.getColumnConstraints().addAll(col1, col2, col3);

			gridpane.setHgap(10);
			// gridpane.setVgap(10);
			gridpane.setPadding(new Insets(5, 20, 5, 20)); // margins around the
															// whole grid

		} else if (couple.isUserField() && !couple.isRequired()) {

			RowConstraints row1 = new RowConstraints();
			row1.setVgrow(Priority.ALWAYS);

			ColumnConstraints col1 = new ColumnConstraints();
			col1.setPercentWidth(100);

			Label label = new Label(couple.getHeading());
			label.setFont(new Font("", 15));
			label.setWrapText(true);

			gridpane.add(label, 0, 1);

			gridpane.getRowConstraints().addAll(row1);
			gridpane.getColumnConstraints().addAll(col1);

			gridpane.setHgap(10);
			// gridpane.setVgap(10);
			gridpane.setPadding(new Insets(5, 20, 5, 20)); // margins around the
															// whole grid

		} else if (!couple.isUserField() && !couple.isRequired()) {

			RowConstraints row1 = new RowConstraints();
			row1.setVgrow(Priority.ALWAYS);

			ColumnConstraints col1 = new ColumnConstraints();
			col1.setPercentWidth(30);
			ColumnConstraints col2 = new ColumnConstraints();
			col2.setPercentWidth(55);
			ColumnConstraints col3 = new ColumnConstraints();
			col3.setPercentWidth(15);

			Label label = new Label(couple.getHeading());
			label.setFont(new Font("", 15));
			label.setWrapText(true);

			TextArea textArea = new TextArea();
			if (endUsers.size() != 0) {
				textArea.setText(couple.getContentOfTextArea());
				// textArea.setDisable(true);
				textArea.setEditable(false);
				textArea.setStyle("-fx-background-color: white");
			}
			textArea.setWrapText(true);
			textArea.setPrefSize(Double.MIN_VALUE, Double.MIN_VALUE);
			this.textAreas.add(textArea);

			gridpane.add(label, 0, 1);
			gridpane.add(textArea, 1, 1);

			gridpane.getRowConstraints().addAll(row1);
			gridpane.getColumnConstraints().addAll(col1, col2);

			gridpane.setHgap(10);
			// gridpane.setVgap(10);
			gridpane.setPadding(new Insets(5, 20, 5, 20)); // margins around the
															// whole grid

		}
		return gridpane;
	}

	private boolean hasSignature() {
		boolean sign = false;
		if (!requiredsign.getText().trim().isEmpty()) {
			Wips.getInstance().getCurrentWorkFlow().getForm().setFromUser(requiredsign.getText().trim());
			sign = true;
		}
		return sign;
	}

	private Couple getOptionalMessage() {
		String s = Wips.getInstance().getCurrentuser().getUsername();
		Couple dummy = new Couple("Message from " + s + ":" + " " + optionalmessage.getText(), false, true);
		return dummy;
	}

	public void send() {
		List<Couple> couples = Wips.getInstance().getCurrentWorkFlow().getForm().getCouples();
		if (checkbox.isSelected() && !optionalmessage.getText().isEmpty()) {
			couples.add(getOptionalMessage());
			optionalM = true;
		} else {
			optionalM = false;
		}
		for (int i = 2; i < couples.size() && textAreas.size() > i - 2; i++) {
			couples.get(i).setContentOfTextArea(textAreas.get(i - 2).getText());
		}
	}

	public void handle(ActionEvent handler) throws IOException, ClassNotFoundException {
		Button b = (Button) handler.getSource();
		if (b == sendbtn) {
			WorkFlow wf = Wips.getInstance().getCurrentWorkFlow();
			AbsError er = new InputError();
			boolean isAllowed = wf.getCurrentState(Wips.getInstance().getRoleOfCurrentUser()).isAllowedtoSend();
			boolean endState = wf.isActive() && wf.getCurrentState(Wips.getInstance().getRoleOfCurrentUser()).isEndState() && hasSignature();
			if (isAllowed && endState ) {
				send();
				Form f = wf.getForm();
				EndUser user = (EndUser) Wips.getInstance().getCurrentuser();
				f.addUser(user);
				user.send(f, f.getUsers().get(0));
				user.setWorkflow(wf);
				f.updateUsers();
				user.update();
				wf.setActive(false);
				int index = user.getRecievedForm().indexOf(wf);
				if (index >= 0)
					user.getRecievedForm().get(index).setActive(false);
				Wips.getInstance().setHasPressedBack(false);
				Parent e = FXMLLoader.load(getClass().getResource("/view/endUser/ehomescreen.fxml"));
				OpenScreen.openScreen("ehomescreen.fxml", handler, "Home", e, getClass(),
						"/view/enduser/ehomescreen.css");

			} else {
				send();
//				if (!endState) {
//					er.addError("Please enter something in the required field.");
//					er.handle();
//				} else if(!isAllowed){
//					er.addError("Waiting for other users in the Work Flow to fill out the form");
//					er.handle();
//				}
				
				boolean checkSign = wf.getForm().isAllowed() && hasSignature() && wf.isActive();
				if (isAllowed && checkSign) {
					Wips.getInstance().setHasPressedBack(false);
					Parent e = FXMLLoader.load(getClass().getResource("/view/endUser/eselectstates.fxml"));
					OpenScreen.openScreen("eselectstates.fxml", handler, "Select States", e, getClass(),
							"/view/enduser/eselectstates.css");
				} else {
					
					if (!optionalmessage.getText().trim().isEmpty() && optionalM == true) {
						List<Couple> couples = Wips.getInstance().getCurrentWorkFlow().getForm().getCouples();
						couples.remove(couples.size() - 1);
					}
					if (!checkSign) {
						er.addError("Please enter something in the required field.");
						er.handle();
					} else if(!isAllowed){
						er.addError("Waiting for other users in the Work Flow to fill out the form");
						er.handle();
					}
				}
			}
		} else if (b == backbutton) {
			Wips.getInstance().setHasPressedBack(false);
			Parent e = FXMLLoader.load(getClass().getResource("/view/endUser/ehomescreen.fxml"));
			OpenScreen.openScreen("ehomescreen.fxml", handler, "Home", e, getClass(), "/view/enduser/ehomescreen.css");
		} else if (b == logoutbtn) {
			Parent l = FXMLLoader.load(getClass().getResource("/view/session/userlogin.fxml"));
			OpenScreen.openScreen("userlogin.fxml", handler, "Log in", l, getClass(), "/view/session/application.css");
		}
	}

}
