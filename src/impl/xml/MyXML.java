package impl.xml;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class MyXML {
	
	public static String createXMLString(Method method, Object... obj){
		return createXMLString(method.getName(), obj);
	}
	
	public static String createXMLString(String methodName, Object... obj){
		String xml = "";
		for (int i = 0; i < obj.length; i++) {
			xml+="<param><value>"+getType(obj[i])+"</value></param>";
		}
		xml = "<params>"+xml+"</params>";
		xml = "<methodName>"+methodName+"</methodName>"+xml;
		xml = "<methodCall>"+xml+"</methodCall>";
		xml = "<?xml version=\"1.0\"?>"+xml;
		return xml;
	}
	
	public static MyXMLObject createXML(byte[] input) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = (Document) builder.parse(new ByteArrayInputStream(input));
			return new MyXMLObject(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String getType(Object obj){
		if(obj instanceof Integer){
			return "<int>"+obj+"</int>";
		}
		if(obj instanceof String){
			return "<string>"+obj+"</string>";
		}
		return null;
	}
}
