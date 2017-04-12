package impl.client;

import impl.interfaces.IHorizontalMovements;

public class HorizontalMovements implements IHorizontalMovements {
	private FiFo<Packet> fifo = null;

	public HorizontalMovements(Sender sender) {
		fifo = sender.getFiFo();
	}

	@Override
	public int moveHorizontalToPercent(int transactionID, int percent) {
		System.out.println("Call to move horizontal -  TID: " + transactionID + " degree " + percent);
		fifo.enqueue(new Packet(Befehl.MOVE_HORIZONTAL, transactionID, percent));
		return 0;
	}

}
