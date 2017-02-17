package controller.developer;

import java.io.IOException;
import java.util.ArrayList;

import helper.OpenScreen;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import model.Wips;
import model.user.Developer;
import model.wips.forms.Couple;
import model.wips.forms.Form;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class CreateFormController {

	@FXML
	TextArea tedit, dedit;
	
	@FXML
	AnchorPane ap;
	
	@FXML
	Button addbtn, createbtn, backbtn,logoutbtn;

	@FXML
	VBox vbox;
	
	@FXML
	ScrollPane sp;
	
	ArrayList<Couple> couples = new ArrayList<Couple>();
	
	int temp = 1, temp2 = 1;
	
	public CreateFormController() {
	}
	
	@FXML
	protected void initialize() {
		sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		vbox.setFillWidth(true);
		
		tedit.setPrefRowCount(1);
		tedit.requestLayout();
		
		dedit.setPrefRowCount(1);
		dedit.requestLayout();

		//Add two couples for title and description.
		Couple dummyT = new Couple("", false, false);
		couples.add(dummyT);
		Couple dummyD = new Couple("", false, false);
		couples.add(dummyD);
   }	
	
	@FXML
	private void textKeyPressed(KeyEvent event)	{		 
		if (event.getCode() == KeyCode.ENTER) {
			tedit.setPrefRowCount(tedit.getPrefRowCount() + 1);
			tedit.requestLayout();
		} else if (event.getCode() == KeyCode.BACK_SPACE) {
			int num2 = tedit.getText().split("\n").length+1;
			tedit.setPrefRowCount(num2-1);
			tedit.requestLayout();
			temp --;
		}
	}
	
	@FXML
	private void textKeyPressedTwo(KeyEvent event)	{
		
		if (event.getCode() == KeyCode.ENTER) {
			dedit.setPrefRowCount(dedit.getPrefRowCount() + 1);
			dedit.requestLayout();
		} else if (event.getCode() == KeyCode.BACK_SPACE) {
			int num2 = dedit.getText().split("\n").length+1;
			dedit.setPrefRowCount(num2-1);
			dedit.requestLayout();
			temp2 --;
		}
	}
			
	private void updateReqArray(int index, boolean tf){
		Couple dummyCouple = couples.get(index);
		dummyCouple.setIsrequired(tf);
		couples.set(index, dummyCouple);
	}
	
	private void updateUFArray (int index, boolean tf) {
		Couple dummyCouple = couples.get(index);
		dummyCouple.setUserField(tf);
		couples.set(index, dummyCouple);
	}
	
	private void updateTArray(int index, String s){
		Couple dummyCouple = couples.get(index);
		dummyCouple.setHeading(s);
		couples.set(index, dummyCouple);
	}
	
	public void handle(ActionEvent handler) throws IOException, ClassNotFoundException {
		Button b = (Button) handler.getSource();
		if (b == addbtn) {
			
			ToggleButton addDelField = new ToggleButton("Delete user field");
			
			 GridPane gridpane = new GridPane();
			 //Grid contstraints.
	 	        RowConstraints row1 = new RowConstraints();
	 	        row1.setVgrow(Priority.SOMETIMES);
	 	        
	 	        ColumnConstraints col1 = new ColumnConstraints();
	 	        col1.setPercentWidth(40);
	 	        ColumnConstraints col2 = new ColumnConstraints();
	 	        col2.setPercentWidth(40);
	 	        ColumnConstraints col3 = new ColumnConstraints();
		        col3.setPercentWidth(20);
			 
			 TextArea ta = new TextArea();
			 ta.setWrapText(true);
			 
			 ta.setOnKeyPressed(new EventHandler<KeyEvent>() {
		            @Override
		            public void handle(KeyEvent event) {
		            	 ta.textProperty().addListener((observable, oldValue, newValue) -> {
		 				 	int cIndex = vbox.getChildren().indexOf((GridPane)((TextArea)event.getSource()).getParent());
		 	            	updateTArray(cIndex, ta.getText());
		 				});
		            }
		        });

			
			 HBox hb = new HBox();
			 
			 TextArea ta2 = new TextArea();
			 
			 hb.getChildren().add(ta2);
			 HBox.setHgrow(ta2, Priority.ALWAYS);
			 hb.setFillHeight(true);

			 VBox vCon = new VBox(5);
			 
			 ToggleButton requiredT = new ToggleButton("Required");
			 requiredT.setOnAction(new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	if(requiredT.isSelected()){
		            		int cIndex = vbox.getChildren().indexOf((GridPane) ((VBox)((ToggleButton)event.getSource()).getParent()).getParent());
		            		updateReqArray(cIndex,true);
		            		requiredT.setStyle("-fx-background-color: #f25c21");
			                requiredT.setText("*Required");
		    	 	        addDelField.setVisible(false);

		            	} else {
		            		int cIndex = vbox.getChildren().indexOf((GridPane) ((VBox)((ToggleButton)event.getSource()).getParent()).getParent());
		            		updateReqArray(cIndex,false);
		            		requiredT.setStyle(null);
			                requiredT.setText("Required");
		    	 	        addDelField.setVisible(true);
		            	}
		            }
		        });
			 vCon.getChildren().add(requiredT);

			 
			 Button deleteBtn = new Button("Delete this row");
			 deleteBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						deleteBtn.setStyle("-fx-background-color: #f25c21");
		    	 	    int cIndex = vbox.getChildren().indexOf((GridPane) ((VBox)((Button)event.getSource()).getParent()).getParent());
		    	 	    couples.remove(cIndex);
						vbox.getChildren().remove((GridPane) ((VBox)((Button)event.getSource()).getParent()).getParent());
					}
				});
			 
			 vCon.getChildren().add(deleteBtn);

		
			 addDelField.setOnAction(new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	if(addDelField.isSelected()){
		            		addDelField.setText("Add user field");
		            		hb.getChildren().remove(0);
		    	 	        col1.setPercentWidth(80);
		    	 	        col2.setPercentWidth(0);
		    	 	        requiredT.setVisible(false);
		    	 	       int cIndex = vbox.getChildren().indexOf((GridPane) ((VBox)((ToggleButton)event.getSource()).getParent()).getParent());
		    	 	        updateUFArray(cIndex, true);
	            		
		            	} else {
		            		addDelField.setText("Delete user field");
		            		TextArea dummy = new TextArea();
		            		dummy.setPromptText("User input");
		        			dummy.setDisable(true);
		        			hb.getChildren().add(dummy);
		    	 	        col1.setPercentWidth(40);
		    	 	        col2.setPercentWidth(40);
		    	 	        requiredT.setVisible(true);
		            		int cIndex = vbox.getChildren().indexOf((GridPane) ((VBox)((ToggleButton)event.getSource()).getParent()).getParent());
		    	 	        updateUFArray(cIndex, false);
		            	}
		            }
		        });
			 vCon.getChildren().add(addDelField);
			 vCon.setAlignment(Pos.CENTER_LEFT);
			 
		     gridpane.add(ta, 0, 1); 
		     gridpane.add(hb, 1, 1);
 			 gridpane.add(vCon, 2, 1);

 			 
 			
 	        gridpane.getRowConstraints().addAll(row1);
 	        gridpane.getColumnConstraints().addAll(col1,col2,col3);
 	        
			
			vbox.getChildren().add(gridpane);
			ta.setPromptText("Heading Title: " + vbox.getChildren().size());
			ta2.setPromptText("User inout");
			ta2.setDisable(true);
			gridpane.setHgap(10);
			gridpane.setVgap(10);
			gridpane.setPrefHeight(0);
			sp.setVvalue(1.0);
			
			Couple dummyC = new Couple(ta.getText(), false, false);
			couples.add(dummyC);
			
			
		}  else if (b == createbtn) {
			//Add title to the array list
			Wips wips = Wips.getInstance();
			updateTArray(0, tedit.getText());
			//Add description to the array list
			updateTArray(1, dedit.getText());
			Form form = new Form("name",wips.getCurrentWorkFlow());
			form.addCouple(couples);
			wips.getCurrentWorkFlow().setForm(form);
			wips.getAllWorkFlows().add(wips.getCurrentWorkFlow());
			wips.getCurrentuser().getAllWorkflows().add(wips.getCurrentWorkFlow());
			wips.setCurrentWorkFlow(null);
			Parent l = FXMLLoader.load(getClass().getResource("/view/developer/dhomescreen.fxml"));
			OpenScreen.openScreen("dhomescreen.fxml", handler, "Home Screen", l, getClass(),"/view/developer/dhomescreen.css");
		
		} else if (b == logoutbtn) {
			Parent l = FXMLLoader.load(getClass().getResource("/view/session/userlogin.fxml"));
			OpenScreen.openScreen("userlogin.fxml", handler, "Log in", l, getClass(),"/view/session/application.css");
		} else if (b == backbtn) {
			Parent l = FXMLLoader.load(getClass().getResource("/view/developer/dstatepscreen.fxml"));
			OpenScreen.openScreen("dstatepscreen.fxml", handler, "State Permission", l, getClass(),"/view/developer/dstatepscreen.css");
		}
	}

}
