package impl.models;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;

import impl.factories.FiFo;
import impl.factories.FiFoFactory;
import rmi.interfaces.IVerticalMovements;

public class ModelVerticalMovements implements IVerticalMovements {
	private FiFo fifo = null;
	private static CaDSEV3RobotStudentImplementation caller = null;
	public ModelVerticalMovements() {
		fifo = FiFoFactory.getFiFo("vertical");
		caller = CaDSEV3RobotStudentImplementation.createInstance(CaDSEV3RobotType.SIMULATION, null, null);

	}
	@Override
	public int moveVerticalToPercent(int percent) {
		caller.stop_v();
		fifo.enqueue((""+percent).getBytes());
		return 0;
	}
}
