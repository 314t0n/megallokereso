package hu.hajnaldavid.android.bkvmegallok.network;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.model.Route;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RouteScheduleGetterASyncTask extends
		AsyncTask<String, Void, List<Route>> {

	private static final String TAG = "LOG_Route_Schedule_ASYNC";

	private final RouteScheduleGetterASyncTaskObserver observer;
	private final ParsingMode parsingMode;
	private final Context context;
	private final Route route;

	public RouteScheduleGetterASyncTask(Context context,
			RouteScheduleGetterASyncTaskObserver observer, ParsingMode mode,
			Route route) {
		this.context = context;
		this.observer = observer;
		this.parsingMode = mode;
		this.route = route;
	}

	@Override
	protected List<Route> doInBackground(String... params) {

		List<Route> returnResult = new ArrayList<Route>();
		Calendar ca = Calendar.getInstance();
		try {

			URL url = new URL(params[0]);
			Log.i(TAG, url.toString());
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();

			if (parsingMode == ParsingMode.ParsingModeJSON) {
				BufferedReader streamReader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				StringBuilder builder = new StringBuilder();

				String inputStr;
				while ((inputStr = streamReader.readLine()) != null)
					builder.append(inputStr);
				JSONObject obj = new JSONObject(builder.toString());
				JSONArray routes = obj.getJSONArray("Routes");

				for (int i = 0; i < routes.length(); ++i) {
					JSONObject routeObject = routes.getJSONObject(i);

					String time = routeObject.getString("departure_time");
					String headsign = routeObject.getString("trip_headsign");

					Integer wheelchair;
					if (!routeObject.isNull("wheelchair_accessible")) {
						wheelchair = routeObject
								.getInt("wheelchair_accessible");
					} else {
						wheelchair = 0;
					}

					StringBuilder date = new StringBuilder();
					date.append(Calendar.getInstance().YEAR);
					date.append(".");
					date.append(Calendar.getInstance().MONTH);
					date.append(".");
					date.append(Calendar.getInstance().DAY_OF_MONTH);
					date.append(" ");
					date.append(time);

					Route myRoute = new Route(route.getID(), null, headsign,
							new SimpleDateFormat("yy.MM.dd HH:mm:ss")
									.parse(date.toString()), route.getType());
					myRoute.setWheelchair(wheelchair.equals(1) ? true : false);
					returnResult.add(myRoute);

				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Log.i(TAG, "futok am");
		System.out.println(returnResult);
		return returnResult;
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
		observer.routesDownloaded(result);
	}

}
