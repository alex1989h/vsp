package impl.lego;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.json.simple.JSONObject;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.models.IStatusMessage;

public class LegoVerticalMovements extends Thread implements IStatusMessage{
	private static CaDSEV3RobotStudentImplementation caller = null;
	private long percent = 0,oldPercent = 0;
	public LegoVerticalMovements() {
		caller  = CaDSEV3RobotStudentImplementation.createInstance(CaDSEV3RobotType.SIMULATION, null, null);
		
	}
	@Override
	public void run() {
		FiFo fifo = FiFoFactory.getFiFo("vertical");
		byte[] command = null;
		while (!isInterrupted()) {
			command = fifo.dequeue();
			moveVerticalToPercent(Integer.parseInt(new String(command)));
		}
	}
	
	public void moveVerticalToPercent(int percent) {
		this.percent = percent;
		if (this.percent < this.oldPercent) {
			caller.stop_v();
			caller.moveDown();
		} else if (this.percent > this.oldPercent) {
			caller.stop_v();
			caller.moveUp();
		}
	}
	@Override
	public void onStatusMessage(JSONObject arg0) {
		Long l = null;
		if(((String)arg0.get("state")).equals("vertical")){
			l = (Long)arg0.get("percent");
			if(percent > oldPercent && l >= percent){
				caller.stop_v();
				oldPercent = percent;
			}else if(percent < oldPercent && l <= percent){
				caller.stop_v();
				oldPercent = percent;
			}
		}
	}
}
