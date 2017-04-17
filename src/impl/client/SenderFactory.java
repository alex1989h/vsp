package impl.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class SenderFactory {
	private static HashMap<IpAndPort,Sender> hashMap = new HashMap<IpAndPort,Sender>();
	
	private SenderFactory(){
		
	}
	
	public static Sender createInstance(String adresse,int port) throws UnknownHostException, IOException{
		IpAndPort iAP = new IpAndPort(adresse,port);
		Sender sender = hashMap.get(iAP);
		if(sender == null){
			sender = new Sender(adresse,port);
			hashMap.put(iAP, sender);
		}
		return sender;
	}
}
