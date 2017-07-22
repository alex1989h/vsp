package rmi.skeletons;

import rmi.interfaces.IGripperActions;
import rmi.interfaces.ISkeleton;
import rmi.xml.MyXML;
import rmi.xml.MyXMLObject;
import rmi.communication.Receiver;

public class SkeletonGripperActions implements ISkeleton {
	private IGripperActions model;
	private String namespace;
	private long oldId = Long.MIN_VALUE;
	private Receiver receiver = null;
	
	public SkeletonGripperActions(IGripperActions model,String namespace) throws Exception {
		this.model = model;
		this.namespace = namespace;
		this.receiver = new Receiver();
		String str;
		str = "<addService><methodName>"+namespace+".openGripper</methodName></addService>";
		receiver.send(str.getBytes());
		str = "<addService><methodName>"+namespace+".closeGripper</methodName></addService>";
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
		if(MyXML.testSignatur(xml, "int", namespace+".openGripper")){
			if (this.oldId < (int)xml.getTransactionsID()) {
				this.oldId = (int)xml.getTransactionsID();
				int r = model.openGripper();
				receiver.send((int)xml.getTransactionsID(),MyXML.createMethodResponse(r).getBytes());
			}
		}
		if(MyXML.testSignatur(xml, "int", namespace+".closeGripper")){
			if (this.oldId < (int)xml.getTransactionsID()) {
				this.oldId = (int)xml.getTransactionsID();
				int r = model.closeGripper();
				receiver.send((int)xml.getTransactionsID(),MyXML.createMethodResponse(r).getBytes());
			}
		}

		receiver.send((int)xml.getTransactionsID(),MyXML.getConnectError(xml));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
