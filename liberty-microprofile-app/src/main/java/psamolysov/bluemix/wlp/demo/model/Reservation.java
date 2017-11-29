package psamolysov.bluemix.wlp.demo.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

	private String id;
	
	private String venue;
	
	private String venueId;
	
	private LocalDate date;
	
	private LocalTime startTime;
	
	private Duration duration;
	
	public Reservation(String venue, String venueId, LocalDate date, LocalTime startTime, 
			Duration duration) {
		this(null, venue, venueId, date, startTime, duration);
	}
	
	public Reservation(String id, String venue, String venueId, LocalDate date, LocalTime startTime, 
			Duration duration) {
		this.id = id;
		this.venue = venue;
		this.venueId = venueId;
		this.date = date;
		this.startTime = startTime;
		this.duration = duration;
	}

	public String getId() {
		return id;
	}

	public String getVenue() {
		return venue;
	}

	public String getVenueId() {
		return venueId;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public Duration getDuration() {
		return duration;
	}
	
	@Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Reservation reservation = (Reservation) o;
        return Objects.equals(venue, reservation.venue) &&
               Objects.equals(date, reservation.date) &&
               Objects.equals(startTime, reservation.startTime) &&
               Objects.equals(duration, reservation.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(venue, date, startTime, duration);
    }
	
	@Override
	public String toString() {
		return "Reservation [" +
				"id = " + id +
				", venue = " + venue + 
				", date = " + date + 
				", startTime = " + startTime + 
				", duration = " + duration + "]";
	}
}
