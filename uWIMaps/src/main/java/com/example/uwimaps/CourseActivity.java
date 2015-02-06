package com.example.uwimaps;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CourseActivity extends Activity implements DBConstants {
	private Spinner courseSubject;
	private AutoCompleteTextView courseCode;
	private AutoCompleteTextView courseTitle;
	private AutoCompleteTextView courseLecturer;
	private Spinner courseLocation;
	private Button saveButton;
	private MapData mapData;
	MainActivity activity;
	private Context context;
	public static final String TAG = CourseActivity.class.getSimpleName();
	public CourseActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_activity);
		context = this;
		courseSubject = (Spinner)findViewById(R.id.etSubject);
		courseCode = (AutoCompleteTextView)findViewById(R.id.etCode);
		courseTitle = (AutoCompleteTextView)findViewById(R.id.etTitle);
		courseLecturer = (AutoCompleteTextView)findViewById(R.id.etLecturer);
		courseLocation=(Spinner)findViewById(R.id.location);
		saveButton = (Button)findViewById(R.id.save);
		mapData = new MapData(this);
		activity = new MainActivity();
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		setCourseCodeAdapter();
		setCourseTitleAdapter();
		setCourseLecturerAdapter();
		
		
		ArrayAdapter subjectAdapter = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_item);
		subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		courseSubject.setAdapter(subjectAdapter);
		
		loadSpinnerData();
		if(getIntent().getExtras()!=null){
			String subject = getIntent().getExtras().getString("subject");
			String title = getIntent().getExtras().getString("title");
			int code = getIntent().getExtras().getInt("code");
			String lecturer = getIntent().getExtras().getString("lecturer");
			String location = getIntent().getExtras().getString("location");
			
			ArrayAdapter<String> sa = (ArrayAdapter<String>) courseSubject.getAdapter();
			int sapos = sa.getPosition(subject);
			courseSubject.setSelection(sapos);
			//courseSubject.setText(subject);
			courseCode.setText(Integer.toString(code));
			courseTitle.setText(title);
			courseLecturer.setText(lecturer);
			ArrayAdapter<String> a = (ArrayAdapter<String>) courseLocation.getAdapter();
			int pos = a.getPosition(location);
			courseLocation.setSelection(pos);
		}
		
		
		OnClickListener listener = new OnClickListener(){
			long rowid=0;
			String subject;
			int code;
			String title;
			String lecturer;
			@Override
			public void onClick(View view) {
				
				try{
					subject = courseSubject.getSelectedItem().toString();
					String[] sub = subject.split(" ");
					subject = sub[0];
					code = Integer.parseInt(courseCode.getText().toString());
					title = courseTitle.getText().toString();
					lecturer = courseLecturer.getText().toString();
				}catch(IllegalArgumentException e){
					Toast.makeText(context, "Invalid Entry", Toast.LENGTH_LONG).show();
					
				}
				String location = courseLocation.getSelectedItem().toString();
				
				Cursor cursor = mapData.getLocationByName(location);
				if(cursor.getCount()>0){
					while(cursor.moveToNext()){
						rowid = cursor.getLong(cursor.getColumnIndex("rowid"));
					}
				}
				if(!isSubjectValid(subject)){
					Toast.makeText(context, "Subject Not Recognized", Toast.LENGTH_LONG).show();
				}else if(!isCodeValid(code)){
					Toast.makeText(context, "Invalid Subject Code", Toast.LENGTH_LONG).show();
				}else{
					addCourse(subject,code,title,lecturer,rowid);
					Intent i = new Intent(context,CourseListActivity.class);
					startActivity(i);
				}
				
			}};
		saveButton.setOnClickListener(listener);
	}
	
	public void addCourse(String subject, int code, String title ,String lecturer,long location){
		long rowid=0;
		if(getIntent().getExtras()!=null){
			rowid = getIntent().getExtras().getLong("rowid");
		}
		if(rowid!=0){
			mapData.updateCourse(rowid, subject,code,title,lecturer,location);
		}else{
			Cursor c = mapData.getCourse(subject, code, title);
			if(c.getCount()>0){
			
			}else{
				long id = mapData.insertCourse(subject, code, title, lecturer, location);
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
	
	public void loadSpinnerData(){
		List<String> locations = new ArrayList<String>();
		locations.add("Choose Location");
		Cursor cursor = mapData.getAllLocations();
		if(cursor.getCount()>0){
			while(cursor.moveToNext()){
				String location = cursor.getString(cursor.getColumnIndex(LOCATION_NAME));
				locations.add(location);
			}
		}
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	                android.R.layout.simple_spinner_item, locations);
		 adapter
         .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 courseLocation.setAdapter(adapter);

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
            Intent homeIntent = new Intent(this, CourseListActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
		}
		return super.onOptionsItemSelected(item);
	}
	class LocationSpinnerAdapter extends CursorAdapter{

		public LocationSpinnerAdapter(Context context, Cursor c,int flags) {
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
			View row=inflater.inflate(R.layout.simple_list_item, parent, false);
			TextView text = (TextView) row.findViewById(R.id.simple_list_item_text);
			String location = cursor.getString(cursor.getColumnIndex(LOCATION_NAME));
			text.setText(location);
			
			
			return row;
		}
		
	}
	public boolean isSubjectValid(String subject){
		boolean valid=true;
		if(subject.length()>4){
			valid=false;
		}
		return valid;
	}
	
	public boolean isCodeValid(int code){
		boolean valid=true;;
		if(code<999 || code>9999){
			valid=false;
		}
		return valid;
	}
	
	public void setCourseCodeAdapter(){
		int[] to=new int[]{android.R.id.text1};
		String[] columns = new String[]{COURSE_CODE};
		
		SimpleCursorAdapter codeAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, columns, to,0);
		
		
		codeAdapter.setFilterQueryProvider(new FilterQueryProvider() {
		     public Cursor runQuery(CharSequence str) {
		    	 Log.d(TAG, "course search: "+str.toString());
		    	 return mapData.getCourseByFTS(str.toString());
		      
		     } });
		 
		codeAdapter.setCursorToStringConverter(new CursorToStringConverter() {
		     public CharSequence convertToString(Cursor cur) {
		       String courseCode = Integer.toString(cur.getInt(cur.getColumnIndex(COURSE_CODE))) ;
		       //return cur.getString(index);
		    	 return courseCode;
		    }});
		
		courseCode.setAdapter(codeAdapter);
		
	}
	public void setCourseTitleAdapter(){
		int[] to=new int[]{android.R.id.text1};
		String[] columns = new String[]{COURSE_TITLE};
		
		SimpleCursorAdapter titleAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, columns, to,0);
		
		
		titleAdapter.setFilterQueryProvider(new FilterQueryProvider() {
		     public Cursor runQuery(CharSequence str) {
		    	 Log.d(TAG, "course search: "+str.toString());
		    	 return mapData.getCourseByFTS(str.toString());
		      
		     } });
		 
		titleAdapter.setCursorToStringConverter(new CursorToStringConverter() {
		     public CharSequence convertToString(Cursor cur) {
		       String courseTitle = cur.getString(cur.getColumnIndex(COURSE_TITLE)) ;
		       //return cur.getString(index);
		    	 return courseTitle;
		    }});
		
		courseTitle.setAdapter(titleAdapter);
		
	}
	
	public void setCourseLecturerAdapter(){
		int[] to=new int[]{android.R.id.text1};
		String[] columns = new String[]{COURSE_LECTURER};
		
		SimpleCursorAdapter lecturerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, columns, to,0);
		
		
		lecturerAdapter.setFilterQueryProvider(new FilterQueryProvider() {
		     public Cursor runQuery(CharSequence str) {
		    	 Log.d(TAG, "course search: "+str.toString());
		    	 return mapData.getCourseByFTS(str.toString());
		      
		     } });
		 
		lecturerAdapter.setCursorToStringConverter(new CursorToStringConverter() {
		     public CharSequence convertToString(Cursor cur) {
		       String courseLecturer = cur.getString(cur.getColumnIndex(COURSE_LECTURER)) ;
		       //return cur.getString(index);
		    	 return courseLecturer;
		    }});
		
		courseLecturer.setAdapter(lecturerAdapter);
		
	}

}
