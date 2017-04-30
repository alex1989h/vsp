package impl.stubs;

import java.net.SocketException;
import java.net.UnknownHostException;
import impl.client.Sender;
import impl.interfaces.IVerticalMovements;
import impl.namespace.Namespace;
import impl.xml.MyXML;

public class StubVerticalMovements implements IVerticalMovements {
	private Sender sender = null;
	
	public StubVerticalMovements()  throws UnknownHostException, SocketException {
		sender = new Sender();
	}
		
	@Override
	public int moveVerticalToPercent(int transactionID, int percent) {
		System.out.format("Move vertical.. ID:%s percent:%s\n",transactionID, percent);
		String str = MyXML.createMethodCall("int",Namespace.getName()+".moveVerticalToPercent", transactionID, percent);
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[1];
	}
	

}
