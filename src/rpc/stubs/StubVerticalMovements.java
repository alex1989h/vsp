package rpc.stubs;

import java.net.SocketException;
import java.net.UnknownHostException;
import rpc.communication.Sender;
import rpc.interfaces.IVerticalMovements;
import rpc.namespace.Namespace;
import rpc.xml.MyXML;

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
