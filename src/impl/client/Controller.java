package impl.client;
import impl.interfaces.IVerticalMovements;
import impl.namespace.Namespace;
import impl.xml.MyXML;
import impl.xml.MyXMLObject;
import impl.interfaces.IHorizontalMovements;
import impl.factories.StubFactory;
import impl.interfaces.IGripperActions;

import java.net.InetAddress;

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
	private Sender sender = null;
	private CaDSRobotGUISwing gui = null;
	
	public CaDSRobotGUISwing getGui() {
		return gui;
	}

	void lookup(CaDSRobotGUISwing gui){
		String send = "<?xml version=\"1.0\"?><getService></getService>";
		byte[] recei = sender.send(send.getBytes());
		if (recei != null) {
			MyXMLObject xml = MyXML.createXML(recei);
			for (int i = 0; i < xml.getParamValues().length; i++) {
				gui.addService((String) xml.getParamValues()[i]);
				if (i == 0) {
					gui.setChoosenService((String) xml.getParamValues()[i]);
					Namespace.setName((String) xml.getParamValues()[i]);
				}

			}
		}
	}
	
	public Controller(String address, int port) throws Exception{
		Broker.setAddress(InetAddress.getByName(address));
		Broker.setPort(port);
		
		gui = new CaDSRobotGUISwing(this, this, this, this, this);
		sender = new Sender();
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
		vertical.moveVerticalToPercent(getTransactionsID(), percent);
		return 0;
	}
	
	@Override
	public int getCurrentVerticalPercent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveHorizontalToPercent(int transactionID, int percent) throws Exception {
		horizontal.moveHorizontalToPercent(getTransactionsID(), percent);
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
        gripper.openGripper(getTransactionsID());
		return 0;
	}

	@Override
	public int closeGripper(int transactionID) throws Exception {
		gripper.closeGripper(getTransactionsID());
		return 0;
	}
	
	@Override
	public int isGripperClosed() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
}
