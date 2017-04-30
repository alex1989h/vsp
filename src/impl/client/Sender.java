package impl.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Sender{
	private DatagramSocket socket = null;
	private int port;
	private InetAddress ia;
	
	public Sender() throws UnknownHostException, SocketException{
		this.socket = new DatagramSocket();
		this.ia = Broker.getAddress();
		this.port = Broker.getPort();
	}
	
	public byte[] send(byte[] send){
		byte[] buffer = new byte[1000];
		DatagramPacket p = new DatagramPacket(buffer,buffer.length);
		boolean received = false;
		int counter = 1;
		while (!received &&  counter <= 5) {
			try {
				socket.send(new DatagramPacket(send, send.length,ia,port));
				socket.setSoTimeout(100);
				Arrays.fill(buffer, (byte)0);
				socket.receive(p);
				received = true;
				return new String(p.getData()).trim().getBytes();
			} catch (IOException e) {
				System.out.println(counter++);
			}
		}
		if(!received){
			System.out.println("Service antwortet nicht");
		}
		return null;
	}
}