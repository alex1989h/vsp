package impl.server;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;

import impl.lego.LegoGripperActions;
import impl.lego.LegoHorizontalMovements;
import impl.lego.LegoVerticalMovements;
import impl.models.*;
import rmi.skeletons.*;
import rmi.communication.Receiver;

public class Controller {

	public static void main(String[] args) throws Exception {
		System.out.println("Server gestartet");
		
		String namespace = "Robot1";
		String address = "localhost";
		int port = 9012;
		
		List<Thread> threads = new LinkedList<Thread>();
		
		if(args.length == 3){
			namespace = args[0];
			address = args[1];
			port = Integer.parseInt(args[2]);
		}else if(args.length == 0){
			
		}else{
			System.out.println("Falsche Parameter Anzahl");
			return;
		}
		Receiver.setAddress(InetAddress.getByName(address));
		Receiver.setPort(port);
		
		StatusMessage status = new StatusMessage();
		CaDSEV3RobotStudentImplementation.createInstance(CaDSEV3RobotType.SIMULATION, status, status);
		
		LegoVerticalMovements legoVertical = new LegoVerticalMovements();
		ModelVerticalMovements vertical = new ModelVerticalMovements();
		SkeletonVerticalMovements skeletonVertical = new SkeletonVerticalMovements(vertical, namespace);
		
		LegoHorizontalMovements legoHorizontal = new LegoHorizontalMovements();
		ModelHorizontalMovements horizontal = new ModelHorizontalMovements();
		SkeletonHorizontalMovements skeletonHorizontal = new SkeletonHorizontalMovements(horizontal, namespace);
		
		LegoGripperActions legoGripper = new LegoGripperActions();
		ModelGripperActions gripper = new ModelGripperActions();
		SkeletonGripperActions skeletonGripper = new SkeletonGripperActions(gripper, namespace);
		
		
		ModelStatusRequests statusRequest = new ModelStatusRequests();
		SkeletonStatusRequests skeletonStatus = new SkeletonStatusRequests(statusRequest, namespace);
		
		status.addObserver(legoVertical);
		status.addObserver(legoHorizontal);
		status.addObserver(legoGripper);
		status.addObserver(statusRequest);
		
		threads.add(legoGripper);
		threads.add(legoHorizontal);
		threads.add(legoVertical);
		
		threads.add(skeletonGripper);
		threads.add(skeletonHorizontal);
		threads.add(skeletonVertical);
		threads.add(skeletonStatus);
		
		for (int i = 0; i < threads.size(); i++) {
			threads.get(i).start();
		}
		
		for (int i = 0; i < threads.size(); i++) {
			threads.get(i).join();
		}
	}
}
