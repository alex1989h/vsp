package impl.nameserver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import impl.xml.MyXML;
import impl.xml.MyXMLObject;

public class NameServer extends Thread{
	private HashMap<String,AddressAndPort> hashMap = null;
	private DatagramSocket server = null;
	private DatagramSocket transfer = null;
	
	public NameServer(int port) throws SocketException {
		hashMap = new HashMap<String,AddressAndPort>();
		server = new DatagramSocket(port);
		transfer = new DatagramSocket();
	}
	
	@Override
	public void run() {
		byte[] test = new byte[1000];
    	DatagramPacket p = new DatagramPacket(test,test.length);
    	AddressAndPort aAP = null;
    	String str;
    	while(!isInterrupted()){
    		try {
    			Arrays.fill(test, (byte)0);
				server.receive(p);
				System.out.println(p.getAddress().getHostAddress());
				System.out.println(p.getPort());
				aAP = process(p);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
	}
	
	private AddressAndPort process(DatagramPacket message) throws ParserConfigurationException, SAXException, IOException{
		byte[] test = new byte[1000];
    	DatagramPacket p = new DatagramPacket(test,test.length);
    	
		MyXMLObject xml = MyXML.createXML(new String(message.getData()).trim().getBytes());
		switch (xml.getXMLTyp()) {
		case "getService":
			return hashMap.get(xml.getMethodName());
		case "addService":
			hashMap.put(xml.getMethodName(),new AddressAndPort(message.getAddress(),message.getPort()));
			break;
		case "methodCall":
			AddressAndPort aAP = hashMap.get(xml.getMethodName());
			transfer.send(new DatagramPacket(message.getData(),message.getLength(),aAP.getAddress(),aAP.getPort()));
			transfer.setSoTimeout(500);
			transfer.receive(p);
			transfer.send(new DatagramPacket(p.getData(),p.getLength(),message.getAddress(),message.getPort()));
			break;
		default:
			break;
		}
		return null;
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
