package impl.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import impl.client.Broker;

public class Receiver{
    private final DatagramSocket server;
    private InetAddress address;
    private int port;
    
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
		// send = "<?xml
		// version=\"1.0\"?><ACK><params><param><value><int>"+MyXML.createXML(send).getParamValues()[0]+"</int></value></param></params></ACK>";
		// server.send(new
		// DatagramPacket(send.getBytes(),send.length(),p.getAddress(),p.getPort()));
	}
    
    public void send(byte[] message) throws Exception {
    	server.send(new DatagramPacket(message, message.length,address,port));
    }
} 
