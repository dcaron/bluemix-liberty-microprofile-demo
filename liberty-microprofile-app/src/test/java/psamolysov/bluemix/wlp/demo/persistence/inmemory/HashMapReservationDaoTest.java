package psamolysov.bluemix.wlp.demo.persistence.inmemory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import psamolysov.bluemix.wlp.demo.model.Reservation;

public class HashMapReservationDaoTest {

	private static final Duration BASIC_RESERVATION_DURATION = Duration.ofMinutes(30);

	private static final LocalTime BASIC_RESERVATION_START_TIME = LocalTime.of(11, 05);

	private static final LocalDate BASIC_RESERVATION_DATE = LocalDate.of(2017, 11, 29);
	
	private static final String RESERVATION_RESERVED_BY = "psamolysov";
	
	private static final String RESERVATION_VENUE = "Large Meeting Room";
	
	private static final String DIFFERENT_RESERVATION_VENUE = "Small Meeting Room";
	
	private static final LocalTime LEFT_TIME = LocalTime.of(11, 05);

	private static final LocalDate LEFT_DATE = LocalDate.of(2017, 11, 29);
	
	private static final LocalTime RIGHT_TIME = LocalTime.of(12, 05);

	private static final LocalDate RIGHT_DATE = LocalDate.of(2017, 11, 29);

	
	private HashMapReservationDao reservationDao;
	
	@Before
 	public void setUp() {
 		reservationDao = new HashMapReservationDao();
 	}
	
	@Test
	public void testGetReservationById() throws Exception {		
		Reservation registeredReservation = createAndSaveTestReservation();
		String id = registeredReservation.getId();

		Reservation reservation	= reservationDao.get(id);
		
		assertNotNull(reservation);
		assertEquals(id, reservation.getId());
		assertEquals(registeredReservation.getVenue(), reservation.getVenue());
		assertEquals(registeredReservation.getReservedBy(), reservation.getReservedBy());
		assertEquals(registeredReservation.getDate(), reservation.getDate());
		assertEquals(registeredReservation.getStartTime(), reservation.getStartTime());
		assertEquals(registeredReservation.getDuration(), reservation.getDuration());
		
		Reservation nullReservation = reservationDao.get("-1");
		assertNull(nullReservation);		
	}
	
	@Test
	public void testCreateNewReservation() throws Exception {		
		Reservation reservation = createAndSaveTestReservation();
		assertNotNull(reservation);
		assertNotNull(reservation.getId());
		assertEquals(RESERVATION_VENUE, reservation.getVenue());
		assertEquals(RESERVATION_RESERVED_BY, reservation.getReservedBy());
		assertEquals(BASIC_RESERVATION_DATE, reservation.getDate());
		assertEquals(BASIC_RESERVATION_START_TIME, reservation.getStartTime());
		assertEquals(BASIC_RESERVATION_DURATION, reservation.getDuration());
		
		Collection<Reservation> reservations = reservationDao.getAllReservations();
		assertEquals(1, reservations.size());
		assertTrue(reservations.contains(reservation));
	}
	
	@Test
	public void testCreateDuplicateReservation() throws Exception {
		Reservation reservation1 = createAndSaveTestReservation();
		Reservation reservation2 = createAndSaveTestReservation();
		
		assertNotNull(reservation1);
		assertNotNull(reservation1.getId());
		assertNotNull(reservation2);
		assertNotNull(reservation2.getId());
		assertNotSame(reservation1, reservation2);
		assertNotEquals(reservation1.getId(), reservation2.getId());
		assertEquals(RESERVATION_VENUE, reservation1.getVenue());
		assertEquals(RESERVATION_RESERVED_BY, reservation1.getReservedBy());
		assertEquals(BASIC_RESERVATION_DATE, reservation1.getDate());
		assertEquals(BASIC_RESERVATION_START_TIME, reservation1.getStartTime());
		assertEquals(BASIC_RESERVATION_DURATION, reservation1.getDuration());
		assertEquals(RESERVATION_VENUE, reservation2.getVenue());
		assertEquals(RESERVATION_RESERVED_BY, reservation2.getReservedBy());
		assertEquals(BASIC_RESERVATION_DATE, reservation2.getDate());
		assertEquals(BASIC_RESERVATION_START_TIME, reservation2.getStartTime());
		assertEquals(BASIC_RESERVATION_DURATION, reservation2.getDuration());		
		
		Collection<Reservation> reservations = reservationDao.getAllReservations();
		assertEquals(2, reservations.size());
		assertTrue(reservations.contains(reservation1));
		assertTrue(reservations.contains(reservation2));
	}
	
