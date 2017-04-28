package impl.models;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.json.simple.JSONObject;

import impl.interfaces.IGripperActions;

public class ModelGripperActions implements IGripperActions,IStatusMessage{
	private static CaDSEV3RobotStudentImplementation caller = null;

	public ModelGripperActions() {
		caller = CaDSEV3RobotStudentImplementation.createInstance(CaDSEV3RobotType.SIMULATION, null, null);
	}

	@Override
	public int openGripper(int transactionID) {
		caller.doOpen();
		return 0;
	}

	@Override
	public int closeGripper(int transactionID) {
		caller.doClose();
		return 0;
	}
	
	@Override
	public void onStatusMessage(JSONObject arg0) {
		// TODO Auto-generated method stub
		
	}

}
