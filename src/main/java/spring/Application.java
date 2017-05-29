package spring;

import java.io.IOException;
import java.net.UnknownHostException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import rmi.communication.Sender;
import rmi.namespace.Namespace;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws UnknownHostException, IOException {
    	String error = "Wrong parameters\n";
		String output = "usage: java -cp Lego.jar impl.client.Controller [-h <hostname>] [-p <port>]\n"
				+ "Paramete:\n" + "-h <hostname> default:255.255.255.255\n"
				+ "-p <port> default:8888";
		String address = "255.255.255.255";
		int port = 8888;
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
    	boolean result = Sender.checkNameServer(address, port);
    	if (result) {
    		System.out.println("NameServer gefunden");
    		String[] roboter = Namespace.lookup();
    		if(roboter!=null && roboter.length>0){
        		System.out.println("Robot gefunden: "+roboter[0]);
        		Namespace.setName(roboter[0]);
	    		SpringApplication.run(Application.class, args);
    		}
		}else{
			System.out.println("NameServer nicht gefunden");
		}
    }
}
