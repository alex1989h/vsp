package generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Generator {

	public static void main(String[] args) throws IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		File xmlFile = new File("/home/students/abx970/VS/src/git/vsp/gen/xml/Stub.xml");
		try {
			builder = factory.newDocumentBuilder();
			document = (Document) builder.parse(xmlFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String stubName = document.getElementsByTagName("stubName").item(0).getTextContent();
		String importI = document.getElementsByTagName("import").item(0).getTextContent();
		String inter = document.getElementsByTagName("interface").item(0).getTextContent();
		
		String methodName = document.getElementsByTagName("methodName").item(0).getTextContent();

		String paramName1 = document.getElementsByTagName("paramName").item(0).getTextContent();
		String paramType1 = document.getElementsByTagName("type").item(0).getTextContent();

		String paramName2 = document.getElementsByTagName("paramName").item(1).getTextContent();
		String paramType2 = document.getElementsByTagName("type").item(1).getTextContent();
		
		String plain = document.getElementsByTagName("plain").item(0).getTextContent();
		

		String fileName = "/home/students/abx970/VS/src/git/vsp/gen/plain/Stub.txt";
		BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

		String line = "";
		StringBuffer buffer = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
			buffer.append("\n");
		}
		reader.close();
		String plainText = buffer.toString();

		
		
		
		
		String classString = String.format(plainText,importI,stubName,inter,stubName,"fifo1",methodName,paramType1+" "+paramName1+","+paramType2+" "+paramName2,plain,methodName,paramName1+","+paramName2);
		
		System.out.println(classString);

		fileName = "/home/students/abx970/VS/src/git/vsp/gen/" + stubName + ".java";
		PrintWriter writer = new PrintWriter(new FileWriter(new File(fileName)));

		writer.print(classString);
		writer.flush();
		writer.close();
		
	}

}
