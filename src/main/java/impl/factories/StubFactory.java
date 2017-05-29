package impl.factories;

import java.net.SocketException;
import java.net.UnknownHostException;

import rmi.interfaces.*;
import rmi.stubs.*;

public class StubFactory {
	private StubFactory() {
	}
	static IVerticalMovements vertical;
	static IHorizontalMovements horizontal;
	static IGripperActions gripper;
	static IStatusRequests status;
	
	public static IVerticalMovements getVerticalMovements(){
		try {
			vertical = new StubVerticalMovements();
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vertical;
	}
	
	public static IHorizontalMovements getHorizontalMovements(){
		try {
			horizontal = new StubHorizontalMovements();
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return horizontal;
	}
	
	public static IGripperActions getGripperActions(){
		try {
			gripper = new StubGripperActions();
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gripper;
	}
	
	public static IStatusRequests getStatusRequests(){
		try {
			status = new StubStatusRequests();
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
}
