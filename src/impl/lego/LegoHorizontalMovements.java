package impl.lego;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.json.simple.JSONObject;

import impl.factories.FiFo;
import impl.factories.FiFoFactory;
import impl.models.IStatusMessage;

public class LegoHorizontalMovements extends Thread implements IStatusMessage{
	private static CaDSEV3RobotStudentImplementation caller = null;
	private long percent = 0, realPercent = 0;
	private enum MoveStatus {STOP,RIGHT,LEFT}
	private MoveStatus status = MoveStatus.STOP;
	
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
		if (this.percent < this.realPercent) {
			caller.stop_h();
			status = MoveStatus.RIGHT;
			caller.moveRight();
		} else if (this.percent > this.realPercent) {
			caller.stop_h();
			status = MoveStatus.LEFT;
			caller.moveLeft();
		}
		status = MoveStatus.STOP;
	}

	@Override
	public void onStatusMessage(JSONObject arg0) {
		if(((String)arg0.get("state")).equals("horizontal")){
			realPercent = (Long)arg0.get("percent");
			if(status == MoveStatus.LEFT && realPercent >= percent){
				caller.stop_h();
			}else if(status == MoveStatus.RIGHT && realPercent <= percent){
				caller.stop_h();
			}
		}
	}
}
