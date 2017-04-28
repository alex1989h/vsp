package impl.models;

import java.util.List;
import java.util.LinkedList;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;

public class StatusMessage implements ICaDSEV3RobotStatusListener,ICaDSEV3RobotFeedBackListener {
	
	private List<IStatusMessage> observers;
	
	public StatusMessage() {
		observers = new LinkedList<IStatusMessage>();
	}
	
	public void addObserver(IStatusMessage status){
		observers.add(status);
	}
	
	@Override
	public void giveFeedbackByJSonTo(JSONObject arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusMessage(JSONObject arg0) {
		for (int i = 0; i < observers.size(); i++) {
			observers.get(i).onStatusMessage(arg0);
		};
	}
	

}
