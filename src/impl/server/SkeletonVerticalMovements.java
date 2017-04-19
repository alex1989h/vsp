package impl.server;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import impl.client.FiFo;
import impl.factories.FiFoFactory;
import impl.interfaces.IVerticalMovements;

public class SkeletonVerticalMovements extends Thread {
	private IVerticalMovements vertical;
	private FiFo fifo;
	private long oldId = Long.MIN_VALUE;
	
	public SkeletonVerticalMovements(IVerticalMovements vertical) {
		this.vertical = vertical;
	}
	
	@Override
	public void run() {
		byte[] b;
		fifo = FiFoFactory.getFiFo("receiverVertical");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;

		while (true) {
			System.out.println("Wait for Queue");
			b = fifo.dequeue();
			System.out.println("Dequeued");
			System.out.println(new String(b));
			try {
				builder = factory.newDocumentBuilder();
				document = (Document) builder.parse(new ByteArrayInputStream(b));
			} catch (Exception e) {
				e.printStackTrace();
			}
			NodeList param;
			if (document.getElementsByTagName("name").item(0).getTextContent().equals("moveVerticalToPercent")) {
				param = document.getElementsByTagName("param");
				String transactionID = param.item(0).getChildNodes().item(1).getTextContent();
				String percent = param.item(1).getChildNodes().item(1).getTextContent();
				moveVerticalToPercent(Integer.parseInt(transactionID), Integer.parseInt(percent));
			}
		}
	}
	
	public int moveVerticalToPercent(int transactionID, int percent) {
		if (this.oldId < transactionID) {
			this.oldId = transactionID;
			vertical.moveVerticalToPercent(transactionID, percent);
		}
		return 0;
	}

}
