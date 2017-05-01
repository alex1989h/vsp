package impl.models;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import rpc.interfaces.IVerticalMovements;

public class ModelVerticalMovements implements IVerticalMovements {
	private FiFo fifo = null;
	public ModelVerticalMovements() {
		fifo = FiFoFactory.getFiFo("vertical");
	}
	@Override
	public int moveVerticalToPercent(int percent) {
		fifo.enqueue((""+percent).getBytes());
		return 0;
	}
}
