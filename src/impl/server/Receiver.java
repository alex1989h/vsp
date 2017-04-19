package impl.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

import impl.client.FiFo;
import impl.factories.FiFoFactory;

public class Receiver extends Thread{
    private final DatagramSocket server;
    private FiFo fifo = null;
    public Receiver(int port,String fifoName) throws IOException {
        server = new DatagramSocket(port);
        fifo = FiFoFactory.getFiFo(fifoName);
    }

    private void receive(Socket socket) throws Exception {
    	byte[] test = new byte[2000];
    	DatagramPacket p = new DatagramPacket(test,test.length);
        while(true) {
        	Arrays.fill(test, (byte)0);
        	server.receive(p);
        	System.out.println("Message received");
        	fifo.enqueue(new String(p.getData()).trim().getBytes());
        }
    }

	@Override
	public void run() {
		Socket socket = null;
        try {
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
		} catch (Exception e) {
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
