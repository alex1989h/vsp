package impl.client;

import impl.factories.StubFactory;
import rmi.namespace.Namespace;
import rmi.interfaces.IVerticalMovements;
import rmi.interfaces.IHorizontalMovements;
import rmi.interfaces.IGripperActions;
import rmi.communication.Sender;

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
	private ICaDSRobotGUIUpdater gui = null;
	
	void lookup(){
		String[] namespaces = Namespace.lookup();
		if (namespaces != null) {
			for (int i = 0; i < namespaces.length; i++) {
				gui.addService(namespaces[i]);
			}
		}
	}
	
	public Controller() throws Exception{
		vertical = StubFactory.getVerticalMovements();
		horizontal = StubFactory.getHorizontalMovements();
		gripper = StubFactory.getGripperActions();
	}
	

	@Override
	public void register(ICaDSRobotGUIUpdater observer) {
		gui = observer;
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
		return vertical.moveVerticalToPercent(percent);
	}
	
	@Override
	public int getCurrentVerticalPercent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveHorizontalToPercent(int transactionID, int percent) throws Exception {
		return horizontal.moveHorizontalToPercent(percent);
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
		return gripper.openGripper();
	}

	@Override
	public int closeGripper(int transactionID) throws Exception {
		return gripper.closeGripper();
	}
	
	@Override
	public int isGripperClosed() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * Main
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String error = "Wrong parameters\n";
		String output = "usage: java -cp CaDSBase.jar:. Lego.jar impl.client.Controller [-h <hostname>] [-p <port>]\n"
				+ "Paramete:\n" + "-h <hostname> default:255.255.255.255\n"
				+ "-p <port> default:8888";
		String address = "255.255.255.255";
		int port = 8888;
		Controller controller = null;
		Status status = new Status();
		CaDSRobotGUISwing gui = null;
		if(args.length > 0 && args[0].equals("-help")){
			System.out.println(output);
			return;
		} else if (args.length % 2 == 0) {
			for (int i = 0; i < args.length; i+=2) {
				switch (args[i]) {
				case "-h":
					address = args[i + 1];
					break;
				case "-p":
					port = Integer.parseInt(args[i + 1]);
					break;
				default:
					System.out.println(error+output);
					return;
				}
			}
		}else {
			System.out.println(error+output);
			return;
		}
		System.out.println("Client started");
		boolean ret = Sender.checkNameServer(address,port);
		if (ret) {
			controller = new Controller();
			status = new Status();
			gui = new CaDSRobotGUISwing(controller, controller, controller, controller, controller);
			
			controller.register(gui);
			controller.lookup();
			
			status.register(gui);
			status.start();
			
			controller.waithere();
		}else {
			System.out.println("NameServer not found");
		}
	}
}
