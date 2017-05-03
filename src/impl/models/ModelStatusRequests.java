package impl.models;

import org.json.simple.JSONObject;

import rmi.interfaces.IStatusRequests;

public class ModelStatusRequests implements IStatusRequests, IStatusMessage {
	
	private long vertical;
	private long horizontal;
	private String gripper;
	
	@Override
	public void onStatusMessage(JSONObject arg0) {
		switch ((String)arg0.get("state")) {
		case "vertical":
			vertical = (Long)arg0.get("percent");
			break;
		case "horizontal":
			horizontal = (Long)arg0.get("percent");
			break;
		case "gripper":
			gripper = (String)arg0.get("value");
			break;
		default:
			break;
		}
	}

	@Override
	public int getHorizontalInPercent() {
		return (int)horizontal;
	}

	@Override
	public int getVerticalInPercent() {
		return (int)vertical;
	}

	@Override
	public String getGripperStatus() {
		return gripper;
	}

}
