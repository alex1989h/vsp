package spring;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/test")
    public Test greeting(@RequestParam(value="name", defaultValue="Nastja") String name) {
		System.out.println("Hallo "+name);
        return new Test(counter.incrementAndGet(),String.format(template, name),"wie gehts");
    }
	
	@RequestMapping("/")
    public String test() {
		System.out.println("Main Page");
        return "<b>Hallo auf der Hauptseite</b>";
    }
}