package hu.hajnaldavid.android.bkvmegallok.network;

import hu.hajnaldavid.android.bkvmegallok.model.Stop;

import java.util.List;

public interface StopsGetterASyncTaskObserver {

	public void stopsDownloaded(List<Stop> result);

}
