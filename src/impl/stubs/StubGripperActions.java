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
	public int openGripper(int transactionID) {
		System.out.format("Open.. ID:%s\n",transactionID);
		String str = MyXML.createMethodCall("int",Namespace.getName()+".openGripper", transactionID);
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[1];
	}
	
	@Override
	public int closeGripper(int transactionID) {
		System.out.format("Close.. ID:%s\n",transactionID);
		String str = MyXML.createMethodCall("int",Namespace.getName()+".closeGripper", transactionID);
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[1];
	}
	

}
