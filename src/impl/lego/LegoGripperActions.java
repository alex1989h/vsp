package impl.lego;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.json.simple.JSONObject;

import impl.factories.FiFo;
import impl.factories.FiFoFactory;
import impl.models.IStatusMessage;

public class LegoGripperActions extends Thread implements IStatusMessage{
	private static CaDSEV3RobotHAL caller = null;
	public LegoGripperActions() {
		caller = CaDSEV3RobotHAL.createInstance(CaDSEV3RobotType.SIMULATION, null, null);
	}

	@Override
	public void run() {
		FiFo fifo = FiFoFactory.getFiFo("gripper");
		byte[] command = null;
		while (!isInterrupted()) {
			command = fifo.dequeue();
			switch (new String(command)) {
			case "open":
				open();
				break;
			case "close":
				close();
				break;
			default:
				break;
			}
		}
	}
	
	private void open(){
		caller.doOpen();
	}
	
	private void close(){
		caller.doClose();
	}

	@Override
	public void onStatusMessage(JSONObject arg0) {
		// TODO Auto-generated method stub
		
	}
}
