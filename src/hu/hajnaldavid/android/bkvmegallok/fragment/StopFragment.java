package hu.hajnaldavid.android.bkvmegallok.fragment;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.adapter.StopAdapter;
import hu.hajnaldavid.android.bkvmegallok.model.Route;
import hu.hajnaldavid.android.bkvmegallok.model.Stop;
import hu.hajnaldavid.android.bkvmegallok.network.ParsingMode;
import hu.hajnaldavid.android.bkvmegallok.network.StopGetterASyncTask;
import hu.hajnaldavid.android.bkvmegallok.network.StopGetterASyncTaskObserver;
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

public class StopFragment extends ListFragment implements
		StopGetterASyncTaskObserver {

	private List<Route> routes;

	protected ParsingMode parsingMode = ParsingMode.ParsingModeJSON;
	protected StopGetterASyncTask getterTask;
	protected String url = Settings.getUrl() + "stop";
	protected Stop selectedStop;

	private Activity activity;

	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

	public Stop getSelectedStop() {
		return selectedStop;
	}

	public void setSelectedStop(Stop selectedStop) {
		this.selectedStop = selectedStop;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.stopdetailerlist, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		this.getListView().findFocus();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();

		ImageView refresh = (ImageView) getView().findViewById(
				R.id.stopsDetailerListRefreshButton);
		refresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.stopsDetailerListRefreshButton:
					if (selectedStop != null) {
						refreshWithStop(selectedStop);
					}
					break;
				}
			}

		});
	}

	@Override
	public void stopsDownloaded(List<Route> routes) {
		this.populate(routes);
	}

	public void refreshWithStop(Stop stop) {
		setSelectedStop(stop);
		if (this.selectedStop != null) {

			Map<String, String> keyV = new HashMap<String, String>();

			keyV.put("id", this.selectedStop.getId());
			String urlWithQuery = Helper.createQueryString(url, keyV);

			getterTask = new StopGetterASyncTask(activity, this, parsingMode);
			getterTask.execute(urlWithQuery);
		}
	}

	protected void populate(List<Route> routes) {
		if (routes != null) {
			this.routes = routes;
			if (this.selectedStop != null) {

				((TextView) activity.findViewById(R.id.routesListTitle))
						.setText(selectedStop.getStopName());

			}
			StopAdapter adapter = new StopAdapter(getActivity(), 0, routes,
					getSelectedStop());
			setListAdapter(adapter);
		} else {
			this.routes = null;
		}
	}
}
