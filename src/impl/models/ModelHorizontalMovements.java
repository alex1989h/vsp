package impl.models;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import rpc.interfaces.IHorizontalMovements;

public class ModelHorizontalMovements implements IHorizontalMovements{
	private FiFo fifo = null;
	public ModelHorizontalMovements() {
		fifo = FiFoFactory.getFiFo("horizontal");
	}
	@Override
	public int moveHorizontalToPercent(int percent) {
		fifo.enqueue((""+percent).getBytes());
		return 0;
	}

}
