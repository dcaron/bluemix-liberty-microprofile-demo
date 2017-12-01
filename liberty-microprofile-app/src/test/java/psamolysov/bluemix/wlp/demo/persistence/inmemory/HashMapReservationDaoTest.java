package psamolysov.bluemix.wlp.demo.persistence.inmemory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import psamolysov.bluemix.wlp.demo.model.Reservation;

public class HashMapReservationDaoTest {

	private static final Duration BASIC_RESERVATION_DURATION = Duration.ofMinutes(30);

	private static final LocalTime BASIC_RESERVATION_START_TIME = LocalTime.of(11, 05);

	private static final LocalDate BASIC_RESERVATION_DATE = LocalDate.of(2017, 11, 29);
	
	private static final String RESERVATION_RESERVED_BY = "psamolysov";
	
	private static final String RESERVATION_VENUE = "Large Meeting Room";
	
	private HashMapReservationDao reservationDao;
	
	@Before
 	public void setUp() {
 		reservationDao = new HashMapReservationDao();
 	}
	
	@Test
	public void testGetReservationById() throws Exception {
		Reservation tempReservation = new Reservation(RESERVATION_VENUE,
				RESERVATION_RESERVED_BY,
				BASIC_RESERVATION_DATE, 
				BASIC_RESERVATION_START_TIME,
				BASIC_RESERVATION_DURATION);
		Reservation registeredReservation = reservationDao.createNew(tempReservation);
		String id = registeredReservation.getId();

		Reservation reservation	= reservationDao.get(id);
		
		assertNotNull(reservation);
		assertEquals(id, reservation.getId());
		assertEquals(tempReservation.getVenue(), reservation.getVenue());
		assertEquals(tempReservation.getDate(), reservation.getDate());
		assertEquals(tempReservation.getStartTime(), reservation.getStartTime());
		assertEquals(tempReservation.getDuration(), reservation.getDuration());
		
		Reservation nullReservation = reservationDao.get("-1");
		assertNull(nullReservation);		
	}
	
	@Test
	public void testCreateNewReservation() throws Exception {
		Reservation tempReservation = new Reservation(RESERVATION_VENUE,
				RESERVATION_RESERVED_BY,
				BASIC_RESERVATION_DATE, 
				BASIC_RESERVATION_START_TIME,
				BASIC_RESERVATION_DURATION);
		Reservation reservation = reservationDao.createNew(tempReservation);
		
		assertNotNull(reservation);
		assertNotNull(reservation.getId());
		assertEquals(tempReservation.getVenue(), reservation.getVenue());
		assertEquals(tempReservation.getDate(), reservation.getDate());
		assertEquals(tempReservation.getStartTime(), reservation.getStartTime());
		assertEquals(tempReservation.getDuration(), reservation.getDuration());
		
		Collection<Reservation> reservations = reservationDao.getAllReservations();
		assertEquals(1, reservations.size());
		assertTrue(reservations.contains(reservation));
	}
}
