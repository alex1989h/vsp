package impl.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "method")
public class MySignatur {
	@XmlElement(name = "name")
	public String methodName;
	@XmlElement(name="returnType")
	public String returnType;
	@XmlElement(name="parameterCount")
	public String parameterCount;
	@XmlElement(name="param")
	public MyParameter[] parameter;
	
	@Override
	public String toString() {
		String ret = returnType + " " + methodName + "(";
		for (int i = 0; i < parameter.length; i++) {
			if(i > 0)ret +=",";
			ret += parameter[i];
		}
		return ret + ")";
	}
}
