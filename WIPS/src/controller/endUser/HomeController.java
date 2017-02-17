package controller.endUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controller.session.LogOutController;
import helper.OpenScreen;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import model.Wips;
import model.user.EndUser;
import model.wips.Entity;
import model.wips.WorkFlow;
import model.wips.forms.Form;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class HomeController {

	@FXML
	Button notibtn, logoutbtn, allwfbtn;

	@FXML
	ListView<WorkFlow> allwflist, jwflist, notilist;

	@FXML
	ComboBox<Entity> cbox;

	@FXML
	TabPane tabpane;

	@FXML
	Tab allworkflows, joinedworkflows, notification;

	@FXML
	Label statuslabel, labelallwf, labeljoinedwf, labelnoti;

	@FXML
	VBox vboxalltab, vboxjoinedtab, vboxnotitab;

	private ObservableList<WorkFlow> allwflistOb, jwflistOb, notilistOb;

	private void anchorPaneVisibility(boolean b) {
		labelallwf.setVisible(b);
		labeljoinedwf.setVisible(b);
		labelnoti.setVisible(b);

		vboxalltab.setVisible(!b);
		vboxjoinedtab.setVisible(!b);
		vboxnotitab.setVisible(!b);
	}

	/**
	 * This method will open “All workflow” tab.
	 */
	public void allWorkFlowController() {
		AllWorkFlowController allWfController = new AllWorkFlowController();
		allwflistOb = FXCollections.observableArrayList(allWfController.getAllWorkFlowsCanJoin());
		allwflist.setItems(allwflistOb);
		if (allwflistOb.size() < 1) {
			allwfbtn.setDisable(true);
		} 
	}

	@FXML
	protected void initialize() {
		allWorkflowBtnDisabler();
		anchorPaneVisibility(true);
		allWorkFlowController();
		updates();
		populate();
		tabListeners();
		// allWorkFlowController();
		// notif();
	}
	
	private void allWorkflowBtnDisabler() {
		allwfbtn.setDisable(true);
		allwflist.getSelectionModel().selectedItemProperty()
	     .addListener(new ChangeListener<WorkFlow>() {
	       public void changed(ObservableValue<? extends WorkFlow> observable,
	    		   WorkFlow oldValue, WorkFlow newValue) {
	    	   allwfbtn.setDisable(false); 
	       }
	     });
		
		notibtn.setDisable(true);
	}

	public void updates() {
		EndUser user = (EndUser) Wips.getInstance().getCurrentuser();
		if (user.getNumOfUpdates() > 0) {
			joinedworkflows.setText("Joined WorkFlows ( " + user.getNumOfUpdates() + " )");
		} else {
			joinedworkflows.setText("Joined WorkFlows");
		}

		if (user.getNumOfNotif() > 0) {
			notification.setText("Notifications ( " + user.getNumOfNotif() + " )");
		} else {
			notification.setText("Notifications");
		}
	}

	public void tabListeners() {
		tabpane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
			if (newTab.equals(allworkflows)) {
				allWorkFlowController();
			} else if (newTab.equals(joinedworkflows)) {
				jwflist.getSelectionModel().clearSelection();
				allJoinedWorkFlows();
			} else if (newTab.equals(notification)) {
				notif();
			}
		});
	}

	public void notif() {
		EndUser u = (EndUser) Wips.getInstance().getCurrentuser();
		// notilist.getSelectionModel().selectedItemProperty().addListener(e ->
		// {
		// int indexOfWorkFlow =
		// notilist.getSelectionModel().getSelectedIndex();
		// if(indexOfWorkFlow >= 0){
		// Wips.getInstance().getCurrentuser().getAllWorkflows().get(indexOfWorkFlow).setHasUpdate(u,
		// false);
		// status(Wips.getInstance().getCurrentuser().getAllWorkflows().get(indexOfWorkFlow).getForm());
		//// EndUser u = (EndUser) Wips.getInstance().getCurrentuser();
		// u.update();
		//
		// System.out.println("new work flow iupdae size" +
		// Wips.getInstance().getCurrentuser().getAllWorkflows().size());
		// System.out.println("form lsize of obsrvabel " + jwflistOb.size());
		// // allJoinedWorkFlows();
		// notilist.setItems(null);
		// notilistOb =
		// FXCollections.observableArrayList(Wips.getInstance().getCurrentuser().getAllWorkflows());
		// jwflist.setItems(jwflistOb);
		// updates();
		// }
		// });
		if (u.getRecievedForm().size() > 0) {
			AllWorkFlowController all = new AllWorkFlowController();
			notilistOb = FXCollections.observableArrayList(all.filterNotifWorkFlows());
			notilist.setItems(notilistOb);
			notibtn.setDisable(false);
		} 
		if(u.getRecievedForm().size()<1){
			notibtn.setDisable(true);
		}
		notilist.getSelectionModel().selectedItemProperty().addListener((e) -> {
			notibtn.setDisable(false);
		});
	}

	public void allJoinedWorkFlows() {
		// EndUser u = (EndUser) Wips.getInstance().getCurrentuser();
		// if (u.getAllWorkflows().size() > 0 ) {
		AllWorkFlowController all = new AllWorkFlowController();
		jwflistOb = FXCollections.observableArrayList(all.filterJoinedWorkFlows());
		jwflist.setItems(jwflistOb);
		jwflist.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<WorkFlow>() {

			@Override
			public void changed(ObservableValue<? extends WorkFlow> observable, WorkFlow oldValue, WorkFlow newValue) {
				int indexOfJoinedWorkFlow = jwflist.getSelectionModel().getSelectedIndex();
				if (indexOfJoinedWorkFlow >= 0) {
					Wips.getInstance().getCurrentuser().getAllWorkflows().get(indexOfJoinedWorkFlow)
							.setHasUpdate((EndUser) Wips.getInstance().getCurrentuser(), false);
					status(Wips.getInstance().getCurrentuser().getAllWorkflows().get(indexOfJoinedWorkFlow).getForm());
					EndUser u = (EndUser) Wips.getInstance().getCurrentuser();
					u.update();

					// allJoinedWorkFlows();
					jwflist.setItems(null);
					jwflistOb = FXCollections
							.observableArrayList(Wips.getInstance().getCurrentuser().getAllWorkflows());
					jwflist.setItems(jwflistOb);
					notilist.setItems(null);
					notilistOb = FXCollections.observableArrayList(u.getRecievedForm());
					notilist.setItems(notilistOb);
					updates();
				}
			}
		});
		// }
	}

	public void populate() {
		cbox.getItems().addAll(Wips.getInstance().getCurrentuser().getRoles());
		if (Wips.getInstance().getRoleOfCurrentUser() != null) {
			cbox.getSelectionModel().select(Wips.getInstance().getRoleOfCurrentUser());
			anchorPaneVisibility(false);
		}
		cbox.setOnAction((event) -> {
			anchorPaneVisibility(false);
			Entity e = cbox.getSelectionModel().getSelectedItem();
			Wips.getInstance().setRoleOfCurrentUser(e);
			allWorkFlowController();
			allJoinedWorkFlows();
			notif();
			// disabler(false);
			// updates();
		});
	}

	/**
	 * This method will open “Active Workflow” tab.
	 */
	public void activeWorkFlows() {

	}

	/**
	 * This method will open “Notifications” tab.
	 */
	public void notifController() {

	}

	public void handle(ActionEvent handler) throws IOException, ClassNotFoundException {
		Button b = (Button) handler.getSource();
		if (b == notibtn) {
			Wips.getInstance().setCurrentWorkFlow(notilist.getSelectionModel().getSelectedItem());
			Parent e = FXMLLoader.load(getClass().getResource("/view/endUser/eformgen.fxml"));
			OpenScreen.openScreen("eformgen.fxml", handler, "Sign in form", e, getClass(),
					"/view/enduser/eformgen.css");
		} else if (b == logoutbtn) {
			LogOutController.logInScreen();
			Parent e = FXMLLoader.load(getClass().getResource("/view/session/userlogin.fxml"));
			OpenScreen.openScreen("userlogin.fxml", handler, "Log in", e, getClass(), "/view/session/application.css");
		} else if (b == allwfbtn) {
			WorkFlow wf = (WorkFlow) allwflist.getSelectionModel().getSelectedItem().clone();
			wf.setActive(true);
			Wips.getInstance().setCurrentWorkFlow(wf);
			Parent e = FXMLLoader.load(getClass().getResource("/view/endUser/eformgen.fxml"));
			OpenScreen.openScreen("eformgen.fxml", handler, "Form", e, getClass(), "/view/endUser/eformgen.css");
		}
	}

	public void status(Form f) {
		StringBuilder sb = new StringBuilder();
		List<Entity> set = new ArrayList<>();
		int index = f.getUsers().indexOf(Wips.getInstance().getCurrentuser());
		for (int i = index; i < f.getRoles().size(); i++) {
			if(!set.contains(f.getRoles().get(i)))
				set.add(f.getRoles().get(i));
		}
		statuslabel.setText("Status: " + set);
	}

	public void disabler(boolean b) {
		tabpane.setDisable(b);
		allwfbtn.setDisable(b);
	}
}