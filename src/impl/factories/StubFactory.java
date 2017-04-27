package impl.factories;

import impl.interfaces.*;
import impl.stubs.*;

public class StubFactory {
	private StubFactory() {
	}
	static IVerticalMovements vertical;
	static IHorizontalMovements horizontal;
	static IGripperActions gripper;
	
	public static IVerticalMovements getVerticalMovements(){
		vertical = new StubVerticalMovements();
		return vertical;
	}
	
	public static IHorizontalMovements getHorizontalMovements(){
		horizontal = new StubHorizontalMovements();
		return horizontal;
	}
	
	public static IGripperActions getGripperActions(){
		gripper = new StubGripperActions();
		return gripper;
	}
}
