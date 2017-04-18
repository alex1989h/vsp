package impl.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyParameter {
	@XmlElement(name="type")
	public String type;
	@XmlElement(name="value")
	public String value;
	@Override
	public String toString() {
		return type+" "+value;
	}
}
