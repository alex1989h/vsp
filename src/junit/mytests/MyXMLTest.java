package junit.mytests;
import org.junit.Test;

import rmi.xml.MyXML;
import rmi.xml.MyXMLObject;

public class MyXMLTest {

	@Test
	public void test() {
		String first = MyXML.createMethodCallNew("String", "hallo.method", (int)23,"Hallo");
		String second;
				
		System.out.println(new String(MyXML.createPacket(22222, first)));
		System.out.println(new String(MyXML.createPacketNew(22222, first)));
		
		
		method("hallo",34);
	}
	
	private void method(String str2, int zahl){
		String xmlString = "<?xml version=\"1.0\"?><packet><transactionsID>-2147483636</transactionsID><methodCall><methodName>Rob1.getVerticalInPercent</methodName><returnType>int</returnType><params></params></methodCall></packet>";
		MyXMLObject xml = MyXML.createXML(xmlString);
		xml.print();
	}

}
