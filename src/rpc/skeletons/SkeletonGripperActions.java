package rpc.skeletons;

import rpc.interfaces.IGripperActions;
import rpc.xml.MyXML;
import rpc.xml.MyXMLObject;
import rpc.communication.Receiver;

public class SkeletonGripperActions extends Thread {
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
	public void run() {
		byte[] b;
		while (!isInterrupted()) {
			try {
			System.out.println("Wait for Message");
			b = receiver.receive();
			System.out.println("Message received");
			System.out.println(new String(b));
			MyXMLObject xml = MyXML.createXML(b);
			if(MyXML.testSignatur(xml, "int", namespace+".openGripper")){
				if (this.oldId < (int)xml.getTransactionsID()) {
					this.oldId = (int)xml.getTransactionsID();
					int r = model.openGripper();
					receiver.send((int)xml.getTransactionsID(),MyXML.createMethodResponse(r).getBytes());
					continue;
				}
			}
			if(MyXML.testSignatur(xml, "int", namespace+".closeGripper")){
				if (this.oldId < (int)xml.getTransactionsID()) {
					this.oldId = (int)xml.getTransactionsID();
					int r = model.closeGripper();
					receiver.send((int)xml.getTransactionsID(),MyXML.createMethodResponse(r).getBytes());
					continue;
				}
			}

			receiver.send((int)xml.getTransactionsID(),MyXML.getConnectError(xml));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
