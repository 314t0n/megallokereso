package hu.hajnaldavid.android.bkvmegallok.fragment;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.adapter.RouteAdapter;
import hu.hajnaldavid.android.bkvmegallok.model.Route;
import hu.hajnaldavid.android.bkvmegallok.model.Stop;
import hu.hajnaldavid.android.bkvmegallok.network.ParsingMode;
import hu.hajnaldavid.android.bkvmegallok.network.RouteScheduleGetterASyncTask;
import hu.hajnaldavid.android.bkvmegallok.network.RouteScheduleGetterASyncTaskObserver;
import hu.hajnaldavid.android.bkvmegallok.util.Helper;
import hu.hajnaldavid.android.bkvmegallok.util.Settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RouteFragment extends ListFragment implements
		RouteScheduleGetterASyncTaskObserver {

	private List<Route> routes;
	// http://localhost/android/bkk-api/route_stop?route_id=1960&stop_id=F00885
	protected ParsingMode parsingMode = ParsingMode.ParsingModeJSON;
	protected RouteScheduleGetterASyncTask getterTask;
	protected String url = Settings.getUrl() + "route_stop";
	protected Stop selectedStop;
	protected Route selectedRoute;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.routeschedulerlist, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();

		ImageView refreshB = (ImageView) getView().findViewById(
				R.id.routeScheduleRefreshBackButton);
		refreshB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.routeScheduleRefreshBackButton:
					if (selectedRoute != null || selectedStop != null) {
						refreshWithRoute(selectedRoute, selectedStop);
					}
					break;
				}
			}

		});

	}

	@Override
	public void routesDownloaded(List<Route> routes) {
		this.populate(routes);
	}

	public void refreshWithRoute(Route route, Stop stop) {
		selectedStop = stop;
		selectedRoute = route;
		if (route != null) {

			StringBuilder title = new StringBuilder();

			title.append(selectedStop.getStopName());

			title.append(", ");

			title.append(getActivity().getResources().getString(
					Helper.getRouteStringNumber(route.getType().getNumber())));

			((TextView) getActivity().findViewById(R.id.routeScheduleListTitle))
					.setText(title.toString());

			Map<String, String> keyV = new HashMap<String, String>();

			keyV.put("route_id", route.getRouteID());
			keyV.put("stop_id", stop.getId());
			String urlWithQuery = Helper.createQueryString(url, keyV);

			getterTask = new RouteScheduleGetterASyncTask(getActivity(), this,
					parsingMode, route);
			getterTask.execute(urlWithQuery);
		}
	}

	protected void populate(List<Route> routes) {
		if (routes != null) {
			this.routes = routes;
			RouteAdapter adapter = new RouteAdapter(getActivity(), 0, routes,
					selectedStop);
			setListAdapter(adapter);
		} else {
			this.routes = null;
		}
	}
}
