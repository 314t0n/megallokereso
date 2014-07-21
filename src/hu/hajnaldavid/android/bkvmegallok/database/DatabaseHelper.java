package hu.hajnaldavid.android.bkvmegallok.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static String DATABASE_NAME = "bk_db";
	private static int DATABASE_VERSION = 17;

	public static final String TABLE_NAME = "favs";

	public static final String KEY_ROWID = "_id";
	public static final String KEY_STOP_ID = "stop_id";
	public static final String KEY_STOP_NAME = "stop_name";
	public static final String DIRECTION = "dir";
	public static final String ROUTES = "routes";

	private static final String CREATE_TABLE_STMT = "CREATE TABLE "
			+ TABLE_NAME + " (" + KEY_ROWID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_STOP_ID
			+ " TEXT NOT NULL," + KEY_STOP_NAME + " TEXT NOT NULL, "
			+ DIRECTION + " INTEGER NOT NULL," + ROUTES
			+ " TEXT NOT NULL, UNIQUE(" + KEY_STOP_ID + ") );";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_STMT);
		db.execSQL("insert into " + TABLE_NAME + "(" + KEY_ROWID + ","
				+ KEY_STOP_ID + "," + KEY_STOP_NAME + "," + DIRECTION + ","
				+ ROUTES
				+ ") values(1,\"F01194\",\"Corvin-negyed M\",1,\"4,6\")");

		db.execSQL("insert into " + TABLE_NAME + "(" + KEY_ROWID + ","
				+ KEY_STOP_ID + "," + KEY_STOP_NAME + "," + DIRECTION + ","
				+ ROUTES
				+ ") values(2,\"F01191\",\"Corvin-negyed M\",0,\"4,6\")");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_NAME);
		onCreate(db);
	}

}
