package impl.client;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

enum Befehl {
	MOVE_HORIZONTAL, MOVE_VERTICAL, CLOSE, OPEN, STOP
}
@XmlRootElement( name = "PACKET" )
public class Packet {
	@XmlElement( name = "BEFEHL" )
	private Befehl befehl;
	@XmlElement( name = "ID" )
	private int id;
	
	private int percent;
	
	public Packet(){
		
	}
	
	public Packet(Befehl befehl,int id,int percent){
		this.befehl = befehl;
		this.id = id;
		this.percent = percent;
	}
	@XmlElement( name = "PERCENT" )
	public int getPercent(){
		return percent;
	}
	
	//@XmlElement( name = "PERCENTT" )
	public int test(int i){
		return i;
	}
	
	@Override
	public String toString() {
		return "Befehl: " + befehl.name() + "\nId: " + id + "\nProzent: " + percent;
	}
}
