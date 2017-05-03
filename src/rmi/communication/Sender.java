package rmi.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import rmi.xml.MyXML;

public class Sender{
	private DatagramSocket socket = null;
	private static InetAddress address;
	private static int port;
	private static int transactionsID = Integer.MIN_VALUE;
	
	public Sender() throws UnknownHostException, SocketException{
		this.socket = new DatagramSocket();
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
				socket.send(new DatagramPacket(send, send.length,address,port));
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
		return MyXML.getConnectError(MyXML.createXML(method));
		
	}
	
	public static void setAddress(InetAddress address) {
		Sender.address = address;
	}
	
	public static void setPort(int port) {
		Sender.port = port;
	}
	
	public static InetAddress getAddress() {
		return address;
	}
	
	public static int getPort() {
		return port;
	}

	public synchronized static int getTransactionsID(){
		return transactionsID++;
	}
}
