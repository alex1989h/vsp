package impl.server;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.plaf.synth.SynthSeparatorUI;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import impl.client.Packet;

public class Receiver extends Thread{
    private final ServerSocket server;
    public Receiver(int port) throws IOException {
        server = new ServerSocket(port);
    }

    private void receive(Socket socket) throws IOException, InterruptedException, ClassNotFoundException, JAXBException {
        Packet packet = null;
        InputStream input = socket.getInputStream();
    	//ObjectInputStream in = new ObjectInputStream(input);
        DataInputStream dos = new DataInputStream(socket.getInputStream());
        JAXBContext context = JAXBContext.newInstance( Packet.class );
        Unmarshaller m = context.createUnmarshaller();
        while(true) {
        	//packet = JAXB.unmarshal( input, Packet.class );
//        	System.out.println(input);
        	//packet = (Packet) in.readObject();
			//System.out.println("Received:\n"+packet);
        }
    }

	@Override
	public void run() {
		Socket socket = null;
        try {
            socket = server.accept();
            receive(socket);
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (socket != null)
                try {
                    socket.close();
                    System.out.println("Close Socket");
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
	}

} 
