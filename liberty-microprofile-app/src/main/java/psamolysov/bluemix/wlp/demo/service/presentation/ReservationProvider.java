package psamolysov.bluemix.wlp.demo.service.presentation;

import static psamolysov.bluemix.wlp.demo.util.DebugUtil.isDebugEnabled;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

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
	@Override
	public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled())
            System.out.println("RP.isWriteable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + 
            		" mediaType=" + mediaType + " ==> " + clazz.equals(Reservation.class));
        return clazz.equals(Reservation.class);		
	}

	@Override
	public long getSize(Reservation reservation, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled())
			System.out.println("RP.getSize() clazz=" + clazz + " type=" + type + " annotations=" + annotations + 
        		" mediaType=" + mediaType);
		return 0;
	}

	@Override
	public void writeTo(Reservation reservation, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> headers, OutputStream os)
			throws IOException, WebApplicationException {
		// TODO Auto-generated method stub		
		// toJSON(os, reservation)		
	}

	@Override
	public boolean isReadable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled())
            System.out.println("RP.isReadable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + 
            		" mediaType=" + mediaType + " ==> " + clazz.equals(Reservation.class));
        return clazz.equals(Reservation.class);		
	}

	@Override
	public Reservation readFrom(Class<Reservation> clazz, Type type, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> headers, InputStream is)
			throws IOException, WebApplicationException {
		// TODO Auto-generated method stub
		return null; // fromJSON(is)
	}
}