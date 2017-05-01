package rpc.skeletons;

import rpc.interfaces.IStatusRequests;
import rpc.xml.MyXML;
import rpc.xml.MyXMLObject;
import rpc.communication.Receiver;

public class SkeletonStatusRequests extends Thread {
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
	public void run() {
		byte[] b;
		while (!isInterrupted()) {
			try {
			System.out.println("Wait for Message");
			b = receiver.receive();
			System.out.println("Message received");
			System.out.println(new String(b));
			MyXMLObject xml = MyXML.createXML(b);
			if(MyXML.testSignatur(xml, "int", namespace+".getHorizontalInPercent")){
				if (this.oldId < (int)xml.getTransactionsID()) {
					this.oldId = (int)xml.getTransactionsID();
					int r = model.getHorizontalInPercent();
					receiver.send((int)xml.getTransactionsID(),MyXML.createMethodResponse(r).getBytes());
					continue;
				}
			}
			if(MyXML.testSignatur(xml, "int", namespace+".getVerticalInPercent")){
				if (this.oldId < (int)xml.getTransactionsID()) {
					this.oldId = (int)xml.getTransactionsID();
					int r = model.getVerticalInPercent();
					receiver.send((int)xml.getTransactionsID(),MyXML.createMethodResponse(r).getBytes());
					continue;
				}
			}
			if(MyXML.testSignatur(xml, "String", namespace+".getGripperStatus")){
				if (this.oldId < (int)xml.getTransactionsID()) {
					this.oldId = (int)xml.getTransactionsID();
					String r = model.getGripperStatus();
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
