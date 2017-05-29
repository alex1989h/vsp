package impl.factories;

import java.util.HashMap;

public class FiFoFactory {
	private static HashMap<String, FiFo> hashMap = new HashMap<String, FiFo>();

	private FiFoFactory(){
		
	}
	
	public synchronized static FiFo getFiFo(String key){
		FiFo fifo = hashMap.get(key);
		if (fifo == null) {
			fifo = new FiFo();
			hashMap.put(key, fifo);
		}
		return fifo;
	}
}
