package rpc.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class Generator {

	public static void main(String[] args) throws IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		File xmlFile = new File("gen/xml/Stub.xml");
		try {
			builder = factory.newDocumentBuilder();
			document = (Document) builder.parse(xmlFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String plainText = "";
		String receiverName = "Receiver";
		String senderName = "Sender";
		String myXMLName = "MyXML";
		String myXMLObjectName = "MyXMLObject";
		String nameServerName = "NameServer";
		String namespaceName = "Namespace";
		
		String communicationPackage = "rpc.communication";
		String stubPackage = "rpc.stubs";
		String interfacePackage = "rpc.interfaces";
		String skeletonPackage = "rpc.skeletons";
		String xmlPackage = "rpc.xml";
		String nameServePackage = "rpc.nameserver";
		String namespacePackage = "rpc.namespace";
		
		NodeList stubList = document.getElementsByTagName("stub");
		for (int i = 0; i < stubList.getLength(); i++) {
			Element stubElement = (Element)stubList.item(i);
			String name = stubElement.getElementsByTagName("name").item(0).getTextContent();
			String interfaceName = "I"+name;
			String stubName = "Stub"+name;
			String skeletonName = "Skeleton"+name;
			
			
			
			String methodString = "";
			String methodStringInterface = "";
			String ifComarators = "";
			String sendService = "";
			
			NodeList methodList = stubElement.getElementsByTagName("method");
			for (int j = 0; j < methodList.getLength(); j++) {
				Element methodElement = (Element)methodList.item(j);
				String methodName = methodElement.getElementsByTagName("methodName").item(0).getTextContent();
				String returnType = methodElement.getElementsByTagName("returnType").item(0).getTextContent();
				String plain = methodElement.getElementsByTagName("plain").item(0).getTextContent();
				
				String parameterliste = ""; 
				String parameterlisteOneTyp = "";
				String ifParam = "";
				String xmlParam = "";
				NodeList paramList = methodElement.getElementsByTagName("param");
				for (int k = 0; k < paramList.getLength(); k++) {
					Element paramElement = (Element)paramList.item(k);
					String type = paramElement.getElementsByTagName("type").item(0).getTextContent();
					String paramName = paramElement.getElementsByTagName("paramName").item(0).getTextContent();
					if(k > 0){
						parameterliste+=", ";
						xmlParam+=", ";
					}
					parameterliste+=type+" "+paramName;
					parameterlisteOneTyp+=", "+paramName;
					ifParam+=", \""+type+"\"";
					xmlParam+="("+type+")"+"xml.getParamValues()["+k+"]";
				}
				plainText = readPlainText("gen/plain/StubMethod.txt");
				methodString += String.format(plainText,returnType,methodName,parameterliste,plain+parameterlisteOneTyp,myXMLName,"\""+returnType+"\"",namespaceName,methodName,parameterlisteOneTyp,returnType,myXMLName);
				plainText = readPlainText("gen/plain/InterfaceMethod.txt");
				methodStringInterface+= String.format(plainText,returnType,methodName,parameterliste);
				plainText = readPlainText("gen/plain/SkeletonMethod.txt");
				ifComarators+= String.format(plainText,myXMLName,"\""+returnType+"\"",methodName,ifParam,returnType,methodName,xmlParam,myXMLName);
				plainText = readPlainText("gen/plain/addService.txt");
				sendService+= String.format(plainText,methodName);
			}
			
			plainText = readPlainText("gen/plain/Stub.txt");
			String classString = String.format(plainText,stubPackage,communicationPackage,senderName,interfacePackage,interfaceName,namespacePackage,namespaceName,xmlPackage,myXMLName,stubName,interfaceName,senderName,stubName,senderName,methodString);
			
			plainText = readPlainText("gen/plain/Interface.txt");
			String interfaceString = String.format(plainText,interfacePackage,interfaceName,methodStringInterface);
			
			plainText = readPlainText("gen/plain/Skeleton.txt");
			String skeletonString = String.format(plainText,skeletonPackage,interfacePackage,interfaceName,xmlPackage,myXMLName,xmlPackage,myXMLObjectName,communicationPackage,receiverName,skeletonName,interfaceName,receiverName,skeletonName,interfaceName,receiverName,sendService,myXMLObjectName,myXMLName,ifComarators,myXMLName);
			
			
			createFile("src/"+stubPackage.replaceAll("\\.", "/"),stubName,classString);
			createFile("src/"+interfacePackage.replaceAll("\\.", "/"),interfaceName,interfaceString);
			createFile("src/"+skeletonPackage.replaceAll("\\.", "/"),skeletonName,skeletonString);
		}
		plainText = readPlainText("gen/plain/Sender.txt");
		String senderString = String.format(plainText,communicationPackage,xmlPackage,myXMLName,senderName,senderName,myXMLName,myXMLName,myXMLName,senderName,senderName);
		
		plainText = readPlainText("gen/plain/Receiver.txt");
		String receiverString = String.format(plainText,communicationPackage,xmlPackage,myXMLName,receiverName,receiverName,myXMLName,receiverName,receiverName);
		
		plainText = readPlainText("gen/plain/MyXML.txt");
		String myXMLString = String.format(plainText,xmlPackage,myXMLName,myXMLObjectName,myXMLObjectName,myXMLObjectName,myXMLObjectName,myXMLObjectName);
		
		plainText = readPlainText("gen/plain/MyXMLObject.txt");
		String myXMLObjectString = String.format(plainText,xmlPackage,myXMLObjectName,myXMLObjectName);
		
		plainText = readPlainText("gen/plain/NameServer.txt");
		String nameServerString = String.format(plainText,nameServePackage,xmlPackage,myXMLName,xmlPackage,myXMLObjectName,nameServerName,nameServerName,myXMLObjectName,myXMLName,nameServerName,nameServerName);
		
		plainText = readPlainText("gen/plain/Namespace.txt");
		String namespaceString = String.format(plainText,namespacePackage,communicationPackage,senderName,xmlPackage,myXMLName,xmlPackage,myXMLObjectName,namespaceName,namespaceName,senderName,senderName,myXMLObjectName,myXMLName);
		
		createFile("src/"+communicationPackage.replaceAll("\\.", "/"),senderName,senderString);
		createFile("src/"+communicationPackage.replaceAll("\\.", "/"),receiverName,receiverString);
		createFile("src/"+xmlPackage.replaceAll("\\.", "/"),myXMLName,myXMLString);
		createFile("src/"+xmlPackage.replaceAll("\\.", "/"),myXMLObjectName,myXMLObjectString);
		createFile("src/"+nameServePackage.replaceAll("\\.", "/"),nameServerName,nameServerString);
		createFile("src/"+namespacePackage.replaceAll("\\.", "/"),namespaceName,namespaceString);
	}
	
	
	private static String readPlainText(String path) throws IOException{
		String fileName = path;
		BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

		String line = "";
		StringBuffer buffer = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
			buffer.append("\n");
		}
		reader.close();
		return buffer.toString();
	}
	
	private static void createFile(String path,String name, String input) throws IOException{
		String className = path+"/"+name+".java";
		new File(path).mkdirs();
		PrintWriter writer = new PrintWriter(new FileWriter(new File(className)));
		writer.print(input);
		writer.flush();
		writer.close();
	}
}
