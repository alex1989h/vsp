package impl.skeletons;

import impl.interfaces.IStatusRequests;
import impl.xml.MyXML;
import impl.xml.MyXMLObject;
import impl.server.Receiver;

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
			if(MyXML.testSignatur(xml, "int", namespace+".getHorizontalInPercent", "int")){
				if (this.oldId < (int)xml.getParamValues()[0]) {
					this.oldId = (int)xml.getParamValues()[0];
					int r = model.getHorizontalInPercent((int)xml.getParamValues()[0]);
					receiver.send(MyXML.createMethodResponse((int)this.oldId,r).getBytes());
					continue;
				}
			}
			if(MyXML.testSignatur(xml, "int", namespace+".getVerticalInPercent", "int")){
				if (this.oldId < (int)xml.getParamValues()[0]) {
					this.oldId = (int)xml.getParamValues()[0];
					int r = model.getVerticalInPercent((int)xml.getParamValues()[0]);
					receiver.send(MyXML.createMethodResponse((int)this.oldId,r).getBytes());
					continue;
				}
			}
			if(MyXML.testSignatur(xml, "String", namespace+".getGripperStatus", "int")){
				if (this.oldId < (int)xml.getParamValues()[0]) {
					this.oldId = (int)xml.getParamValues()[0];
					String r = model.getGripperStatus((int)xml.getParamValues()[0]);
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
