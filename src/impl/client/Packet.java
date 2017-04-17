package impl.client;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

enum Befehl {
	MOVE_HORIZONTAL, MOVE_VERTICAL, CLOSE, OPEN, STOP
}

@XmlRootElement
public class Packet {
	@XmlElement
	private Befehl befehl;
	@XmlElement
	private int id;
	@XmlElement
	private int percent;
	
	public Packet(){
		
	}
	
	public Packet(Befehl befehl,int id,int percent){
		this.befehl = befehl;
		this.id = id;
		this.percent = percent;
	}
	
	public int test(int i){
		return i;
	}
	
	@Override
	public String toString() {
		return "Befehl: " + befehl.name() + "\nId: " + id + "\nProzent: " + percent;
	}
}
