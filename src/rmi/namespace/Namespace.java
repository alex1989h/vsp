package rmi.namespace;

import java.net.SocketException;
import java.net.UnknownHostException;

import rmi.communication.Sender;
import rmi.xml.MyXML;
import rmi.xml.MyXMLObject;

public class Namespace {
	private static String name = "";

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		Namespace.name = name;
	}
	
	public static String[] lookup() {
		try {
			Sender sender = new Sender();
			String send = "<getService><returnType>String</returnType></getService>";
			String[] ret;
			byte[] recei = sender.send(send.getBytes());
			if (recei != null) {
				MyXMLObject xml = MyXML.createXML(recei);
				ret = new String[xml.getParamValues().length];
				for (int i = 0; i < xml.getParamValues().length; i++) {
					ret[i] = (String) xml.getParamValues()[i];
				}
				return ret;
			}
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
		return null;
	}
}
