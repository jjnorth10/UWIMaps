package com.example.uwimaps;

import java.util.HashMap;
import java.util.List;

import com.example.entity.Course;
import com.example.entity.Event;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ContentOptionsListAdapter extends BaseExpandableListAdapter {
	private List<String> options;
	private HashMap<String,List<Object>>  contents; //= new String[99][99];
	private Context context;
	MapData mapData;
	public static final String TAG = ContentOptionsListAdapter.class.getSimpleName();
	public ContentOptionsListAdapter(Context context,List<String> options, HashMap<String,List<Object>>  contents) {
		// TODO Auto-generated constructor stub
		this.context=context;
		mapData=new MapData(context);
		this.options=options;
		this.contents=contents;
		
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return options.size();
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		String group = options.get(groupPosition);
		return contents.get(group).size();//[groupPosition].length;
	}
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return options.get(groupPosition);
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		String group = options.get(groupPosition);
		Object o = contents.get(group).get(childPosition);
		Object childObject = null;
		switch(groupPosition){
		case 0:
			//.values().toArray()[0];
			Course course = (Course)o;
			//String courseInfo = course.getSubject()+" "+course.getCode();
			childObject = course;
			
		break;
		case 1:
			Event event = (Event)o;
			childObject = event;
			
		}
		return childObject;
		
	}
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		long id = 0;
		String group = options.get(groupPosition);
		Object o = contents.get(group).get(childPosition);
		switch(groupPosition){
		case 0:
			
			Course course = (Course)o;
			
			id = course.getId();
		break;
		case 1:
			Event event = (Event)o;
			id = event.getId();
			
		}
		return id;
		
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.group, parent, false);
		TextView title = (TextView)row.findViewById(R.id.group_list_item_text);
		title.setText(options.get(groupPosition));
		return row;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.child, parent, false);
		row.setBackgroundColor(context.getResources().getColor(R.color.blue));
		TextView title = (TextView)row.findViewById(R.id.child_list_item_text);
		title.setTextColor(context.getResources().getColor(R.color.white));
		String group = options.get(groupPosition);
		Object o = contents.get(group).get(childPosition);
		switch(groupPosition){
		case 0:
			
			Course course = (Course)o;
			String courseInfo = course.getSubject()+" "+course.getCode();
			title.setText(courseInfo);
			
		break;	
		case 1:
			Event event = (Event)o;
			String eventInfo = event.getName();
			title.setText(eventInfo);
			
		}
		
		//title.setText((String)contents.get(group).get(childPosition).values().toArray()[0]);
		return row;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
