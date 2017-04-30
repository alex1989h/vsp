package impl.factories;

import java.net.SocketException;
import java.net.UnknownHostException;

import impl.interfaces.*;
import impl.stubs.*;

public class StubFactory {
	private StubFactory() {
	}
	static IVerticalMovements vertical;
	static IHorizontalMovements horizontal;
	static IGripperActions gripper;
	
	public static IVerticalMovements getVerticalMovements() throws UnknownHostException, SocketException{
		vertical = new StubVerticalMovements();
		return vertical;
	}
	
	public static IHorizontalMovements getHorizontalMovements() throws UnknownHostException, SocketException{
		horizontal = new StubHorizontalMovements();
		return horizontal;
	}
	
	public static IGripperActions getGripperActions() throws UnknownHostException, SocketException{
		gripper = new StubGripperActions();
		return gripper;
	}
}
