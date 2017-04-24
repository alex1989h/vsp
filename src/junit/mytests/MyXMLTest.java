package junit.mytests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import impl.xml.MyXML;
import impl.xml.MyXMLObject;

public class MyXMLTest {

	@Test
	public void test() {
		method("hallo",34);
	}
	
	private void method(String str2, int zahl){
		Method method = new Object(){}.getClass().getEnclosingMethod();
		String str = MyXML.createXMLString(method, str2, zahl);
		MyXMLObject xml = MyXML.createXML(str.getBytes());
		System.out.println(str);
		System.out.println(xml.getXMLTyp());
		System.out.println(xml.getMethodName());
		for (int i = 0; i < xml.getParamValues().length; i++) {
			System.out.println(xml.getParamTypes()[i]);
			System.out.println(xml.getParamValues()[i]);
		}
	}

}
