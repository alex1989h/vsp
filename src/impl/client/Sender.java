package impl.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;

public class Sender extends Thread{
	private FiFo<Packet> fifo;
	private Packet packet = null;
	private Socket socket = null;
	
	public Sender(String ip, int port) throws UnknownHostException, IOException{
		fifo = new FiFo<Packet>(new LinkedList<Packet>());
		socket = new Socket(ip,port);
	}

	public FiFo<Packet> getFiFo() {
		return fifo;
	}
	
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
	    String eingabe = "";
        try {
            OutputStream raus = socket.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(raus);
            while(!eingabe.equals("stop")){
            	packet = fifo.dequeue();
            	if(packet!=null){
            		out.writeObject(packet);
            	}
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host...");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOProbleme...");
            e.printStackTrace();
        } finally {
            if (socket != null)
                try {
                	sc.close();
                    socket.close();
                    System.out.println("Socket geschlossen...");
                } catch (IOException e) {
                    System.out.println("Socket nicht zu schliessen...");
                    e.printStackTrace();
                }
            
        }
    } 
}


