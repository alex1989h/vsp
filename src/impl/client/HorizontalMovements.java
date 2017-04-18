package impl.client;

import java.lang.reflect.Method;

import impl.factories.FiFoFactory;
import impl.interfaces.IHorizontalMovements;
import impl.xml.MyXML;

public class HorizontalMovements implements IHorizontalMovements {
	private FiFo fifo = null;

	public HorizontalMovements() {
		fifo = FiFoFactory.getFiFo("transmitterHorizontal");
	}

	@Override
	public int moveHorizontalToPercent(int transactionID, int percent) {
		System.out.println("Call to move horizontal -  TID: " + transactionID + " degree " + percent);
		Method method = new Object(){}.getClass().getEnclosingMethod();
		String str = MyXML.parse(method, transactionID, percent);
		fifo.enqueue(str.getBytes());
		return 0;
	}

}
