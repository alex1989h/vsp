package impl.client;

import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import rpc.stubs.StubStatusRequests;

public class Status extends Thread {
	private StubStatusRequests status = null;
	private CaDSRobotGUISwing gui = null;
	private static int errorCounter = 0;
	
	private synchronized void errorIncrease(){
		errorCounter++;
	}
	
	private static void resetErroCounter(){
		errorCounter = 0;
	}
	
	private boolean checkErrorCounter(){
		if(errorCounter > 5)return true;
		return false;
	}
	
	public Status(CaDSRobotGUISwing gui) throws Exception {
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
				if(checkErrorCounter()){
					System.out.println("Shutdown Status Thread");
					interrupt();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void getGripperStatus() {
		String gripperStatus = status.getGripperStatus();
		switch (gripperStatus) {
		case "closed":
			resetErroCounter();
			gui.setGripperClosed();
			break;
		case "open":
			resetErroCounter();
			gui.setGripperOpen();
			break;
		default:
			errorIncrease();
			break;
		}
	}
	private void getVerticalStatus() {
		int percent = status.getVerticalInPercent();
		if (percent < 0) {
			errorIncrease();
		}else{
			resetErroCounter();
			gui.setVerticalProgressbar(percent);
		}
	}
	private void getHorizontalStatus() {
		int percent = status.getHorizontalInPercent();
		if (percent < 0) {
			errorIncrease();
		}else{
			resetErroCounter();
			gui.setHorizontalProgressbar(percent);
		}
		
	}
}
