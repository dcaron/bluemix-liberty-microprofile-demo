package psamolysov.bluemix.wlp.demo.service.presentation;

import java.io.InputStream;
import java.io.OutputStream;

public interface JsonAdapter<T> {

	public T fromJson(InputStream is);
	
	public void toJson(OutputStream os, T object);
}
