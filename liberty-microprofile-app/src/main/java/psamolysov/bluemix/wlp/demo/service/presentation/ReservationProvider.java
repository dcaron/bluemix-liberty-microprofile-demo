package psamolysov.bluemix.wlp.demo.service.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import psamolysov.bluemix.wlp.demo.model.Reservation;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReservationProvider implements MessageBodyReader<Reservation>, MessageBodyWriter<Reservation> {
	
	@Inject	
	private JsonAdapter<Reservation> reservationJsonAdapter;
	
	@Override
	public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
        return clazz.equals(Reservation.class);		
	}

	@Override
	public long getSize(Reservation reservation, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		return 0;
	}

	@Override
	public void writeTo(Reservation reservation, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> headers, OutputStream os)
			throws IOException, WebApplicationException {
		reservationJsonAdapter.toJson(os, reservation);
	}

	@Override
	public boolean isReadable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
        return clazz.equals(Reservation.class);		
	}

	@Override
	public Reservation readFrom(Class<Reservation> clazz, Type type, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> headers, InputStream is)
			throws IOException, WebApplicationException {
		return reservationJsonAdapter.fromJson(is);
	}
}