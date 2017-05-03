package rmi.stubs;

import java.net.SocketException;
import java.net.UnknownHostException;
import rmi.communication.Sender;
import rmi.interfaces.IGripperActions;
import rmi.namespace.Namespace;
import rmi.xml.MyXML;

public class StubGripperActions implements IGripperActions {
	private Sender sender = null;
	
	public StubGripperActions()  throws UnknownHostException, SocketException {
		sender = new Sender();
	}
		
	@Override
	public int openGripper() {
		System.out.println("Open.. ");
		String str = MyXML.createMethodCall("int",Namespace.getName()+".openGripper");
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[0];
	}
	
	@Override
	public int closeGripper() {
		System.out.println("Close.. ");
		String str = MyXML.createMethodCall("int",Namespace.getName()+".closeGripper");
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[0];
	}
	

}
