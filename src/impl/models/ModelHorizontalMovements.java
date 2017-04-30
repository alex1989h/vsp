package impl.models;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IHorizontalMovements;

public class ModelHorizontalMovements implements IHorizontalMovements{
	private FiFo fifo = null;
	public ModelHorizontalMovements() {
		fifo = FiFoFactory.getFiFo("horizontal");
	}
	@Override
	public int moveHorizontalToPercent(int transactionID, int percent) {
		fifo.enqueue((""+percent).getBytes());
		return 0;
	}

}
