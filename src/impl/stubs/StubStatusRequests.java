package impl.stubs;

import java.net.SocketException;
import java.net.UnknownHostException;
import impl.client.Sender;
import impl.interfaces.IStatusRequests;
import impl.namespace.Namespace;
import impl.xml.MyXML;

public class StubStatusRequests implements IStatusRequests {
	private Sender sender = null;
	
	public StubStatusRequests()  throws UnknownHostException, SocketException {
		sender = new Sender();
	}
		
	@Override
	public int getHorizontalInPercent(int transactionID) {
		System.out.format("Ask for Horizontal Position.. ID:%s\n",transactionID);
		String str = MyXML.createMethodCall("int",Namespace.getName()+".getHorizontalInPercent", transactionID);
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[1];
	}
	
	@Override
	public int getVerticalInPercent(int transactionID) {
		System.out.format("Ask for Vertical Position.. ID:%s\n",transactionID);
		String str = MyXML.createMethodCall("int",Namespace.getName()+".getVerticalInPercent", transactionID);
		byte[] reply = sender.send(str.getBytes());
		return (int)MyXML.createXML(reply).getParamValues()[1];
	}
	
	@Override
	public String getGripperStatus(int transactionID) {
		System.out.format("Ask for Gripper Status.. ID:%s\n",transactionID);
		String str = MyXML.createMethodCall("String",Namespace.getName()+".getGripperStatus", transactionID);
		byte[] reply = sender.send(str.getBytes());
		return (String)MyXML.createXML(reply).getParamValues()[1];
	}
	

}
