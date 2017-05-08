package junit.mockup;

import rmi.interfaces.IGripperActions;
import rmi.interfaces.IHorizontalMovements;
import rmi.interfaces.IStatusRequests;
import rmi.interfaces.IVerticalMovements;

public class TestModel implements IVerticalMovements, IHorizontalMovements, IGripperActions ,IStatusRequests{
	private static int zaehler = 0;
	private static int zaehler2 = 0;
	private static int horizontal = 0;
	private static int vertical = 0;
	private static String status = "closed";
	@Override
	public int openGripper() {
		status = "open";
		return zaehler++;
	}

	@Override
	public int closeGripper() {
		status = "closed";
		return 100-zaehler2++;
	}

	@Override
	public int moveHorizontalToPercent(int percent) {
		horizontal = percent;
		return percent+percent;
	}

	@Override
	public int moveVerticalToPercent(int percent) {
		vertical = percent;
		return percent+percent+percent;
	}

	@Override
	public int getHorizontalInPercent() {
		return horizontal;
	}

	@Override
	public int getVerticalInPercent() {
		return vertical;
	}

	@Override
	public String getGripperStatus() {
		return status;
	}

}
