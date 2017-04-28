package impl.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class MyXMLObject {
	Document document = null;
	public MyXMLObject(Document document) {
		this.document = document;
	}
	public String getXMLTyp(){
		return document.getDocumentElement().getNodeName();
	}
	public String getMethodName(){
		return document.getElementsByTagName("methodName").item(0).getTextContent();
	}
	
	public String[] getParamTypes(){
		String[] cl;
		NodeList nL = document.getElementsByTagName("param");
		cl = new String[nL.getLength()];
		for (int i = 0; i < nL.getLength(); i++) {
			cl[i] = getParamType(nL.item(i));
		}
		return cl;
	}
	
	private String getParamType(Node node){
		switch (node.getFirstChild().getFirstChild().getNodeName()) {
		case "int":
			return "int";
		case "string":
			return "String";
		default:
			break;
		}
		return "";
	}
	
	public Object[] getParamValues(){
		Object[] obj;
		NodeList nL = document.getElementsByTagName("param");
		obj = new Object[nL.getLength()];
		for (int i = 0; i < nL.getLength(); i++) {
			obj[i] = getParamValue(nL.item(i));
		}
		return obj;
	}
	
	private Object getParamValue(Node node){
		switch (node.getFirstChild().getFirstChild().getNodeName()) {
		case "int":
			return Integer.parseInt(node.getFirstChild().getFirstChild().getFirstChild().getTextContent());
		case "string":
			return node.getFirstChild().getFirstChild().getFirstChild().getTextContent();
		default:
			break;
		}
		return null;
	}
}
