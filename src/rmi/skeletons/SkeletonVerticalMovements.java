package rmi.skeletons;

import rmi.interfaces.IVerticalMovements;
import rmi.interfaces.ISkeleton;
import rmi.xml.MyXML;
import rmi.xml.MyXMLObject;
import rmi.communication.Receiver;

public class SkeletonVerticalMovements implements ISkeleton {
	private IVerticalMovements model;
	private String namespace;
	private long oldId = Long.MIN_VALUE;
	private Receiver receiver = null;
	
	public SkeletonVerticalMovements(IVerticalMovements model,String namespace) throws Exception {
		this.model = model;
		this.namespace = namespace;
		this.receiver = new Receiver();
		String str;
		str = "<addService><methodName>"+namespace+".moveVerticalToPercent</methodName></addService>";
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
		if(MyXML.testSignatur(xml, "int", namespace+".moveVerticalToPercent", "int")){
			if (this.oldId < (int)xml.getTransactionsID()) {
				this.oldId = (int)xml.getTransactionsID();
				int r = model.moveVerticalToPercent((int)xml.getParamValues()[0]);
				receiver.send((int)xml.getTransactionsID(),MyXML.createMethodResponse(r).getBytes());
			}
		}

		receiver.send((int)xml.getTransactionsID(),MyXML.getConnectError(xml));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
