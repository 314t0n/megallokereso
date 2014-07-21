package hu.hajnaldavid.android.bkvmegallok.fragment;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.activity.MainActivity;
import hu.hajnaldavid.android.bkvmegallok.adapter.StopsAdapter;
import hu.hajnaldavid.android.bkvmegallok.database.Database;
import hu.hajnaldavid.android.bkvmegallok.model.Stop;
import hu.hajnaldavid.android.bkvmegallok.network.ParsingMode;
import hu.hajnaldavid.android.bkvmegallok.network.StopsGetterASyncTask;
import hu.hajnaldavid.android.bkvmegallok.network.StopsGetterASyncTaskObserver;
import hu.hajnaldavid.android.bkvmegallok.util.GPSTracker;
import hu.hajnaldavid.android.bkvmegallok.util.Helper;
import hu.hajnaldavid.android.bkvmegallok.util.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

public class StopsFragment extends ListFragment implements
		StopsGetterASyncTaskObserver {

	private Database datasource;

	// newpest 47.561342; 19.105497;
	// elte 47.474273; 19.060206;
	// astoria 47.494154,19.059384
	// japan 34.326404,135.611339

	private Double lat = 47.561342;
	private Double lon = 19.105497;
	private Double dist = 0.50;

	protected ParsingMode parsingMode = ParsingMode.ParsingModeJSON;
	protected StopsGetterASyncTask getterTask;
	protected String url = Settings.getUrl() + "stops";
	private GPSTracker gps;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.stoplist, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		datasource = new Database(getActivity());
	}

	private void moveBack() {
		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onStart() {
		super.onStart();

		ImageView back = (ImageView) getView().findViewById(
				R.id.stopsListBackButton);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.stopsListBackButton:

					moveBack();
					break;
				}
			}

		});

		ImageView refresh = (ImageView) getView().findViewById(
				R.id.stopsListRefreshButton);
		refresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.stopsListRefreshButton:
					if (gps.canGetLocation()) {

						lat = gps.getLatitude();
						lon = gps.getLongitude();

						loadData();
					} else {
						gps.showSettingsAlert();
					}
					break;
				}
			}

		});

		gps = new GPSTracker(getActivity());

		if (gps.canGetLocation()) {

			// DecimalFormat dfLat = new DecimalFormat("#.######");
			// DecimalFormat dfLon = new DecimalFormat("#.######");

			lat = gps.getLatitude();
			lon = gps.getLongitude();

			System.out.println(lat);
			System.out.println(lon);

			loadData();

		} else {
			gps.showSettingsAlert();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		gps.stopUsingGPS();
	}

	private void loadData() {
		if (lat != null && lon != null) {
			try {
				Toast.makeText(getActivity(),
						"lat: " + lat.toString() + ",lon: " + lon.toString(),
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String, String> keyV = new HashMap<String, String>();

			keyV.put("lat", lat.toString());
			keyV.put("lon", lon.toString());
			keyV.put("dist", dist.toString());

			String urlWithQuery = Helper.createQueryString(url, keyV);

			System.out.println(urlWithQuery);

			Toast.makeText(getActivity(), urlWithQuery, Toast.LENGTH_LONG)
					.show();

			getterTask = new StopsGetterASyncTask(getActivity(), this,
					parsingMode);
			getterTask.execute(urlWithQuery);
		} else {
			Toast.makeText(
					getActivity(),
					getActivity().getResources().getString(
							R.string.error_no_data), Toast.LENGTH_LONG).show();
		}
	}

	private List<Stop> stopsWithFav(List<Stop> stops) {
		List<Stop> temp = datasource.getStops();
		for (int i = 0; i < temp.size(); i++) {
			for (int j = 0; j < stops.size(); j++) {
				if (temp.get(i).getId().equals(stops.get(j).getId())) {
					stops.get(j).setFav(true);
				}
			}
		}
		return stops;
	}

	@Override
	public void stopsDownloaded(List<Stop> stops) {

		if (stops != null) {

			this.stopsWithFav(stops);

			try {
				ExpandableListView listView = (ExpandableListView) getView()
						.findViewById(android.R.id.list);
				StopsAdapter adapter = new StopsAdapter(getActivity(),
						(ArrayList<Stop>) stops);
				listView.setAdapter(adapter);
				int count = adapter.getGroupCount();
				for (int position = 1; position <= count; position++)
					listView.expandGroup(position - 1);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
		}

	}
}
