package impl.interfaces;

import java.rmi.Remote;

public interface IGripperActions extends Remote {
	public int openGripper(int transactionID);
	public int closeGripper(int transactionID);
}
