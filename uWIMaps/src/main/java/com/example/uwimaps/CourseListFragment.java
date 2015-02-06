package com.example.uwimaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.communication.APIEndPoints;
import com.example.communication.Communication;
import com.example.communication.CommunicationResponse;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CourseListFragment extends ListFragment implements DBConstants{
	SimpleCursorAdapter adapter;
	CourseListFragmentAdapter clfAdapter;
	ListView listView;
	MapData mapData;
	HashMap<String,Object> map;
	MainActivity act;
	public static final String TAG = CourseListFragment.class.getSimpleName();

	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		listView = getListView();
		Cursor cursor = mapData.getAllCoursesWithLocations();
		if(cursor.getCount()>0){
			
			clfAdapter = new CourseListFragmentAdapter(getActivity().getBaseContext(),cursor,0);
		}else{
			Log.d(TAG, "Course cursor null");
		}
		
		
		listView.setAdapter(clfAdapter);
		
		
		
		
		
		
		
		
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		map=new HashMap<String,Object>();
		mapData = new MapData(getActivity());
		act =(MainActivity)getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.course_list_fragment, container,false);
		return v;
	}

	public CourseListFragment() {
		// TODO Auto-generated constructor stub
	}
	
	class CourseListFragmentAdapter extends CursorAdapter{

		public CourseListFragmentAdapter(Context context, Cursor c,int flags) {
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
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Cursor cursor = (Cursor)listView.getItemAtPosition(position);
		long locId= cursor.getInt(cursor.getColumnIndex(COURSE_LOCATION));
		Log.d(TAG, "location id: "+locId);
		double latitude=0.0;
		double longitude=0.0;
				
			
			
		
		Cursor locCursor = mapData.getLocationById(locId);
		if(locCursor.getCount()>0){
			while(locCursor.moveToNext()){
				latitude = locCursor.getDouble(locCursor.getColumnIndex(LOCATION_LATITUDE));
				longitude = locCursor.getDouble(locCursor.getColumnIndex(LOCATION_LONGITUDE));
			
				
			}
		}else{
			Log.d(TAG, "location cursor null");
		}
		act.zoomIn(latitude, longitude);
	}
	

	
	

}
