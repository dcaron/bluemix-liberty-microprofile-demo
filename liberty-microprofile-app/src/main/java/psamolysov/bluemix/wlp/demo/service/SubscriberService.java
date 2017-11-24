package psamolysov.bluemix.wlp.demo.service;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/subscriber")
@ApplicationScoped
public class SubscriberService {

	@GET
	@Path("/")
	@Produces(TEXT_HTML)
	public String info() {
		return "Microservice Billing API Application - Subscriber Service";
	}
}
