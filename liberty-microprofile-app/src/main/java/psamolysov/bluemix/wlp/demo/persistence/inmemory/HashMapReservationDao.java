package psamolysov.bluemix.wlp.demo.persistence.inmemory;

import static java.util.stream.Collectors.toSet;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;

import psamolysov.bluemix.wlp.demo.model.Reservation;
import psamolysov.bluemix.wlp.demo.persistence.ReservationDao;

@InMemory
@ApplicationScoped
public class HashMapReservationDao implements ReservationDao {

	private ConcurrentMap<String, Reservation> reservations = new ConcurrentHashMap<>();
	
	private AtomicLong idcounter = new AtomicLong(0L);
	
	@Override
	public Reservation get(String id) {
		return reservations.get(id);	
	}

	@Override
	public Reservation createNew(Reservation reservation) {		
		String id = nextId();
		Reservation newReservation = new Reservation(id,
				reservation.getVenue(),
				reservation.getReservedBy(),
				reservation.getDate(),
				reservation.getStartTime(),
				reservation.getDuration());
		reservations.put(id, newReservation);
		return newReservation;
	}	
	
	@Override
	public Set<Reservation> getReservations(String venue, LocalDate date, LocalTime startTime, Duration duration) {
		// Let's start from an implementation of the simplest linear searh algorithm
		LocalDateTime startDateTime = date.atTime(startTime);
		LocalDateTime endDateTime = startDateTime.plus(duration);
		return getVenueReservations(venue).filter(conflictReservation -> {
			LocalDateTime conflictStartDateTime = conflictReservation.getDate().atTime(conflictReservation.getStartTime());
			LocalDateTime conflictEndDateTime = conflictStartDateTime.plus(conflictReservation.getDuration());
			return startDateTime.isEqual(conflictStartDateTime)
					|| isInBetween(startDateTime, conflictStartDateTime, conflictEndDateTime) 
					|| isInBetween(endDateTime, conflictStartDateTime, conflictEndDateTime)
					|| isInBetween(conflictStartDateTime, startDateTime, endDateTime)
					|| isInBetween(conflictEndDateTime, startDateTime, endDateTime);
		}).collect(toSet());
	}

	@Override
	public void cancel(String id) {
		reservations.remove(id);
	}
	
	Stream<Reservation> getVenueReservations(String venue) {
		if (venue == null)
			return new ArrayList<Reservation>().stream();
		return getAllReservations().stream().filter(res -> venue.equals(res.getVenue()));
	}
	
	Collection<Reservation> getAllReservations() {
		return reservations.values();
	}
	
	/**
	 * InBetween is calculated by the following rules:
	 * <ul>
	 * <li>the <code>test</code> datetime is exclusively after
	 * 		the <code>start</code> one	 
	 * <li>and the <code>test</code> datetime is exclusively before
	 *      the <code>end</code> one
	 * <li>and the <code>start</code> is before <code>end</code>.
	 * </ul>
	 * @param test
	 * @param start
	 * @param end
	 * @return
	 */
	boolean isInBetween(LocalDateTime test, LocalDateTime start, LocalDateTime end) {
		return test.isAfter(start) && test.isBefore(end) && start.isBefore(end);
	}
	
	private String nextId() {
		return Long.toString(idcounter.incrementAndGet());
	}
}
