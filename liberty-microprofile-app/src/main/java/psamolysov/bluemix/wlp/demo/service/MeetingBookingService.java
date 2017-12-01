package psamolysov.bluemix.wlp.demo.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

import java.text.MessageFormat;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import psamolysov.bluemix.wlp.demo.model.Reservation;
import psamolysov.bluemix.wlp.demo.persistence.ReservationDao;
import psamolysov.bluemix.wlp.demo.persistence.inmemory.InMemory;

@Path("/")
@ApplicationScoped
public class MeetingBookingService {
	
	private static final String BANNER = "Microservice Meeting Room Booking API Application";
	
	private static final String ERROR_RESERVATION_CONFLICT_FMT = 
			"The venue \"{0}\" is already booked";
	
	private static final String ERROR_RESERVATION_IS_EXISTS_FMT = 
			"The reservation #\"{0}\" is already exists";
	
	@Inject @InMemory
	private ReservationDao reservationDao;
	
	@GET
	@Path("/")
	@Produces(TEXT_HTML)
	public String info() {
		return BANNER;
	}
	
	@GET
	@Path("/meeting/{id}")
	@Produces(APPLICATION_JSON)
	public Reservation get(@PathParam("id") String id) {
		Reservation reservation = reservationDao.get(id);
		if (reservation == null)
			throw new NotFoundException();
		return reservation;
	}
	
	@POST
	@Path("/meeting")
	@Produces(APPLICATION_JSON)
	@Consumes(APPLICATION_JSON)
	public Reservation make(Reservation reservation) {
		if (reservation.getId() != null)
			throw new BadRequestException(MessageFormat.format(ERROR_RESERVATION_IS_EXISTS_FMT, reservation.getId()));
		Set<Reservation> alreadyMadeReservations = reservationDao.getReservations(
				reservation.getVenue(), 
				reservation.getDate(), 
				reservation.getStartTime(),
				reservation.getDuration());
		if (alreadyMadeReservations != null && !alreadyMadeReservations.isEmpty())
			throw new BadRequestException(MessageFormat.format(ERROR_RESERVATION_CONFLICT_FMT, reservation.getVenue()));
		
		return reservationDao.createNew(reservation);
	}
}
