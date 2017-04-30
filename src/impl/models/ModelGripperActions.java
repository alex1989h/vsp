package impl.models;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IGripperActions;

public class ModelGripperActions implements IGripperActions{
	private FiFo fifo = null;
	private final String open = "open";
	private final String close = "close";
	
	public ModelGripperActions() {
		fifo = FiFoFactory.getFiFo("gripper");
	}

	@Override
	public int openGripper(int transactionID) {
		fifo.enqueue(open.getBytes());
		return 0;
	}

	@Override
	public int closeGripper(int transactionID) {
		fifo.enqueue(close.getBytes());
		return 0;
	}
}
