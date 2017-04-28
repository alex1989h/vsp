package impl.models;

import org.cads.ev3.gui.swing.CaDSRobotGUISwing;

import impl.interfaces.IStatusResponses;

public class ModelStatusResponses implements IStatusResponses {
	CaDSRobotGUISwing gui = null;
	
	public ModelStatusResponses(CaDSRobotGUISwing gui) {
		this.gui = gui;
	}
	@Override
	public int getHorizontalInPercent(int transactionID, long percent) {
		gui.setHorizontalProgressbar((int)percent);
		return 0;
	}

	@Override
	public int getVerticalInPercent(int transactionID, long percent) {
		gui.setVerticalProgressbar((int)percent);
		return 0;
	}

	@Override
	public int getGripperStatus(int transactionID, String gripperStatus) {
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
		return 0;
	}

}
