package impl.client;

import impl.factories.FiFoFactory;
import impl.interfaces.IVerticalMovements;
import impl.robot.Robot;
import impl.xml.MyXML;

public class VerticalMovements implements IVerticalMovements {
	private FiFo fifo = null;
	
	public VerticalMovements() {
		fifo = FiFoFactory.getFiFo("transmitterVertical");
	}
	
	@Override
	public int moveVerticalToPercent(int transactionID,int percent) {
		System.out.println("Call to move vertical -  TID: " + transactionID + " degree " + percent);
		String str = MyXML.createXMLString(Robot.getName()+".moveVerticalToPercent", transactionID, percent);
		fifo.enqueue(str.getBytes());
		return 0;
	}
}
