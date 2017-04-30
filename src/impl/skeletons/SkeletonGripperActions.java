package impl.skeletons;

import impl.interfaces.IGripperActions;
import impl.xml.MyXML;
import impl.xml.MyXMLObject;
import impl.server.Receiver;

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
			if(MyXML.testSignatur(xml, "int", namespace+".openGripper", "int")){
				if (this.oldId < (int)xml.getParamValues()[0]) {
					this.oldId = (int)xml.getParamValues()[0];
					int r = model.openGripper((int)xml.getParamValues()[0]);
					receiver.send(MyXML.createMethodResponse((int)this.oldId,r).getBytes());
					continue;
				}
			}
			if(MyXML.testSignatur(xml, "int", namespace+".closeGripper", "int")){
				if (this.oldId < (int)xml.getParamValues()[0]) {
					this.oldId = (int)xml.getParamValues()[0];
					int r = model.closeGripper((int)xml.getParamValues()[0]);
					receiver.send(MyXML.createMethodResponse((int)this.oldId,r).getBytes());
					continue;
				}
			}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
