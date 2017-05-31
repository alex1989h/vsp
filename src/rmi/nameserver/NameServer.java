package rmi.nameserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import rmi.xml.MyXML;
import rmi.xml.MyXMLObject;

public class NameServer extends Thread{
	private HashMap<String,AddressAndPort> hashMap = null;
	private HashMap<Integer,AddressAndPort> reply = null;
	private List<String> services = null;
	
	private DatagramSocket server = null;
	
	private AddressAndPort nextNameServer = null;
	
	public void setNextNameServer(AddressAndPort nextNameServer) {
		this.nextNameServer = nextNameServer;
	}

	public NameServer(int port) throws SocketException{
		hashMap = new HashMap<String,AddressAndPort>();
		reply = new HashMap<Integer,AddressAndPort>();
		services = new LinkedList<String>();
		server = new DatagramSocket(port);
	}
	
	private class AddressAndPort {
		private final InetAddress address;
		private final int port;
		public AddressAndPort(InetAddress address, int port){
			this.address = address;
			this.port = port;
		}
		public int getPort() {
			return port;
		}
		public InetAddress getAddress() {
			return address;
		}
	}
	
	@Override
	public void run() {
		byte[] test = new byte[1000];
    	DatagramPacket p = new DatagramPacket(test,test.length);
    	while(!isInterrupted()){
    		try {
    			Arrays.fill(test, (byte)0);
				server.receive(p);
				process(p);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    	}
	}
	
	private void process(DatagramPacket message) throws IOException{
    	AddressAndPort aAP = null;
		MyXMLObject xml = MyXML.createXML(new String(message.getData()).trim().getBytes());
		System.out.println(xml.getXMLTyp()+" "+message.getAddress().getHostAddress()+" "+message.getPort());
		switch (xml.getXMLTyp()) {
		case "getService":
			byte[] send = getServices(xml.getTransactionsID());
			server.send(new DatagramPacket(send,send.length,message.getAddress(),message.getPort()));
			break;
		case "addService":
			System.out.println("New method: "+ xml.getMethodName());
			hashMap.put(xml.getMethodName(),new AddressAndPort(message.getAddress(),message.getPort()));
			if(!services.contains(xml.getMethodName().split("\\.")[0])){
				services.add(xml.getMethodName().split("\\.")[0]);
			}
			if(nextNameServer != null){
				System.out.println("Send to next NameServer");
				server.send(new DatagramPacket(message.getData(),message.getLength(),nextNameServer.getAddress(),nextNameServer.getPort()));
			}
			break;
		case "methodCall":
			aAP = hashMap.get(xml.getMethodName());
			if(aAP!= null){
				server.send(new DatagramPacket(message.getData(),message.getLength(),aAP.getAddress(),aAP.getPort()));
				reply.put((Integer)xml.getTransactionsID(), new AddressAndPort(message.getAddress(), message.getPort()));
			}
			break;
		case "methodResponse":
			aAP = reply.get((Integer)xml.getTransactionsID());
			if(aAP != null){
				server.send(new DatagramPacket(message.getData(),message.getLength(),aAP.getAddress(),aAP.getPort()));
				reply.remove((Integer)xml.getTransactionsID());
			}
			break;
		case "SYN":
			String ack = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><packet><ACK><params><param><value><string></string></value></param></params></ACK></packet>";
			server.send(new DatagramPacket(ack.getBytes(),ack.length(),message.getAddress(),message.getPort()));
			break;
		default:
			break;
		}
	}
	private byte[] getServices(int transactionsID){
		String ret = "";
		for (int i = 0; i < services.size(); i++) {
			ret = ret+"<param><value><string>"+services.get(i)+"</string></value></param>";
		}
		ret = "<params>"+ret+"</params>";
		ret = "<getService>"+ret+"</getService>";
		ret = "<packet>"+"<transactionsID>"+transactionsID+"</transactionsID>"+ret+"</packet>";
		ret = "<?xml version=\"1.0\"?>"+ret;
		return ret.getBytes();
	}
	
	public static void main(String[] args) {
		String error = "Wrong parameters\n";
		String output = "usage: java -cp Lego.jar rmi.nameserver.NameServer [-p <port>] [-bh <next broker host>] [-bp <next broker port>]\n"
				+ "-p <port> default:8888\n"
				+ "-bh <next broker host> default:\n"
				+ "[-bp <next broker port>] default:-1";
		int port = 8888;
		
		int nextNameServerPort = -1;
		String nextNameServerAddress = "";
		
		try {
			if(args.length > 0 && args[0].equals("-help")){
				System.out.println(output);
				return;
			}else if(args.length %2 == 0){
				for (int i = 0; i < args.length; i+=2) {
					switch (args[i]) {
					case "-p":
						port = Integer.parseInt(args[i + 1]);
						break;
					case "-bp":
						nextNameServerPort = Integer.parseInt(args[i + 1]);
						break;
					case "-bh":
						nextNameServerAddress = args[i + 1];
						break;
					default:
						System.out.println(error+output);
						return;
					}
				}
			}else{
				System.out.println(error+output);
				return;
			}
			System.out.println("Name Server started");
			NameServer n = new NameServer(port);
			if (nextNameServerPort != -1 && !nextNameServerAddress.equals("")) {
				n.setNextNameServer(n.new AddressAndPort(InetAddress.getByName(nextNameServerAddress), nextNameServerPort));
				System.out.println("Next NameServer:\n"+"Host: "+ nextNameServerAddress+"Port: "+nextNameServerPort);
			}
			n.start();
			n.join();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
