package impl.interfaces;

public interface IStatusResponses{
	public int getHorizontalInPercent(int transactionID, long percent);
	public int getVerticalInPercent(int transactionID, long percent);
	public int getGripperStatus(int transactionID, String gripperStatus);

}
