package impl.stubs;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IHorizontalMovements;
import impl.robot.Robot;
import impl.xml.MyXML;

public class StubHorizontalMovements implements IHorizontalMovements {
	private FiFo fifo = null;
	
	public StubHorizontalMovements() {
		fifo = FiFoFactory.getFiFo("horizontal");
	}
		
	@Override
	public int moveHorizontalToPercent(int transactionID, int percent) {
		System.out.format("Move horizontal.. ID:%s percent:%s",transactionID, percent);
		String str = MyXML.createXMLString(Robot.getName()+".moveHorizontalToPercent", transactionID, percent);
		fifo.enqueue(str.getBytes());
		return 0;
	}
	

}
