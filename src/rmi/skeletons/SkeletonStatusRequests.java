package rmi.skeletons;

import rmi.interfaces.IStatusRequests;
import rmi.interfaces.ISkeleton;
import rmi.xml.MyXML;
import rmi.xml.MyXMLObject;
import rmi.communication.Receiver;

public class SkeletonStatusRequests implements ISkeleton {
	private IStatusRequests model;
	private String namespace;
	private long oldId = Long.MIN_VALUE;
	private Receiver receiver = null;
	
	public SkeletonStatusRequests(IStatusRequests model,String namespace) throws Exception {
		this.model = model;
		this.namespace = namespace;
		this.receiver = new Receiver();
		String str;
		str = "<addService><methodName>"+namespace+".getHorizontalInPercent</methodName></addService>";
		receiver.send(str.getBytes());
		str = "<addService><methodName>"+namespace+".getVerticalInPercent</methodName></addService>";
		receiver.send(str.getBytes());
		str = "<addService><methodName>"+namespace+".getGripperStatus</methodName></addService>";
		receiver.send(str.getBytes());

	}
	
	@Override
	public void method() {
		byte[] b;
		try {
		System.out.println("Wait for Message");
		b = receiver.receive();
		System.out.println("Message received");
		MyXMLObject xml = MyXML.createXML(b);
		xml.print();
		if(MyXML.testSignatur(xml, "int", namespace+".getHorizontalInPercent")){
			if (this.oldId < (int)xml.getTransactionsID()) {
				this.oldId = (int)xml.getTransactionsID();
				int r = model.getHorizontalInPercent();
				receiver.send((int)xml.getTransactionsID(),MyXML.createMethodResponse(r).getBytes());
			}
		}
		if(MyXML.testSignatur(xml, "int", namespace+".getVerticalInPercent")){
			if (this.oldId < (int)xml.getTransactionsID()) {
				this.oldId = (int)xml.getTransactionsID();
				int r = model.getVerticalInPercent();
				receiver.send((int)xml.getTransactionsID(),MyXML.createMethodResponse(r).getBytes());
			}
		}
		if(MyXML.testSignatur(xml, "String", namespace+".getGripperStatus")){
			if (this.oldId < (int)xml.getTransactionsID()) {
				this.oldId = (int)xml.getTransactionsID();
				String r = model.getGripperStatus();
				receiver.send((int)xml.getTransactionsID(),MyXML.createMethodResponse(r).getBytes());
			}
		}

		receiver.send((int)xml.getTransactionsID(),MyXML.getConnectError(xml));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
