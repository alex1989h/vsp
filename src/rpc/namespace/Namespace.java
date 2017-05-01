package rpc.namespace;

public class Namespace {
	private static String name = "";

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		Namespace.name = name;
	}
}
