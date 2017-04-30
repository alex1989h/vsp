package impl.stubs;

import java.net.SocketException;
import java.net.UnknownHostException;
import impl.client.Sender;
import impl.interfaces.IHorizontalMovements;
import impl.namespace.Namespace;
import impl.xml.MyXML;

public class StubHorizontalMovements implements IHorizontalMovements {
	private Sender sender = null;
	
	public StubHorizontalMovements()  throws UnknownHostException, SocketException {
		sender = new Sender();
	}
		
	@Override
	public int moveHorizontalToPercent(int transactionID, int percent) {
		System.out.format("Move horizontal.. ID:%s percent:%s\n",transactionID, percent);
		String str = MyXML.createMethodCall("int",Namespace.getName()+".moveHorizontalToPercent", transactionID, percent);
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[1];
	}
	

}
