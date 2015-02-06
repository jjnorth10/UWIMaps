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

public class CourseDetailsActivity extends Activity implements DBConstants {
	TextView courseSubject;
	TextView courseCode;
	TextView courseTitle;
	TextView courseLecturer;
	TextView courseLocation;
	AlertDialog dialog;
	AlertDialog.Builder builder;
	MapData mapData;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_details_activity);
		mapData = new MapData(this);
		context = this;
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		courseSubject = (TextView)findViewById(R.id.courseSubject);
		courseCode = (TextView)findViewById(R.id.courseCode);
		courseTitle = (TextView)findViewById(R.id.courseTitle);
		courseLecturer = (TextView)findViewById(R.id.courseLecturer);
		courseLocation =(TextView)findViewById(R.id.courseLocation);
		
		String subject = getIntent().getExtras().getString("subject");
		String title = getIntent().getExtras().getString("title");
		int code = getIntent().getExtras().getInt("code");
		String lecturer = getIntent().getExtras().getString("lecturer");
		long location = getIntent().getExtras().getLong("location");
		courseSubject.setText(subject);
		courseTitle.setText(title);
		courseCode.setText(Integer.toString(code));
		courseLecturer.setText(lecturer);
		Cursor cursor = mapData.getLocationById(location);
		String locationName="";
		if(cursor.getCount()>0){
			while(cursor.moveToNext()){
				locationName = cursor.getString(cursor.getColumnIndex(LOCATION_NAME));
			}
		}
		courseLocation.setText(locationName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.course_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		final long rowid = getIntent().getExtras().getLong("rowid");
		int menuid=item.getItemId();
		if(menuid == R.id.menu_edit){
			String subject = courseSubject.getText().toString();
			int code = Integer.parseInt(courseCode.getText().toString());
			String title = courseTitle.getText().toString();
			String lecturer = courseLecturer.getText().toString();
			String location = courseLocation.getText().toString();
		
			Intent i = new Intent(this,CourseActivity.class);
		
			i.putExtra("rowid", rowid);
			i.putExtra("subject", subject);
			i.putExtra("code", code);
			i.putExtra("title", title);
			i.putExtra("lecturer", lecturer);
			i.putExtra("location", location);
			startActivity(i);
			
		}
		else if(menuid==R.id.menu_delete){
			builder = new AlertDialog.Builder(this);
			builder.setTitle("Delete Course");
			builder.setPositiveButton("Delete",  new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mapData.deleteCourse(rowid);
					Toast.makeText(context, "Course Deleted", Toast.LENGTH_LONG).show();
					Intent i = new Intent(context,CourseListActivity.class);
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
			
		}else if(menuid ==android.R.id.home ) {
			Intent homeIntent = new Intent(this, CourseListActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
		}
		
		
		
		return super.onOptionsItemSelected(item);
	}

	public CourseDetailsActivity() {
		// TODO Auto-generated constructor stub
	}

}
