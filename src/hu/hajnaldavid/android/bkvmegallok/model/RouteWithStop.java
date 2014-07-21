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
	 * J�rat meg�ll�hoz specifikusan, a kedvencekben t�roltakhoz
	 * 
	 * @param ID
	 *            j�rat neve pl 4
	 * @param departure
	 *            j�rat c�l�ll.
	 * @param destination
	 *            j�rat c�l�ll
	 * @param type
	 *            j�rat t�pus
	 * @param stopID
	 *            meg�ll� db azon
	 * @param routeID
	 *            j�rat db azon
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
