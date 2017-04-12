package impl.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import impl.client.Packet;

public class Receiver extends Thread{
    private final ServerSocket server;
    public Receiver(int port) throws IOException {
        server = new ServerSocket(port);
    }

    private void receive(Socket socket) throws IOException, InterruptedException, ClassNotFoundException {
        Packet packet = null;
    	ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        while(true) {
        	try {
				packet = (Packet) in.readObject();
	            System.out.println("Received:\n"+packet);
			} catch (SocketException e) {
				break;
			}
	            
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
