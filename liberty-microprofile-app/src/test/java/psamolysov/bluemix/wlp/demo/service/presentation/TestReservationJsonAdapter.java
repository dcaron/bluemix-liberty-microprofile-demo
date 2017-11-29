package psamolysov.bluemix.wlp.demo.service.presentation;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import psamolysov.bluemix.wlp.demo.model.Reservation;

public class TestReservationJsonAdapter {

	private static final String JSON_WO_ID_TEMPLATE = "\"venue\":\"{0}\",\"venueId\":\"{1}\","
			+ "\"date\":\"{2}\",\"startAt\":\"{3}\",\"duration\":{4}";
	
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
		Reservation reservation = new Reservation("Demo Venue", "123", LocalDate.of(2017, 11, 29), 
				LocalTime.of(11, 05), Duration.ofMinutes(30));
		adapter.toJson(os, reservation);
		String result = os.toString();
		String expected = MessageFormat.format(JSON_WO_ID_TEMPLATE, 
			reservation.getVenue(), 
			reservation.getVenueId(), 
			reservation.getDate(), 
			reservation.getStartTime(),
			reservation.getDuration().toMinutes());
		assertEquals("{" + expected + "}", result);
	}
}
