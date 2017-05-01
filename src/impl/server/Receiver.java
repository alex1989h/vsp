package impl.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import impl.client.Broker;
import impl.xml.MyXML;

public class Receiver{
    private final DatagramSocket server;
    private InetAddress address;
    private int port;
    private static int transactionsID = Integer.MIN_VALUE;
	public synchronized static int getTransactionsID(){
		return transactionsID++;
	}
	
    public Receiver() throws IOException {
    	this.address = Broker.getAddress();
    	this.port = Broker.getPort();
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
    
    public void send(int transactionsID,byte[] message) throws Exception {
    	byte[] send = MyXML.createPacket(transactionsID, message);
    	server.send(new DatagramPacket(send, send.length,address,port));
    }
    
    public void send(byte[] message) throws Exception {
    	send(getTransactionsID(),message);
    }
} 
