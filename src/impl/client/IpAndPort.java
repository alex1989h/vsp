package impl.client;

public class IpAndPort {
	private String ip;
	private int port;
	
	public IpAndPort(String ip, int port){
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IpAndPort){
			IpAndPort iAP = (IpAndPort) obj;
			return ip == iAP.ip && port == iAP.port;
			
		}
		return false;
	}
}
