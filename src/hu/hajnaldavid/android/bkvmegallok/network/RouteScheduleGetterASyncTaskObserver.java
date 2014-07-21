package hu.hajnaldavid.android.bkvmegallok.network;

import hu.hajnaldavid.android.bkvmegallok.model.Route;

import java.util.List;

public interface RouteScheduleGetterASyncTaskObserver {

	public void routesDownloaded(List<Route> result);

}
