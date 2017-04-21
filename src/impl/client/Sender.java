package impl.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.plaf.synth.SynthSeparatorUI;

import impl.factories.FiFoFactory;

public class Sender extends Thread{
	private FiFo fifo;
	private DatagramSocket socket = null;
	private int port;
	private InetAddress ia;
	
	public Sender(String ip, int port, String fifoName) throws UnknownHostException, SocketException{
		this.fifo = FiFoFactory.getFiFo(fifoName);
		//this.port = port;
		//this.ia = InetAddress.getByName(ip);
		this.socket = new DatagramSocket();
		System.out.println(socket.getLocalPort());
		String str = "<packet><type>get</type><name>vertical</name></packet>";
		String[] s;
		byte[] temp = new byte[1000];
		DatagramPacket p = new DatagramPacket(temp, temp.length);
		try {
			socket.send(new DatagramPacket(str.getBytes(), str.length(),InetAddress.getByName("localhost"),8888));
			socket.receive(p);
			str = new String(p.getData()).trim();
			s = str.split("S");
			this.ia = InetAddress.getByName(s[0]);
			this.port = Integer.parseInt(s[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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