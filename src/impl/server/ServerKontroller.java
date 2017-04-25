package impl.server;

import java.io.IOException;

public class ServerKontroller {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Server gestartet");
		int defaultPort = 9012;
		Receiver receiver = null;
		SkeletonVerticalMovements upDown = null;
		String queue = "receiverVertical";
		if(args.length == 2){
			upDown = new SkeletonVerticalMovements(new ModelVerticalMovements(),args[0]);
			receiver = new Receiver(Integer.parseInt(args[1]),queue,args[0]);
			receiver.start();
			upDown.start();
			receiver.join();
			upDown.join();
		}else if(args.length == 0){
			upDown = new SkeletonVerticalMovements(new ModelVerticalMovements(),"Robot1");
			receiver = new Receiver(defaultPort,queue,"Robot1");
			receiver.start();
			upDown.start();
			receiver.join();
			upDown.join();
		}else{
			System.out.println("Falsche Parameter Anzahl");
		}
		
	}

}
