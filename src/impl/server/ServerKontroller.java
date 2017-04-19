package impl.server;

import java.io.IOException;

public class ServerKontroller {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Server gestartet");
		int defaultPort = 9012;
		Receiver receiver = null;
		SkeletonVerticalMovements upDown = new SkeletonVerticalMovements(new ModelVerticalMovements());
		String queue = "receiverVertical";
		if(args.length == 1){
			receiver = new Receiver(Integer.parseInt(args[0]),queue);
			receiver.start();
			upDown.start();
			receiver.join();
			upDown.join();
		}else if(args.length == 0){
			receiver = new Receiver(defaultPort,queue);
			receiver.start();
			upDown.start();
			receiver.join();
			upDown.join();
		}else{
			System.out.println("Falsche Parameter Anzahl");
		}
		
	}

}
