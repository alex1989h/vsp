package rpc.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import rpc.xml.MyXML;

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

	public static void setAddress(InetAddress address) {
		Receiver.address = address;
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
