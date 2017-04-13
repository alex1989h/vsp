package impl.client;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

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
		Packet lockPack;
		Scanner sc = new Scanner(System.in);
	    String eingabe = "";
        try {
            OutputStream output = new FileOutputStream( "nosferatu.xml" );
            InputStream input = new FileInputStream( "nosferatu.xml" );
            //ObjectOutputStream out = new ObjectOutputStream(output);
            JAXBContext context = JAXBContext.newInstance( Packet.class );
            Marshaller m = context.createMarshaller();
            m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            Unmarshaller um = context.createUnmarshaller();
            while(!eingabe.equals("stop")){
            	packet = fifo.dequeue();
            	if(packet!=null){
            		//JAXB.marshal( packet, output );
            		System.out.println("1");
            		m.marshal( packet, output );
            		System.out.println("2");
            		sleep(1000);
            		System.out.println("3");
            		lockPack = (Packet) um.unmarshal(input);
            		input.close();
            		System.out.println("4");
            		//out.writeObject(packet);
            		System.out.println("In: "+lockPack);
            	}
            }
        } catch (IOException e) {
            System.out.println("IOProbleme...");
            e.printStackTrace();
        } catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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


