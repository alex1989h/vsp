package impl.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.xml.MyXML;

public class Receiver extends Thread{
    private final DatagramSocket server;
    private FiFo fifo = null;
    private InetAddress address;
    private int port;
    
    public Receiver(String address, int port,String fifoName,String service) throws IOException {
    	this.address = InetAddress.getByName(address);
    	this.port = port;
        server = new DatagramSocket();
        fifo = FiFoFactory.getFiFo(fifoName);
    }

    private void receive() throws Exception {
    	byte[] test = new byte[1000];
    	DatagramPacket p = new DatagramPacket(test,test.length);
        while(true) {
        	Arrays.fill(test, (byte)0);
        	server.receive(p);
        	System.out.println("Message received");
        	String send = new String(p.getData()).trim();
        	fifo.enqueue(send.getBytes());
        	send = "<?xml version=\"1.0\"?><ACK><params><param><value><int>"+MyXML.createXML(send).getParamValues()[0]+"</int></value></param></params></ACK>";
        	server.send(new DatagramPacket(send.getBytes(),send.length(),p.getAddress(),p.getPort()));
        }
    }
    
    public void send(byte[] message) throws Exception {
    	server.send(new DatagramPacket(message, message.length,address,port));
    }

	@Override
	public void run() {
        try {
            receive();
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (server != null) {
				server.close();
				System.out.println("Close Socket");
			}
        }
	}
} 
