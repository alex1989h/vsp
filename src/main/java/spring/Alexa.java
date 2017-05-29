package spring;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import impl.factories.StubFactory;
import rmi.interfaces.IGripperActions;
import rmi.interfaces.IHorizontalMovements;
import rmi.interfaces.IVerticalMovements;
import rmi.namespace.Namespace;

@RestController
public class Alexa {
	private static IVerticalMovements vertical = StubFactory.getVerticalMovements();
	private static IHorizontalMovements horizontal =  StubFactory.getHorizontalMovements();
	private static IGripperActions gripper = StubFactory.getGripperActions();
	private static String[] nameSpaces;
	private static int index = 0;
	
	@RequestMapping("/vertical")
	public int moveVerticalToPercent(@RequestParam("percent") int percent){
		System.out.println("Move vertical to "+percent+"%");
		return vertical.moveVerticalToPercent(percent);
	}
	@RequestMapping("/horizontal")
	public int moveHorizontalToPercent(@RequestParam("percent") int percent){
		System.out.println("Move horizontal to "+percent+"%");
		return horizontal.moveHorizontalToPercent(percent);
	}
	@RequestMapping("/close")
	public int closeGripper(){
		System.out.println("Close Gripper");
		return gripper.closeGripper();
	}
	@RequestMapping("/open")
	public int openGripper(){
		System.out.println("Open Gripper");
		return gripper.openGripper();
	}
	
	@RequestMapping("/lookup")
	public String[] lookup(){
		nameSpaces = Namespace.lookup();
		System.out.println("Lookup");
		String[] temp = new String[nameSpaces.length];
		for (int i = 0; i < nameSpaces.length; i++) {
			if(nameSpaces[i].equals(Namespace.getName())){
				temp[i] = "*"+nameSpaces[i];
			}else{
				temp[i] = nameSpaces[i];
			}
		}
		return temp;
	}
	
	@RequestMapping("/switch")
	public String[] switchRob(){
		index++;
		System.out.println("Lookup");
		if(index>=nameSpaces.length){
			index = 0;
		}
 		Namespace.setName(nameSpaces[index]);
		return lookup();
	}
}
