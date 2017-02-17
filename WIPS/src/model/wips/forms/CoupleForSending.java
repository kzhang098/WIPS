package model.wips.forms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import model.Wips;
import model.user.EndUser;

//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah


public class CoupleForSending {

	private ListView<String> distinctValues;
	private ListView<EndUser> filteredEndUser;
	private String label = "";
	private ObservableList<String> distinctValuesOb;
	private ObservableList<EndUser> filteredEndUserOb;
	
	public CoupleForSending(Set<String> distinctVal) {
		distinctValues = new ListView<String>();
		List<String> vals = new ArrayList<>(distinctVal);
		filteredEndUserOb = FXCollections.observableArrayList();
		filteredEndUser = new ListView<EndUser>();
		filteredEndUser.setItems(filteredEndUserOb);
		distinctValuesOb = FXCollections.observableArrayList(vals);
		distinctValues.setItems(distinctValuesOb);
		listner();
	}
	public void setDistinctOb(List<String> disticnVal) {
		distinctValuesOb.addAll(distinctValuesOb);
		distinctValues.setItems(distinctValuesOb);
	}
	
	public void filterUser(String val) {
		Wips wips = Wips.getInstance();
		filteredEndUserOb = FXCollections.observableArrayList();
		List<EndUser> allUser = wips.getEndUser();
		for(int i = 0; i<allUser.size(); i++) {
			if(!allUser.get(i).equals(wips.getCurrentuser()) && allUser.get(i).checkValue(val))
				filteredEndUserOb.add(allUser.get(i));
		}
//		EndUser user = new EndUser("the guy", true);
//		filteredEndUserOb = FXCollections.observableArrayList();
//		filteredEndUserOb.add(user);
		filteredEndUser.setItems(filteredEndUserOb);
	//	wips.addUser(user);
	}
	
	public EndUser getEndUser() {
		EndUser user = filteredEndUser.getSelectionModel().getSelectedItem();
		return user;
	}
	public void listner() {
		distinctValues.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String> () {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String val = distinctValues.getSelectionModel().getSelectedItem();
				filterUser(val);
			}
			
		});
	}
	
	public CoupleForSending(){
		distinctValues = new ListView<>();
		filteredEndUser = new ListView<>();
	}
	
	public ListView<String> getdisVal(){
		return distinctValues;
	}
	
	public ListView<EndUser> getfilUser() {
		return filteredEndUser;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
}
