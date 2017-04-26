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
		String parameterliste = ""; 
		String parameterlisteOneTyp = ""; 		
		NodeList stubList = document.getElementsByTagName("stub");
		Element stubElement;
		String iFace;
		String stub;
		for (int i = 0; i < stubList.getLength(); i++) {
			stubElement = (Element)stubList.item(i);
			iFace = stubElement.getElementsByTagName("interface").item(0).getTextContent();
			String packageStubs = stubElement.getElementsByTagName("package").item(0).getTextContent();
			String fifo = stubElement.getElementsByTagName("fifo").item(0).getTextContent();;
			stub = "Stub"+iFace;
			NodeList methodList = stubElement.getElementsByTagName("method");
			Element methodElement;
			String methodName;
			String methodString = "";
			String methodStringInterface = "";
			String plain;
			for (int j = 0; j < methodList.getLength(); j++) {
				methodElement = (Element)methodList.item(j);
				methodName = methodElement.getElementsByTagName("methodName").item(0).getTextContent();
				plain = methodElement.getElementsByTagName("plain").item(0).getTextContent();
				NodeList paramList = methodElement.getElementsByTagName("param");
				Element paramElement;
				String type;
				String paramName;
				parameterliste = ""; 
				parameterlisteOneTyp = ""; 
				for (int k = 0; k < paramList.getLength(); k++) {
					paramElement = (Element)paramList.item(k);
					type = paramElement.getElementsByTagName("type").item(0).getTextContent();
					paramName = paramElement.getElementsByTagName("paramName").item(0).getTextContent();
					if(k > 0){
						parameterliste+=", ";
						parameterlisteOneTyp+=", ";
					}
					parameterliste+=type+" "+paramName;
					parameterlisteOneTyp+=paramName;
				}
				String plainText = readPlainText("gen/plain/PublicMethod.txt");
				methodString += String.format(plainText,methodName,parameterliste,plain+parameterlisteOneTyp,methodName,parameterlisteOneTyp);
				plainText = readPlainText("gen/plain/InterfaceMethod.txt");
				methodStringInterface+= String.format(plainText,methodName,parameterliste);
			}
			String plainText = readPlainText("gen/plain/Stub.txt");
			String classString = String.format(plainText,packageStubs,iFace,stub,iFace,stub,fifo,methodString);
			plainText = readPlainText("gen/plain/Interface.txt");
			String interfaceString = String.format(plainText,iFace,methodStringInterface);
			
			System.out.println(classString);
			System.out.println(interfaceString);
			String className = "src/"+packageStubs.replaceAll("\\.", "/")+"/"+stub+".java";
			new File("src/"+packageStubs.replaceAll("\\.", "/")).mkdirs();
			PrintWriter writer = new PrintWriter(new FileWriter(new File(className)));
			writer.print(classString);
			writer.flush();
			writer.close();
			new File("src/impl/interfaces").mkdirs();
			className = "src/impl/interfaces/"+iFace+".java";
			writer = new PrintWriter(new FileWriter(new File(className)));
			writer.print(interfaceString);
			writer.flush();
			writer.close();
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
}
