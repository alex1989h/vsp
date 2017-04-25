package impl.client;

import impl.interfaces.IVerticalMovements;
import impl.robot.Robot;
import impl.xml.MyXML;
import impl.xml.MyXMLObject;
import impl.interfaces.IHorizontalMovements;
import impl.factories.FiFoFactory;
import impl.interfaces.IGripperActions;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import org.cads.ev3.gui.ICaDSRobotGUIUpdater;
import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import org.cads.ev3.rmi.consumer.ICaDSRMIConsumer;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;


public class ClientKontroller implements IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal, IIDLCaDSEV3RMIMoveVertical, IIDLCaDSEV3RMIUltraSonic, ICaDSRMIConsumer  {
	
	synchronized public void waithere() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	
	private class GUI implements Runnable {
		
		ClientKontroller c;
		
        public GUI(ClientKontroller _c) {
            c = _c;
        }

        @Override
        public void run() {
            try {
                CaDSRobotGUISwing gui = new CaDSRobotGUISwing(c, c, c, c, c);
                lookup(gui,"Services");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	private IVerticalMovements vertical = null;
	private IHorizontalMovements horizontal = null;
	private IGripperActions gripper = null;
	private Sender sender = null;
	
	void lookup(CaDSRobotGUISwing gui, String str){
		String send = "<?xml version=\"1.0\"?><getService></getService>";
		byte[] recei = sender.send(send.getBytes());
		if (recei != null) {
			MyXMLObject xml = MyXML.createXML(recei);
			for (int i = 0; i < xml.getParamValues().length; i++) {
				gui.addService((String) xml.getParamValues()[i]);
				if (i == 0) {
					gui.setChoosenService((String) xml.getParamValues()[i]);
					Robot.setName((String) xml.getParamValues()[i]);
				}

			}
		}
	}
	public ClientKontroller(String ip, int port) throws UnknownHostException, IOException{
		sender = new Sender(ip, port, "transmitterVertical");
		vertical = new VerticalMovements();
		horizontal = new HorizontalMovements();
		gripper = new GripperActions();
		sender.start();
	}
	public int startGUI(){
		SwingUtilities.invokeLater(new GUI(this));
        waithere();
		return 0;
	}
	/**
	 * Main
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		System.out.println("Client gestartet");
		String defaultIp = "localhost";
		int defaultPort = 8888;
		ClientKontroller kontroller = null;
		if(args.length == 2){
			kontroller = new ClientKontroller(args[0],Integer.parseInt(args[1]));
			kontroller.startGUI();
		} else if(args.length == 0){
			kontroller = new ClientKontroller(defaultIp,defaultPort);
			kontroller.startGUI();
		}else{
			System.out.println("Falsche Parameter Anzahl");
		}
	}

	@Override
	public void register(ICaDSRobotGUIUpdater observer) {
		//TODO: Neue Roboter dazuaddieren
	}

	@Override
	public void update(String comboBoxText) {
		System.out.println("Combo Box updated " + comboBoxText);
		Robot.setName(comboBoxText);
	}

	@Override
	public int isUltraSonicOccupied() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveVerticalToPercent(int transactionID, int percent) throws Exception {
		vertical.moveVerticalToPercent(transactionID, percent);
		return 0;
	}
	
	@Override
	public int getCurrentVerticalPercent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveHorizontalToPercent(int transactionID, int percent) throws Exception {
		horizontal.moveHorizontalToPercent(transactionID, percent);
		return 0;
	}
	
	@Override
	public int getCurrentHorizontalPercent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int stop(int transactionID) throws Exception {
		System.out.println("Stop movement.... TID: " + transactionID);
		return 0;
	}

	@Override
	public int openGripper(int transactionID) throws Exception {
        gripper.openGripper(transactionID);
		return 0;
	}

	@Override
	public int closeGripper(int transactionID) throws Exception {
		gripper.closeGripper(transactionID);
		return 0;
	}
	
	@Override
	public int isGripperClosed() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
}
