package rmi.stubs;

import java.net.SocketException;
import java.net.UnknownHostException;
import rmi.communication.Sender;
import rmi.interfaces.IStatusRequests;
import rmi.namespace.Namespace;
import rmi.xml.MyXML;
import rmi.xml.MyXMLObject;

public class StubStatusRequests implements IStatusRequests {
	private Sender sender = null;
	
	public StubStatusRequests()  throws UnknownHostException, SocketException {
		sender = new Sender();
	}
		
	@Override
	public int getHorizontalInPercent() {
		System.out.println("Ask for Horizontal Position.. ");
		String str = MyXML.createMethodCall("int",Namespace.getName()+".getHorizontalInPercent");
		byte[] reply = sender.send(str.getBytes());
		MyXMLObject xml = MyXML.createXML(reply);
		xml.print();
		return (int)xml.getParamValues()[0];
	}
	
	@Override
	public int getVerticalInPercent() {
		System.out.println("Ask for Vertical Position.. ");
		String str = MyXML.createMethodCall("int",Namespace.getName()+".getVerticalInPercent");
		byte[] reply = sender.send(str.getBytes());
		MyXMLObject xml = MyXML.createXML(reply);
		xml.print();
		return (int)xml.getParamValues()[0];
	}
	
	@Override
	public String getGripperStatus() {
		System.out.println("Ask for Gripper Status.. ");
		String str = MyXML.createMethodCall("String",Namespace.getName()+".getGripperStatus");
		byte[] reply = sender.send(str.getBytes());
		MyXMLObject xml = MyXML.createXML(reply);
		xml.print();
		return (String)xml.getParamValues()[0];
	}
	

}
