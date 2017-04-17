package impl.client;

import impl.interfaces.IGripperActions;

public class GripperActions implements IGripperActions {
	private FiFo fifo = null;
	
	public GripperActions() {
		fifo = FiFoFactory.createInstance("sender");
	}
	
	@Override
	public int openGripper(int transactionID) {
		System.out.println("open.... TID: " + transactionID);
		String str = "<method>"
				+ "<name>openGripper</name>"
        		+ "<returnType>int</returnType>"
        		+ "<parameterCount>1</parameterCount>"
        		+ "<param>"
        		+ "<type>int</type>"
        		+ "<value>"+transactionID+"</value>"
        		+ "</param>"
        		+ "</method>";
		fifo.enqueue(str.getBytes());
		return 0;
	}

	@Override
	public int closeGripper(int transactionID) {
		System.out.println("Close.... TID: " + transactionID);
		String str = "<method>"
				+ "<name>closeGripper</name>"
        		+ "<returnType>int</returnType>"
        		+ "<parameterCount>1</parameterCount>"
        		+ "<param>"
        		+ "<type>int</type>"
        		+ "<value>"+transactionID+"</value>"
        		+ "</param>"
        		+ "</method>";
		fifo.enqueue(str.getBytes());
		return 0;
	}

}
