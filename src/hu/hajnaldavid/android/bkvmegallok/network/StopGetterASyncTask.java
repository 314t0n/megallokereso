package hu.hajnaldavid.android.bkvmegallok.network;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.fragment.StopFragment;
import hu.hajnaldavid.android.bkvmegallok.model.Route;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class StopGetterASyncTask extends AsyncTask<String, Void, List<Route>> {

	private static final String LOG_Megallo_ASYNC = "LOG_Megallo_ASYNC";

	private final StopGetterASyncTaskObserver observer;
	private final ParsingMode parsingMode;
	private final Context context;

	public StopGetterASyncTask(Context context, StopFragment stopFragment,
			ParsingMode parsingMode) {
		this.context = context;
		this.observer = stopFragment;
		this.parsingMode = parsingMode;
	}

	@Override
	protected List<Route> doInBackground(String... params) {

		List<Route> result = null;
		try {

			URL url = new URL(params[0]);
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();

			System.out.println(url.toString());

			if (parsingMode == ParsingMode.ParsingModeJSON) {
				BufferedReader streamReader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				StringBuilder builder = new StringBuilder();

				String inputStr;
				while ((inputStr = streamReader.readLine()) != null)
					builder.append(inputStr);
				JSONObject obj = new JSONObject(builder.toString());
				JSONArray jsonStops = obj.getJSONArray("Routes");
				String[] timestamp = obj.getString("Timestamp").split("\\s+");

				result = new ArrayList<Route>();
				for (int i = 0; i < jsonStops.length(); ++i) {
					JSONObject stopsObject = jsonStops.getJSONObject(i);

					String routeId = stopsObject.getString("route_short_name");
					String headsign = stopsObject.getString("trip_headsign");
					Integer wheelchair;

					if (!stopsObject.isNull("wheelchair_accessible")) {
						wheelchair = stopsObject
								.getInt("wheelchair_accessible");
					} else {
						wheelchair = 0;
					}
					String time = stopsObject.getString("departure_time");
					String routeIdDB = stopsObject.getString("route_id");
					String routeTypeNumber = stopsObject
							.getString("route_type");

					Route.Type routeType = null;

					try {
						routeType = Route.getTypeByNumber(Integer
								.parseInt(routeTypeNumber));
					} catch (Exception e) {

					}

					Route route = new Route(routeId, null, headsign,
							new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
									.parse(timestamp[0] + " " + time),
							routeType);
					route.setRouteID(routeIdDB);
					route.setWheelchair(wheelchair.equals(1) ? true : false);
					result.add(route);

				}
			}

		} catch (Exception ex) {
			result = null;
			Log.i(LOG_Megallo_ASYNC, ex.getMessage());
			ex.printStackTrace();
		}
		return result;
	}

	private ProgressDialog mDialog;

	@Override
	protected void onPreExecute() {
		mDialog = ProgressDialog.show(context, null, this.context
				.getResources().getString(R.string.loading));
		mDialog.setCancelable(true);
	}

	@Override
	protected void onPostExecute(List<Route> result) {
		mDialog.dismiss();
		observer.stopsDownloaded(result);
	}

}
