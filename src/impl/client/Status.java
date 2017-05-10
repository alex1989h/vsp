package impl.client;

import org.cads.ev3.gui.ICaDSRobotGUIUpdater;

import impl.factories.StubFactory;
import rmi.interfaces.IStatusRequests;

public class Status extends Thread {
	private IStatusRequests status = null;
	private ICaDSRobotGUIUpdater gui = null;
	private static int errorCounter = 0;
	
	private synchronized void errorIncrease(){
		errorCounter++;
	}
	
	private static void resetErroCounter(){
		errorCounter = 0;
	}
	
	private boolean checkErrorCounter(){
		if(errorCounter > 20)return true;
		return false;
	}
	
	public Status() throws Exception {
		this.status = StubFactory.getStatusRequests();
	}
	
	public void register(ICaDSRobotGUIUpdater observer) {
		gui = observer;
	}
	
	@Override
	public void run() {
		if (gui != null) {
			while (!isInterrupted()) {
				try {
					sleep(500);
					getGripperStatus();
					getHorizontalStatus();
					getVerticalStatus();
					if (checkErrorCounter()) {
						System.out.println("Shutdown Status Thread");
						interrupt();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("GUI not found");
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
