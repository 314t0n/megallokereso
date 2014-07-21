package hu.hajnaldavid.android.bkvmegallok.widget;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.database.Database;
import hu.hajnaldavid.android.bkvmegallok.model.Route;
import hu.hajnaldavid.android.bkvmegallok.model.Stop;
import hu.hajnaldavid.android.bkvmegallok.network.ParsingMode;
import hu.hajnaldavid.android.bkvmegallok.util.Helper;
import hu.hajnaldavid.android.bkvmegallok.util.Settings;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FavsWidgetService extends Service {

	public interface FavsWidgetObserver {

		public void buildUpdate(List<Route> result);

	}

	private FavsWidgetObserver listener;

	private static final String LOG = "FavsWidgetService";

	private Database datasource;
	private List<Stop> stops;
	private long timeDifference;
	private List<Route> routes;
	private int selectedStopIndex = 0;

	private Calendar currentDateTime;
	private Route currentRoute;

	@Override
	public void onCreate() {
		datasource = new Database(getApplicationContext());
		stops = datasource.getStops();

		buildUpdate();
		Log.d(LOG, "Created");
	}

	private void setNextStop() {
		selectedStopIndex = (selectedStopIndex + 1 == stops.size()) ? 0
				: selectedStopIndex + 1;
	}

	private void setPrevStop() {
		selectedStopIndex = (selectedStopIndex - 1 < 0) ? stops.size() - 1
				: selectedStopIndex - 1;
	}

	private Stop getSelectedStop() {
		Stop s = null;
		if (stops != null && stops.size() > 0) {
			s = stops.get(selectedStopIndex);
		}
		return s;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG, "onStartCommand");
		stops = datasource.getStops();
		try {

			if (intent.hasExtra("CLICK")) {

				String action = intent.getStringExtra("CLICK");

				System.out.println(action);

				if (FavsWidget.NEXT_CLICKED.equals(action)) {

					setNextStop();

				} else if (FavsWidget.PREV_CLICKED.equals(action)) {

					setPrevStop();

				} else if (FavsWidget.STOP_CLICKED.equals(action)) {

					Toast.makeText(
							getApplicationContext(),
							getApplicationContext().getResources().getString(
									R.string.loading), Toast.LENGTH_SHORT)
							.show();

					getRoutes();

				} else if (FavsWidget.STOP_WIDGET_CLICKED.equals(action)) {

					routes = null;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		buildUpdate();
		return START_STICKY;

	}

	private RemoteViews view;

	private void buildUpdate() {
		Log.d(LOG, "buildUpdate");
		try {

			String lastUpdated = DateFormat.format("hh:mm:ss", new Date())
					.toString();
			Log.d(LOG, lastUpdated);

			Stop stop = getSelectedStop();

			// RemoteViews view = FavsWidget.remoteViews;
			view = null;
			if (view == null) {
				view = new RemoteViews(getPackageName(), R.layout.favs_widget);
			}

			if (stop != null) {
				view.setTextViewText(R.id.favWidgetStopName, stop.getStopName());

				if (stop.getDirection() == 0) {
					view.setInt(R.id.favWidgetDir, "setBackgroundResource",
							R.drawable.dir_0_icon);

					view.setInt(R.id.favWidgetDir, "setVisibility",
							R.drawable.dir_0_icon);
				} else {
					view.setInt(R.id.favWidgetDir, "setBackgroundResource",
							R.drawable.dir_1_icon);
				}

				view.setInt(R.id.favWidgetDir, "setVisibility", View.VISIBLE);

			} else {
				view.setTextViewText(R.id.favWidgetStopName, this
						.getResources().getString(R.string.favs_empty));

				view.setInt(R.id.favWidgetDir, "setVisibility", View.INVISIBLE);

			}

			if (routes != null) {

				System.out.println(routes.size());

				currentDateTime.add(Calendar.SECOND, 1);

				if (routes.isEmpty()) {
					getRoutes();
				} else {

					System.out.println(timeDifference);
					System.out.println(new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss").format(currentDateTime
							.getTime()));
					System.out.println(new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss").format(currentRoute
							.getDepartureTime().getTime()));

					view.setTextViewText(R.id.favWidgetRouteID,
							currentRoute.getID());
					view.setTextViewText(R.id.favWidgetRouteDest,
							currentRoute.getDestination());
					view.setTextViewText(R.id.favWidgetRouteTime,
							currentRoute.getDepartureTimeAsString());
					view.setInt(R.id.favWidgetRouteID, "setBackgroundResource",
							Helper.getRouteTypeColor(currentRoute.getType(),
									currentRoute.getID()));

					timeDifference = timeDifference - 1000;

					if (timeDifference < 0 || timeDifference == 0) {
						routes.remove(0);
						if (!routes.isEmpty())
							currentRoute = routes.get(0);
						timeDifference = currentRoute.getDepartureTime()
								.getTime().getTime()
								- currentDateTime.getTime().getTime();
					}
				}
			} else {

				view.setTextViewText(R.id.favWidgetRouteID, "");
				view.setTextViewText(R.id.favWidgetRouteDest, "");
				view.setTextViewText(R.id.favWidgetRouteTime, "");
				view.setInt(R.id.favWidgetRouteID, "setBackgroundResource",
						Color.TRANSPARENT);

			}

			ComponentName thisWidget = new ComponentName(this, FavsWidget.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			manager.updateAppWidget(thisWidget, view);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getRoutes() {

		ParsingMode parsingMode = ParsingMode.ParsingModeJSON;

		List<Route> result = null;
		try {
			Stop stop = getSelectedStop();
			String urlString = Settings.getUrl() + "stop?id=" + stop.getId();
			URL url = new URL(urlString);
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
				JSONArray Megallos = obj.getJSONArray("Routes");
				String[] timestamp = obj.getString("Timestamp").split("\\s+");

				currentDateTime = Calendar.getInstance();
				currentDateTime.setTime(new SimpleDateFormat(
						"yyyy.MM.dd HH:mm:ss").parse(timestamp[0] + " "
						+ timestamp[1]));

				result = new ArrayList<Route>();
				for (int i = 0; i < Megallos.length(); ++i) {
					JSONObject MegalloObject = Megallos.getJSONObject(i);

					String routeId = MegalloObject
							.getString("route_short_name");
					String headsign = MegalloObject.getString("trip_headsign");
					boolean wheelchair;
					if (!obj.isNull("wheelchair_accessible")) {
						wheelchair = MegalloObject
								.getBoolean("wheelchair_accessible");
					} else {
						wheelchair = false;
					}
					String time = MegalloObject.getString("departure_time");
					String routeIdDB = MegalloObject.getString("route_id");
					String routeTypeNumber = MegalloObject
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
					route.setWheelchair(wheelchair);
					result.add(route);

				}
			}

		} catch (Exception ex) {
			result = null;
			ex.printStackTrace();

		} finally {
			routes = result;
			if (routes != null && !routes.isEmpty()) {
				currentRoute = routes.get(0);

				timeDifference = currentRoute.getDepartureTime().getTime()
						.getTime()
						- currentDateTime.getTime().getTime();

			}

			listener.buildUpdate(result);

		}
	}

	@Override
	public void onDestroy() {
		Log.d(LOG, "FavsWidgetService destroyed.");
		datasource.close();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
