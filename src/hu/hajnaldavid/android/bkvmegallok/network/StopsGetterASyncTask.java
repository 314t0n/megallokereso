package hu.hajnaldavid.android.bkvmegallok.network;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.model.Route;
import hu.hajnaldavid.android.bkvmegallok.model.Stop;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class StopsGetterASyncTask extends AsyncTask<String, Void, List<Stop>> {

	private static final String LOG_Megallo_ASYNC = "LOG_Megallo_ASYNC";

	private final StopsGetterASyncTaskObserver observer;
	private final ParsingMode parsingMode;
	private final Context context;

	public StopsGetterASyncTask(Context context,
			StopsGetterASyncTaskObserver observer, ParsingMode mode) {
		this.context = context;
		this.observer = observer;
		this.parsingMode = mode;
	}

	// stops?lat=47.474273&lon=19.060206&dist=0.75

	@Override
	protected List<Stop> doInBackground(String... params) {

		Map<String, Stop> result = null;
		List<Stop> returnResult = new ArrayList<Stop>();

		try {

			URL url = new URL(params[0]);
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
				JSONArray Megallos = obj.getJSONArray("Stops");
				// JSONArray Date = obj.getJSONArray("Date");
				// JSONArray Date = obj.getString("Date");

				// System.out.println(obj.getString("Date"));

				result = new HashMap<String, Stop>();
				// Set<String> routeIds = new HashSet<String>();
				for (int i = 0; i < Megallos.length(); ++i) {
					JSONObject MegalloObject = Megallos.getJSONObject(i);
					// String id = MegalloObject.getString("id");
					String id = MegalloObject.getString("stop_id");
					String name = MegalloObject.getString("stop_name");
					String headsign = MegalloObject.getString("trip_headsign");
					int direction = MegalloObject.getInt("direction_id");
					String routeTypeNumber = MegalloObject
							.getString("route_type");
					BigDecimal dist = new BigDecimal(
							MegalloObject.getString("dist"));
					// double dist = MegalloObject.getDouble("dist");
					// routeIds.add(MegalloObject.getString("route_short_name"));

					Route.Type routeType = null;

					try {
						routeType = Route.getTypeByNumber(Integer
								.parseInt(routeTypeNumber));
					} catch (Exception e) {

					}

					if (result.containsKey(id)) {

						Route r = new Route(
								MegalloObject.getString("route_short_name"),
								null, headsign, null, routeType);

						result.get(id).addRoute(r);

						result.get(id).addHeadsign(headsign);

					} else {

						Route r = new Route(
								MegalloObject.getString("route_short_name"),
								null, headsign, null, routeType);

						Stop myMegallo = new Stop(id, name);

						// távolság
						myMegallo.setDistance(dist.doubleValue());
						// járat
						myMegallo.addRoute(r);
						// végállomások
						myMegallo.addHeadsign(headsign);

						myMegallo.setDirection(direction);

						result.put(id, myMegallo);
					}

				}

				// elkészült halmaz átadása listának

				returnResult.addAll(result.values());

				Log.i("----------------------------------", "yeeee-----");
				// lista rendezése (dist szerint)
				Collections.sort(returnResult);

			}

		} catch (Exception ex) {
			result = null;
			Log.i(LOG_Megallo_ASYNC, ex.getMessage());
			ex.printStackTrace();
		}
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
	protected void onPostExecute(List<Stop> result) {
		mDialog.dismiss();
		observer.stopsDownloaded(result);
	}

}
