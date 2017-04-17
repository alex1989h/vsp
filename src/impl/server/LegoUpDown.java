package impl.server;

import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;

import javax.xml.crypto.dsig.XMLObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;
import org.omg.CORBA.ARG_OUT;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import impl.client.FiFo;
import impl.client.FiFoFactory;

public class LegoUpDown extends Thread implements ICaDSEV3RobotStatusListener, ICaDSEV3RobotFeedBackListener  {
	private static CaDSEV3RobotStudentImplementation caller = null;
	private FiFo fifo;
	private long percent = 0,oldPercent = 0;
	private long id = 0;
	@Override
	public void giveFeedbackByJSonTo(JSONObject arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusMessage(JSONObject arg0) {
		Long l = null;
		if(((String)arg0.get("state")).equals("vertical")){
			l = (Long)arg0.get("percent");
			if(percent > oldPercent && l >= percent){
				caller.stop_v();
				oldPercent = percent;
			}else if(percent < oldPercent && l <= percent){
				caller.stop_v();
				oldPercent = percent;
			}
		}
	}

	@Override
	public void run() {
		byte[] b;
		caller = CaDSEV3RobotStudentImplementation.createInstance(CaDSEV3RobotType.SIMULATION, this, this);
		fifo = FiFoFactory.getFiFo("receiver");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;

		while (true) {
			b = fifo.dequeue();
			try {
				builder = factory.newDocumentBuilder();
				document = (Document) builder.parse(new ByteArrayInputStream(b));
			} catch (Exception e) {
				e.printStackTrace();
			}
			NodeList param;
			if (document.getElementsByTagName("name").item(0).getTextContent().equals("moveVerticalToPercent")) {
				param = document.getElementsByTagName("param");
				id = Long.parseLong(param.item(0).getChildNodes().item(1).getTextContent());
				percent = Long.parseLong(param.item(1).getChildNodes().item(1).getTextContent());
				System.out.println(param);
					if (percent < oldPercent) {
						caller.stop_v();
						caller.moveDown();
					} else if (percent > oldPercent) {
						caller.stop_v();
						caller.moveUp();
					}
			}
		}
	}

}