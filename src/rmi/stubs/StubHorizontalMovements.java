package rmi.stubs;

import java.net.SocketException;
import java.net.UnknownHostException;
import rmi.communication.Sender;
import rmi.interfaces.IHorizontalMovements;
import rmi.namespace.Namespace;
import rmi.xml.MyXML;

public class StubHorizontalMovements implements IHorizontalMovements {
	private Sender sender = null;
	
	public StubHorizontalMovements()  throws UnknownHostException, SocketException {
		sender = new Sender();
	}
		
	@Override
	public int moveHorizontalToPercent(int percent) {
		System.out.println("Move horizontal.. ");
		String str = MyXML.createMethodCall("int",Namespace.getName()+".moveHorizontalToPercent", percent);
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[0];
	}
	

}
