package impl.client;

import java.util.Queue;

public class FiFo<T>{
	private Queue<T> queue;
	
	public FiFo(Queue<T> _queue){
		queue = _queue;
	}
	
	synchronized T dequeue(){
		return queue.poll();
	}
	
	synchronized boolean enqueue(T t){
		return queue.add(t);
	}
}
