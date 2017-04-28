package impl.models;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.json.simple.JSONObject;

import impl.interfaces.IVerticalMovements;

public class ModelVerticalMovements implements IVerticalMovements, IStatusMessage {
	private static CaDSEV3RobotStudentImplementation caller = null;
	
	private long percent = 0,oldPercent = 0;
	
	public ModelVerticalMovements() {
		caller = CaDSEV3RobotStudentImplementation.createInstance(CaDSEV3RobotType.SIMULATION, null, null);
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

	@Override
	public int moveVerticalToPercent(int transactionID, int percent) {
		this.percent = percent;
		if (this.percent < this.oldPercent) {
			caller.stop_v();
			caller.moveDown();
		} else if (this.percent > this.oldPercent) {
			caller.stop_v();
			caller.moveUp();
		}
		return 0;
	}
}
