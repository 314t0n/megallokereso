package hu.hajnaldavid.android.bkvmegallok.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Stop implements Comparable {

	private final String id; // adatbázis azonosító
	private String stopName; // megálló neve
	private Set<Route> routes; // járatok ebbõl a megállóból
	private Double distance; // távolság km-ben
	private final Set<String> headsigns; // végállomások ebbõl a megállóból
	private int direction;
	private boolean isFav;

	public Stop(String id, String stopName) {
		super();
		this.id = id;
		this.stopName = stopName;
		this.routes = new HashSet<Route>();
		this.headsigns = new HashSet<String>();
	}

	public void addHeadsign(String headsign) {
		this.headsigns.add(headsign);
	}

	public Set<String> getHeadsigns() {
		return headsigns;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		if (distance >= 0)
			this.distance = distance;
	}

	public void addRoute(Route r) {
		this.routes.add(r);
	}

	public String getId() {
		return id;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public Set<Route> getRoutes() {
		return routes;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setRoutes(Set<Route> routes) {
		this.routes = routes;
	}

	public List<Route> getRoutesAsList() {
		List<Route> routeList = new ArrayList<Route>();
		routeList.addAll(this.getRoutes());
		Collections.sort(routeList);
		return routeList;
	}

	public String getRoutesAsString() {
		Set<String> uniqueRouteIDs = new TreeSet<String>();
		for (Route r : getRoutes()) {
			uniqueRouteIDs.add(r.getID());
			;
		}
		StringBuilder str = new StringBuilder();
		int i = 1;
		for (String s : uniqueRouteIDs) {
			str.append(s);
			if (i < uniqueRouteIDs.size()) {
				str.append(",");
			}
			++i;
		}
		return str.toString();
	}

	public void setRoutes(HashSet<Route> routes) {
		this.routes = routes;
	}

	public boolean isFav() {
		return isFav;
	}

	public void setFav(boolean isFav) {
		this.isFav = isFav;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public int compareTo(Object o) {
		if (o == this)
			return 0;

		Stop s = (Stop) o;

		return this.distance.compareTo(s.getDistance());
	}

}
