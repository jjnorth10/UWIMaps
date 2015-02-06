package com.example.uwimaps;

import com.example.uwimaps.CourseListActivity.CourseListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AddCourseListActivity extends ListActivity implements DBConstants {
	ListView listView;
	MapData mapData;
	CourseListAdapter cladapter;
	AlertDialog dialog;
	AlertDialog.Builder builder;
	public static final String TAG = AddCourseListActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_course_list_activity);
		
		mapData = new MapData(this);
		Cursor cursor = mapData.getAllCourses();
		cladapter = new CourseListAdapter(this, cursor, 0);
		listView = getListView();
		listView.setAdapter(cladapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	
	public AddCourseListActivity() {
		// TODO Auto-generated constructor stub
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
		final long locationId = getIntent().getExtras().getLong("locationId");
		Log.d(TAG, "location id: "+locationId);
		
		Cursor cursor = (Cursor)listView.getItemAtPosition(position);
		//final long courseId = cursor.getLong(cursor.getColumnIndex("rowid"));
		//Log.d(TAG, "course id: "+courseId);
		final String subject= cursor.getString(cursor.getColumnIndex(COURSE_SUBJECT));
		Log.d(TAG, "subject: "+subject);
		final int code = cursor.getInt(cursor.getColumnIndex(COURSE_CODE));
		Log.d(TAG, "code: "+code);
		final String title = cursor.getString(cursor.getColumnIndex(COURSE_TITLE));
		Log.d(TAG, "title: "+title);
		final long rowid = cursor.getLong(cursor.getColumnIndex("rowid"));
		builder = new AlertDialog.Builder(this);
		builder.setTitle("Add Course To Location");
		builder.setPositiveButton("Save Course", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				long count=mapData.updateCourseLocation(rowid,locationId);
				if(count<1){
					//Toast.makeText(this, "Error adding a location", Toast.LENGTH_LONG);
					Log.d(TAG, "Error updating the course");
				}else{
					//Toast.makeText(this, "Location added successfully", Toast.LENGTH_LONG);
					Log.d(TAG, "Course updated");
				}
				
			}
		} );
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
		dialog = builder.create();
		dialog.show();
	}


}
