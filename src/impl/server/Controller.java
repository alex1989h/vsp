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
		CaDSEV3RobotType[] sim = {CaDSEV3RobotType.REAL,CaDSEV3RobotType.SIMULATION};
		int simIndex = 0;
		String error = "Wrong parameters\n";
		String output = "usage: java -cp Lego.jar impl.server.Controller [-n <namespace>] [-h <hostname>] [-p <port>] [-sim {0,1}]\n"
				+ "Paramete:\n-n <namespace> default:Robot[random number]\n" + "-h <hostname> default:255.255.255.255\n"
				+ "-p <port> default:8888\n"
				+ "-sim {0,1} 0:simulation off, 1:simulation on, default:0";
		String namespace = "Robot" + ((int) (Math.random() * 10000) + 1);
		String address = "255.255.255.255";
		int port = 8888;

		List<Thread> threads = new LinkedList<Thread>();
		if(args.length > 0 && args[0].equals("-help")){
			System.out.println(output);
			return;
		}else if(args.length%2==0){
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
				case "-sim":
					simIndex = Integer.parseInt(args[i + 1]);
					if(simIndex < 0 || simIndex > 1){
						System.out.println("Error on -sim "+simIndex+"only 0 or 1 allowed");
						System.out.println(output);
						return;
					}
					break;
				default:
					System.out.println(error+output);
					return;
				}
			}
		}else{
			System.out.println(error+output);
			return;
		}
		System.out.println("Server started");
		boolean ret = Receiver.checkNameServer(address,port);
		if (ret) {
			StatusMessage status = new StatusMessage();
			CaDSEV3RobotStudentImplementation.createInstance(sim[simIndex], status, status);
			
			LegoVerticalMovements legoVertical = new LegoVerticalMovements();
			ModelVerticalMovements vertical = new ModelVerticalMovements();
			SkeletonVerticalMovements skeletonVertical = new SkeletonVerticalMovements(vertical, namespace);
			ControllerThread verticalController = new ControllerThread(skeletonVertical);
			
			LegoHorizontalMovements legoHorizontal = new LegoHorizontalMovements();
			ModelHorizontalMovements horizontal = new ModelHorizontalMovements();
			SkeletonHorizontalMovements skeletonHorizontal = new SkeletonHorizontalMovements(horizontal, namespace);
			ControllerThread horizontalController = new ControllerThread(skeletonHorizontal);
			
			LegoGripperActions legoGripper = new LegoGripperActions();
			ModelGripperActions gripper = new ModelGripperActions();
			SkeletonGripperActions skeletonGripper = new SkeletonGripperActions(gripper, namespace);
			ControllerThread gripperController = new ControllerThread(skeletonGripper);
			
			ModelStatusRequests statusRequest = new ModelStatusRequests();
			SkeletonStatusRequests skeletonStatus = new SkeletonStatusRequests(statusRequest, namespace);
			ControllerThread statusController = new ControllerThread(skeletonStatus);
			
			status.addObserver(legoVertical);
			status.addObserver(legoHorizontal);
			status.addObserver(legoGripper);
			status.addObserver(statusRequest);
			
			threads.add(legoGripper);
			threads.add(legoHorizontal);
			threads.add(legoVertical);
			
			threads.add(verticalController);
			threads.add(horizontalController);
			threads.add(gripperController);
			threads.add(statusController);
			
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
