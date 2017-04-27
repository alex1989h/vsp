package impl.stubs;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IGripperActions;
import impl.robot.Robot;
import impl.xml.MyXML;

public class StubGripperActions implements IGripperActions {
	private FiFo fifo = null;
	
	public StubGripperActions() {
		fifo = FiFoFactory.getFiFo("gripper");
	}
		
	@Override
	public int openGripper(int transactionID) {
		System.out.format("Open.. ID:%s",transactionID);
		String str = MyXML.createXMLString(Robot.getName()+".openGripper", transactionID);
		fifo.enqueue(str.getBytes());
		return 0;
	}
	
	@Override
	public int closeGripper(int transactionID) {
		System.out.format("Close.. ID:%s",transactionID);
		String str = MyXML.createXMLString(Robot.getName()+".closeGripper", transactionID);
		fifo.enqueue(str.getBytes());
		return 0;
	}
	

}
