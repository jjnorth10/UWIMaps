package com.example.uwimaps;

public interface DBConstants {
	public static final String DATABASE_NAME="uwimaps.db";
	//Database Tables
	public static final String LOCATION_TABLE="location";
	public static final String COURSE_TABLE="course";
	public static final String EVENT_TABLE="event";
	
	
	
	
	//Columns for location table
	public static final String LOCATION_ID = "_id";
	public static final String LOCATION_NAME = "name";
	public static final String LOCATION_DESCRIPTION = "description";
	public static final String LOCATION_LATITUDE="latitude";
	public static final String LOCATION_LONGITUDE="longitude";
	public static final String LOCATION_UNIQUE_INDEX="location_index";
	
	//Columns for course table
	public static final String COURSE_ID = "_id";
	public static final String COURSE_SUBJECT="subject";
	public static final String COURSE_CODE = "code";
	public static final String COURSE_TITLE = "title";
	public static final String COURSE_LECTURER = "lecturer";
	public static final String COURSE_LOCATION = "location";
	
	
	//Columns for location table
	public static final String EVENT_ID = "_id";
	public static final String EVENT_NAME = "name";
	public static final String EVENT_DESCRIPTION = "description";
	public static final String EVENT_LOCATION = "location";

	
	//Columns for search suggestions
	public static final String SUGGESTION_ID = "_id";

}
