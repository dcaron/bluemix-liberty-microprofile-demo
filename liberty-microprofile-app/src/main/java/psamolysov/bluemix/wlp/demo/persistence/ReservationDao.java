package psamolysov.bluemix.wlp.demo.persistence;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import psamolysov.bluemix.wlp.demo.model.Reservation;

public interface ReservationDao {
	
	public Reservation get(String id);
	
	public Reservation createNew(Reservation reservation);
	
	public Set<Reservation> getReservations(String venue, LocalDate date, LocalTime startTime, Duration duration);
	
	public void cancel(String id);
}
