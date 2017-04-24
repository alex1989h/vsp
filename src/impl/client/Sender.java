package impl.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.plaf.synth.SynthSeparatorUI;

import impl.factories.FiFoFactory;

public class Sender extends Thread{
	private FiFo fifo;
	private DatagramSocket socket = null;
	private int port;
	private InetAddress ia;
	
	public Sender(String ip, int port, String fifoName) throws UnknownHostException, SocketException{
		this.fifo = FiFoFactory.getFiFo(fifoName);
		this.socket = new DatagramSocket();
		this.ia = InetAddress.getByName(ip);
		this.port = port;
	}
	
	private void send(DatagramPacket sendPacket){
		byte[] buffer = new byte[1000];
		DatagramPacket p = new DatagramPacket(buffer,buffer.length);
		boolean received = false;
		int counter = 1;
		while (!received &&  counter <= 5) {
			try {
				socket.send(sendPacket);
				socket.setSoTimeout(100);
				Arrays.fill(buffer, (byte)0);
				socket.receive(p);
				System.out.println(new String(p.getData()).trim());
				received = true;
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println(counter++);
			}
		}
		if(!received){
			System.out.println("Service antwortet nicht");
		}
	}

	@Override
	public void run() {
		byte[] send = null;
		byte[] rec = new byte[1000];
		DatagramPacket p = new DatagramPacket(rec,rec.length,ia,port);;
        try {
            while(!isInterrupted()){
            	send = fifo.dequeue();
            	if(send != null){
            		System.out.println(new String(send));
            		send(new DatagramPacket(send,send.length,ia,port));
            		send = null;
            	}
            }
        } finally {
            if (socket != null) {
            socket.close();
            System.out.println("Socket geschlossen...");
			}
            
        }
    }
}