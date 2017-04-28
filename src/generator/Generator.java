package generator;

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
		
		NodeList stubList = document.getElementsByTagName("stub");
		for (int i = 0; i < stubList.getLength(); i++) {
			Element stubElement = (Element)stubList.item(i);
			String interfaceName = stubElement.getElementsByTagName("interfaceName").item(0).getTextContent();
			String stubPackage = stubElement.getElementsByTagName("stubPackage").item(0).getTextContent();
			String interfacePackage = stubElement.getElementsByTagName("interfacePackage").item(0).getTextContent();
			String skeletonPackage = stubElement.getElementsByTagName("skeletonPackage").item(0).getTextContent();
			String fifo = stubElement.getElementsByTagName("fifo").item(0).getTextContent();
			String stubName = stubElement.getElementsByTagName("stubName").item(0).getTextContent();
			String skeletonName = stubElement.getElementsByTagName("skeletonName").item(0).getTextContent();
			
			String methodString = "";
			String methodStringInterface = "";
			String ifComarators = "";
			String sendService = "";
			
			NodeList methodList = stubElement.getElementsByTagName("method");
			for (int j = 0; j < methodList.getLength(); j++) {
				Element methodElement = (Element)methodList.item(j);
				String methodName = methodElement.getElementsByTagName("methodName").item(0).getTextContent();
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
						parameterlisteOneTyp+=", ";
						ifParam+=", ";
						xmlParam+=", ";
					}
					parameterliste+=type+" "+paramName;
					parameterlisteOneTyp+=paramName;
					ifParam+="\""+type+"\"";
					xmlParam+="("+type+")"+"xml.getParamValues()["+k+"]";
				}
				String plainText = readPlainText("gen/plain/PublicMethod.txt");
				methodString += String.format(plainText,methodName,parameterliste,plain+parameterlisteOneTyp,methodName,parameterlisteOneTyp);
				plainText = readPlainText("gen/plain/InterfaceMethod.txt");
				methodStringInterface+= String.format(plainText,methodName,parameterliste);
				plainText = readPlainText("gen/plain/ifcompare.txt");
				ifComarators+= String.format(plainText,methodName,ifParam,methodName,xmlParam);
				plainText = readPlainText("gen/plain/sendService.txt");
				sendService+= String.format(plainText,methodName);
			}
			
			String plainText = readPlainText("gen/plain/Stub.txt");
			String classString = String.format(plainText,stubPackage,interfaceName,stubName,interfaceName,stubName,fifo,methodString);
			
			plainText = readPlainText("gen/plain/Interface.txt");
			String interfaceString = String.format(plainText,interfacePackage,interfaceName,methodStringInterface);
			
			plainText = readPlainText("gen/plain/Skeleton.txt");
			String skeletonString = String.format(plainText,skeletonPackage,interfaceName,skeletonName,interfaceName,skeletonName,interfaceName,sendService,fifo,ifComarators);
			
			System.out.println(classString);
			System.out.println(interfaceString);
			createFile("src/"+stubPackage.replaceAll("\\.", "/"),stubName,classString);
			createFile("src/impl/interfaces",interfaceName,interfaceString);
			createFile("src/"+skeletonPackage.replaceAll("\\.", "/"),skeletonName,skeletonString);
		}
		
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
