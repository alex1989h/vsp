package impl.server;

import java.io.IOException;

public class ServerKontroller {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Server gestartet");
		int defaultPort = 9012;
		Receiver receiver = null;
		LegoUpDown upDown = new LegoUpDown();
		if(args.length == 1){
			receiver = new Receiver(Integer.parseInt(args[0]));
			receiver.start();
			upDown.start();
			receiver.join();
			upDown.join();
		}else if(args.length == 0){
			receiver = new Receiver(defaultPort);
			receiver.start();
			upDown.start();
			receiver.join();
			upDown.join();
		}else{
			System.out.println("Falsche Parameter Anzahl");
		}
		
	}

}
