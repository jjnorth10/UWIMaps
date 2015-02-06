package com.example.uwimaps;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class MapHelper extends SQLiteOpenHelper implements DBConstants {
	
	public static final String TAG = MapHelper.class.getSimpleName();
	private static final int DATABASE_VERSION = 29;
	private Context context;
	

	public MapHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	private static final String CREATE_TABLE_LOCATION="create virtual table "+LOCATION_TABLE+" using fts3 "+"("
			+LOCATION_ID			+" integer primary key autoincrement, "
			+LOCATION_NAME			+" text not null, "
			+LOCATION_DESCRIPTION	+" text, "
			+LOCATION_LATITUDE		+" double, "
			+LOCATION_LONGITUDE		+" double, "
			+"unique ("+LOCATION_NAME+") ON CONFLICT replace);";
	
	private static final String CREATE_TABLE_COURSE="create virtual table "+COURSE_TABLE+" using fts3 "+"("
			+COURSE_ID				+" integer primary key autoincrement, "
			+COURSE_SUBJECT			+" text not null, "
			+COURSE_CODE			+" integer not null, "
			+COURSE_TITLE			+" text not null unique, "
			+COURSE_LECTURER		+" text, "
			+COURSE_LOCATION		+" integer, "
			+"FOREIGN KEY ("+COURSE_LOCATION+") REFERENCES "+COURSE_TABLE+" ("+LOCATION_ID+"));";
	
	private static final String CREATE_TABLE_EVENT="create virtual table "+EVENT_TABLE+" using fts3 "+"("
			+EVENT_ID				+" integer primary key autoincrement, "
			+EVENT_NAME				+" text not null, "
			+EVENT_DESCRIPTION		+" text, "
			+EVENT_LOCATION			+" integer, "
			+"FOREIGN KEY ("+EVENT_LOCATION+") REFERENCES "+COURSE_TABLE+" ("+LOCATION_ID+"));";
			

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			db.execSQL(CREATE_TABLE_LOCATION);
			Log.d(TAG, "Created table: "+LOCATION_TABLE);
			
			//db.execSQL(CREATE_UNIQUE_INDEX_LOCATION);
			
			db.execSQL(CREATE_TABLE_COURSE);
			Log.d(TAG, "Created table: "+COURSE_TABLE);
			
			db.execSQL(CREATE_TABLE_EVENT);
			Log.d(TAG, "Created table: "+EVENT_TABLE);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this.context, ""+e, Toast.LENGTH_LONG).show();
			Log.e(TAG, e.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this.context, ""+e, Toast.LENGTH_LONG).show();
			Log.e(TAG, e.getMessage());
		}
		
		onCreate(db);
		Log.d(TAG, "Upgraded table: "+LOCATION_TABLE);
		Log.d(TAG, "Upgraded table: "+COURSE_TABLE);
		Log.d(TAG, "Upgraded table: "+EVENT_TABLE);

	}

}
