package impl.client;

import impl.factories.FiFoFactory;
import impl.interfaces.IVerticalMovements;

public class VerticalMovements implements IVerticalMovements {
	private FiFo fifo = null;
	
	public VerticalMovements() {
		fifo = FiFoFactory.getFiFo("transmitterVertical");
	}
	
	@Override
	public int moveVerticalToPercent(int transactionID,int percent) {
		System.out.println("Call to move vertical -  TID: " + transactionID + " degree " + percent);
		String str = "<method>"
				+ "<name>moveVerticalToPercent</name>"
        		+ "<returnType>int</returnType>"
        		+ "<parameterCount>2</parameterCount>"
        		+ "<param>"
        		+ "<type>int</type>"
        		+ "<value>"+transactionID+"</value>"
        		+ "</param>"
        		+ "<param>"
        		+ "<type>int</type>"
        		+ "<value>"+percent+"</value>"
        		+ "</param>"
        		+ "</method>";
		fifo.enqueue(str.getBytes());
		return 0;
	}

}
