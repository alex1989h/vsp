package impl.server;

import rmi.interfaces.ISkeleton;

public class ControllerThread extends Thread {
	private ISkeleton skeleton;

	public ControllerThread(ISkeleton skeleton) {
		this.skeleton = skeleton;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			skeleton.method();
		}
	}
}
