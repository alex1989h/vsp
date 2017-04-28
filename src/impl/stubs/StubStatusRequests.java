package impl.stubs;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IStatusRequests;
import impl.robot.Robot;
import impl.xml.MyXML;

public class StubStatusRequests implements IStatusRequests {
	private FiFo fifo = null;
	
	public StubStatusRequests() {
		fifo = FiFoFactory.getFiFo("statusRequest");
	}
		
	@Override
	public int getHorizontalInPercent(int transactionID) {
		System.out.format("Ask for Horizontal Position.. ID:%s",transactionID);
		String str = MyXML.createXMLString(Robot.getName()+".getHorizontalInPercent", transactionID);
		fifo.enqueue(str.getBytes());
		return 0;
	}
	
	@Override
	public int getVerticalInPercent(int transactionID) {
		System.out.format("Ask for Vertical Position.. ID:%s",transactionID);
		String str = MyXML.createXMLString(Robot.getName()+".getVerticalInPercent", transactionID);
		fifo.enqueue(str.getBytes());
		return 0;
	}
	
	@Override
	public int getGripperStatus(int transactionID) {
		System.out.format("Ask for Gripper Status.. ID:%s",transactionID);
		String str = MyXML.createXMLString(Robot.getName()+".getGripperStatus", transactionID);
		fifo.enqueue(str.getBytes());
		return 0;
	}
	

}
