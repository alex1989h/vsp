package impl.skeletons;

import impl.interfaces.IHorizontalMovements;
import impl.xml.MyXML;
import impl.xml.MyXMLObject;
import impl.server.Receiver;

public class SkeletonHorizontalMovements extends Thread {
	private IHorizontalMovements model;
	private String namespace;
	private long oldId = Long.MIN_VALUE;
	private Receiver receiver = null;
	
	public SkeletonHorizontalMovements(IHorizontalMovements model,String namespace) throws Exception {
		this.model = model;
		this.namespace = namespace;
		this.receiver = new Receiver();
		String str;
		str = "<addService><methodName>"+namespace+".moveHorizontalToPercent</methodName></addService>";
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
			if(MyXML.testSignatur(xml, "int", namespace+".moveHorizontalToPercent", "int")){
				if (this.oldId < (int)xml.getTransactionsID()) {
					this.oldId = (int)xml.getTransactionsID();
					int r = model.moveHorizontalToPercent((int)xml.getParamValues()[0]);
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
