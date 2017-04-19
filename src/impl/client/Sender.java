package impl.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import impl.factories.FiFoFactory;

public class Sender extends Thread{
	private FiFo fifo;
	private DatagramSocket socket = null;
	private int port;
	private InetAddress ia;
	
	public Sender(String ip, int port, String fifoName) throws UnknownHostException, SocketException{
		this.fifo = FiFoFactory.getFiFo(fifoName);
		this.port = port;
		this.ia = InetAddress.getByName(ip);
		this.socket = new DatagramSocket();
	}
	
	@Override
	public void run() {
		byte[] send = null;
		DatagramPacket p = null;
        try {
            while(true){
            	send = fifo.dequeue();
            	if(send != null){
            		System.out.println(new String(send));
            		p = new DatagramPacket(send,send.length,ia,port);
            		socket.send(p);
            		send = null;
            	}
            }
        } catch (IOException e) {
            System.out.println("IOProbleme...");
            e.printStackTrace();
        } finally {
            if (socket != null) {
            socket.close();
            System.out.println("Socket geschlossen...");
			}
            
        }
    }
}