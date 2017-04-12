package impl.server;

import java.io.IOException;

public class ServerKontroller {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Server gestartet");
		int defaultPort = 9012;
		Receiver receiver = null;
		if(args.length == 1){
			receiver = new Receiver(Integer.parseInt(args[0]));
			receiver.start();
			receiver.join();
		}else if(args.length == 0){
			receiver = new Receiver(defaultPort);
			receiver.start();
			receiver.join();
		}else{
			System.out.println("Falsche Parameter Anzahl");
		}
		
	}

}
