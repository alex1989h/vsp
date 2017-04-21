package impl.nameserver;

import java.net.InetAddress;

public class AddressAndPort {
	private final InetAddress address;
	private final int port;
	
	public AddressAndPort(InetAddress address, int port){
		this.address = address;
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}

	public InetAddress getAddress() {
		return address;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AddressAndPort){
			AddressAndPort temp = (AddressAndPort) obj;
			return address == temp.getAddress() && port == temp.getPort();
		}
		return false;
	}
}
