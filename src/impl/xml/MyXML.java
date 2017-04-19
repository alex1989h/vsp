package impl.xml;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class MyXML {
	
	public static String createXMLString(Method method, Object... obj){
		Parameter[] p = method.getParameters();
		MySignatur sig = new MySignatur();
		MyParameter[] param = new MyParameter[obj.length];
		for (int i = 0; i < obj.length; i++) {
			param[i] = new MyParameter();
			param[i].type = p[i].getType().getName();
			param[i].value = obj[i].toString();

		}
		sig.methodName = method.getName();
		sig.returnType = method.getReturnType().getName();
		sig.parameterCount = ""+obj.length;
		sig.parameter = param;

		StringWriter writer = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance(MySignatur.class);
			Marshaller marsh = context.createMarshaller();
			marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
			marsh.marshal(sig, writer);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}
}
