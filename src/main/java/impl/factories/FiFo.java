package impl.factories;

import java.util.LinkedList;
import java.util.Queue;

public class FiFo{
	private Queue<byte[]> queue;
	public FiFo(){
		queue = new LinkedList<byte[]>();
	}
	
	public synchronized byte[] dequeue(){
		while(queue.isEmpty())
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		notifyAll();
		return queue.poll();
	}
	
	public synchronized boolean enqueue(byte[] b){
		notifyAll();
		return queue.add(b);
	}
}
