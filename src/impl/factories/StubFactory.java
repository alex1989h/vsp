package impl.factories;

import java.net.SocketException;
import java.net.UnknownHostException;

import rpc.interfaces.*;
import rpc.stubs.*;

public class StubFactory {
	private StubFactory() {
	}
	static IVerticalMovements vertical;
	static IHorizontalMovements horizontal;
	static IGripperActions gripper;
	static IStatusRequests status;
	
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
	
	public static IStatusRequests getStatusRequests() throws UnknownHostException, SocketException{
		status = new StubStatusRequests();
		return status;
	}
}
