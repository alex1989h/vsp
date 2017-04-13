package impl.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Receiver extends Thread{
    private final ServerSocket server;
    public Receiver(int port) throws IOException {
        server = new ServerSocket(port);
    }

    private void receive(Socket socket) throws Exception {
        InputStream input = socket.getInputStream();
        String st; 
    	ObjectInputStream in = new ObjectInputStream(input);
        while(true) {
        	st = (String) in.readObject(); 
        	
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
            DocumentBuilder builder; 
            Document document = null;
            try  
            {  
                builder = factory.newDocumentBuilder();  
                document = (Document) builder.parse( new InputSource( new StringReader(st) ) );  
            } catch (Exception e) {  
                e.printStackTrace();  
            }
            Element element;
            NodeList nl;
            element = document.getDocumentElement();
            nl = element.getChildNodes();
            for (int j = 0; j < nl.getLength(); j++) {
				 Node nNode = nl.item(j);
		        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		            Element eElement = (Element) nNode;
		            System.out.println(eElement.getNodeName()+ " : "+eElement.getTextContent());
		        }
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
