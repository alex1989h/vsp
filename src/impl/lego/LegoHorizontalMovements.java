package impl.lego;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.json.simple.JSONObject;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.models.IStatusMessage;

public class LegoHorizontalMovements extends Thread implements IStatusMessage{
	private static CaDSEV3RobotStudentImplementation caller = null;
	private long percent = 0, oldPercent = 0;

	public LegoHorizontalMovements() {
		caller = CaDSEV3RobotStudentImplementation.createInstance(CaDSEV3RobotType.SIMULATION, null, null);

	}

	@Override
	public void run() {
		FiFo fifo = FiFoFactory.getFiFo("horizontal");
		byte[] command = null;
		while (!isInterrupted()) {
			command = fifo.dequeue();
			moveHorizontalToPercent(Integer.parseInt(new String(command)));
		}
	}
	
	private void moveHorizontalToPercent(int percent) {
		this.percent = percent;
		if (this.percent < this.oldPercent) {
			caller.stop_h();
			caller.moveRight();;
		} else if (this.percent > this.oldPercent) {
			caller.stop_h();
			caller.moveLeft();
		}
	}

	@Override
	public void onStatusMessage(JSONObject arg0) {
		Long l = null;
		if(((String)arg0.get("state")).equals("horizontal")){
			l = (Long)arg0.get("percent");
			if(percent > oldPercent && l >= percent){
				caller.stop_h();
				oldPercent = percent;
			}else if(percent < oldPercent && l <= percent){
				caller.stop_h();
				oldPercent = percent;
			}
		}
	}
}
