package impl.stubs;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IVerticalMovements;
import impl.robot.Robot;
import impl.xml.MyXML;

public class StubVerticalMovements implements IVerticalMovements {
	private FiFo fifo = null;
	
	public StubVerticalMovements() {
		fifo = FiFoFactory.getFiFo("vertical");
	}
		
	@Override
	public int moveVerticalToPercent(int transactionID, int percent) {
		System.out.format("Move vertical.. ID:%s percent:%s",transactionID, percent);
		String str = MyXML.createXMLString(Robot.getName()+".moveVerticalToPercent", transactionID, percent);
		fifo.enqueue(str.getBytes());
		return 0;
	}
	

}
