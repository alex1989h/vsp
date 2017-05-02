package impl.client;

import impl.factories.StubFactory;
import java.net.InetAddress;

import rpc.namespace.Namespace;
import rpc.interfaces.IVerticalMovements;
import rpc.interfaces.IHorizontalMovements;
import rpc.interfaces.IGripperActions;
import rpc.communication.Sender;

import org.cads.ev3.gui.ICaDSRobotGUIUpdater;
import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import org.cads.ev3.rmi.consumer.ICaDSRMIConsumer;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;


public class Controller implements IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal, IIDLCaDSEV3RMIMoveVertical, IIDLCaDSEV3RMIUltraSonic, ICaDSRMIConsumer  {
	private static int transactionsID = Integer.MIN_VALUE;
	
	public synchronized static int getTransactionsID(){
		return transactionsID++;
	}
	
	synchronized public void waithere() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	
	private IVerticalMovements vertical = null;
	private IHorizontalMovements horizontal = null;
	private IGripperActions gripper = null;
	private CaDSRobotGUISwing gui = null;
	
	public CaDSRobotGUISwing getGui() {
		return gui;
	}

	void lookup(CaDSRobotGUISwing gui){
		String[] namespaces = Namespace.lookup();
		if (namespaces != null) {
			for (int i = 0; i < namespaces.length; i++) {
				gui.addService(namespaces[i]);
			}
			gui.setChoosenService(namespaces[0]);
			Namespace.setName(namespaces[0]);
		}
	}
	
	public Controller(String address, int port) throws Exception{
		Sender.setAddress(InetAddress.getByName(address));
		Sender.setPort(port);
		
		gui = new CaDSRobotGUISwing(this, this, this, this, this);
		vertical = StubFactory.getVerticalMovements();
		horizontal = StubFactory.getHorizontalMovements();
		gripper = StubFactory.getGripperActions();
		new Status(gui).start();
		lookup(gui);
	}
	
	/**
	 * Main
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		System.out.println("Client gestartet");
		String address = "localhost";
		int port = 8888;
		Controller c = null;
		if(args.length == 2){
			address = args[0];
			port = Integer.parseInt(args[1]);
		} else if(args.length == 0){
			
		}else{
			System.out.println("Falsche Parameter Anzahl");
			return;
		}
		c = new Controller(address,port);
        c.waithere();
	}

	@Override
	public void register(ICaDSRobotGUIUpdater observer) {
		//TODO: Neue Roboter dazuaddieren
	}

	@Override
	public void update(String comboBoxText) {
		System.out.println("Combo Box updated " + comboBoxText);
		Namespace.setName(comboBoxText);
	}

	@Override
	public int isUltraSonicOccupied() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveVerticalToPercent(int transactionID, int percent) throws Exception {
		vertical.moveVerticalToPercent(percent);
		return 0;
	}
	
	@Override
	public int getCurrentVerticalPercent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveHorizontalToPercent(int transactionID, int percent) throws Exception {
		horizontal.moveHorizontalToPercent(percent);
		return 0;
	}
	
	@Override
	public int getCurrentHorizontalPercent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int stop(int transactionID) throws Exception {
		System.out.println("Stop movement.... TID: " + getTransactionsID());
		return 0;
	}

	@Override
	public int openGripper(int transactionID) throws Exception {
        gripper.openGripper();
		return 0;
	}

	@Override
	public int closeGripper(int transactionID) throws Exception {
		gripper.closeGripper();
		return 0;
	}
	
	@Override
	public int isGripperClosed() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
}
