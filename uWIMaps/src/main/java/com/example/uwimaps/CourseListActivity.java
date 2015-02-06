package com.example.uwimaps;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.communication.APIEndPoints;
import com.example.communication.Communication;
import com.example.communication.CommunicationResponse;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class CourseListActivity extends ListActivity implements DBConstants, OnItemClickListener {
	

	ListView listView;
	HashMap<String,Object> map;
	CourseListAdapter cladapter;
	
	MapData mapData;
	long locationId;
	AlertDialog dialog;
	AlertDialog.Builder builder;
	
	DrawerLayout drawerLayout;
	ListView leftNavigationList;
	
	String[] navigationOptions;
	ActionBarDrawerToggle drawerListener;
	
	public static final String TAG = CourseListActivity.class.getSimpleName();

	public CourseListActivity() {
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_list_activity);
		
		mapData = new MapData(this);
		drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
		leftNavigationList = (ListView)findViewById(R.id.leftNavigationMenu);
		navigationOptions = getResources().getStringArray(R.array.navigation_menu_options);
		getActionBar().setTitle(navigationOptions[1]);
		leftNavigationList.setAdapter(new ArrayAdapter<String>(this, R.layout.navigation_list_item,R.id.navigation_list_item_text, navigationOptions));
		
		leftNavigationList.setOnItemClickListener(this);
		
		drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, 0, 0){

			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
			}};
			
		drawerLayout.setDrawerListener(drawerListener);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
	    
	    Cursor cursor = mapData.getAllCourses();
	    cladapter = new CourseListAdapter(this, cursor, 0);
		listView = getListView();
		listView.setAdapter(cladapter);
		
		
		
		//map.put("location_id", id);
		//Log.d(TAG, "location id: "+id);
		//String query=Communication.getQueryString(map);
		//Log.d(TAG, "query: "+query);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.course_list, menu);
		/*
		 // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default*/
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(drawerListener.onOptionsItemSelected(item)){
			return true;
		}
		int menuid = item.getItemId();
		if(menuid == R.id.menu_add_course){
			Intent i = new Intent(this,CourseActivity.class);
			startActivity(i);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	 @Override
		protected void onPostCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onPostCreate(savedInstanceState);
			drawerListener.syncState();
		}
	
	
	class CourseListAdapter extends CursorAdapter{

		public CourseListAdapter(Context context, Cursor c,int flags) {
			super(context, c,flags);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row=inflater.inflate(R.layout.list_item, parent, false);
			TextView title = (TextView) row.findViewById(R.id.list_item_title);
			TextView description = (TextView) row.findViewById(R.id.list_item_desc);
			String subject = cursor.getString(cursor.getColumnIndex(COURSE_SUBJECT));
			int code = cursor.getInt(cursor.getColumnIndex(COURSE_CODE));
			String courseTitle = cursor.getString(cursor.getColumnIndex(COURSE_TITLE));
			title.setText(subject+" "+code);
			description.setText(courseTitle);
			
			
			return row;
		}
		
	}

	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		long rowid=0;
		String subject="";
		int code=0;
		String title="";
		String lecturer="";
		long location=0;
		Intent i = new Intent(this,CourseDetailsActivity.class);
		Cursor cursor = (Cursor)listView.getItemAtPosition(position);
		if(cursor.getCount()>0){
				rowid = cursor.getLong(cursor.getColumnIndex("rowid"));
				subject = cursor.getString(cursor.getColumnIndex(COURSE_SUBJECT));
				code = cursor.getInt(cursor.getColumnIndex(COURSE_CODE));
				title = cursor.getString(cursor.getColumnIndex(COURSE_TITLE));
				lecturer = cursor.getString(cursor.getColumnIndex(COURSE_LECTURER));
				location = cursor.getLong(cursor.getColumnIndex(COURSE_LOCATION));
				Log.d(TAG, "subject: "+subject);
				
				
			
			
		}
		
		i.putExtra("rowid", rowid);
		i.putExtra("subject", subject);
		i.putExtra("code", code);
		i.putExtra("title", title);
		i.putExtra("lecturer", lecturer);
		i.putExtra("location", location);
		startActivity(i);
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i;
		switch(position){
		case 0:
			i = new Intent(this,MainActivity.class);
			startActivity(i);
			
		break;
		case 1:
			
			
		break;
		case 2:
			i = new Intent(this,EventListActivity.class);
			startActivity(i);
			
		}
		
		
	}

	

}
