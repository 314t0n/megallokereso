package hu.hajnaldavid.android.bkvmegallok.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Kaszperek Mihály
 * 
 *         Járat típus
 */
@SuppressWarnings("rawtypes")
public class Route implements Comparable {

	public enum Type {

		/*
		 * https://developers.google.com/transit/gtfs/reference#routes_fields
		 * 
		 * 0 - Tram, Streetcar, Light rail. Any light rail or street level
		 * system within a metropolitan area. 1 - Subway, Metro. Any underground
		 * rail system within a metropolitan area. 2 - Rail. Used for intercity
		 * or long-distance travel. 3 - Bus. Used for short- and long-distance
		 * bus routes. 4 - Ferry. Used for short- and long-distance boat
		 * service. 5 - Cable car. Used for street-level cable cars where the
		 * cable runs beneath the car. 6 - Gondola, Suspended cable car.
		 * Typically used for aerial cable cars where the car is suspended from
		 * the cable. 7 - Funicular. Any rail system designed for steep
		 * inclines.
		 */
		Tram(0), Metro(1), Rail(2), Bus(3), Ferry(4), Funicular(7);

		private final int n;

		private Type(final int n) {
			this.n = n;
		}

		public int getNumber() {
			return this.n;
		}

	}

	public static Type getTypeByNumber(int n) {
		Route.Type o = null;
		switch (n) {
		case 0:
			o = Route.Type.Tram;
			break;
		case 1:
			o = Route.Type.Metro;
			break;
		case 2:
			o = Route.Type.Rail;
			break;
		case 3:
			o = Route.Type.Bus;
			break;
		case 4:
			o = Route.Type.Ferry;
			break;
		case 7:
			o = Route.Type.Funicular;
			break;
		}
		return o;

	}

	private final String ID;
	private String routeID; // járat db azon opc.
	private String tripID; // útvonal azon opc.
	private final String destination;
	private final String departure;
	private final Calendar departureTime;
	private final Type type;
	private String stopName; // megálló neve opc.
	private String stopID; // megálló neve opc.
	private boolean wheelchair;

	/**
	 * 
	 * @param ID
	 *            járatszám
	 * @param departure
	 *            induló állomás
	 * @param destination
	 *            célállomás neve
	 * @param departureTime
	 *            indulás ideje
	 * @param type
	 *            járat típusa
	 */
	public Route(String ID, String departure, String destination,
			Date departureTime, Type type) {
		super();
		this.ID = ID;
		this.destination = destination;
		this.departure = departure;
		this.departureTime = Calendar.getInstance();
		if (null != departureTime) {
			this.departureTime.setTime(departureTime);
		}
		this.type = type;
		// this.departureTime = Calendar.getInstance();
		// this.departureTime.setTime(departureTime);
	}

	public Type getType() {
		return type;
	}

	public String getID() {
		return ID;
	}

	public String getDestination() {
		return destination;
	}

	public Calendar getDepartureTime() {
		return departureTime;
	}

	public String getDepartureTimeAsString() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Date d = this.departureTime.getTime();
		// return departureTime.format("%H:%M");
		return df.format(d);
	}

	public String getStopID() {
		return stopID;
	}

	public void setStopID(String stopID) {
		this.stopID = stopID;
	}

	public String getRouteID() {
		return routeID;
	}

	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public static Calendar getCalendar(int year, int month, int day, int hour,
			int min) {

		Calendar c = Calendar.getInstance();
		c.set(year, month, day, hour, min);
		return c;

	}

	@Override
	public int compareTo(Object o) {
		if (o == this)
			return 0;

		Route r = (Route) o;

		return this.departureTime.compareTo(r.getDepartureTime());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		result = prime * result
				+ ((departureTime == null) ? 0 : departureTime.hashCode());
		result = prime * result
				+ ((destination == null) ? 0 : destination.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Route)) {
			return false;
		}
		Route other = (Route) obj;
		if (ID == null) {
			if (other.ID != null) {
				return false;
			}
		} else if (!ID.equals(other.ID)) {
			return false;
		}
		if (departureTime == null) {
			if (other.departureTime != null) {
				return false;
			}
		} else if (!departureTime.equals(other.departureTime)) {
			return false;
		}
		if (destination == null) {
			if (other.destination != null) {
				return false;
			}
		} else if (!destination.equals(other.destination)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Route [ID=" + ID + ", tripID=" + tripID + ", destination="
				+ destination + ", departure=" + departure + ", departureTime="
				+ departureTime.getTime().toGMTString() + ", type=" + type
				+ "]";
	}

	public boolean isWheelchair() {
		return wheelchair;
	}

	public void setWheelchair(boolean wheelchair) {
		this.wheelchair = wheelchair;
	}

}
