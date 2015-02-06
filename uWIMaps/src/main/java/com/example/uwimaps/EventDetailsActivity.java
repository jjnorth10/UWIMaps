package com.example.uwimaps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetailsActivity extends Activity {
	TextView eventName;
	TextView eventDescription;
	TextView eventLocation;
	AlertDialog dialog;
	AlertDialog.Builder builder;
	MapData mapData;
	Context context;

	public EventDetailsActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details_activity);
		mapData = new MapData(this);
		context = this;
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		eventName = (TextView)findViewById(R.id.eventName);
		eventDescription = (TextView)findViewById(R.id.eventDescription);
		eventLocation =(TextView)findViewById(R.id.eventLocation);
		
		String name = getIntent().getExtras().getString("name");
		String description = getIntent().getExtras().getString("description");
		long location = getIntent().getExtras().getLong("location");
		eventName.setText(name);
		eventDescription.setText(description);
		Cursor cursor = mapData.getLocationById(location);
		String locationName="";
		if(cursor.getCount()>0){
			while(cursor.moveToNext()){
				locationName = cursor.getString(cursor.getColumnIndex(DBConstants.LOCATION_NAME));
			}
		}
		eventLocation.setText(locationName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.event_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		final long rowid = getIntent().getExtras().getLong("rowid");
		int menuid=item.getItemId();
		if(menuid == R.id.menu_edit_event){
			String name = eventName.getText().toString();
			String description = eventDescription.getText().toString();
			String location = eventLocation.getText().toString();
		
			Intent i = new Intent(this,EventActivity.class);
		
			i.putExtra("rowid", rowid);
			i.putExtra("name", name);
			i.putExtra("description", description);
			i.putExtra("location", location);
			startActivity(i);
			
		}
		else if(menuid==R.id.menu_delete_event){
			builder = new AlertDialog.Builder(this);
			builder.setTitle("Delete Event");
			builder.setPositiveButton("Delete",  new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mapData.deleteEvent(rowid);
					Toast.makeText(context, "Event Deleted", Toast.LENGTH_LONG).show();
					Intent i = new Intent(context,EventListActivity.class);
					startActivity(i);
				
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				
				}
			});
			dialog = builder.create();
			dialog.show();
			
		}if (item.getItemId() == android.R.id.home) {
       
            Intent homeIntent = new Intent(this, EventListActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
		}
		
		
		return super.onOptionsItemSelected(item);
	}

}
