package rmi.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import rmi.xml.MyXML;
import rmi.xml.MyXMLObject;

public class Receiver {
	private final DatagramSocket server;
	private static InetAddress address;
	private static int port;
	private static int transactionsID = Integer.MIN_VALUE;

	public Receiver() throws IOException {
		this.server = new DatagramSocket();
	}

	public byte[] receive() throws Exception {
		byte[] test = new byte[1000];
		DatagramPacket p = new DatagramPacket(test, test.length);
		Arrays.fill(test, (byte) 0);
		server.receive(p);
		System.out.println("Message received");
		String send = new String(p.getData()).trim();
		return send.getBytes();
	}

	public void send(int transactionsID, byte[] message) throws Exception {
		byte[] send = MyXML.createPacket(transactionsID, message);
		server.send(new DatagramPacket(send, send.length, address, port));
	}

	public void send(byte[] message) throws Exception {
		send(getTransactionsID(), message);
	}

	public static boolean checkNameServer(String address, int port) throws UnknownHostException, IOException {
		DatagramSocket socket = new DatagramSocket();
		byte[] recei = new byte[1000];
		DatagramPacket packet = new DatagramPacket(recei, recei.length);
		byte[] send = MyXML.createPacket(getTransactionsID(),"<SYN><returnType>String</returnType></SYN>");
		socket.send(new DatagramPacket(send, send.length, InetAddress.getByName(address), port));
		socket.setSoTimeout(3000);
		try{
		socket.receive(packet);
		MyXMLObject xml = MyXML.createXML(new String(packet.getData()).trim());
		if (xml.getXMLTyp().equals("ACK")) {
			Receiver.address = packet.getAddress();
			Receiver.port = packet.getPort();
			socket.close();
			return true;
		}
		}catch (Exception e) {
		}
		socket.close();
		return false;
	}
	
	public static void setAddress(String address) throws UnknownHostException {
		Receiver.address = InetAddress.getByName(address);
	}

	public static void setPort(int port) {
		Receiver.port = port;
	}

	public static InetAddress getAddress() {
		return address;
	}

	public static int getPort() {
		return port;
	}

	public synchronized static int getTransactionsID() {
		return transactionsID++;
	}
}
