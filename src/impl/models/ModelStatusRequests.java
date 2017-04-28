package impl.models;

import org.json.simple.JSONObject;

import impl.interfaces.IStatusRequests;
import impl.interfaces.IStatusResponses;
import impl.stubs.StubStatusResponses;

public class ModelStatusRequests implements IStatusRequests, IStatusMessage {
	
	private long vertical;
	private long horizontal;
	private String gripper;
	private static int serverTransactionsID = Integer.MIN_VALUE;
	
	IStatusResponses response = new StubStatusResponses();
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
	public int getHorizontalInPercent(int transactionID) {
		response.getHorizontalInPercent(serverTransactionsID++, horizontal);
		return 0;
	}

	@Override
	public int getVerticalInPercent(int transactionID) {
		response.getVerticalInPercent(serverTransactionsID++, vertical);
		return 0;
	}

	@Override
	public int getGripperStatus(int transactionID) {
		response.getGripperStatus(serverTransactionsID++, gripper);
		return 0;
	}

}