	@Test
	public void testIsInBetweenLeft() throws Exception {
		LocalDateTime leftDateTime = LEFT_DATE.atTime(LEFT_TIME);
		LocalDateTime rightDateTime = RIGHT_DATE.atTime(RIGHT_TIME);
			
		assertFalse("leftDateTime isn't expected in between leftDateTime and rightDateTime", 
				reservationDao.isInBetween(leftDateTime, leftDateTime, rightDateTime));		
	}

	@Test
	public void testIsInBetweenRight() throws Exception {	
		LocalDateTime leftDateTime = LEFT_DATE.atTime(LEFT_TIME);
		LocalDateTime rightDateTime = RIGHT_DATE.atTime(RIGHT_TIME);
			
		assertFalse("rightDateTime isn't expected in between leftDateTime and rightDateTime", 
			reservationDao.isInBetween(rightDateTime, leftDateTime, rightDateTime));
	}
	
	@Test
	public void testIsInBetweenLeftPlusASecond() throws Exception {
		LocalDateTime leftDateTime = LEFT_DATE.atTime(LEFT_TIME);				

		LocalTime testTime = LEFT_TIME.plusSeconds(1);
		LocalDate testDate = LEFT_DATE;
		LocalDateTime testDateTime = testDate.atTime(testTime);
		
		LocalDateTime rightDateTime = RIGHT_DATE.atTime(RIGHT_TIME);
			
		assertTrue("testDateTime is expected in between leftDateTime and rightDateTime", 
			reservationDao.isInBetween(testDateTime, leftDateTime, rightDateTime));		
	}
	
	@Test
	public void testIsInBetweenLeftMinusASecond() throws Exception {		
		LocalDateTime leftDateTime = LEFT_DATE.atTime(LEFT_TIME);
					
		LocalTime testTime = LEFT_TIME.minusSeconds(1);
		LocalDate testDate = LEFT_DATE;
		LocalDateTime testDateTime = testDate.atTime(testTime);
			
		LocalDateTime rightDateTime = RIGHT_DATE.atTime(RIGHT_TIME);
				
		assertFalse("testDateTime isn't expected in between leftDateTime and rightDateTime", 
			reservationDao.isInBetween(testDateTime, leftDateTime, rightDateTime));
	}
	
	@Test
	public void testIsInBetweenRightPlusASecond() throws Exception {
		LocalDateTime leftDateTime = LEFT_DATE.atTime(LEFT_TIME);
		
		LocalTime testTime = RIGHT_TIME.plusSeconds(1);
		LocalDate testDate = RIGHT_DATE;
		LocalDateTime testDateTime = testDate.atTime(testTime);
			
		LocalDateTime rightDateTime = RIGHT_DATE.atTime(RIGHT_TIME);
				
		assertFalse("testDateTime isn't expected in between leftDateTime and rightDateTime", 
			reservationDao.isInBetween(testDateTime, leftDateTime, rightDateTime));
	}
	
	@Test
	public void testIsInBetweenRightMinusASecond() throws Exception {
		LocalDateTime leftDateTime = LEFT_DATE.atTime(LEFT_TIME);
		
		LocalTime testTime = RIGHT_TIME.minusSeconds(1);
		LocalDate testDate = RIGHT_DATE;
		LocalDateTime testDateTime = testDate.atTime(testTime);
			
		LocalDateTime rightDateTime = RIGHT_DATE.atTime(RIGHT_TIME);
				
		assertTrue("testDateTime is expected in between leftDateTime and rightDateTime", 
			reservationDao.isInBetween(testDateTime, leftDateTime, rightDateTime));
	}
	
