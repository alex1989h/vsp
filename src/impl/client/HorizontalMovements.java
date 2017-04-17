package impl.client;

import impl.factories.FiFoFactory;
import impl.interfaces.IHorizontalMovements;

public class HorizontalMovements implements IHorizontalMovements {
	private FiFo fifo = null;

	public HorizontalMovements() {
		fifo = FiFoFactory.getFiFo("transmitterHorizontal");
	}

	@Override
	public int moveHorizontalToPercent(int transactionID, int percent) {
		System.out.println("Call to move horizontal -  TID: " + transactionID + " degree " + percent);
		String str = "<method>"
				+ "<name>moveHorizontalToPercent</name>"
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
