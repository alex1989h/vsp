package rpc.stubs;

import java.net.SocketException;
import java.net.UnknownHostException;
import rpc.communication.Sender;
import rpc.interfaces.IHorizontalMovements;
import rpc.namespace.Namespace;
import rpc.xml.MyXML;

public class StubHorizontalMovements implements IHorizontalMovements {
	private Sender sender = null;
	
	public StubHorizontalMovements()  throws UnknownHostException, SocketException {
		sender = new Sender();
	}
		
	@Override
	public int moveHorizontalToPercent(int percent) {
		System.out.format("Move horizontal.. Percent:%s\n", percent);
		String str = MyXML.createMethodCall("int",Namespace.getName()+".moveHorizontalToPercent", percent);
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[0];
	}
	

}
