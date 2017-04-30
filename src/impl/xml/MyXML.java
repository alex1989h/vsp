package impl.xml;

import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class MyXML {
	
	public static String createMethodCall(String returnType, String methodName, Object... obj){
		String xml = "";
		for (int i = 0; i < obj.length; i++) {
			xml+="<param><value>"+getType(obj[i])+"</value></param>";
		}
		xml = "<params>"+xml+"</params>";
		xml = "<returnType>"+returnType+"</returnType>"+xml;
		xml = "<methodName>"+methodName+"</methodName>"+xml;
		xml = "<methodCall>"+xml+"</methodCall>";
		xml = "<?xml version=\"1.0\"?>"+xml;
		return xml;
	}
	
	public static String createMethodResponse(Object... obj){
		String xml = "";
		for (int i = 0; i < obj.length; i++) {
			xml+="<param><value>"+getType(obj[i])+"</value></param>";
		}
		xml = "<params>"+xml+"</params>";
		xml = "<methodResponse>"+xml+"</methodResponse>";
		xml = "<?xml version=\"1.0\"?>"+xml;
		return xml;
	}
	
	public static MyXMLObject createXML(byte[] input) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		if(input != null){
			try {
				builder = factory.newDocumentBuilder();
				document = (Document) builder.parse(new ByteArrayInputStream(input));
				return new MyXMLObject(document);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static MyXMLObject createXML(String input) {
		return createXML(input.getBytes());
	}
	
	private static String getType(Object obj){
		if(obj instanceof Integer){
			return "<int>"+obj+"</int>";
		}
		if(obj instanceof String){
			return "<string>"+obj+"</string>";
		}
		if(obj instanceof Long){
			return "<long>"+obj+"</long>";
		}
		return null;
	}
	
	public static boolean testSignatur(MyXMLObject xml,String returnType,String methodName,String... params){
		if(!xml.getMethodName().equals(methodName)) return false;
		if(!xml.getReturnType().equals(returnType)) return false;
		if(xml.getParamValues().length != params.length) return false;
		for (int i = 0; i < params.length; i++) {
			if(!xml.getParamTypes()[i].equals(params[i]))return false;
		}
		return true;
	}
}
