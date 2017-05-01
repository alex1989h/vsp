package rpc.stubs;

import java.net.SocketException;
import java.net.UnknownHostException;
import rpc.communication.Sender;
import rpc.interfaces.IStatusRequests;
import rpc.namespace.Namespace;
import rpc.xml.MyXML;

public class StubStatusRequests implements IStatusRequests {
	private Sender sender = null;
	
	public StubStatusRequests()  throws UnknownHostException, SocketException {
		sender = new Sender();
	}
		
	@Override
	public int getHorizontalInPercent() {
		System.out.format("Ask for Horizontal Position.. \n");
		String str = MyXML.createMethodCall("int",Namespace.getName()+".getHorizontalInPercent");
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[0];
	}
	
	@Override
	public int getVerticalInPercent() {
		System.out.format("Ask for Vertical Position.. \n");
		String str = MyXML.createMethodCall("int",Namespace.getName()+".getVerticalInPercent");
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[0];
	}
	
	@Override
	public String getGripperStatus() {
		System.out.format("Ask for Gripper Status.. \n");
		String str = MyXML.createMethodCall("String",Namespace.getName()+".getGripperStatus");
		byte[] reply = sender.send(str.getBytes());
		return (String)MyXML.createXML(reply).getParamValues()[0];
	}
	

}
