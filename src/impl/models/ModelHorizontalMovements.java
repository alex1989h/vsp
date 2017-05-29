package impl.models;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;

import impl.factories.FiFo;
import impl.factories.FiFoFactory;
import rmi.interfaces.IHorizontalMovements;

public class ModelHorizontalMovements implements IHorizontalMovements{
	private FiFo fifo = null;
	private static CaDSEV3RobotStudentImplementation caller = null;
	public ModelHorizontalMovements() {
		fifo = FiFoFactory.getFiFo("horizontal");
		caller = CaDSEV3RobotStudentImplementation.createInstance(CaDSEV3RobotType.SIMULATION, null, null);
	}
	@Override
	public int moveHorizontalToPercent(int percent) {
		caller.stop_h();
		fifo.enqueue((""+percent).getBytes());
		return 0;
	}

}
