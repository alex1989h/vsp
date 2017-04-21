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

public class NameServer extends Thread{
	private HashMap<String,AddressAndPort> hashMap = null;
	private DatagramSocket server = null;
	
	public NameServer(int port) throws SocketException {
		hashMap = new HashMap<String,AddressAndPort>();
		server = new DatagramSocket(port);
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
				if(aAP != null){
					str = aAP.getAddress().getHostAddress()+"S"+aAP.getPort();
					server.send(new DatagramPacket(str.getBytes(), str.length(), p.getAddress(), p.getPort()));
				}
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
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;
		builder = factory.newDocumentBuilder();
		document = (Document) builder.parse(new InputSource(new StringReader(new String(message.getData()).trim())));
		switch (document.getElementsByTagName("type").item(0).getTextContent()) {
		case "get":
			return hashMap.get(document.getElementsByTagName("name").item(0).getTextContent());
		case "add":
			hashMap.put(document.getElementsByTagName("name").item(0).getTextContent(),new AddressAndPort(message.getAddress(),message.getPort()));
			
			break;
		default:
			break;
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println("Name Server started");
		try {
			NameServer n = new NameServer(8888);
			n.start();
			n.join();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
