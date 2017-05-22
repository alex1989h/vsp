package impl.http.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		ServerSocket server;
		try {
			server = new ServerSocket(8000);
			while (true) {
				Socket socket = server.accept();
				System.out.println("Got a client !");

				// either open the datainputstream directly
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				
				// or chain them, but do not open two different streams:
				// DataInputStream dis = new DataInputStream(new
				// BufferedInputStream(socket.getInputStream()));

				// Your DataStream allows you to read/write objects, use it!
				byte[] b = new byte[1000];
				dis.read(b);
				System.out.println("received: "+new String(b).trim());
				dos.write("Das ist eine Antwort".getBytes());
				dos.flush();
				dis.close();
				dos.close();
				// in case you have a bufferedInputStream inside of
				// Datainputstream:
				// you do not have to close the bufferedstream
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
