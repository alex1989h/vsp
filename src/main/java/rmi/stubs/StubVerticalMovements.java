package rmi.stubs;

import java.net.SocketException;
import java.net.UnknownHostException;
import rmi.communication.Sender;
import rmi.interfaces.IVerticalMovements;
import rmi.namespace.Namespace;
import rmi.xml.MyXML;
import rmi.xml.MyXMLObject;

public class StubVerticalMovements implements IVerticalMovements {
	private Sender sender = null;
	
	public StubVerticalMovements()  throws UnknownHostException, SocketException {
		sender = new Sender();
	}
		
	@Override
	public int moveVerticalToPercent(int percent) {
		System.out.println("Move vertical.. ");
		String str = MyXML.createMethodCall("int",Namespace.getName()+".moveVerticalToPercent", percent);
		byte[] reply = sender.send(str.getBytes());
		MyXMLObject xml = MyXML.createXML(reply);
		xml.print();
		return (int)xml.getParamValues()[0];
	}
	

}
