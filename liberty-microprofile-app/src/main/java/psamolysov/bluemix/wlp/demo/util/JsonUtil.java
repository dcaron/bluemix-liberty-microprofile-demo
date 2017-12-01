package psamolysov.bluemix.wlp.demo.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;

public class JsonUtil {

	private static final String FORMATTED_OBJECTS_ISNT_ALLOWED_TO_BE_NULL = 
			"Formatted date, time or duration isn't allowed to be null";

	private static DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd");
	
	private static DateTimeFormatter LOCAL_TIME_FORMATTER = DateTimeFormatter
			.ofPattern("HH:mm");
	
	public static String getString(String key, JsonObject json) {
    	String result = null;
		if (json.containsKey(key)) {
			JsonString value = json.getJsonString(key);
			if (value != null) {
				result = value.getString();
			}
		}
		return result;
	}
	
	public static LocalDate getDate(String key, JsonObject json) {
    	LocalDate result = null;
		if (json.containsKey(key)) {
			JsonString value = json.getJsonString(key);
			if (value != null) {
				result = date(value.getString());
			}
		}
		return result;
	}
	
	public static LocalTime getTime(String key, JsonObject json) {
		LocalTime result = null;
		if (json.containsKey(key)) {
			JsonString value = json.getJsonString(key);
			if (value != null) {
				result = time(value.getString());
			}
		}
		return result;
	}
	
	public static Duration getDuration(String key, JsonObject json) {
		Duration result = null;
		if (json.containsKey(key)) {
			JsonNumber value = json.getJsonNumber(key);
			if (value != null) {
				result = duration(value.intValueExact());
			}
		}
		return result;
	}
	
	public static String format(LocalDate date) {
		Objects.requireNonNull(date, FORMATTED_OBJECTS_ISNT_ALLOWED_TO_BE_NULL);
		return LOCAL_DATE_FORMATTER.format(date);
	}
	
	public static String format(LocalTime time) {
		Objects.requireNonNull(time, FORMATTED_OBJECTS_ISNT_ALLOWED_TO_BE_NULL);
		return LOCAL_TIME_FORMATTER.format(time);
	}
	
	public static long format(Duration duration) {
		Objects.requireNonNull(duration, FORMATTED_OBJECTS_ISNT_ALLOWED_TO_BE_NULL);
		return duration.toMinutes();
	}
	
	public static LocalDate date(String date) {
		return LocalDate.parse(date, LOCAL_DATE_FORMATTER);
	}
	
	public static LocalTime time(String time) {
		return LocalTime.parse(time, LOCAL_TIME_FORMATTER);
	}
	
	public static Duration duration(int minutes) {
		return Duration.ofMinutes(minutes);
	}
}
