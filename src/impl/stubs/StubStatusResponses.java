package impl.stubs;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IStatusResponses;
import impl.robot.Robot;
import impl.xml.MyXML;

public class StubStatusResponses implements IStatusResponses {
	private FiFo fifo = null;
	
	public StubStatusResponses() {
		fifo = FiFoFactory.getFiFo("statusResponse");
	}
		
	@Override
	public int getHorizontalInPercent(int transactionID, long percent) {
		System.out.format("Response for Horizontal Position.. ID:%s",transactionID, percent);
		String str = MyXML.createXMLString(Robot.getName()+".getHorizontalInPercent", transactionID, percent);
		fifo.enqueue(str.getBytes());
		return 0;
	}
	
	@Override
	public int getVerticalInPercent(int transactionID, long percent) {
		System.out.format("Response for Vertical Position.. ID:%s",transactionID, percent);
		String str = MyXML.createXMLString(Robot.getName()+".getVerticalInPercent", transactionID, percent);
		fifo.enqueue(str.getBytes());
		return 0;
	}
	
	@Override
	public int getGripperStatus(int transactionID, String gripperStatus) {
		System.out.format("Response for Gripper Status.. ID:%s",transactionID, gripperStatus);
		String str = MyXML.createXMLString(Robot.getName()+".getGripperStatus", transactionID, gripperStatus);
		fifo.enqueue(str.getBytes());
		return 0;
	}
	

}
