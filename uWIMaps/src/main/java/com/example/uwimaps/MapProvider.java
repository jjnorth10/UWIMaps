package com.example.uwimaps;

import java.util.HashMap;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class MapProvider extends ContentProvider {
	public static final String AUTHORITY = "content://com.example.uwimaps.MapProvider";
	public static final String BASE = "com.example.uwimaps.MapProvider";
	public static final Uri CONTENT_URI = Uri.parse(AUTHORITY);
	public static final Uri LOCATION_URI = Uri.parse(AUTHORITY+"/"+DBConstants.LOCATION_TABLE);
	public static final Uri COURSE_URI = Uri.parse(AUTHORITY+"/"+DBConstants.COURSE_TABLE);
	public static final Uri EVENT_URI = Uri.parse(AUTHORITY+"/"+DBConstants.EVENT_TABLE);
	public static final Uri SUGGESTION_URI = Uri.parse(AUTHORITY+"/"+SearchManager.SUGGEST_URI_PATH_QUERY);
	
	public static final String TAG = MapProvider.class.getSimpleName();
	
	final static int LOCATION = 1;
	final static int LOCATION_ID = 2;
	final static int COURSE = 3;
	final static int COURSE_ID = 4;
	final static int EVENT = 5;
	final static int EVENT_ID = 6;
	final static int SEARCH_SUGGEST =7;
	
	 private static final String[] SEARCH_SUGGEST_COLUMNS = {
        DBConstants.SUGGESTION_ID,
         SearchManager.SUGGEST_COLUMN_TEXT_1,
         SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
 };
	
	
	private SQLiteDatabase db;
	MapHelper dbHelper;
	
	
	//private final static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	private static UriMatcher makeUriMatcher() {
		
		UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(BASE, DBConstants.LOCATION_TABLE, LOCATION);
		uriMatcher.addURI(BASE, DBConstants.LOCATION_TABLE+"/#", LOCATION);
		
		uriMatcher.addURI(BASE, DBConstants.COURSE_TABLE, COURSE);
		uriMatcher.addURI(BASE, DBConstants.COURSE_TABLE+"/#", COURSE);
		
		uriMatcher.addURI(BASE, DBConstants.EVENT_TABLE, EVENT);
		uriMatcher.addURI(BASE, DBConstants.EVENT_TABLE+"/#", EVENT);
		
		uriMatcher.addURI(BASE, SearchManager.SUGGEST_URI_PATH_QUERY , SEARCH_SUGGEST);
		uriMatcher.addURI(BASE, "limit/*", SEARCH_SUGGEST);
	    uriMatcher.addURI(BASE, SearchManager.SUGGEST_URI_PATH_QUERY + "/#", SEARCH_SUGGEST);
	
		
		
	    
	    return uriMatcher;
		
		
		
	}
	static{
		
		/*
		uriMatcher.addURI(AUTHORITY, DBConstants.LOCATION_TABLE, LOCATION);
		uriMatcher.addURI(AUTHORITY, DBConstants.LOCATION_TABLE+"/#", LOCATION_ID);
		
		uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
		uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "*", SEARCH_SUGGEST);
	    uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/#", SEARCH_SUGGEST);*/
	    
	    
	}
	public MapProvider() {
		// TODO Auto-generated constructor stub
		//setupSuggestions(AUTHORITY, SEARCH_SUGGEST);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbHelper = new MapHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Log.d(TAG, uri.toString());
		Log.d(TAG, "Uri matcher: "+makeUriMatcher().match(uri));
		 switch (makeUriMatcher().match(uri)) {
         case SEARCH_SUGGEST:
             Log.d(TAG, "Search suggestions requested.");
        	 SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
             builder.setTables(DBConstants.LOCATION_TABLE);
             
             //String select = DBConstants.LOCATION_TABLE+" MATCH ?";
             
            // String query = uri.getLastPathSegment().toLowerCase();
            // String selectArgs[] = {query};

             HashMap<String, String> columnMap = new HashMap<String, String>();
             columnMap.put(DBConstants.SUGGESTION_ID, "rowid" + " AS " + DBConstants.SUGGESTION_ID);
             columnMap.put(SearchManager.SUGGEST_COLUMN_TEXT_1, DBConstants.LOCATION_NAME + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
             columnMap.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid" + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
             builder.setProjectionMap(columnMap);
             //builder.setTables(DBConstants.LOCATION_TABLE);

             //dbHelper = new ToursDBOpenHelper(getContext());
             //database = dbhelper.getReadableDatabase();
             String[] selArgs = {"%"+selectionArgs[0]+"%"};

             SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = builder.query(db,SEARCH_SUGGEST_COLUMNS, selection, selArgs, null, null, sortOrder, null);

             return cursor;
         default:
        	db = dbHelper.getWritableDatabase();
     		Cursor defcursor=null;
     		defcursor = db.query(uri.getLastPathSegment(), projection, selection, selectionArgs, null, null, sortOrder);
     		
     		//db.close();
     		defcursor.setNotificationUri(getContext().getContentResolver(), uri);
     		return defcursor;
     }
		
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		  switch (makeUriMatcher().match(uri)) {
          case SEARCH_SUGGEST:
              return SearchManager.SUGGEST_MIME_TYPE;
          default:
        	  return null;
      }
		
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		db = dbHelper.getWritableDatabase();
		long id = db.insert(uri.getLastPathSegment(), null, values);
		
		//db.close();
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(DBConstants.LOCATION_TABLE+"/"+id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		db = dbHelper.getWritableDatabase();
		int count=0;
		count = db.delete(uri.getLastPathSegment(), selection, selectionArgs);
		
		//db.close();
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		db = dbHelper.getWritableDatabase();
		int count=0;
		count=db.update(uri.getLastPathSegment(), values, selection, selectionArgs);
			
		
		//db.close();
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
