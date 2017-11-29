package psamolysov.bluemix.wlp.demo.service.presentation;

import static psamolysov.bluemix.wlp.demo.util.JsonUtil.format;
import static psamolysov.bluemix.wlp.demo.util.JsonUtil.getString;

import java.io.InputStream;
import java.io.OutputStream;
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
        	String venueId = getString("venueId", registrationJson);
        	// TODO add an implementation for date, startTime and duration
            return new Reservation(id, venue, venueId, null, null, null);            
    	}
	}

	@Override
	public void toJson(OutputStream os, Reservation reservation) {
		Objects.requireNonNull(reservation, RESERVATION_ISNT_ALLOWED_TO_BE_NULL);
		JsonWriter jsonWriter = Json.createWriter(os);
		JsonObjectBuilder builder = Json.createObjectBuilder();
        if (reservation.getId() != null)
            builder = builder.add("id", reservation.getId());
        if (reservation.getVenue() != null)
        	builder = builder.add("venue", reservation.getVenue());
        if (reservation.getVenueId() != null)
        	builder = builder.add("venueId", reservation.getVenueId());
        if (reservation.getDate() != null)
        	builder = builder.add("date", format(reservation.getDate()));
        if (reservation.getStartTime() != null)
        	builder = builder.add("startAt", format(reservation.getStartTime()));
        if (reservation.getDuration() != null)
            builder = builder.add("duration", format(reservation.getDuration()));
        jsonWriter.writeObject(builder.build());
        jsonWriter.close();
	}
}
