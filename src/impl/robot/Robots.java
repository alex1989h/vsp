package impl.robot;

import java.util.HashMap;

public class Robots {
	private static Robots instance = null;
	private HashMap<String,Robot> hashMap = null;
	private Robot currentRobot = null;
	private Robots() {
		hashMap = new HashMap<String,Robot>();
	}

	public static synchronized Robots getInstance() {
		if (instance == null) {
			instance = new Robots();
		}
		return instance;
	}
	
	public Robot getCurrentRobot(){
		return currentRobot;
	}
	
	public void changeCurrentRobot(String key){
		currentRobot = hashMap.get(key);
	}
	
	public void addRobot(String key, Robot robot){
		hashMap.put(key, robot);
	}
}
