package impl.server;
import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;

import impl.client.Sender;
import impl.models.*;
import impl.robot.Robot;
import impl.skeletons.*;
import impl.xml.MyXML;
import impl.xml.MyXMLObject;

public class ServerKontroller {

	public static void main(String[] args) throws Exception {
		System.out.println("Server gestartet");
		
		String namespace = "Robot1";
		String address = "localhost";
		int port = 9012;
		
		String[] queue = {"vertical","horizontal","gripper","statusRequest"};
		
		Receiver[] receiver = new Receiver[4];
		
		Thread thread[] = new Thread[4];
		
		if(args.length == 3){
			namespace = args[0];
			address = args[1];
			port = Integer.parseInt(args[2]);
		}else if(args.length == 0){
			
		}else{
			System.out.println("Falsche Parameter Anzahl");
			return;
		}
		StatusMessage status = new StatusMessage();
		CaDSEV3RobotStudentImplementation.createInstance(CaDSEV3RobotType.SIMULATION, status, status);
		
		receiver[0] = new Receiver(address, port, queue[0], namespace);
		ModelVerticalMovements vertical = new ModelVerticalMovements();
		thread[0] = new SkeletonVerticalMovements(vertical, namespace, receiver[0]);
		
		receiver[1] = new Receiver(address, port, queue[1], namespace);
		ModelHorizontalMovements horizontal = new ModelHorizontalMovements();
		thread[1] = new SkeletonHorizontalMovements(horizontal, namespace, receiver[1]);
		
		receiver[2] = new Receiver(address, port, queue[2], namespace);
		ModelGripperActions gripper = new ModelGripperActions();
		thread[2] = new SkeletonGripperActions(gripper, namespace, receiver[2]);
		
		receiver[3] = new Receiver(address, port, queue[3], namespace);
		ModelStatusRequests statusRequest = new ModelStatusRequests();
		thread[3] = new SkeletonStatusRequests(statusRequest, namespace, receiver[3]);
		
		status.addObserver(vertical);
		status.addObserver(horizontal);
		status.addObserver(gripper);
		status.addObserver(statusRequest);
		
		Sender sender = new Sender(address, port, "statusResponse");
		Robot.setName("Client");
		sender.start();
		
		
		
		for (int i = 0; i < thread.length; i++) {
			receiver[i].start();
			thread[i].start();
		}
		
		for (int i = 0; i < thread.length; i++) {
			receiver[i].join();
			thread[i].join();
		}
		sender.join();
	}
}
