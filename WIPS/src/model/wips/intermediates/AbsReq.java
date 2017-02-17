package model.wips.intermediates;

import java.io.Serializable;
import java.util.List;

import model.wips.Entity;
import model.wips.Transition;


//@helper: Kenneth Zhang, Deepkumar Patel, Hassan Shah

public interface AbsReq extends Serializable{
	
	boolean isAllowed();
	void add(Transition t);
	int size();
	void markedSend();
	List<Entity> getEntity();
}
