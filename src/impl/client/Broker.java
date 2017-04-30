package impl.client;

import java.net.InetAddress;

public class Broker {
	private static int port;
	private static InetAddress address;
	public static int getPort() {
		return port;
	}
	public static void setPort(int port) {
		Broker.port = port;
	}
	public static InetAddress getAddress() {
		return address;
	}
	public static void setAddress(InetAddress address) {
		Broker.address = address;
	}
}
