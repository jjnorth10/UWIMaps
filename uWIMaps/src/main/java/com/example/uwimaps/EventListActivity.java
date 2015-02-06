package com.example.uwimaps;

import java.util.HashMap;

import com.example.uwimaps.CourseListActivity.CourseListAdapter;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EventListActivity extends ListActivity implements OnItemClickListener {
	
	ListView listView;
	HashMap<String,Object> map;
	EventListAdapter evadapter;
	
	MapData mapData;
	long locationId;
	AlertDialog dialog;
	AlertDialog.Builder builder;
	
	DrawerLayout drawerLayout;
	ListView leftNavigationList;
	
	String[] navigationOptions;
	ActionBarDrawerToggle drawerListener;
	public static final String TAG = EventListActivity.class.getSimpleName();
	public EventListActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_list_activity);
		
		mapData = new MapData(this);
		drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
		leftNavigationList = (ListView)findViewById(R.id.leftNavigationMenu);
		navigationOptions = getResources().getStringArray(R.array.navigation_menu_options);
		getActionBar().setTitle(navigationOptions[2]);
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
		
	    
	    Cursor cursor = mapData.getAllEvents();
	    evadapter = new EventListAdapter(this, cursor, 0);
		listView = getListView();
		listView.setAdapter(evadapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.event_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(drawerListener.onOptionsItemSelected(item)){
			return true;
		}
		int menuid = item.getItemId();
		if(menuid == R.id.menu_add_event){
			Intent i = new Intent(this,EventActivity.class);
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
	 
	 	class EventListAdapter extends CursorAdapter{

			public EventListAdapter(Context context, Cursor c,int flags) {
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
				String eventName = cursor.getString(cursor.getColumnIndex(DBConstants.EVENT_NAME));
				String eventDescription = cursor.getString(cursor.getColumnIndex(DBConstants.EVENT_DESCRIPTION));
				title.setText(eventName);
				description.setText(eventDescription);
				
				
				return row;
			}
			
		}
	 	@Override
		protected void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			super.onListItemClick(l, v, position, id);
			long rowid=0;
			String name="";
			String description="";
			long location=0;
			Intent i = new Intent(this,EventDetailsActivity.class);
			Cursor cursor = (Cursor)listView.getItemAtPosition(position);
			if(cursor.getCount()>0){
					rowid = cursor.getLong(cursor.getColumnIndex("rowid"));
					name = cursor.getString(cursor.getColumnIndex(DBConstants.EVENT_NAME));
					description = cursor.getString(cursor.getColumnIndex(DBConstants.EVENT_DESCRIPTION));
					location = cursor.getLong(cursor.getColumnIndex(DBConstants.EVENT_LOCATION));
					Log.d(TAG, "name: "+name);
					
					
				
				
			}
			
			i.putExtra("rowid", rowid);
			i.putExtra("name", name);
			i.putExtra("description", description);
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
				i = new Intent(this,CourseListActivity.class);
				startActivity(i);
				
			break;
			case 2:
				
				
			}
			
			
		}



}
