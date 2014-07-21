package hu.hajnaldavid.android.bkvmegallok.model;

/**
 * 
 *
 * 
 */
public class RouteWithStop extends Route {

	private final String stopID;
	private final String routeID;

	/**
	 * Járat megállóhoz specifikusan, a kedvencekben tároltakhoz
	 * 
	 * @param ID
	 *            járat neve pl 4
	 * @param departure
	 *            járat céláll.
	 * @param destination
	 *            járat céláll
	 * @param type
	 *            járat típus
	 * @param stopID
	 *            megálló db azon
	 * @param routeID
	 *            járat db azon
	 */
	public RouteWithStop(String ID, String departure, String destination,
			Type type, String stopID, String routeID) {
		super(ID, null, destination, null, type);
		this.stopID = stopID;
		this.routeID = routeID;
	}

	public String getStopID() {
		return stopID;
	}

	public String getRouteID() {
		return routeID;
	}

}
