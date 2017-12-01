package psamolysov.bluemix.wlp.demo.service.presentation;

import java.io.InputStream;
import java.io.OutputStream;

import javax.json.JsonObject;

public interface JsonAdapter<T> {

	public T fromJson(InputStream is);
	
	public JsonObject toJson(T object);
	
	public void toJson(OutputStream os, T object);
}
