package impl.skeletons;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IGripperActions;
import impl.xml.MyXML;
import impl.xml.MyXMLObject;
import impl.server.Receiver;

public class SkeletonGripperActions extends Thread {
	private IGripperActions model;
	private String namespace;
	private FiFo fifo;
	private long oldId = Long.MIN_VALUE;
	
	public SkeletonGripperActions(IGripperActions model,String namespace,Receiver receiver) throws Exception {
		this.model = model;
		this.namespace = namespace;
		String str;
		str = "<addService><methodName>"+namespace+".openGripper</methodName></addService>";
		receiver.send(str.getBytes());
		str = "<addService><methodName>"+namespace+".closeGripper</methodName></addService>";
		receiver.send(str.getBytes());

	}
	
	@Override
	public void run() {
		byte[] b;
		fifo = FiFoFactory.getFiFo("gripper");
		while (true) {
			System.out.println("Wait for Queue");
			b = fifo.dequeue();
			System.out.println("Dequeued");
			System.out.println(new String(b));
			MyXMLObject xml = MyXML.createXML(b);
			if(MyXML.testSignatur(xml, namespace+".openGripper", "int")){
				if (this.oldId < (int)xml.getParamValues()[0]) {
					this.oldId = (int)xml.getParamValues()[0];
					model.openGripper((int)xml.getParamValues()[0]);
				}
			}
			if(MyXML.testSignatur(xml, namespace+".closeGripper", "int")){
				if (this.oldId < (int)xml.getParamValues()[0]) {
					this.oldId = (int)xml.getParamValues()[0];
					model.closeGripper((int)xml.getParamValues()[0]);
				}
			}

		}
	}
}
