package psamolysov.bluemix.wlp.demo.persistence.inmemory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

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
		// TODO Auto-generated method stub
		return new HashSet<>();
	}

	@Override
	public void cancel(String id) {
		reservations.remove(id);
	}
	
	Collection<Reservation> getAllReservations() {
		return reservations.values();
	}
	
	private String nextId() {
		return Long.toString(idcounter.incrementAndGet());
	}
}
