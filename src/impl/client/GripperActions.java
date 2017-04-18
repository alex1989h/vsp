package impl.client;

import java.lang.reflect.Method;

import impl.factories.FiFoFactory;
import impl.interfaces.IGripperActions;
import impl.xml.MyXML;

public class GripperActions implements IGripperActions {
	private FiFo fifo = null;
	
	public GripperActions() {
		fifo = FiFoFactory.getFiFo("transmitterGripper");
	}
	
	@Override
	public int openGripper(int transactionID) {
		System.out.println("open.... TID: " + transactionID);
		Method method = new Object(){}.getClass().getEnclosingMethod();
		String str = MyXML.createXMLString(method, transactionID);
		fifo.enqueue(str.getBytes());
		return 0;
	}

	@Override
	public int closeGripper(int transactionID) {
		System.out.println("Close.... TID: " + transactionID);
		Method method = new Object(){}.getClass().getEnclosingMethod();
		String str = MyXML.createXMLString(method, transactionID);
		fifo.enqueue(str.getBytes());
		return 0;
	}

}
