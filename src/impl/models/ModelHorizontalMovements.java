package impl.models;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;

import impl.interfaces.IHorizontalMovements;

public class ModelHorizontalMovements implements IHorizontalMovements, IStatusMessage  {
	private static CaDSEV3RobotStudentImplementation caller = null;
	private long percent = 0,oldPercent = 0;
	public ModelHorizontalMovements() {
		caller = CaDSEV3RobotStudentImplementation.createInstance(CaDSEV3RobotType.SIMULATION, null, null);
	}
	@Override
	public int moveHorizontalToPercent(int transactionID, int percent) {
		this.percent = percent;
		if (this.percent < this.oldPercent) {
			caller.stop_h();
			caller.moveRight();;
		} else if (this.percent > this.oldPercent) {
			caller.stop_h();
			caller.moveLeft();
		}
		return 0;
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
