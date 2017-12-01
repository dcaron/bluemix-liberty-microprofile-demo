package psamolysov.bluemix.wlp.demo.service.presentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import psamolysov.bluemix.wlp.demo.model.Reservation;

public class TestReservationJsonAdapter {

	private static final Duration RESERVATION_DURATION = Duration.ofMinutes(30);

	private static final LocalTime RESERVATION_START_TIME = LocalTime.of(11, 05);

	private static final LocalDate RESERVATION_DATE = LocalDate.of(2017, 11, 29);

	private static final String RESERVATION_VENUE_ID = "101-a";

	private static final String RESERVATION_VENUE = "Large Meeting Room";
	
	private static final String RESERVATION_ID = "100";

	private static final String JSON_TEMPLATE = "\"id\":\"{0}\",\"venue\":\"{1}\",\"venueId\":\"{2}\","
			+ "\"date\":\"{3}\",\"startAt\":\"{4}\",\"duration\":{5}";
	
	private static final String JSON_WO_ID_TEMPLATE = "\"venue\":\"{0}\",\"venueId\":\"{1}\","
			+ "\"date\":\"{2}\",\"startAt\":\"{3}\",\"duration\":{4}";

	private static final String JSON_WO_ID_AND_DATES_TEMPLATE = "\"venue\":\"{0}\",\"venueId\":\"{1}\"";

	private static final String JSON_WO_DATES_TEMPLATE = "\"id\":\"{0}\",\"venue\":\"{1}\",\"venueId\":\"{2}\"";
	
	private static ReservationJsonAdapter adapter = new ReservationJsonAdapter();
	
	private ByteArrayOutputStream os;
	
	@Before
	public void setUp() {
		os = new ByteArrayOutputStream(512);
	}
	
	@Test(expected = NullPointerException.class)
	public void testToJsonNull() throws Exception {
		adapter.toJson(os, null);
	}
	
	@Test
	public void testToJsonEmpty() throws Exception {
		Reservation reservation = new Reservation(null, null, null, null, null);
		adapter.toJson(os, reservation);
		String result = os.toString();		
		assertEquals("{}", result);
	}
	
	@Test
	public void testToJsonEmptyId() throws Exception {
		Reservation reservation = new Reservation(RESERVATION_VENUE, 
				RESERVATION_VENUE_ID, 
				RESERVATION_DATE, 
				RESERVATION_START_TIME, 
				RESERVATION_DURATION);
		adapter.toJson(os, reservation);
		String result = os.toString();
		String expected = toJson(JSON_WO_ID_TEMPLATE,
			reservation.getVenue(), 
			reservation.getVenueId(), 
			reservation.getDate(), 
			reservation.getStartTime(),
			reservation.getDuration().toMinutes());
		assertEquals(expected, result);
	}
	
	@Test
	public void testToJsonWithId() throws Exception {
		Reservation reservation = new Reservation(RESERVATION_ID,
				RESERVATION_VENUE, 
				RESERVATION_VENUE_ID, 
				RESERVATION_DATE, 
				RESERVATION_START_TIME, 
				RESERVATION_DURATION);
		adapter.toJson(os, reservation);
		String result = os.toString();
		String expected = toJson(JSON_TEMPLATE,
			reservation.getId(),
			reservation.getVenue(), 
			reservation.getVenueId(), 
			reservation.getDate(), 
			reservation.getStartTime(),
			reservation.getDuration().toMinutes());
		assertEquals(expected, result);
	}
	
	@Test
	public void testFromJsonWithoutDatesAndId() throws Exception {
		InputStream jsonIs = toJsonInputStream(JSON_WO_ID_AND_DATES_TEMPLATE, 
				RESERVATION_VENUE, 
				RESERVATION_VENUE_ID);		
		Reservation reservation = adapter.fromJson(jsonIs);
		assertNull(reservation.getId());
		assertEquals(RESERVATION_VENUE, reservation.getVenue());
		assertEquals(RESERVATION_VENUE_ID, reservation.getVenueId());
	}

	@Test
	public void testFromJsonWithoutDates() throws Exception {
		InputStream jsonIs = toJsonInputStream(JSON_WO_DATES_TEMPLATE,
				RESERVATION_ID,
				RESERVATION_VENUE, 
				RESERVATION_VENUE_ID);		
		Reservation reservation = adapter.fromJson(jsonIs);
		assertEquals(RESERVATION_ID, reservation.getId());
		assertEquals(RESERVATION_VENUE, reservation.getVenue());
		assertEquals(RESERVATION_VENUE_ID, reservation.getVenueId());
	}
	
	@Test
	public void testFromJsonWithDates() throws Exception {
		InputStream jsonIs = toJsonInputStream(JSON_WO_ID_TEMPLATE,
				RESERVATION_VENUE, 
				RESERVATION_VENUE_ID,
				RESERVATION_DATE,
				RESERVATION_START_TIME,
				RESERVATION_DURATION.toMinutes());
		Reservation reservation = adapter.fromJson(jsonIs);
		assertEquals(RESERVATION_VENUE, reservation.getVenue());
		assertEquals(RESERVATION_VENUE_ID, reservation.getVenueId());
		assertEquals(RESERVATION_DATE, reservation.getDate());
		assertEquals(RESERVATION_START_TIME, reservation.getStartTime());
		assertEquals(RESERVATION_DURATION, reservation.getDuration());
	}
	
	private String toJson(String template, Object... params) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append(MessageFormat.format(template, params));
		builder.append("}");
		return builder.toString();
	}
	
	private InputStream toJsonInputStream(String template, Object... params) throws UnsupportedEncodingException {
		return new ByteArrayInputStream(toJson(template, params).getBytes(StandardCharsets.UTF_8.name()));		
	}
}
