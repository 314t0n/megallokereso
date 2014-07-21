package hu.hajnaldavid.android.bkvmegallok.database;

import hu.hajnaldavid.android.bkvmegallok.model.Route;
import hu.hajnaldavid.android.bkvmegallok.model.Stop;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database {

	private DatabaseHelper helper = null;
	private static SQLiteDatabase db = null;
	private final Context context;
	private static final String TAG = "Database";

	public Database(Context ctxt) {
		context = ctxt;
		helper = new DatabaseHelper(context);
	}

	public Database open() {
		db = helper.getWritableDatabase();
		return this;
	}

	public void close() {
		helper.close();
	}

	public void saveStop(Stop stop) {
		try {
			open();
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.KEY_STOP_ID, stop.getId());
			values.put(DatabaseHelper.KEY_STOP_NAME, stop.getStopName());
			values.put(DatabaseHelper.ROUTES, stop.getRoutesAsString());
			values.put(DatabaseHelper.DIRECTION, stop.getDirection());
			db.insertOrThrow(DatabaseHelper.TABLE_NAME, null, values);
		} catch (SQLiteConstraintException e) {
			Log.i(TAG, "duplikált");
		} finally {
			close();
		}

	}

	public void deleteStop(String stopID) {
		open();
		db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.KEY_STOP_ID + "=?",
				new String[] { stopID });
		close();
	}

	public List<Stop> getStops() {
		open();
		Log.i("DB---", "meghívódás");
		String[] columns = { DatabaseHelper.KEY_ROWID,
				DatabaseHelper.KEY_STOP_ID, DatabaseHelper.KEY_STOP_NAME,
				DatabaseHelper.DIRECTION, DatabaseHelper.ROUTES };
		Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, columns, null,
				null, null, null, null);
		cursor.moveToFirst();
		List<Stop> stops = new ArrayList<Stop>();

		try {
			while (!cursor.isAfterLast()) {
				Stop s = new Stop(cursor.getString(1), cursor.getString(2));

				s.setDirection(cursor.getInt(3));

				String routes = cursor.getString(4);
				String[] temp = routes.split(",");

				for (int i = 0; i < temp.length; i++) {
					s.addRoute(new Route(temp[i], null, null, null, null));
				}

				stops.add(s);

				cursor.moveToNext();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return stops;
	}
}
