package impl.skeletons;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IHorizontalMovements;
import impl.xml.MyXML;
import impl.xml.MyXMLObject;
import impl.server.Receiver;

public class SkeletonHorizontalMovements extends Thread {
	private IHorizontalMovements model;
	private String namespace;
	private FiFo fifo;
	private long oldId = Long.MIN_VALUE;
	
	public SkeletonHorizontalMovements(IHorizontalMovements model,String namespace,Receiver receiver) throws Exception {
		this.model = model;
		this.namespace = namespace;
		String str;
		str = "<addService><methodName>"+namespace+".moveHorizontalToPercent</methodName></addService>";
		receiver.send(str.getBytes());

	}
	
	@Override
	public void run() {
		byte[] b;
		fifo = FiFoFactory.getFiFo("horizontal");
		while (true) {
			System.out.println("Wait for Queue");
			b = fifo.dequeue();
			System.out.println("Dequeued");
			System.out.println(new String(b));
			MyXMLObject xml = MyXML.createXML(b);
			if(MyXML.testSignatur(xml, namespace+".moveHorizontalToPercent", "int", "int")){
				if (this.oldId < (int)xml.getParamValues()[0]) {
					this.oldId = (int)xml.getParamValues()[0];
					model.moveHorizontalToPercent((int)xml.getParamValues()[0], (int)xml.getParamValues()[1]);
				}
			}

		}
	}
}
