package hu.hajnaldavid.android.bkvmegallok.widget;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.model.Route;
import hu.hajnaldavid.android.bkvmegallok.widget.FavsWidgetService.FavsWidgetObserver;

import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FavsWidget extends AppWidgetProvider implements FavsWidgetObserver {

	@Override
	public void buildUpdate(List<Route> result) {
		Log.d(LOG, "observer ... ");

	}

	public static final String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
	public static final String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
	public static final String ACTION_WIDGET_DELETED = "FavsWidget.ACTION_WIDGET_DELETED";
	public static final String NEXT_CLICKED = "nextClick";
	public static final String PREV_CLICKED = "prevClick";
	public static final String STOP_CLICKED = "stopClick";
	public static final String STOP_WIDGET_CLICKED = "stopWidgetClick";
	public static final String REFRESH = "refresh";
	public static final String LOG = "FavsWidget";

	private PendingIntent service = null;
	private PendingIntent refreshService = null;
	private AlarmManager alarmManager;

	private RemoteViews remoteViews;

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		String action = intent.getAction();

		Log.d(LOG, "onReceive action: " + action);

		try {

			Intent clickIntent = new Intent(context, FavsWidgetService.class);
			clickIntent.putExtra("CLICK", intent.getAction());

			if (service == null) {
				service = PendingIntent.getService(context, 0, clickIntent,
						PendingIntent.FLAG_CANCEL_CURRENT);

			}

			alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			if (action.equals(STOP_CLICKED)) {

				Toast.makeText(context,
						context.getResources().getString(R.string.loading),
						Toast.LENGTH_SHORT).show();

				Intent refreshIntent = new Intent(context,
						FavsWidgetService.class);

				clickIntent.putExtra(REFRESH, intent.getAction());

				if (refreshService == null) {
					refreshService = PendingIntent.getService(context, 0,
							refreshIntent, PendingIntent.FLAG_CANCEL_CURRENT);
				}

				Calendar TIME = Calendar.getInstance();
				TIME.set(Calendar.MINUTE, 0);
				TIME.set(Calendar.SECOND, 0);
				TIME.set(Calendar.MILLISECOND, 0);

				alarmManager.setRepeating(AlarmManager.RTC, TIME.getTime()
						.getTime(), 1000, refreshService);

				context.startService(clickIntent);

			} else if (action.equals(PREV_CLICKED)
					|| action.equals(NEXT_CLICKED)
					|| action.equals(AppWidgetManager.ACTION_APPWIDGET_ENABLED)
					|| action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {

				context.startService(clickIntent);

			} else if (action.equals(STOP_WIDGET_CLICKED)) {

				Toast.makeText(context,
						context.getResources().getString(R.string.widget_stop),
						Toast.LENGTH_SHORT).show();

				Intent stopClickIntent = new Intent(context,
						FavsWidgetService.class);
				// stopClickIntent.putExtra("STOP_WIDGET_CLICK",
				// intent.getAction());
				stopClickIntent.putExtra("CLICK", intent.getAction());

				context.startService(stopClickIntent);

				int widgetID = intent.getIntExtra(
						AppWidgetManager.EXTRA_APPWIDGET_ID,
						AppWidgetManager.INVALID_APPWIDGET_ID);
				cancelAlarmManager(context, widgetID);
			}

			if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {

				int widgetID = intent.getIntExtra(
						AppWidgetManager.EXTRA_APPWIDGET_ID,
						AppWidgetManager.INVALID_APPWIDGET_ID);
				this.onDeleted(context, new int[] { widgetID });

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEnabled(Context context) {

		Log.d(LOG, "onEnabled");

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName thisWidget = new ComponentName(context, FavsWidget.class);

		buildUI(context, appWidgetManager,
				appWidgetManager.getAppWidgetIds(thisWidget));

	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d(LOG, "onUpdate");

		buildUI(context, appWidgetManager, appWidgetIds);

	}

	protected void buildUI(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		remoteViews = null;

		try {
			for (int i = 0; i < appWidgetIds.length; i++) {
				int appWidgetId = appWidgetIds[i];

				if (remoteViews == null) {
					remoteViews = new RemoteViews(context.getPackageName(),
							R.layout.favs_widget);
				}

				ComponentName thisWidget = new ComponentName(context,
						FavsWidget.class);

				remoteViews.setOnClickPendingIntent(R.id.favWidgetNext,
						getPendingSelfIntent(context, NEXT_CLICKED));

				remoteViews.setOnClickPendingIntent(R.id.favWidgetPrev,
						getPendingSelfIntent(context, PREV_CLICKED));

				remoteViews.setOnClickPendingIntent(R.id.favWidgetStopName,
						getPendingSelfIntent(context, STOP_CLICKED));

				remoteViews.setOnClickPendingIntent(R.id.favWidgetStop,
						getPendingSelfIntent(context, STOP_WIDGET_CLICKED));

				appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
			}
		} catch (Exception e) {
			Log.e(LOG, "FavsWidget onUpdate");
			e.printStackTrace();
		}
	}

	protected PendingIntent getPendingSelfIntent(Context context, String action) {
		Log.d(LOG, "Pending intent");
		Intent intent = new Intent(context, getClass());
		intent.setAction(action);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return PendingIntent.getBroadcast(context, 0, intent, 0);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.d(LOG, "Widget delete");
		super.onDeleted(context, appWidgetIds);
		for (int appWidgetId : appWidgetIds) {
			cancelAlarmManager(context, appWidgetId);
		}
		service = null;
		refreshService = null;
		Intent stopIntent = new Intent(context, FavsWidgetService.class);
		stopIntent.setAction(ACTION_WIDGET_DELETED);
		context.stopService(stopIntent);

	}

	protected void cancelAlarmManager(Context context, int widgetID) {
		AlarmManager alarm = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intentUpdate = new Intent(context, FavsWidget.class);

		intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_DELETED);

		intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
		PendingIntent pendingIntentAlarm = PendingIntent.getBroadcast(context,
				0, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);

		alarm.cancel(pendingIntentAlarm);
		Log.d(LOG, "cancelAlarmManager");

	}

}
