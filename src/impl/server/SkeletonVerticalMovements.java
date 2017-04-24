package impl.server;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IVerticalMovements;
import impl.xml.MyXML;
import impl.xml.MyXMLObject;

public class SkeletonVerticalMovements extends Thread {
	private IVerticalMovements vertical;
	private FiFo fifo;
	private long oldId = Long.MIN_VALUE;
	
	public SkeletonVerticalMovements(IVerticalMovements vertical) {
		this.vertical = vertical;
	}
	
	@Override
	public void run() {
		byte[] b;
		fifo = FiFoFactory.getFiFo("receiverVertical");
		while (true) {
			System.out.println("Wait for Queue");
			b = fifo.dequeue();
			System.out.println("Dequeued");
			System.out.println(new String(b));
			MyXMLObject xml = MyXML.createXML(b);
			if(xml.getMethodName().equals("moveVerticalToPercent")){
				if(xml.getParamValues().length == 2){
					if(xml.getParamTypes()[0].equals(Integer.TYPE) && xml.getParamTypes()[1].equals(Integer.TYPE)){
						moveVerticalToPercent((int)xml.getParamValues()[0], (int)xml.getParamValues()[1]);
					}
				}
			}
		}
	}
	
	private int moveVerticalToPercent(int transactionID, int percent) {
		if (this.oldId < transactionID) {
			this.oldId = transactionID;
			vertical.moveVerticalToPercent(transactionID, percent);
		}
		return 0;
	}

}
