package spring;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="Max") String name) {
		System.out.println("Hallo "+name);
        return new Greeting(counter.incrementAndGet(),String.format(template, name));
    }
	
	 @RequestMapping("/greeting2")
    public Greeting greeting2(@RequestParam(value="name", defaultValue="Alex") String name) {
		System.out.println("Hallo "+name);
		return new Greeting(counter.incrementAndGet(),String.format(template, name));
    }
}
