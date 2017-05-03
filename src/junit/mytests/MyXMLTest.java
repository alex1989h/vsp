package junit.mytests;
import org.junit.Test;

import rmi.xml.MyXML;
import rmi.xml.MyXMLObject;

public class MyXMLTest {

	@Test
	public void test() {
		method("hallo",34);
	}
	
	private void method(String str2, int zahl){
		String xmlString = "<?xml version=\"1.0\"?><packet><transactionsID>-2147483636</transactionsID><methodCall><methodName>Rob1.getVerticalInPercent</methodName><returnType>int</returnType><params></params></methodCall></packet>";
		MyXMLObject xml = MyXML.createXML(xmlString);
		xml.print();
	}

}
