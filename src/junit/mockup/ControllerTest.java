package junit.mockup;

import static org.junit.Assert.*;

import org.junit.Test;

import impl.client.Controller;
import rmi.communication.Receiver;
import rmi.communication.Sender;
import rmi.interfaces.IStatusRequests;
import rmi.namespace.Namespace;
import rmi.skeletons.SkeletonGripperActions;
import rmi.skeletons.SkeletonHorizontalMovements;
import rmi.skeletons.SkeletonStatusRequests;
import rmi.skeletons.SkeletonVerticalMovements;
import rmi.stubs.StubStatusRequests;

public class ControllerTest {

	@Test
	public void test() throws Exception {
		String namespace = "Test";
		Controller c = new Controller();
		IStatusRequests s = new StubStatusRequests();
		Sender.checkNameServer("127.0.0.1", 8888);
		Receiver.checkNameServer("127.0.0.1", 8888);
		Namespace.setName(namespace);
		TestModel model = new TestModel();
		
		SkeletonGripperActions gripper = new SkeletonGripperActions(model, namespace);
		SkeletonHorizontalMovements horizont = new SkeletonHorizontalMovements(model, namespace);
		SkeletonVerticalMovements vertical = new SkeletonVerticalMovements(model, namespace);
		SkeletonStatusRequests status = new SkeletonStatusRequests(model, namespace);
		
		gripper.start();
		horizont.start();
		vertical.start();
		status.start();
		
		for (int i = 0; i <= 100; i++) {
			assertEquals(c.openGripper(0), i);
			assertEquals(s.getGripperStatus(), "open");
			assertEquals(c.closeGripper(0), 100-i);
			assertEquals(s.getGripperStatus(), "closed");
			assertEquals(c.moveHorizontalToPercent(0, i), i+i);
			assertEquals(s.getHorizontalInPercent(), i);
			assertEquals(c.moveVerticalToPercent(0, i), i+i+i);
			assertEquals(s.getVerticalInPercent(), i);
		}
		
		gripper.interrupt();
		horizont.interrupt();
		vertical.interrupt();
		status.interrupt();
		
		c.openGripper(0);
		c.moveHorizontalToPercent(0, 100);
		c.moveVerticalToPercent(0, 100);
		s.getGripperStatus();
	}

}
