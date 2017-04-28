package impl.client;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.cads.ev3.gui.swing.CaDSRobotGUISwing;

import impl.models.ModelStatusResponses;
import impl.server.Receiver;
import impl.skeletons.SkeletonStatusResponses;
import impl.stubs.StubStatusRequests;

public class StatusController extends Thread {
	StubStatusRequests status = null;
	ClientKontroller controller = null;
	
	public StatusController(String ip, int port, ClientKontroller controller) throws Exception {
		this.controller = controller;
		new Sender(ip, port, "statusRequest").start();
		status = new StubStatusRequests();
		Receiver receiver = new Receiver(ip, port, "statusResponse", "Client");
		ModelStatusResponses statusResponses = new ModelStatusResponses(controller.getGui());
		SkeletonStatusResponses thread = new SkeletonStatusResponses(statusResponses, "Client", receiver);
		receiver.start();
		thread.start();
	}
	
	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				sleep(500);
				status.getGripperStatus(controller.getTransactionsID());
				status.getVerticalInPercent(controller.getTransactionsID());
				status.getHorizontalInPercent(controller.getTransactionsID());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
