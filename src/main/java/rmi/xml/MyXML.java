package rmi.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
import org.xml.sax.SAXException;

public class MyXML {
	
	
	public static String createMethodCall(String returnType, String methodName, Object... obj) {
		Document document = createDocumentFromFile("MethodCall.xml");
		
		Node root = document.getFirstChild();
		NodeList rootChildren = root.getChildNodes();
		rootChildren.item(0).setTextContent(methodName);
		rootChildren.item(1).setTextContent(returnType);

		Node params = rootChildren.item(2);
		Node param = params.getFirstChild();
		params.removeChild(param);
		Node newChild;
		for (int i = 0; i < obj.length; i++) {
			newChild = param.cloneNode(true);
			newChild.getFirstChild().appendChild(document.createElement(getType(obj[i])));
			newChild.getFirstChild().getFirstChild().setTextContent(obj[i].toString());
			params.appendChild(newChild);
		}
		return transformToString(document);
	}
	
	public static String createMethodResponse(Object... obj) {
		Document document = createDocumentFromFile("MethodResponse.xml");
		Node root = document.getFirstChild();

		Node params = root.getFirstChild();
		Node param = params.getChildNodes().item(0);
		params.removeChild(param);
		
		Node newChild;
		for (int i = 0; i < obj.length; i++) {
			newChild = param.cloneNode(true);
			newChild.getFirstChild().appendChild(document.createElement(getType(obj[i])));
			newChild.getFirstChild().getFirstChild().setTextContent(obj[i].toString());
			params.appendChild(newChild);
		}
		return transformToString(document);
	}
	
	private static Document createDocumentFromFile(String pathname){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = (Document) builder.parse(new File(pathname));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	public static byte[] createPacket(int transactionsID, String input) {
		Document document = createDocumentFromFile("Packet.xml");
		document.setXmlStandalone(true);
		
		Node root = document.getFirstChild();
		root.getFirstChild().setTextContent("" + transactionsID);
		
		Document methodCall = createDocumentFromString(input);
		Node methodNode = methodCall.getFirstChild();
		
		Node importNode = document.importNode(methodNode, true);
		root.appendChild(importNode);
		
		return transformToString(document).getBytes();
	}
	
	public static byte[] createPacket(int transactionsID, byte[] input){
		return createPacket(transactionsID, new String(input));
	}
	
	private static Document createDocumentFromString(String xmlString){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = (Document) builder.parse(new InputSource(new StringReader(xmlString)));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	private static String transformToString(Document document){
		Transformer transformer;
		String xmlString = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			xmlString = result.getWriter().toString();
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			e.printStackTrace();
		}
		return xmlString;
	}
	
	public static MyXMLObject createXML(byte[] input) {
		MyXMLObject xmlObject =  null;
		if (input != null) {
			Document document = createDocumentFromString(new String(input));
			xmlObject = new MyXMLObject(document.getDocumentElement());
		}
		return xmlObject;
	}
	
	public static MyXMLObject createXML(String input) {
		return createXML(input.getBytes());
	}
	
	private static String getType(Object obj){
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
