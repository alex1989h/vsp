package impl.client;

import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import impl.stubs.StubStatusRequests;

public class StatusController extends Thread {
	private StubStatusRequests status = null;
	private CaDSRobotGUISwing gui = null;
	
	public StatusController(CaDSRobotGUISwing gui) throws Exception {
		this.status = new StubStatusRequests();
		this.gui = gui;
	}
	
	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				sleep(500);
				getGripperStatus();
				getHorizontalStatus();
				getVerticalStatus();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void getGripperStatus() {
		String gripperStatus = status.getGripperStatus(ClientKontroller.getTransactionsID());
		switch (gripperStatus) {
		case "closed":
			gui.setGripperClosed();
			break;
		case "open":
			gui.setGripperOpen();
			break;
		default:
			break;
		}
	}
	private void getVerticalStatus() {
		int percent = status.getVerticalInPercent(ClientKontroller.getTransactionsID());
		gui.setVerticalProgressbar(percent);
	}
	private void getHorizontalStatus() {
		int percent = status.getHorizontalInPercent(ClientKontroller.getTransactionsID());
		gui.setHorizontalProgressbar(percent);
	}
}
