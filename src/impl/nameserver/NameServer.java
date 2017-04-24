package impl.nameserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;

import impl.xml.MyXML;
import impl.xml.MyXMLObject;

public class NameServer extends Thread{
	private HashMap<String,AddressAndPort> hashMap = null;
	private HashMap<Integer,AddressAndPort> reply = null;
	private DatagramSocket server = null;
	
	public NameServer(int port) throws SocketException{
		hashMap = new HashMap<String,AddressAndPort>();
		reply = new HashMap<Integer,AddressAndPort>();
		server = new DatagramSocket(port);
	}
	
	@Override
	public void run() {
		byte[] test = new byte[1000];
    	DatagramPacket p = new DatagramPacket(test,test.length);
    	while(!isInterrupted()){
    		try {
    			Arrays.fill(test, (byte)0);
				server.receive(p);
				System.out.println(p.getAddress().getHostAddress());
				System.out.println(p.getPort());
				process(p);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    	}
	}
	
	private void process(DatagramPacket message) throws IOException{
    	AddressAndPort aAP = null;
		MyXMLObject xml = MyXML.createXML(new String(message.getData()).trim().getBytes());
		switch (xml.getXMLTyp()) {
		case "getService":
			hashMap.get(xml.getMethodName());
			break;
		case "addService":
			hashMap.put(xml.getMethodName(),new AddressAndPort(message.getAddress(),message.getPort()));
			break;
		case "methodCall":
			aAP = hashMap.get(xml.getMethodName());
			if(aAP!= null){
				server.send(new DatagramPacket(message.getData(),message.getLength(),aAP.getAddress(),aAP.getPort()));
				reply.put((Integer)xml.getParamValues()[0], new AddressAndPort(message.getAddress(), message.getPort()));
			}
			break;
		case "ACK":
			aAP = reply.get((Integer)xml.getParamValues()[0]);
			if(aAP != null){
				server.send(new DatagramPacket(message.getData(),message.getLength(),aAP.getAddress(),aAP.getPort()));
				reply.remove((Integer)xml.getParamValues()[0]);
			}
			break;
		default:
			break;
		}
	}
	
	public void send(DatagramPacket packet){
		
	}
	
	public static void main(String[] args) {
		System.out.println("Name Server started");
		int defaulPort = 8888;
		try {
			if(args.length == 0){
				NameServer n = new NameServer(defaulPort);
				n.start();
				n.join();
			} else if(args.length == 1){
				NameServer n = new NameServer(Integer.parseInt(args[0]));
				n.start();
				n.join();
			} else {
				System.out.println("Falsche Parameteranzahl");
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
