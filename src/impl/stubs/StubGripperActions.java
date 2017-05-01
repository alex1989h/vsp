package impl.stubs;

import java.net.SocketException;
import java.net.UnknownHostException;
import impl.client.Sender;
import impl.interfaces.IGripperActions;
import impl.namespace.Namespace;
import impl.xml.MyXML;

public class StubGripperActions implements IGripperActions {
	private Sender sender = null;
	
	public StubGripperActions()  throws UnknownHostException, SocketException {
		sender = new Sender();
	}
		
	@Override
	public int openGripper() {
		System.out.format("Open.. \n");
		String str = MyXML.createMethodCall("int",Namespace.getName()+".openGripper");
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[0];
	}
	
	@Override
	public int closeGripper() {
		System.out.format("Close.. \n");
		String str = MyXML.createMethodCall("int",Namespace.getName()+".closeGripper");
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[0];
	}
	

}
