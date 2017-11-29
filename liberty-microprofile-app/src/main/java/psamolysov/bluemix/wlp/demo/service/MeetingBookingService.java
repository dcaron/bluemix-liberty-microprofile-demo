package psamolysov.bluemix.wlp.demo.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import psamolysov.bluemix.wlp.demo.model.Reservation;

@Path("/meeting")
@ApplicationScoped
public class MeetingBookingService {

	@GET
	@Path("/")
	@Produces(TEXT_HTML)
	public String info() {
		return "Microservice Meeting Room Booking API Application";
	}
	
	@GET
	@Path("/{id}")
	@Produces(APPLICATION_JSON)
	public Reservation get(@PathParam("id") String id) {		
		return new Reservation("Room 200", "100", LocalDate.now(), LocalTime.now(), Duration.of(2, ChronoUnit.HOURS));
	}
}
