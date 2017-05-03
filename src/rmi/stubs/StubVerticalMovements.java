package rmi.stubs;

import java.net.SocketException;
import java.net.UnknownHostException;
import rmi.communication.Sender;
import rmi.interfaces.IVerticalMovements;
import rmi.namespace.Namespace;
import rmi.xml.MyXML;

public class StubVerticalMovements implements IVerticalMovements {
	private Sender sender = null;
	
	public StubVerticalMovements()  throws UnknownHostException, SocketException {
		sender = new Sender();
	}
		
	@Override
	public int moveVerticalToPercent(int percent) {
		System.out.format("Move vertical.. Percent:%s\n", percent);
		String str = MyXML.createMethodCall("int",Namespace.getName()+".moveVerticalToPercent", percent);
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[0];
	}
	

}
