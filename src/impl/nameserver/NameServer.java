package impl.nameserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import impl.xml.MyXML;
import impl.xml.MyXMLObject;

public class NameServer extends Thread{
	private HashMap<String,AddressAndPort> hashMap = null;
	private HashMap<Integer,AddressAndPort> reply = null;
	private List<String> services = null;
	
	private DatagramSocket server = null;
	
	public NameServer(int port) throws SocketException{
		hashMap = new HashMap<String,AddressAndPort>();
		reply = new HashMap<Integer,AddressAndPort>();
		services = new LinkedList<String>();
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
			byte[] send = getServices();
			server.send(new DatagramPacket(send,send.length,message.getAddress(),message.getPort()));
			break;
		case "addService":
			System.out.println("New method: "+ xml.getMethodName());
			hashMap.put(xml.getMethodName(),new AddressAndPort(message.getAddress(),message.getPort()));
			if(!services.contains(xml.getMethodName().split("\\.")[0])){
				services.add(xml.getMethodName().split("\\.")[0]);
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
		default:
			break;
		}
	}
	private byte[] getServices(){
		String ret = "";
		for (int i = 0; i < services.size(); i++) {
			ret = ret+"<param><value><string>"+services.get(i)+"</string></value></param>";
		}
		ret = "<params>"+ret+"</params>";
		ret = "<getService>"+ret+"</getService>";
		ret = "<?xml version=\"1.0\"?>"+ret;
		return ret.getBytes();
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
