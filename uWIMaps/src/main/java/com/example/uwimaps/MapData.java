package com.example.uwimaps;

import java.text.DecimalFormat;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MapData implements DBConstants {
	private Context context;
	MapHelper mapHelper;
	SQLiteDatabase db; 
	ContentResolver resolver;
	Uri tableUri;
	public static final String TAG = MapData.class.getSimpleName();
	
	public MapData(Context context) {
		this.context = context;
		mapHelper = new MapHelper(context);
		resolver = context.getContentResolver();
		
	}
	
	public long insertLocation(String name,String description,double latitude,double longtitude){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+LOCATION_TABLE);
		db = mapHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(LOCATION_NAME, name);
		values.put(LOCATION_DESCRIPTION, description);
		values.put(LOCATION_LATITUDE, latitude);
		values.put(LOCATION_LONGITUDE, longtitude);
		Uri insertUri = resolver.insert(tableUri, values);//db.insert(LOCATION_TABLE, null, values);
		long id = ContentUris.parseId(insertUri);
		db.close();
		return id;
		
	}
	
	public Cursor getAllLocations(){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+LOCATION_TABLE);
		//db = mapHelper.getWritableDatabase();
		String[] columns ={LOCATION_ID,LOCATION_NAME,LOCATION_DESCRIPTION,LOCATION_LATITUDE,LOCATION_LONGITUDE};
		Cursor cursor = resolver.query(tableUri, columns, null, null, null);//db.query(MapHelper.LOCATION_TABLE, columns, null, null, null, null, null);
		//StringBuffer buffer = new StringBuffer();
		/*while(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex(LOCATION_ID));
			String name = cursor.getString(cursor.getColumnIndex(LOCATION_NAME));
			String description = cursor.getString(cursor.getColumnIndex(LOCATION_DESCRIPTION));
			double latitude = cursor.getDouble(cursor.getColumnIndex(LOCATION_LATITUDE));
			double longitude = cursor.getDouble(cursor.getColumnIndex(LOCATION_LONGITUDE));
			buffer.append(id+" "+name+" "+description+" "+latitude+" "+longitude);
		}
		return buffer.toString();*/
		return cursor;
	}
	
	public Cursor getLocationByLatLng(double latitude,double longitude){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+LOCATION_TABLE);
		db = mapHelper.getWritableDatabase();
		String[] columns = {LOCATION_ID,LOCATION_NAME,LOCATION_DESCRIPTION};
		//DecimalFormat df = new DecimalFormat("#.######");
		String[] selectionArgs = {Double.toString(latitude),Double.toString(longitude)};
		Log.d(TAG, "latitude queried: "+latitude+" longitude queried: "+longitude);
		Cursor cursor = resolver.query(tableUri, columns, LOCATION_LATITUDE+" =? AND "+ LOCATION_LONGITUDE+" =?", selectionArgs, null);//db.query(LOCATION_TABLE, columns, LOCATION_LATITUDE+" =? AND "+ LOCATION_LONGITUDE+" =?", selectionArgs, null, null, null);
		return cursor;
		
	}
	
	public Cursor getLocationByName(String title){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+LOCATION_TABLE);
		db = mapHelper.getWritableDatabase();
		String[] columns = {"rowid",LOCATION_ID,LOCATION_NAME,LOCATION_DESCRIPTION,LOCATION_LATITUDE,LOCATION_LONGITUDE};
		String[] selectionArgs = {title};
		Log.d(TAG, "title queried: "+title);
		Cursor cursor = resolver.query(tableUri, columns, LOCATION_NAME+" =?", selectionArgs, null);//db.query(LOCATION_TABLE, columns, LOCATION_NAME+" =?", selectionArgs, null, null, null);
		return cursor;
		
	}
	
	public Cursor getLocationById(long id){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+LOCATION_TABLE);
		db = mapHelper.getWritableDatabase();
		String[] columns = {LOCATION_ID,LOCATION_NAME,LOCATION_DESCRIPTION,LOCATION_LATITUDE,LOCATION_LONGITUDE};
		String[] selectionArgs = {Long.toString(id)};
		Log.d(TAG, "id queried: "+id);
		Cursor cursor = resolver.query(tableUri, columns, "rowid"+" =?", selectionArgs, null);//db.query(LOCATION_TABLE, columns, "rowid"+" =?", selectionArgs, null, null, null);
		return cursor;
		
	}
	
	public Cursor getLocationByFTS(String query){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+LOCATION_TABLE);
		db = mapHelper.getWritableDatabase();
		String[] columns = {LOCATION_ID,LOCATION_NAME,LOCATION_DESCRIPTION,LOCATION_LATITUDE,LOCATION_LONGITUDE};
		String[] selectionArgs = {query};
		Log.d(TAG, "FTS query: "+query);
		Cursor cursor = resolver.query(tableUri, columns, LOCATION_TABLE+" MATCH ?", selectionArgs, null);//db.query(LOCATION_TABLE, columns, LOCATION_TABLE+" MATCH ?", selectionArgs, null, null, null);
		 if (cursor == null) {
			 Log.d(TAG, "Cursor null");
		      return null;
		  } else if (!cursor.moveToFirst()) {
			  Log.d(TAG, "Cursor null");
			  cursor.close();
			  return null;
		  }
		return cursor;
		
		
	}
	
	public long insertCourse(String subject,int code,String title,String lecturer,long location){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+COURSE_TABLE);
		db = mapHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COURSE_SUBJECT, subject);
		values.put(COURSE_CODE, code);
		values.put(COURSE_TITLE, title);
		values.put(COURSE_LECTURER, lecturer);
		values.put(COURSE_LOCATION, location);
		
		Uri insertUri = resolver.insert(tableUri, values);
		long id = ContentUris.parseId(insertUri);
		db.close();
		return id;
	}
	
	public long updateCourseLocation(long id,long location){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+COURSE_TABLE);
		db = mapHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();
		String[] whereArgs = {Long.toString(id)};
		values.put(COURSE_LOCATION, location);
		long count = resolver.update(tableUri, values, "rowid"+"= ?", whereArgs);//db.update(COURSE_TABLE, values,"rowid"+"= ?", whereArgs);
		db.close();
		return count;
	}
	public long updateCourse(long id,String subject,int code,String title,String lecturer,long location){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+COURSE_TABLE);
		db = mapHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();
		String[] whereArgs = {Long.toString(id)};
		values.put(COURSE_SUBJECT, subject);
		values.put(COURSE_CODE, Integer.toString(code));
		values.put(COURSE_TITLE, title);
		values.put(COURSE_LECTURER, lecturer);
		values.put(COURSE_LOCATION, location);
		long count = resolver.update(tableUri, values,"rowid"+"= ?", whereArgs);
		db.close();
		return count;
	}
	
	public long deleteCourse(long id){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+COURSE_TABLE);
		db = mapHelper.getWritableDatabase();	
		String [] whereArgs = {Long.toString(id)};
		long count = resolver.delete(tableUri, "rowid"+"= ?", whereArgs);
		db.close();
		return count;
		
	}
	
	public Cursor getCourseByLoc(long locationId){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+COURSE_TABLE);
		db = mapHelper.getWritableDatabase();
		String[] columns = {COURSE_ID,COURSE_CODE,COURSE_TITLE};
		String[] selectionArgs = {Long.toString(locationId)};
		Log.d(TAG, "location id queried: "+locationId);
		Cursor cursor = resolver.query(tableUri, columns, COURSE_LOCATION+" =?", selectionArgs, null);
		return cursor;
	}
	public Cursor getCourseById(long id){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+COURSE_TABLE);
		db = mapHelper.getWritableDatabase();
		String[] columns = {COURSE_ID,COURSE_SUBJECT,COURSE_CODE,COURSE_TITLE,COURSE_LOCATION};
		String[] selectionArgs = {Long.toString(id)};
		Cursor cursor = resolver.query(tableUri, columns, "rowid"+" =?", selectionArgs, null);
		return cursor;
		
	}
	
	public Cursor getCourse(String subject,int code,String title){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+COURSE_TABLE);
		db = mapHelper.getWritableDatabase();
		String[] columns = {COURSE_ID,COURSE_SUBJECT,COURSE_CODE,COURSE_TITLE,COURSE_LOCATION};
		String[] selectionArgs = {subject,Integer.toString(code),title};
		Cursor cursor = resolver.query(tableUri, columns, COURSE_SUBJECT+" =? AND "+COURSE_CODE+" =? AND "+COURSE_TITLE+" =?", selectionArgs, null);
		return cursor;
		
	}
	public Cursor getCourse(String subject,String code){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+COURSE_TABLE);
		db = mapHelper.getWritableDatabase();
		String[] columns = {"rowid",COURSE_ID,COURSE_SUBJECT,COURSE_CODE,COURSE_TITLE,COURSE_LOCATION};
		String[] selectionArgs = {subject,code};
		Cursor cursor = resolver.query(tableUri, columns, COURSE_SUBJECT+" =? AND "+COURSE_CODE+" =?", selectionArgs, null);
		return cursor;
		
	}
	
	public Cursor getAllCourses(){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+COURSE_TABLE);
		db = mapHelper.getWritableDatabase();
		String[] columns = {"rowid",COURSE_ID,COURSE_SUBJECT,COURSE_CODE,COURSE_TITLE,COURSE_LECTURER,COURSE_LOCATION};
		Cursor cursor = resolver.query(tableUri, columns, null, null, null);
		return cursor;
		
	}
	public Cursor getAllCoursesWithLocations(){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+COURSE_TABLE);
		db = mapHelper.getWritableDatabase();
		String[] columns = {COURSE_ID,COURSE_SUBJECT,COURSE_CODE,COURSE_TITLE,COURSE_LECTURER,COURSE_LOCATION};
		String[] selectionArgs = {"0"};
		Cursor cursor = resolver.query(tableUri, columns, COURSE_LOCATION+" > "+0, null, null);
		return cursor;
		
	}
	
	public Cursor getCourseByFTS(String query){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+COURSE_TABLE);
		db = mapHelper.getWritableDatabase();
		String[] columns = {COURSE_ID,COURSE_SUBJECT,COURSE_CODE,COURSE_TITLE,COURSE_LECTURER,COURSE_LOCATION};
		String[] selectionArgs = {query};
		Log.d(TAG, "FTS query: "+query);
		Cursor cursor = resolver.query(tableUri, columns, COURSE_TABLE+" MATCH ?", selectionArgs, null);//db.query(LOCATION_TABLE, columns, LOCATION_TABLE+" MATCH ?", selectionArgs, null, null, null);
		 if (cursor == null) {
			 Log.d(TAG, "Cursor null");
		      return null;
		  } else if (!cursor.moveToFirst()) {
			  Log.d(TAG, "Cursor null");
			  cursor.close();
			  return null;
		  }
		return cursor;
		
		
	}
	
	public long insertEvent(String name,String description,long location){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+EVENT_TABLE);
		
		ContentValues values = new ContentValues();
		values.put(EVENT_NAME, name);
		values.put(EVENT_DESCRIPTION, description);
		values.put(EVENT_LOCATION, location);
		
		Uri insertUri = resolver.insert(tableUri, values);
		long id = ContentUris.parseId(insertUri);
		
		return id;
	}
	
	public long updateEventLocation(long id,long location){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+EVENT_TABLE);
		
		ContentValues values = new ContentValues();
		String[] whereArgs = {Long.toString(id)};
		values.put(EVENT_LOCATION, location);
		long count = resolver.update(tableUri, values, "rowid"+"= ?", whereArgs);//db.update(COURSE_TABLE, values,"rowid"+"= ?", whereArgs);
		
		return count;
	}
	public long updateEvent(long id,String name,String description,long location){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+EVENT_TABLE);
		
		ContentValues values = new ContentValues();
		String[] whereArgs = {Long.toString(id)};
		values.put(EVENT_NAME, name);
		values.put(EVENT_DESCRIPTION, description);
		values.put(EVENT_LOCATION, location);
		long count = resolver.update(tableUri, values,"rowid"+"= ?", whereArgs);
		
		return count;
	}
	public long deleteEvent(long id){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+EVENT_TABLE);
		
		String [] whereArgs = {Long.toString(id)};
		long count = resolver.delete(tableUri, "rowid"+"= ?", whereArgs);
		
		return count;
		
	}
	
	public Cursor getEventByLoc(long locationId){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+EVENT_TABLE);
		
		String[] columns = {"rowid",EVENT_ID,EVENT_NAME,EVENT_DESCRIPTION};
		String[] selectionArgs = {Long.toString(locationId)};
		Log.d(TAG, "location id queried: "+locationId);
		Cursor cursor = resolver.query(tableUri, columns, EVENT_LOCATION+" =?", selectionArgs, null);
		return cursor;
	}
	public Cursor getEventById(long id){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+EVENT_TABLE);
		
		String[] columns = {EVENT_ID,EVENT_NAME,EVENT_DESCRIPTION,EVENT_LOCATION};
		String[] selectionArgs = {Long.toString(id)};
		Cursor cursor = resolver.query(tableUri, columns, "rowid"+" =?", selectionArgs, null);
		return cursor;
		
	}
	
	public Cursor getAllEvents(){
		tableUri = Uri.parse(MapProvider.AUTHORITY+"/"+EVENT_TABLE);
		
		String[] columns = {"rowid",EVENT_ID,EVENT_NAME,EVENT_DESCRIPTION,EVENT_LOCATION};
		Cursor cursor = resolver.query(tableUri, columns, null, null, null);
		return cursor;
		
	}
	
	public Cursor getTableName(){
		db = mapHelper.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
		return c;
	}
	
	public Cursor getTableByNames(){
		Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE name='course'", null);
		return c;
	}

}
