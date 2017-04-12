package impl.client;

import impl.interfaces.IVerticalMovements;

public class VerticalMovements implements IVerticalMovements {
	private FiFo<Packet> fifo = null;
	public VerticalMovements(Sender sender) {
		fifo = sender.getFiFo();
	}
	
	@Override
	public int moveVerticalToPercent(int transactionID,int percent) {
		System.out.println("Call to move vertical -  TID: " + transactionID + " degree " + percent);
		fifo.enqueue(new Packet(Befehl.MOVE_VERTICAL, transactionID, percent));
		return 0;
	}

}
