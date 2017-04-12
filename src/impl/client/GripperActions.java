package impl.client;

import impl.interfaces.IGripperActions;

public class GripperActions implements IGripperActions {
	private FiFo<Packet> fifo = null;
	
	public GripperActions(Sender sender) {
		fifo = sender.getFiFo();
	}
	
	@Override
	public int openGripper(int transactionID) {
		System.out.println("open.... TID: " + transactionID);
		fifo.enqueue(new Packet(Befehl.OPEN,transactionID,0));
		return 0;
	}

	@Override
	public int closeGripper(int transactionID) {
		System.out.println("Close.... TID: " + transactionID);
		fifo.enqueue(new Packet(Befehl.CLOSE,transactionID,0));
		return 0;
	}

}
