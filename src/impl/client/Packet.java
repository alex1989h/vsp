package impl.client;

import java.io.Serializable;

enum Befehl {
	MOVE_HORIZONTAL, MOVE_VERTICAL, CLOSE, OPEN, STOP
}

@SuppressWarnings("serial")
public class Packet implements Serializable {
	
	private Befehl befehl;
	private int id;
	private int percent;
	
	public Packet(Befehl befehl,int id,int percent){
		this.befehl = befehl;
		this.id = id;
		this.percent = percent;
	}
	
	@Override
	public String toString() {
		return "Befehl: " + befehl.name() + "\nId: " + id + "\nProzent: " + percent;
	}
}
