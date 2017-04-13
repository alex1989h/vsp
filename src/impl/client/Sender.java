package impl.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

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
            OutputStream output = socket.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(output);
            StringWriter sw = new StringWriter();
            
            JAXBContext context = JAXBContext.newInstance( Packet.class );
            Marshaller m = context.createMarshaller();
            m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            
            while(!eingabe.equals("stop")){
            	packet = fifo.dequeue();
            	if(packet!=null){
            		
            		m.marshal(packet, sw);
            		System.out.println(sw.toString());
            		out.writeObject(sw.toString());
            		sw.getBuffer().setLength(0);
            	}
            }
        } catch (IOException e) {
            System.out.println("IOProbleme...");
            e.printStackTrace();
        } catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
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


