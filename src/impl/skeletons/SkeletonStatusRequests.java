package impl.skeletons;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IStatusRequests;
import impl.xml.MyXML;
import impl.xml.MyXMLObject;
import impl.server.Receiver;

public class SkeletonStatusRequests extends Thread {
	private IStatusRequests model;
	private String namespace;
	private FiFo fifo;
	private long oldId = Long.MIN_VALUE;
	
	public SkeletonStatusRequests(IStatusRequests model,String namespace,Receiver receiver) throws Exception {
		this.model = model;
		this.namespace = namespace;
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
		fifo = FiFoFactory.getFiFo("statusRequest");
		while (true) {
			System.out.println("Wait for Queue");
			b = fifo.dequeue();
			System.out.println("Dequeued");
			System.out.println(new String(b));
			MyXMLObject xml = MyXML.createXML(b);
			if(MyXML.testSignatur(xml, namespace+".getHorizontalInPercent", "int")){
				if (this.oldId < (int)xml.getParamValues()[0]) {
					this.oldId = (int)xml.getParamValues()[0];
					model.getHorizontalInPercent((int)xml.getParamValues()[0]);
				}
			}
			if(MyXML.testSignatur(xml, namespace+".getVerticalInPercent", "int")){
				if (this.oldId < (int)xml.getParamValues()[0]) {
					this.oldId = (int)xml.getParamValues()[0];
					model.getVerticalInPercent((int)xml.getParamValues()[0]);
				}
			}
			if(MyXML.testSignatur(xml, namespace+".getGripperStatus", "int")){
				if (this.oldId < (int)xml.getParamValues()[0]) {
					this.oldId = (int)xml.getParamValues()[0];
					model.getGripperStatus((int)xml.getParamValues()[0]);
				}
			}

		}
	}
}
