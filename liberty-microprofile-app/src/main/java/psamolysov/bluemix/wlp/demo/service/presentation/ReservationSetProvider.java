package psamolysov.bluemix.wlp.demo.service.presentation;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import psamolysov.bluemix.wlp.demo.model.Reservation;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ReservationSetProvider implements MessageBodyWriter<Set<Reservation>> {

	@Inject	
	private JsonAdapter<Reservation> reservationJsonAdapter;
	
	@Override
	public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		boolean isWriteable = Set.class.isAssignableFrom(clazz);
		if (isWriteable && type instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) type;
			Type[] actualTypes = paramType.getActualTypeArguments();
			if (actualTypes.length == 1) {
				isWriteable = actualTypes[0] == Reservation.class;
			}
		}
		return isWriteable;
	}

	@Override
	public long getSize(Set<Reservation> set, Class<?> clazz, Type type, Annotation[] annotations,
			MediaType mediaType) {
		return 0;
	}

	@Override
	public void writeTo(Set<Reservation> set, Class<?> clazz, Type type, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> headers, OutputStream os)
			throws IOException, WebApplicationException {		
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		Iterator<Reservation> reservations = set.iterator();
		while (reservations.hasNext()) {
			Reservation reservation = reservations.next();
			arrayBuilder.add(reservationJsonAdapter.toJson(reservation));
		}
		JsonWriter jsonWriter = Json.createWriter(os);
		jsonWriter.writeArray(arrayBuilder.build());
		jsonWriter.close();
	}
}
