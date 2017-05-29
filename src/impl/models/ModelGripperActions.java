package impl.models;

import impl.factories.FiFo;
import impl.factories.FiFoFactory;
import rmi.interfaces.IGripperActions;

public class ModelGripperActions implements IGripperActions{
	private FiFo fifo = null;
	private final String open = "open";
	private final String close = "close";
	
	public ModelGripperActions() {
		fifo = FiFoFactory.getFiFo("gripper");
	}

	@Override
	public int openGripper() {
		fifo.enqueue(open.getBytes());
		return 0;
	}

	@Override
	public int closeGripper() {
		fifo.enqueue(close.getBytes());
		return 0;
	}
}
