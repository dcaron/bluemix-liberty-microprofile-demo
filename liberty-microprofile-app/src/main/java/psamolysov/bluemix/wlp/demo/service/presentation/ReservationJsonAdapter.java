package psamolysov.bluemix.wlp.demo.service.presentation;

import static psamolysov.bluemix.wlp.demo.util.JsonUtil.format;
import static psamolysov.bluemix.wlp.demo.util.JsonUtil.getDate;
import static psamolysov.bluemix.wlp.demo.util.JsonUtil.getDuration;
import static psamolysov.bluemix.wlp.demo.util.JsonUtil.getString;
import static psamolysov.bluemix.wlp.demo.util.JsonUtil.getTime;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import psamolysov.bluemix.wlp.demo.model.Reservation;

@ApplicationScoped
public class ReservationJsonAdapter implements JsonAdapter<Reservation> {

	private static final String RESERVATION_ISNT_ALLOWED_TO_BE_NULL = 
			"Reservation isn't allowed to be null";
	
	@Override
	public Reservation fromJson(InputStream is) {
		try (JsonReader rdr = Json.createReader(is)) {
            JsonObject registrationJson = rdr.readObject();
            String id = getString("id", registrationJson);
        	String venue = getString("venue", registrationJson);
        	String reservedBy = getString("reservedBy", registrationJson);
        	LocalDate date = getDate("date", registrationJson);
        	LocalTime time = getTime("startAt", registrationJson);
        	Duration duration = getDuration("duration", registrationJson);
        	// TODO add BeanValidation, so bean can't be deserialized wo date, time, and duration
        	// TODO add reaction to Non number (Duration) and AriphmeticException (Duration fractional)
            return new Reservation(id, venue, reservedBy, date, time, duration);
    	}
	}

	@Override
	public JsonObject toJson(Reservation reservation) {
		Objects.requireNonNull(reservation, RESERVATION_ISNT_ALLOWED_TO_BE_NULL);
		JsonObjectBuilder builder = Json.createObjectBuilder();
        if (reservation.getId() != null)
            builder = builder.add("id", reservation.getId());
        if (reservation.getVenue() != null)
        	builder = builder.add("venue", reservation.getVenue());
        if (reservation.getReservedBy() != null)
        	builder = builder.add("reservedBy", reservation.getReservedBy());
        if (reservation.getDate() != null)        	
        	builder = builder.add("date", format(reservation.getDate()));
        if (reservation.getStartTime() != null)
        	builder = builder.add("startAt", format(reservation.getStartTime()));
        if (reservation.getDuration() != null)
            builder = builder.add("duration", format(reservation.getDuration()));
        return builder.build();
	}
	
	@Override
	public void toJson(OutputStream os, Reservation reservation) {
		Objects.requireNonNull(reservation, RESERVATION_ISNT_ALLOWED_TO_BE_NULL);
		JsonWriter jsonWriter = Json.createWriter(os);
        jsonWriter.writeObject(toJson(reservation));
        jsonWriter.close();
	}
}
