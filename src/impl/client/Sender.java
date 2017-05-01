package impl.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import impl.xml.MyXML;

public class Sender{
	private DatagramSocket socket = null;
	private int port;
	private InetAddress ia;
	private static int transactionsID = Integer.MIN_VALUE;
	
	public synchronized static int getTransactionsID(){
		return transactionsID++;
	}
	
	public Sender() throws UnknownHostException, SocketException{
		this.socket = new DatagramSocket();
		this.ia = Broker.getAddress();
		this.port = Broker.getPort();
	}
	
	public byte[] send(byte[] method){
		return send(getTransactionsID(), method);
	}
	
	public byte[] send(int transactionsID, byte[] method){
		byte[] buffer = new byte[1000];
		DatagramPacket p = new DatagramPacket(buffer,buffer.length);
		boolean received = false;
		int counter = 1;
		byte[] send = MyXML.createPacket(transactionsID, method);
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