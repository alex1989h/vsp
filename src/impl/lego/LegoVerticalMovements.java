package impl.lego;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.json.simple.JSONObject;

import impl.factories.FiFo;
import impl.factories.FiFoFactory;
import impl.models.IStatusMessage;

public class LegoVerticalMovements extends Thread implements IStatusMessage{
	private static CaDSEV3RobotStudentImplementation caller = null;
	private long percent = 0,realPercent = 0;
	private enum MoveStatus {STOP,UP,DOWN}
	private MoveStatus status = MoveStatus.STOP;
	
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
		if (this.percent < this.realPercent) {
			caller.stop_v();
			status = MoveStatus.DOWN;
			caller.moveDown();
		} else if (this.percent > this.realPercent) {
			caller.stop_v();
			status = MoveStatus.UP;
			caller.moveUp();
		}
		status = MoveStatus.STOP;
	}
	
	@Override
	public void onStatusMessage(JSONObject arg0) {
		if(((String)arg0.get("state")).equals("vertical")){
			realPercent = (Long)arg0.get("percent");
			if(status == MoveStatus.UP && realPercent >= percent){
				caller.stop_v();
			}else if(status == MoveStatus.DOWN && realPercent <= percent){
				caller.stop_v();
			}
		}
	}
}
