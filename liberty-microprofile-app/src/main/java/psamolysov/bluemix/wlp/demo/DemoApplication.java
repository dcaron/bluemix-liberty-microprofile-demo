package psamolysov.bluemix.wlp.demo;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/billing")
public class DemoApplication extends Application {
	
	@PostConstruct
	public void init() {
		System.out.println("---- Init Demo Application");
	}
}