	@Test
	public void testGetReservationsEmpty() throws Exception {
		Set<Reservation> reservations = reservationDao.getReservations(RESERVATION_VENUE, BASIC_RESERVATION_DATE, 
				BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		assertNotNull(reservations);
		assertEquals(0, reservations.size());
	}
	
	@Test
	public void testGetReservationsTotalMasking() throws Exception {
		Reservation reservation = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		Set<Reservation> responses = reservationDao.getReservations(RESERVATION_VENUE, BASIC_RESERVATION_DATE,
				BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		assertNotNull(responses);
		assertEquals(1, responses.size());
		assertTrue(responses.contains(reservation));
	}
	
	@Test
	public void testGetReservationsTotalMaskingDifferentVenue() throws Exception {
		Reservation reservation = createAndSaveTestReservation(DIFFERENT_RESERVATION_VENUE, BASIC_RESERVATION_START_TIME, 
				BASIC_RESERVATION_DURATION);
		Set<Reservation> responses = reservationDao.getReservations(RESERVATION_VENUE, BASIC_RESERVATION_DATE,
				BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		assertNotNull(responses);
		assertEquals(0, responses.size());
		assertFalse(responses.contains(reservation));
	}
	
	@Test
	public void testGetReservationsTotalMaskingDifferentVenue2() throws Exception {
		Reservation reservation = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		Set<Reservation> responses = reservationDao.getReservations(DIFFERENT_RESERVATION_VENUE, BASIC_RESERVATION_DATE,
				BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		assertNotNull(responses);
		assertEquals(0, responses.size());
		assertFalse(responses.contains(reservation));
	}
	
	@Test
	public void testGetReservationsSubInterval() throws Exception {
		// small subset of the requested time - duration - 1m
		Reservation reservation = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME, Duration.ofMinutes(1));
		
		// full requested time interval
		Set<Reservation> reservations = reservationDao.getReservations(RESERVATION_VENUE, BASIC_RESERVATION_DATE,
				BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		assertNotNull(reservations);
		assertEquals(1, reservations.size());
		assertTrue(reservations.contains(reservation));
	}
	
	@Test
	public void testGetTwoReservationsSubInterval() throws Exception {
		// small subset of the requested time - duration - 1m
		Reservation reservation1 = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME, Duration.ofMinutes(1));
		Reservation reservation2 = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME.plusMinutes(2), 
				Duration.ofMinutes(1));

		// full requested time interval
		Set<Reservation> reservations = reservationDao.getReservations(RESERVATION_VENUE, BASIC_RESERVATION_DATE,
				BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		assertNotNull(reservations);
		assertEquals(2, reservations.size());
		assertTrue(reservations.contains(reservation1));
		assertTrue(reservations.contains(reservation2));
	}
	
	@Test
	public void testGetReservationsShiftRightByDurationPlus1m() throws Exception {		
		Reservation reservation = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		
		// the requested start time is BASIC_RESERVATION_DURATION + 1 min later
		LocalTime requestedTime = BASIC_RESERVATION_START_TIME.plus(BASIC_RESERVATION_DURATION).plusMinutes(1);
		Set<Reservation> reservations = reservationDao.getReservations(RESERVATION_VENUE, BASIC_RESERVATION_DATE,
				requestedTime, BASIC_RESERVATION_DURATION);
		assertNotNull(reservations);
		assertEquals(0, reservations.size());
		assertFalse(reservations.contains(reservation));
	}
	
	@Test
	public void testGetReservationsShiftRightByDuration() throws Exception {
		Reservation reservation = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		
		// the requested start time is BASIC_RESERVATION_DURATION later
		LocalTime requestedTime = BASIC_RESERVATION_START_TIME.plus(BASIC_RESERVATION_DURATION);
		Set<Reservation> reservations = reservationDao.getReservations(RESERVATION_VENUE, BASIC_RESERVATION_DATE,
				requestedTime, BASIC_RESERVATION_DURATION);
		assertNotNull(reservations);
		assertEquals(0, reservations.size());
		assertFalse(reservations.contains(reservation));
	}

	
	@Test
	public void testGetReservationsShiftLeftByDuration() throws Exception {
		Reservation reservation = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		
		// the requested start time is BASIC_RESERVATION_DURATION early
		LocalTime requestedTime = BASIC_RESERVATION_START_TIME.minus(BASIC_RESERVATION_DURATION);
		Set<Reservation> reservations = reservationDao.getReservations(RESERVATION_VENUE, BASIC_RESERVATION_DATE,
				requestedTime, BASIC_RESERVATION_DURATION);
		assertNotNull(reservations);
		assertEquals(0, reservations.size());
		assertFalse(reservations.contains(reservation));
	}
	
	@Test
	public void testGetReservationsShiftLeftByDoubleDuration() throws Exception {
		Reservation reservation = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		
		// the requested start time is 2 x BASIC_RESERVATION_DURATION early
		LocalTime requestedTime = BASIC_RESERVATION_START_TIME.minus(BASIC_RESERVATION_DURATION.multipliedBy(2));
		Set<Reservation> reservations = reservationDao.getReservations(RESERVATION_VENUE, BASIC_RESERVATION_DATE,
				requestedTime, BASIC_RESERVATION_DURATION);
		assertNotNull(reservations);
		assertEquals(0, reservations.size());
		assertFalse(reservations.contains(reservation));
	}
	
	@Test
	public void testGetReservationsShiftLeftByDurationAndRightByDuration() throws Exception {
		Reservation reservation = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		
		// the requested start time is BASIC_RESERVATION_DURATION early
		LocalTime requestedTime = BASIC_RESERVATION_START_TIME.minus(BASIC_RESERVATION_DURATION);
		// the requested duration is doubled BASIC_RESERVATION_DURATION
		Duration requestedDuration = BASIC_RESERVATION_DURATION.multipliedBy(2);
		Set<Reservation> reservations = reservationDao.getReservations(RESERVATION_VENUE, BASIC_RESERVATION_DATE,
				requestedTime, requestedDuration);
		assertNotNull(reservations);
		assertEquals(1, reservations.size());
		assertTrue(reservations.contains(reservation));
	}
	
	@Test
	public void testGetReservationsShiftLeftByDurationAndRightByDoubleDuration() throws Exception {
		Reservation reservation = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		
		// the requested start time is BASIC_RESERVATION_DURATION early
		LocalTime requestedTime = BASIC_RESERVATION_START_TIME.minus(BASIC_RESERVATION_DURATION);
		// the requested duration is tripled BASIC_RESERVATION_DURATION
		Duration requestedDuration = BASIC_RESERVATION_DURATION.multipliedBy(3);
		Set<Reservation> reservations = reservationDao.getReservations(RESERVATION_VENUE, BASIC_RESERVATION_DATE,
				requestedTime, requestedDuration);
		assertNotNull(reservations);
		assertEquals(1, reservations.size());
		assertTrue(reservations.contains(reservation));
	}
	
	@Test
	public void testGetTwoReservationsShiftLeftByDurationAndRightByDoubleDuration() throws Exception {
		Reservation reservation1 = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME, BASIC_RESERVATION_DURATION);
		Reservation reservation2 = createAndSaveTestReservation(BASIC_RESERVATION_START_TIME.plus(BASIC_RESERVATION_DURATION), 
				BASIC_RESERVATION_DURATION);
		
		// the requested start time is BASIC_RESERVATION_DURATION early
		LocalTime requestedTime = BASIC_RESERVATION_START_TIME.minus(BASIC_RESERVATION_DURATION);
		// the requested duration is tripled BASIC_RESERVATION_DURATION
		Duration requestedDuration = BASIC_RESERVATION_DURATION.multipliedBy(3);
		Set<Reservation> reservations = reservationDao.getReservations(RESERVATION_VENUE, BASIC_RESERVATION_DATE,
				requestedTime, requestedDuration);
		assertNotNull(reservations);
		assertEquals(2, reservations.size());
		assertTrue(reservations.contains(reservation1));
		assertTrue(reservations.contains(reservation2));
	}
	
	private Reservation createAndSaveTestReservation(LocalTime startTime, Duration duration) {
		Reservation tmpReservation = new Reservation(RESERVATION_VENUE,
				RESERVATION_RESERVED_BY,
				BASIC_RESERVATION_DATE,
				startTime,
				duration);
		return reservationDao.createNew(tmpReservation);
	}
	
	private Reservation createAndSaveTestReservation(String venue, LocalTime startTime, Duration duration) {
		Reservation tmpReservation = new Reservation(venue,
				RESERVATION_RESERVED_BY,
				BASIC_RESERVATION_DATE,
				startTime,
				duration);
		return reservationDao.createNew(tmpReservation);
	}
	
	private Reservation createAndSaveTestReservation() {
		Reservation tmpReservation = new Reservation(RESERVATION_VENUE,
				RESERVATION_RESERVED_BY,
				BASIC_RESERVATION_DATE,
				BASIC_RESERVATION_START_TIME,
				BASIC_RESERVATION_DURATION);
		return reservationDao.createNew(tmpReservation);
	}
}
