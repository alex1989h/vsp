package impl.robot;

import java.util.HashMap;

import impl.nameserver.AddressAndPort;

public class Robot {
	private HashMap<String,AddressAndPort> hashMap = null;
	
	public Robot() {
		hashMap = new HashMap<String,AddressAndPort>();
	}
	
	public AddressAndPort getService(String key){
		return hashMap.get(key);
	}
	
	public void setService(String key, AddressAndPort aAP){
		hashMap.put(key, aAP);
	}
}
