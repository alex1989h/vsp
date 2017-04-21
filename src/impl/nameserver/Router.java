package impl.nameserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Router extends Thread{
	private final InetAddress ai;
	private final int port;
	private final DatagramSocket socket;
	
	public Router(String ai,int port, int localPort) throws SocketException, UnknownHostException {
		this.ai = InetAddress.getByName(ai);;
		this.port = port;
		socket = new DatagramSocket();
	}
	
	@Override
	public void run() {
		byte[] test = new byte[2000];
    	DatagramPacket p = new DatagramPacket(test,test.length);
		while(!isInterrupted()){
			Arrays.fill(test, (byte)0);
			try {
				socket.receive(p);
				System.out.println("Message received");
				p.setAddress(ai);
				p.setPort(port);
				socket.send(p);
				System.out.println("Message send");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
