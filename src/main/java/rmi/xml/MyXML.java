package rmi.xml;

import java.awt.Robot;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class MyXML {
	
	public static String createMethodCall(String returnType, String methodName, Object... obj){
		return createMethodCallNew(returnType, methodName, obj);
	}
	
	public static String createMethodCallNew(String returnType, String methodName, Object... obj) {
		String file = "MethodCall.xml";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = (Document) builder.parse(new File(file));

			Node root = document.getFirstChild();
			NodeList rootChildren = root.getChildNodes();
			
			rootChildren.item(0).setTextContent(methodName);
			rootChildren.item(1).setTextContent(returnType);

			Node params = rootChildren.item(2);
			Node param = params.getChildNodes().item(0);
			
			params.removeChild(param);
			Node newChild;
			for (int i = 0; i < obj.length; i++) {
				newChild = param.cloneNode(true);
				newChild.getFirstChild().appendChild(document.createElement(getType2(obj[i])));
				newChild.getFirstChild().getFirstChild().setTextContent(obj[i].toString());
				params.appendChild(newChild);
			}
			
			Transformer transformer;
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			String xmlString = result.getWriter().toString();

			return xmlString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String createMethodResponse(Object... obj){
		String xml = "";
		for (int i = 0; i < obj.length; i++) {
			xml+="<param><value>"+getType(obj[i])+"</value></param>";
		}
		xml = "<params>"+xml+"</params>";
		xml = "<methodResponse>"+xml+"</methodResponse>";
		return xml;
	}
	
	public static byte[] createPacket(int transactionsID, String input){
		return createPacketNew(transactionsID, input);
	}
	
	public static byte[] createPacketNew(int transactionsID, String input){
		String file = "Packet.xml";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		Document methodCall = null;
		try {
			builder = factory.newDocumentBuilder();
			document = (Document) builder.parse(new File(file));
			document.setXmlStandalone(true); 
			Node root = document.getFirstChild();
			NodeList rootChildren = root.getChildNodes();
			
			rootChildren.item(0).setTextContent(""+transactionsID);
			methodCall = (Document) builder.parse(new InputSource(new StringReader(input)));
			Node methodNode = methodCall.getFirstChild();
			Node importNode = document.importNode(methodNode, true);
			root.appendChild(importNode);
			
			Transformer transformer;
			transformer = TransformerFactory.newInstance().newTransformer();
			
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			String xmlString = result.getWriter().toString();

			return xmlString.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static byte[] createPacket(int transactionsID, byte[] input){
		return createPacket(transactionsID, new String(input));
	}
	
	public static MyXMLObject createXML(byte[] input) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		if(input != null){
			try {
				builder = factory.newDocumentBuilder();
				document = (Document) builder.parse(new ByteArrayInputStream(input));
				return new MyXMLObject(document.getDocumentElement());
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
	
	private static String getType2(Object obj){
		if(obj instanceof Integer){
			return "int";
		}
		if(obj instanceof String){
			return "string";
		}
		if(obj instanceof Long){
			return "long";
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

	public static byte[] getConnectError(MyXMLObject xml) {
		switch (xml.getReturnType()) {
		case "String":
			return createMethodResponse("ERROR").getBytes();
		case "int":
			return createMethodResponse(-1).getBytes();
		case "long":
			return createMethodResponse(-1L).getBytes();
		default:
			return null;
		}
	}
}
