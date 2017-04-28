package impl.skeletons;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IVerticalMovements;
import impl.xml.MyXML;
import impl.xml.MyXMLObject;
import impl.server.Receiver;

public class SkeletonVerticalMovements extends Thread {
	private IVerticalMovements model;
	private String namespace;
	private FiFo fifo;
	private long oldId = Long.MIN_VALUE;
	
	public SkeletonVerticalMovements(IVerticalMovements model,String namespace,Receiver receiver) throws Exception {
		this.model = model;
		this.namespace = namespace;
		String str;
		str = "<addService><methodName>"+namespace+".moveVerticalToPercent</methodName></addService>";
		receiver.send(str.getBytes());

	}
	
	@Override
	public void run() {
		byte[] b;
		fifo = FiFoFactory.getFiFo("vertical");
		while (true) {
			System.out.println("Wait for Queue");
			b = fifo.dequeue();
			System.out.println("Dequeued");
			System.out.println(new String(b));
			MyXMLObject xml = MyXML.createXML(b);
			if(MyXML.testSignatur(xml, namespace+".moveVerticalToPercent", "int", "int")){
				if (this.oldId < (int)xml.getParamValues()[0]) {
					this.oldId = (int)xml.getParamValues()[0];
					model.moveVerticalToPercent((int)xml.getParamValues()[0], (int)xml.getParamValues()[1]);
				}
			}

		}
	}
}
