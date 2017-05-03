package impl.server;
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
		String error = "Wrong parameters\n";
		String output = "usage: java -cp CaDSBase.jar:. Lego.jar impl.server.Controller [-n <namespace>] [-h <hostname>] [-p <port>]\n"
				+ "Paramete:\n-n <namespace> default:Robot[random number]\n" + "-h <hostname> default:255.255.255.255\n"
				+ "-p <port> default:8888";
		System.out.println("Server gestartet");

		String namespace = "Robot" + ((int) (Math.random() * 10000) + 1);
		String address = "255.255.255.255";
		int port = 8888;

		List<Thread> threads = new LinkedList<Thread>();
		if(args.length%2==0){
		for (int i = 0; i < args.length; i+=2) {
			switch (args[i]) {
			case "-n":
				namespace = args[i + 1];
				break;
			case "-h":
				address = args[i + 1];
				break;
			case "-p":
				port = Integer.parseInt(args[i + 1]);
				break;
			case "-help":
				System.out.println(output);
				return;
			default:
				System.out.println(error+output);
				return;
			}
		}
		}else{
			System.out.println(error+output);
			return;
		}
		
		boolean ret = Receiver.checkNameServer(address,port);
		if (ret) {
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
		}else{
			System.out.println("NameServer not found");
		}
	}
}
