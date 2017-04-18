package impl.client;

import java.lang.reflect.Method;
import impl.factories.FiFoFactory;
import impl.interfaces.IVerticalMovements;
import impl.xml.MyXML;

public class VerticalMovements implements IVerticalMovements {
	private FiFo fifo = null;
	
	public VerticalMovements() {
		fifo = FiFoFactory.getFiFo("transmitterVertical");
	}
	
	@Override
	public int moveVerticalToPercent(int transactionID,int percent) {
		System.out.println("Call to move vertical -  TID: " + transactionID + " degree " + percent);
		Method method = new Object(){}.getClass().getEnclosingMethod();
		String str = MyXML.parse(method, transactionID, percent);
		fifo.enqueue(str.getBytes());
		return 0;
	}
}
