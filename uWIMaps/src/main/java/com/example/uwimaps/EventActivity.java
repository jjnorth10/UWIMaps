package com.example.uwimaps;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class EventActivity extends Activity {
	
	private AutoCompleteTextView eventName;
	private AutoCompleteTextView eventDescription;
	private Spinner eventLocation;
	private Button saveButton;
	private MapData mapData;
	MainActivity activity;
	private Context context;
	public static final String TAG = EventActivity.class.getSimpleName();;

	public EventActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_activity);
		context = this;
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		eventName = (AutoCompleteTextView)findViewById(R.id.name);
		eventDescription = (AutoCompleteTextView)findViewById(R.id.description);
		
		eventLocation=(Spinner)findViewById(R.id.location);
		saveButton = (Button)findViewById(R.id.save);
		mapData = new MapData(this);
		activity = new MainActivity();
		loadSpinnerData();
		if(getIntent().getExtras()!=null){
			String name = getIntent().getExtras().getString("name");
			String description = getIntent().getExtras().getString("description");
			String location = getIntent().getExtras().getString("location");
			
			eventName.setText(name);
			eventDescription.setText(description);
			ArrayAdapter<String> a = (ArrayAdapter<String>) eventLocation.getAdapter();
			int pos = a.getPosition(location);
			eventLocation.setSelection(pos);
		}
		OnClickListener listener = new OnClickListener(){
			long rowid=0;
			String name;
			String description;
			@Override
			public void onClick(View view) {
				
				try{
					name = eventName.getText().toString();
					description = eventDescription.getText().toString();
				}catch(IllegalArgumentException e){
					Toast.makeText(context, "Invalid Entry", Toast.LENGTH_LONG).show();
					
				}
				String location = eventLocation.getSelectedItem().toString();
				
				Cursor cursor = mapData.getLocationByName(location);
				if(cursor.getCount()>0){
					while(cursor.moveToNext()){
						rowid = cursor.getLong(cursor.getColumnIndex("rowid"));
					}
				}
				
				addEvent(name,description,rowid);
				Intent i = new Intent(context,EventListActivity.class);
				startActivity(i);
				
				
			}};
		saveButton.setOnClickListener(listener);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
        case android.R.id.home:
            Intent homeIntent = new Intent(this, EventListActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
		}
		return super.onOptionsItemSelected(item);
		
	}

	public void loadSpinnerData(){
		List<String> locations = new ArrayList<String>();
		locations.add("Choose Location");
		Cursor cursor = mapData.getAllLocations();
		if(cursor.getCount()>0){
			while(cursor.moveToNext()){
				String location = cursor.getString(cursor.getColumnIndex(DBConstants.LOCATION_NAME));
				locations.add(location);
			}
		}
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	                android.R.layout.simple_spinner_item, locations);
		 adapter
         .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 eventLocation.setAdapter(adapter);

	}
	
	public void addEvent(String name, String description,long location){
		long rowid=0;
		if(getIntent().getExtras()!=null){
			rowid = getIntent().getExtras().getLong("rowid");
		}
		if(rowid!=0){
			mapData.updateEvent(rowid, name,description,location);
		}else{
			long id = mapData.insertEvent(name, description, location);
				if(id<0){
					Toast.makeText(this, "Error adding course", Toast.LENGTH_LONG).show();
					Log.d(TAG, "Error adding a course");
				}else{
					Toast.makeText(this, "Course added successfully", Toast.LENGTH_LONG).show();
					Log.d(TAG, "Course added");
				}
			
		}
		
	}

}
